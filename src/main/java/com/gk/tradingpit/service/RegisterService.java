package com.gk.tradingpit.service;

import com.gk.tradingpit.controller.dto.request.ClientClickDto;
import com.gk.tradingpit.controller.dto.request.ClientConversionDto;
import com.gk.tradingpit.persistance.entity.AffiliateClientMap;
import com.gk.tradingpit.persistance.entity.AffiliateTransaction;
import com.gk.tradingpit.persistance.enums.OperationType;
import com.gk.tradingpit.persistance.repository.AffiliateTransactionsRepository;
import com.gk.tradingpit.service.exceptions.EntityNotFoundException;
import com.gk.tradingpit.service.exercise_tap.ExerciseTapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class RegisterService {
    private final ExerciseTapService exerciseTapService;
    private final AffiliateClientMapService affiliateClientMapService;
    private final AffiliateTransactionsRepository affiliateTransactionsRepository;
    private final String currency;

    public RegisterService(ExerciseTapService exerciseTapService, AffiliateClientMapService affiliateClientMapService,
                           AffiliateTransactionsRepository affiliateTransactionsRepository, @Value("${trading.pit.currency}") String currency) {
        this.exerciseTapService = exerciseTapService;
        this.affiliateClientMapService = affiliateClientMapService;
        this.affiliateTransactionsRepository = affiliateTransactionsRepository;
        this.currency = currency;
    }

    public String createClick(ClientClickDto clientClickDto, String ip, String userAgent){
        Optional<String> clickIdContainer = this.exerciseTapService.retryableClick(clientClickDto, ip, userAgent);
        if(clickIdContainer.isPresent()){
            saveAffiliateClientMap(clickIdContainer.get(), clientClickDto, ip, userAgent);
            return clickIdContainer.get();
        }
        return null;
    }

    private void saveAffiliateClientMap(String clickId, ClientClickDto clientClickDto, String ip, String userAgent){
        AffiliateClientMap affiliateClientMap = new AffiliateClientMap();
        affiliateClientMap.setClickId(clickId);
        affiliateClientMap.setClientId(clientClickDto.getClientId());
        affiliateClientMap.setUserAgent(userAgent);
        affiliateClientMap.setIp(ip);
        affiliateClientMap.setReferralCode(clientClickDto.getReferralCode());
        affiliateClientMap.setCreationDate(LocalDateTime.now());
        this.affiliateClientMapService.save(affiliateClientMap);
    }


    public Integer createConversation(ClientConversionDto clientConversionDto) throws EntityNotFoundException {
        AffiliateClientMap affiliateClientMap = this.affiliateClientMapService.getByClientId(clientConversionDto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Entity not found by client-id: " + clientConversionDto.getClientId()));
        Map<String, Integer> conversationDetails = this.exerciseTapService.retryableConversion(clientConversionDto, affiliateClientMap.getClickId(),
                this.currency, affiliateClientMap.getIp(), affiliateClientMap.getUserAgent());
        if(!conversationDetails.isEmpty()){
            Integer convId = conversationDetails.get("conversionId");
            Integer amount = conversationDetails.get("amount");
            saveAffiliateTransaction(convId, amount, clientConversionDto, affiliateClientMap.getReferralCode());
            return convId;
        }
        return null;
    }

    private void saveAffiliateTransaction(Integer convId, Integer amount,
                                          ClientConversionDto clientConversionDto,
                                          String referralCode){
        AffiliateTransaction affiliateTransaction = new AffiliateTransaction();
        affiliateTransaction.setConversionId(convId);
        affiliateTransaction.setConversionAmount(amount);
        affiliateTransaction.setCurrency(currency);
        affiliateTransaction.setTransactionType(clientConversionDto.getTransactionType());
        affiliateTransaction.setClientId(clientConversionDto.getClientId());
        affiliateTransaction.setOrderId(clientConversionDto.getOrderId());
        affiliateTransaction.setCreationDate(LocalDateTime.now());
        affiliateTransaction.setReferralCode(referralCode);
        affiliateTransaction.setOrderAmount(clientConversionDto.getTotalPrice());
        this.affiliateTransactionsRepository.save(affiliateTransaction);
    }
}
