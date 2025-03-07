package com.in28minutes.microservices.currency_conversion_service.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// /h2-console
// jdbc:h2:mem:tsetdb
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchange {

	private Long id;
	private String from;
	private String to;
	private BigDecimal conversionMultiple;
	
	private String environment;
	
}
