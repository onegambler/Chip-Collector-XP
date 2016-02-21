package com.chipcollector.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MoneyAmount {

	public enum Currency {
		DOLLAR,
		EURO,
		POUND
	}

	private Currency currency;
	private BigDecimal amount;
}