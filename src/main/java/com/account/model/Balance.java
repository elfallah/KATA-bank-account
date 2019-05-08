package com.account.model;

public class Balance {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Balance.class);

	private final Amount amount;

	public Balance(Amount amount) {

		this.amount = amount;
	}

	public Balance deposit(Amount amount) {
		log.info("balance after deposit: " + new Balance(this.amount.add(amount)).toString());
		return new Balance(this.amount.add(amount));
	}

	public Balance withdraw(Amount amount) {
		log.info("balance after withdraw: " + new Balance(this.amount.subtract(amount)).toString());
		return new Balance(this.amount.subtract(amount));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Balance balance = (Balance) o;

		return amount != null ? amount.equals(balance.amount) : balance.amount == null;
	}

	@Override
	public int hashCode() {
		return amount != null ? amount.hashCode() : 0;
	}

	public boolean isWithDrawNegativeResult(Amount amount) {
		return this.amount.isWithDrawNegativeResult(amount);
	}

	@Override
	public String toString() {
		return "" + amount;
	}
}
