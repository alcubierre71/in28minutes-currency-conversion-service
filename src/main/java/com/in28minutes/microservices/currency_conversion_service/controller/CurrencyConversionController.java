package com.in28minutes.microservices.currency_conversion_service.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.in28minutes.microservices.currency_conversion_service.entity.CurrencyConversion;
import com.in28minutes.microservices.currency_conversion_service.entity.CurrencyExchange;
import com.in28minutes.microservices.currency_conversion_service.proxy.CurrencyExchangeProxy;

@RestController
@RequestMapping("/currency-conversion")
public class CurrencyConversionController {

	private Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);
	
	//@Autowired
	//RestTemplate restTemplate;
	
	@Autowired
	CurrencyExchangeProxy proxy;
	
	// http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10
	@GetMapping("/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		String url = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		// Invocacion a la API CurrencyExchangeService
		// http://localhost:8000/currency-exchange/from/AUD/to/INR
		ResponseEntity<CurrencyExchange> responseEntity = restTemplate.getForEntity(url, CurrencyExchange.class, uriVariables);
		
		// Datos del Exchange
		CurrencyExchange exchange = responseEntity.getBody();
		BigDecimal conversionMultiple = exchange.getConversionMultiple();
		
		// Construimos la conversion de respuesta
		BigDecimal totalCalculatedAmount = quantity.multiply(conversionMultiple); 
		String environment = exchange.getEnvironment();
				
		CurrencyConversion conversion = new CurrencyConversion(10001L, from, to, quantity, conversionMultiple, totalCalculatedAmount, environment);
		
		return conversion;
		
	}
	
	// http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10
	@GetMapping("/feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		logger.info("calculateCurrencyConversionFeign called with {} to {} quantity {}", from, to, quantity);
		
		// Invocacion a la API CurrencyExchangeService
		// http://localhost:8000/feign/currency-exchange/from/AUD/to/INR
		CurrencyExchange exchange = proxy.retrieveExchangeValue(from, to);
		
		BigDecimal conversionMultiple = exchange.getConversionMultiple();
		
		// Construimos la conversion de respuesta
		BigDecimal totalCalculatedAmount = quantity.multiply(conversionMultiple); 
		String environment = exchange.getEnvironment();
				
		CurrencyConversion conversion = new CurrencyConversion(10001L, from, to, quantity, conversionMultiple, totalCalculatedAmount, environment + " - feign ");
		
		return conversion;
		
	}
	
}
