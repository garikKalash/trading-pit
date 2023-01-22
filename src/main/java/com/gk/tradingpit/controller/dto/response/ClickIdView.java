package com.gk.tradingpit.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClickIdView {
    @JsonProperty("click_id")
    private String clickId;
}
