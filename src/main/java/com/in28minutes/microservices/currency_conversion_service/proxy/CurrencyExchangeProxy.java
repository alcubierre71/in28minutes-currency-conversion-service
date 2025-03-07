package com.in28minutes.microservices.currency_conversion_service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.in28minutes.microservices.currency_conversion_service.entity.CurrencyExchange;

@FeignClient(name="currency-exchange",url="http://localhost:8000",path="/currency-exchange")
public interface CurrencyExchangeProxy {

	@GetMapping("/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
		
}
