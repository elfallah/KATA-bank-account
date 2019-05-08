package com.account;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;

import org.junit.Test;

import com.account.model.Amount;

public class AmountTest {

	@Test
	public void adding_a_amount_to_an_other() throws Exception {
		Amount amount = new Amount(new BigDecimal(10));
		amount = amount.add(new Amount(new BigDecimal(10)));
		Amount expectedAmount = new Amount(new BigDecimal(20));
		Assertions.assertThat(amount).isEqualTo(expectedAmount);
	}

	@Test
	public void subtract_a_amount_to_an_other() throws Exception {
		Amount amount = new Amount(new BigDecimal(10));
		amount = amount.subtract(new Amount(new BigDecimal(10)));
		Amount expectedAmount = new Amount(BigDecimal.ZERO);
		Assertions.assertThat(amount).isEqualTo(expectedAmount);
	}

	@Test
	public void should_return_false_when_the_result_is_negative() throws Exception {
		Amount amount = new Amount(new BigDecimal(10));
		Assertions.assertThat(amount.isWithDrawNegativeResult(new Amount(new BigDecimal(20)))).isTrue();
	}

	@Test
	public void should_return_true_when_the_result_is_positive() throws Exception {
		Amount amount = new Amount(new BigDecimal(10));
		Assertions.assertThat(amount.isWithDrawNegativeResult(new Amount(new BigDecimal(5)))).isFalse();
	}
}
