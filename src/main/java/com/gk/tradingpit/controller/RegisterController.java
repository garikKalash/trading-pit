package com.gk.tradingpit.controller;


import com.gk.tradingpit.controller.dto.request.ClientClickDto;
import com.gk.tradingpit.controller.dto.request.ClientConversionDto;
import com.gk.tradingpit.controller.dto.response.ClickIdView;
import com.gk.tradingpit.controller.dto.response.ConversationIdView;
import com.gk.tradingpit.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@RestController
@RequestMapping("/register")
@Validated
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("client")
    public ClickIdView createClick(@Valid @RequestBody ClientClickDto affiliateClickDto,
                                   @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
                                   HttpServletRequest request){
        String ips = request.getHeader("X-FORWARDED-FOR");
        if(Strings.isBlank(ips)) ips = request.getRemoteAddr();
        String ipAddress = Arrays.stream(ips.split(",")).findFirst()
                .orElseThrow(()->new IllegalArgumentException("Failed to extract ip address"));
        String clickId = registerService.createClick(affiliateClickDto, ipAddress, userAgent);
        return ClickIdView.builder().clickId(clickId).build();
    }


    @PostMapping("conversion")
    public ConversationIdView createConversion(@Valid @RequestBody ClientConversionDto affiliateTransactionsDTO){
        Integer convId = registerService.createConversation(affiliateTransactionsDTO);
        return ConversationIdView.builder().conversationId(convId).build();
    }
}
