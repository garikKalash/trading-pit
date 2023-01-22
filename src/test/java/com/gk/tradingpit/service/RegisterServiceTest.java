package com.gk.tradingpit.service;

import com.gk.tradingpit.controller.dto.request.ClientClickDto;
import com.gk.tradingpit.controller.dto.request.ClientConversionDto;
import com.gk.tradingpit.persistance.entity.AffiliateClientMap;
import com.gk.tradingpit.persistance.repository.AffiliateTransactionsRepository;
import com.gk.tradingpit.service.exceptions.EntityNotFoundException;
import com.gk.tradingpit.service.exercise_tap.ExerciseTapService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegisterServiceTest {
    private static final String TEST_CLICK_ID = "test-click-id";
    private static final Integer TEST_CONVERSION_ID = 10;
    private static final String TEST_CURRENCY = "EUR";
    private static final String TEST_IP = "test-ip";
    private static final String TEST_USER_AGENT = "test-user-agent";
    private static final String TEST_CLIENT_ID = "test_client_id";
    private static final String TEST_LANDING_PAGE = "client_landing";
    private static final String TEST_REFERRAL_CODE = "client_referral";
    @Autowired
    private RegisterService registerService;
    @MockBean
    private ExerciseTapService exerciseTapService;
    @MockBean
    private AffiliateClientMapService affiliateClientMapService;

    @MockBean
    private AffiliateTransactionsRepository affiliateTransactionsRepository;


    @Test
    public void testValidClickCreate(){
        ClientClickDto clientClickDto = takeTestClientClickDto();
        AffiliateClientMap mockAffClientMap = affiliateClientMap();
        doReturn(Optional.of(TEST_CLICK_ID)).when(exerciseTapService).retryableClick(any(),eq(TEST_IP), eq(TEST_USER_AGENT));
        doReturn(mockAffClientMap).when(affiliateClientMapService).save(any());
        String clickId = registerService.createClick(clientClickDto, TEST_IP, TEST_USER_AGENT);
        assertEquals(clickId, TEST_CLICK_ID);
    }

    @Test
    public void testValidConversionCreate(){
        ClientConversionDto clientClickDto = takeTestClientConversionDto();
        AffiliateClientMap affiliateClientMap = affiliateClientMap();
        doReturn(Optional.of(affiliateClientMap)).when(affiliateClientMapService).getByClientId(TEST_CLIENT_ID);
        doReturn(Map.of("conversionId", TEST_CONVERSION_ID, "amount", 10)).when(exerciseTapService)
                .retryableConversion(any(),
                        eq(TEST_CLICK_ID),
                        eq(TEST_CURRENCY),
                        eq(TEST_IP),
                        eq(TEST_USER_AGENT));

        doReturn(null).when(affiliateTransactionsRepository).save(any());
        Integer conversation = registerService.createConversation(clientClickDto);
        assertEquals(conversation, TEST_CONVERSION_ID);
    }

    @Test
    public void testInvalidClientIdConversionCreate(){
        ClientConversionDto clientClickDto = takeTestClientConversionDto();
        doReturn(Optional.empty()).when(affiliateClientMapService).getByClientId(TEST_CLIENT_ID);
        doReturn(Map.of("conversionId", TEST_CONVERSION_ID, "amount", 10)).when(exerciseTapService)
                .retryableConversion(any(),
                        eq(TEST_CLICK_ID),
                        eq(TEST_CURRENCY),
                        eq(TEST_IP),
                        eq(TEST_USER_AGENT));

        assertThrows(EntityNotFoundException.class, ()->registerService.createConversation(clientClickDto));
    }



    private ClientClickDto takeTestClientClickDto(){
        ClientClickDto clientClickDto = mock(ClientClickDto.class);
        when(clientClickDto.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(clientClickDto.getLandingPage()).thenReturn(TEST_LANDING_PAGE);
        when(clientClickDto.getReferralCode()).thenReturn(TEST_REFERRAL_CODE);
        return clientClickDto;
    }

    private ClientConversionDto takeTestClientConversionDto(){
        ClientConversionDto clientConversionDto = mock(ClientConversionDto.class);
        when(clientConversionDto.getClientId()).thenReturn(TEST_CLIENT_ID);
        return clientConversionDto;
    }

    private AffiliateClientMap affiliateClientMap(){
        AffiliateClientMap affiliateClientMap = mock(AffiliateClientMap.class);
        when(affiliateClientMap.getClientId()).thenReturn(TEST_CLIENT_ID);
        when(affiliateClientMap.getIp()).thenReturn(TEST_IP);
        when(affiliateClientMap.getUserAgent()).thenReturn(TEST_USER_AGENT);
        when(affiliateClientMap.getClickId()).thenReturn(TEST_CLICK_ID);
        return affiliateClientMap;
    }
}
