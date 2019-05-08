package com.account;

import static com.account.model.OperationType.DEPOSIT_OPERATION;
import static com.account.model.OperationType.WITHDRAWAL_OPERATION;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
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
public class TransactionTest {

	@Mock
	private DateOperation dateOperation;

	@Test
	public void should_print_a_transaction_with_a_positive_amount() throws Exception {

		Client client = new Client("toto", "titi");
		Account account = new Account(new Balance(new Amount(BigDecimal.ZERO)), dateOperation, client);

		Transaction transaction = Transaction.TransactionBuilder.getInstance().withLocalDate(LocalDate.of(2019, 5, 1))
				.withAmount(new Amount(new BigDecimal(100))).forAccount(account).withOperation(DEPOSIT_OPERATION)
				.build();

		String result = transaction.toString();
		Assertions.assertThat(result).isEqualTo("2019-05-01 | +100 | 0");
	}

	@Test
	public void should_print_a_transaction_with_a_negative_amount() throws Exception {
		when(dateOperation.now()).thenReturn(LocalDate.of(2019, 5, 1));

		Client client = new Client("toto", "titi");
		Account account = new Account(new Balance(new Amount(BigDecimal.ZERO)), dateOperation, client);

		Transaction transaction = Transaction.TransactionBuilder.getInstance().withLocalDate(dateOperation.now())
				.withAmount(new Amount(new BigDecimal(100))).forAccount(account).withOperation(WITHDRAWAL_OPERATION)
				.build();

		String result = transaction.toString();
		Assertions.assertThat(result).isEqualTo("2019-05-01 | -100 | 0");
	}

	@Test
	public void should_print_all_transaction() throws Exception {
		when(dateOperation.now()).thenReturn(LocalDate.of(2019, 5, 1), LocalDate.of(2019, 5, 2),
				LocalDate.of(2019, 5, 3));

		Client client = new Client("toto", "titi");
		Account account = new Account(new Balance(new Amount(BigDecimal.ZERO)), dateOperation, client);

		account.deposit(new Amount(new BigDecimal(200)));
		account.withdraw(new Amount(new BigDecimal(100)));
		account.deposit(new Amount(new BigDecimal(500)));

		Transaction.TransactionBuilder.getInstance().execute(account);

		Assertions.assertThat(account.getAllTransactions().get(0).toString()).isEqualTo("2019-05-01 | +200 | 200");
		Assertions.assertThat(account.getAllTransactions().get(1).toString()).isEqualTo("2019-05-02 | -100 | 100");
		Assertions.assertThat(account.getAllTransactions().get(2).toString()).isEqualTo("2019-05-03 | +500 | 600");

	}

	@Test
	public void should_subtract_by_threadpool_to_my_account() throws Exception {
		when(dateOperation.now()).thenReturn(LocalDate.of(2019, 5, 1), LocalDate.of(2019, 5, 2),
				LocalDate.of(2019, 5, 3));

		Client client = new Client("toto", "titi");
		Account account = new Account(new Balance(new Amount(new BigDecimal(50))), dateOperation, client);

		ExecutorService executor = Executors.newFixedThreadPool(5);// creating a pool of 5 threads
		for (int i = 0; i < 5; i++) {
			Runnable worker = new WorkerThread(new BigDecimal(10), account);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Finished all threads");
		Assertions.assertThat(account.getBalance()).isEqualTo(new Balance(new Amount(BigDecimal.ZERO)));
	}
}
