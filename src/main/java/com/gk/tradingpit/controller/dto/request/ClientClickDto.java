package com.gk.tradingpit.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class ClientClickDto {

	@NotEmpty(message = "client id is required")
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
			message = "Passed client id should be UUID")
	@JsonProperty("client_id")
	private String clientId;
	
	@NotEmpty
	@JsonProperty("landing_page")
	@Pattern(regexp = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$",
			message = "Invalid URL")
	private String landingPage;
	
	@JsonProperty("referral_code")
	private String referralCode;
}
