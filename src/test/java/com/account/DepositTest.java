package com.account;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.account.model.Account;
import com.account.model.Amount;
import com.account.model.Balance;
import com.account.model.Client;
import com.account.model.DateOperation;
import com.account.model.Transaction;

@RunWith(MockitoJUnitRunner.class)
public class DepositTest {

	private Account account;
	private Client client;
	@Mock
	private DateOperation dateOperation;

	@Before
	public void setUp() throws Exception {
		client = new Client("toto", "titi");
		account = new Account(new Balance(new Amount(BigDecimal.ZERO)), dateOperation, client);
	}

	@Test
	public void should_adding_zero_to_my_account() throws Exception {
		when(dateOperation.now()).thenReturn(LocalDate.of(2019, 5, 7));
		account.deposit(new Amount(BigDecimal.ZERO));
		
		Assertions.assertThat(account.getBalance()).isEqualTo(new Balance(new Amount(BigDecimal.ZERO)));
	}

	@Test
	public void should_adding_ten_to_my_account() throws Exception {
		when(dateOperation.now()).thenReturn(LocalDate.of(2019, 5, 7));
		account.deposit(new Amount(new BigDecimal(10)));
		Transaction.TransactionBuilder.getInstance().execute(account);
		Assertions.assertThat(account.getBalance()).isEqualTo(new Balance(new Amount(new BigDecimal(10))));
	}

	@Test
	public void should_adding_ten_twice_to_my_account() throws Exception {
		when(dateOperation.now()).thenReturn(LocalDate.of(2019, 5, 7));
		account.deposit(new Amount(new BigDecimal(10)));
		account.deposit(new Amount(new BigDecimal(10)));
		Transaction.TransactionBuilder.getInstance().execute(account);
		Assertions.assertThat(account.getBalance()).isEqualTo(new Balance(new Amount(new BigDecimal(20))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void should_not_authorize_deposit_negative_value() throws Exception {
		account.deposit(new Amount(new BigDecimal(-10)));
		Transaction.TransactionBuilder.getInstance().execute(account);
	}
}
