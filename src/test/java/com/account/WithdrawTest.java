package com.account;

import java.math.BigDecimal;

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
import com.account.model.Transaction.TransactionStatus;

@RunWith(MockitoJUnitRunner.class)
public class WithdrawTest {
	private Account account;
	private Client client;

	@Mock
	private DateOperation dateOperation;

	@Before
	public void setUp() throws Exception {
		client = new Client("toto", "titi");
		account = new Account(new Balance(new Amount(new BigDecimal(50))), dateOperation, client);
	}

	@Test
	public void should_subtract_zero_to_my_account() throws Exception {
		account.withdraw(new Amount(BigDecimal.ZERO));
		Transaction.TransactionBuilder.getInstance().execute(account);
		Assertions.assertThat(account.getBalance()).isEqualTo(new Balance(new Amount(new BigDecimal(50))));
	}

	@Test
	public void should_subtract_ten_to_my_account() throws Exception {
		account.withdraw(new Amount(new BigDecimal(10)));
		Transaction.TransactionBuilder.getInstance().execute(account);
		Assertions.assertThat(account.getBalance()).isEqualTo(new Balance(new Amount(new BigDecimal(40))));
	}

	@Test
	public void should_subtract_ten_twice_to_my_account() throws Exception {
		account.withdraw(new Amount(new BigDecimal(10)));
		account.withdraw(new Amount(new BigDecimal(10)));
		Transaction.TransactionBuilder.getInstance().execute(account);
		Assertions.assertThat(account.getBalance()).isEqualTo(new Balance(new Amount(new BigDecimal(30))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void should_not_authorize_withdrawal_negative_value() throws Exception {
		account.withdraw(new Amount(new BigDecimal(-50)));
		Transaction.TransactionBuilder.getInstance().execute(account);
	}

	public void should_not_authorize_withdrawal_an_amount_which_is_not_present_in_an_account() throws Exception {
		account.getAllTransactions().clear();
		account.withdraw(new Amount(new BigDecimal(60)));
		Transaction.TransactionBuilder.getInstance().execute(account);
		account.getAllTransactions().stream().filter(t -> t.getStatus() == TransactionStatus.FAILED);
		
		Assertions.assertThat(account.getAllTransactions().get(0).getStatus() == TransactionStatus.FAILED);
	}

}
