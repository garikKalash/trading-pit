package com.gk.tradingpit.service.exercise_tap.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExerciseTapClick {
    @JsonProperty("referral_code")
    private String referralCode;
    @JsonProperty("landing_page")
    private String landingPage;
    @JsonProperty("user_agent")
    private String userAgent;
    @JsonProperty("ip")
    private String ip;
}
