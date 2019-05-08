package com.account.model;

import java.time.LocalDate;

public class Transaction {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Transaction.class);

	private LocalDate localDate;
	private Amount amount;
	private Balance currentBalance;
	private Account account;
	private OperationType operationType;
	private TransactionStatus status;

	private Transaction(LocalDate localDate, Amount amount, Account account, OperationType operationType) {
		this.localDate = localDate;
		this.amount = amount;
		this.account = account;
		currentBalance = account.getBalance();
		this.operationType = operationType;
		status = TransactionStatus.INITIALIZED;
	}
	
	
	public TransactionStatus getStatus() {
		return status;
	}



	private void execute() {
		log.info("Execute transaction {}", this.toString());
		if (status == TransactionStatus.INITIALIZED) {
			status = TransactionStatus.STARTED;
			try {
				switch (operationType) {
				case DEPOSIT_OPERATION:
					currentBalance = account.getBalance().deposit(amount);
					account.setBalance(currentBalance);
					status = TransactionStatus.SUCCESS;
					break;
				case WITHDRAWAL_OPERATION:
					if (account.getBalance().isWithDrawNegativeResult(amount)) {
						status = TransactionStatus.FAILED;
						log.error("Forbidden operation : currentBalance {} isWithDrawNegativeResult {} ",
								account.getBalance(), amount);
						break;
					}
					currentBalance = account.getBalance().withdraw(amount);
					account.setBalance(currentBalance);
					status = TransactionStatus.SUCCESS;
					break;
				default:
					break;
				}
			} catch (Exception e) {
				status = TransactionStatus.FAILED;
				String msg = String.format("Error occured while executing the transaction {}", this.toString());
				log.error(msg);
			}
			log.info("Transaction {} executed with status {}", this.toString(), this.status);
		} else if (status == TransactionStatus.STARTED)
			log.error("Transaction currently executing with status {}", status);
		else
			log.error("Transaction already executed with status {}", status);
	}

	@Override
	public String toString() {
		return localDate + " | " + operationType.getOperation() + amount + " | " + currentBalance;
	}

	public static final class TransactionBuilder {
		private LocalDate localDate;
		private Amount amount;
		private Account account;
		private OperationType operationType;
		
		private static TransactionBuilder instance;

		private TransactionBuilder() {
		}

		public static TransactionBuilder getInstance() {
			if ( instance == null )
				instance = new TransactionBuilder();
			
			return instance;
		}

		public TransactionBuilder withLocalDate(LocalDate localDate) {
			this.localDate = localDate;
			return this;
		}

		public TransactionBuilder withAmount(Amount amount) {
			this.amount = amount;
			return this;
		}

		public TransactionBuilder forAccount(Account account) {
			this.account = account;
			return this;
		}

		public TransactionBuilder withOperation(OperationType operationType) {
			this.operationType = operationType;
			return this;
		}

		public Transaction build() {
			return new Transaction(localDate, amount, account, operationType);
		}
		
		public synchronized void execute(Account account) {
			if ( account != null )
				account.getAllTransactions().stream().forEach(Transaction::execute);
			
			
		}
	}

	public enum TransactionStatus {
		INITIALIZED, STARTED, SUCCESS, FAILED
	}
}
