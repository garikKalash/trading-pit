package com.gk.tradingpit.service.exercise_tap;

import com.gk.tradingpit.controller.dto.request.ClientClickDto;
import com.gk.tradingpit.controller.dto.request.ClientConversionDto;
import com.gk.tradingpit.persistance.entity.FailedCall;
import com.gk.tradingpit.persistance.enums.OperationType;
import com.gk.tradingpit.persistance.repository.FailedCallsRepository;
import com.gk.tradingpit.service.exercise_tap.exception.ApiClickExternalException;
import com.gk.tradingpit.service.exercise_tap.exception.ApiConversationExternalException;
import com.gk.tradingpit.service.exercise_tap.model.request.ExerciseTapClick;
import com.gk.tradingpit.service.exercise_tap.model.request.ExerciseTapConversion;
import com.gk.tradingpit.service.exercise_tap.model.response.ExerciseTapClickResponse;
import com.gk.tradingpit.service.exercise_tap.model.response.ExerciseTapConversionResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class ExerciseTapService {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseTapService.class);
    private static final String POST_CLICK = "clicks";
    private static final String POST_CONVERSATION = "conversation";

    private final FailedCallsRepository failedCallsRepository;

    private final RestTemplate mainTemplate;
    private final boolean simulateByMockingResp;
    private final String getClickFilePath;
    private final String getConversionFilePath;

    public ExerciseTapService(FailedCallsRepository failedCallsRepository,
                              @Value("${external.exercise.tap.baseUrl}") String baseUrl,
                              @Value("${simulate.by.mock.data}") boolean simulateByMockingResp,
                              @Value("${get.tap.click.id.mock.response.file}") String getClickFilePath,
                              @Value("${get.tap.click.conversation.mock.response.file}") String getConversionFilePath) {
        this.failedCallsRepository = failedCallsRepository;
        this.simulateByMockingResp = simulateByMockingResp;
        this.getClickFilePath = getClickFilePath;
        this.getConversionFilePath = getConversionFilePath;
        this.mainTemplate = new RestTemplate();
        this.mainTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));

    }

    @Retryable(retryFor = {ApiClickExternalException.class}, backoff= @Backoff(1000), maxAttempts = 3)
    public Optional<String> retryableClick(ClientClickDto clientClickDto, String ip, String userAgent) throws ApiClickExternalException {
        try {
            logger.info("Clicking to external API {}", clientClickDto.getClientId());
            ExerciseTapClickResponse exerciseTapClick;
            if(simulateByMockingResp) {
                exerciseTapClick = new Gson().fromJson(Files.newBufferedReader(Paths.get(getClass().getClassLoader().getResource(getClickFilePath).getPath())),
                        ExerciseTapClickResponse.class);
            } else {
                exerciseTapClick = mainTemplate.postForObject(POST_CLICK, buildFromClientClick(clientClickDto, ip, userAgent), ExerciseTapClickResponse.class);
            }
            if(exerciseTapClick == null) throw new ApiClickExternalException("Null response from click to external service");
            return Optional.of(exerciseTapClick.getId());
        } catch (Exception exception){
            ApiClickExternalException apiClickExternalException = new ApiClickExternalException("Exception from external API: " + exception.getMessage());
            apiClickExternalException.setClientClickDto(clientClickDto);
            throw apiClickExternalException;
        }
    }


    @Retryable(retryFor = {ApiConversationExternalException.class}, backoff= @Backoff(1000), maxAttempts = 3)
    public Map<String, Integer> retryableConversion(ClientConversionDto clientConversionDto,
                                    String clickId, String currency, String ip,
                                    String userAgent){
        ExerciseTapConversion exerciseTapConversion = buildFromClientConversation(clientConversionDto, clickId, ip,
                userAgent, currency);
        try {
            logger.info("Conversation retrieving dto {}", clientConversionDto.getClientId());
            ExerciseTapConversionResponse exerciseTapClick;

            if(simulateByMockingResp) {
                exerciseTapClick = new Gson().fromJson(Files.newBufferedReader(Paths.get(getClass().getClassLoader().getResource(getConversionFilePath).getPath())),
                        ExerciseTapConversionResponse.class);
            } else {
                exerciseTapClick = mainTemplate.postForObject(POST_CONVERSATION, exerciseTapConversion, ExerciseTapConversionResponse.class);
            }

            if(exerciseTapClick == null) throw new ApiClickExternalException("Null response from click to external service");
            Integer conversationId = exerciseTapClick.getId();
            Integer amount = CollectionUtils.isEmpty(exerciseTapClick.getCommissions()) ? -1 : exerciseTapClick.getCommissions().get(0).getAmount();
            return Map.of("conversionId", conversationId, "amount", amount);
        } catch (Exception exception){
            ApiConversationExternalException apiClickExternalException = new ApiConversationExternalException("Exception from external API: " + exception.getMessage());
            apiClickExternalException.setExerciseTapConversion(exerciseTapConversion);
            throw apiClickExternalException;
        }
    }

    @Recover
    public Optional<String> recoverClick(ApiClickExternalException t){
        logger.info("recover dto {}", t.getClientClickDto().getClientId());
        String payload = new Gson().toJson(t.getClientClickDto());
        String clientId = t.getClientClickDto().getClientId();
        String reason = t.getMessage();
        saveFailedCall(clientId, payload, OperationType.createClick, reason);
        return Optional.empty();
    }

    @Recover
    public Map<String, Object> recoverConversation(ApiConversationExternalException t){
        logger.info("recover conversation dto client id {}", t.getExerciseTapConversion().getCustomerId());
        String payload = new Gson().toJson(t.getExerciseTapConversion());
        String clientId = t.getExerciseTapConversion().getCustomerId();
        String reason = t.getMessage();
        saveFailedCall(clientId, payload, OperationType.createConversation, reason);
        return new HashMap<>();
    }

    private void saveFailedCall(String clientId, String payload,
                                OperationType type, String reason){
        FailedCall failedCall = new FailedCall();
        failedCall.setClientId(clientId);
        failedCall.setPayload(payload);
        failedCall.setTime(LocalDateTime.now());
        failedCall.setRequestType(type.name());
        failedCall.setReason(reason);
        this.failedCallsRepository.save(failedCall);
    }

    private ExerciseTapClick buildFromClientClick(ClientClickDto clientClickDto, String ip, String userAgent) {
        ExerciseTapClick exerciseTapClick = new ExerciseTapClick();
        exerciseTapClick.setIp(ip);
        exerciseTapClick.setReferralCode(clientClickDto.getReferralCode());
        exerciseTapClick.setLandingPage(clientClickDto.getLandingPage());
        exerciseTapClick.setUserAgent(userAgent);
        return exerciseTapClick;
    }

    private ExerciseTapConversion buildFromClientConversation(ClientConversionDto clientClickDto,
                                                              String clickId, String ip, String userAgent,
                                                              String currency) {
        ExerciseTapConversion exerciseTapClick = new ExerciseTapConversion();
        exerciseTapClick.setCurrency(currency);
        exerciseTapClick.setClickId(clickId);
        exerciseTapClick.setIp(ip);
        exerciseTapClick.setUserAgent(userAgent);

        exerciseTapClick.setCustomerId(clientClickDto.getClientId());
        exerciseTapClick.setExternalId(clientClickDto.getOrderId());
        exerciseTapClick.setAmount(clientClickDto.getTotalPrice());
        return exerciseTapClick;
    }

}
