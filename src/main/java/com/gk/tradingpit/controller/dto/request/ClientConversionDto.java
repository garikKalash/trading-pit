package com.gk.tradingpit.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
public class ClientConversionDto {

	@NotEmpty
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
	@JsonProperty("order_id")
	private String orderId;
	
	@NotNull
	@JsonProperty("total_price")
	@Positive
	private Double totalPrice;
	
	@NotEmpty
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
	@JsonProperty("client_id")
	private String clientId;
	
	@Pattern(regexp = "(New|Reset|Extend)", message = "Invalid transaction type")
	@JsonProperty("transaction_type")
	private String transactionType;
	
}
