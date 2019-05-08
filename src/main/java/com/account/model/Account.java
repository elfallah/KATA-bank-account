package com.account.model;

import static com.account.model.OperationType.DEPOSIT_OPERATION;
import static com.account.model.OperationType.WITHDRAWAL_OPERATION;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Account.class);

	private Balance balance;
	private final CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();
	private final DateOperation dateOperation;
	private final Client client;
	private final String accountNumber;

	public Account(Balance balance, DateOperation dateOperation, Client client) {
		this.balance = balance;
		this.dateOperation = dateOperation;
		this.client = client;
		this.accountNumber = UUID.randomUUID().toString();

	}

	public void deposit(Amount amount) {
		log.info("Deposit: {} on {}", amount.getValue(), this.toString());
		createTransaction(amount, DEPOSIT_OPERATION);

	}

	public void withdraw(Amount amount) {
		createTransaction(amount, WITHDRAWAL_OPERATION);
		log.info("Withdraw: {} on {}", amount.getValue(), this.toString());
	}

	public List<Transaction> getAllTransactions() {
		return transactions;

	}

	private void createTransaction(Amount amount, OperationType operationType) {
		Transaction transaction = Transaction.TransactionBuilder.getInstance().withLocalDate(dateOperation.now())
				.withAmount(amount).forAccount(this).withOperation(operationType).build();
		transactions.add(transaction);
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public Client getClient() {
		return client;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	@Override
	public String toString() {
		return "Account [dateOperation=" + dateOperation + ", client=" + client + ", accountNumber=" + accountNumber
				+ "]";
	}
	

}
