package com.account;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.account.model.Amount;
import com.account.model.Balance;

public class BalanceTest {

	@Test
	public void should_adding_ten_to_my_balance() throws Exception {
		Balance balance = new Balance(new Amount(BigDecimal.ZERO));
		balance = balance.deposit(new Amount(new BigDecimal(10)));
		Balance expectedBalance = new Balance(new Amount(new BigDecimal(10)));
		Assertions.assertThat(balance).isEqualTo(expectedBalance);
	}

	@Test
	public void should_subtract_ten_to_my_balance() throws Exception {
		Balance balance = new Balance(new Amount(new BigDecimal(30)));
		balance = balance.withdraw(new Amount(new BigDecimal(10)));
		Balance expectedBalance = new Balance(new Amount(new BigDecimal(20)));
		Assertions.assertThat(balance).isEqualTo(expectedBalance);
	}

	@Test
	public void should_return_false_when_the_result_is_negative() throws Exception {
		Balance balance = new Balance(new Amount(new BigDecimal(10)));
		boolean isWithDrawNegative = balance.isWithDrawNegativeResult(new Amount(new BigDecimal(20)));
		Assertions.assertThat(isWithDrawNegative).isTrue();
	}

	@Test
	public void should_return_true_when_the_result_is_positive() throws Exception {
		Balance balance = new Balance(new Amount(new BigDecimal(10)));
		boolean isWithDrawNegative = balance.isWithDrawNegativeResult(new Amount(new BigDecimal(5)));
		Assertions.assertThat(isWithDrawNegative).isFalse();
	}
}
