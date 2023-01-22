package com.gk.tradingpit.service.exercise_tap.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExerciseTapConversion {
    @JsonProperty("click_id")
    private String clickId;
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("amount")
    private Double amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("user_agent")
    private String userAgent;
    @JsonProperty("ip")
    private String ip;
}
