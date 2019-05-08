package com.account.model;

import java.math.BigDecimal;

public class Amount {
	private BigDecimal value;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Amount.class);

	public Amount(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) == -1) {
			log.error("Negative amount not accepted {} ", value);
			throw new IllegalArgumentException();
		}
		this.value = value;
	}

	public Amount add(Amount amount) {
		return new Amount(this.value.add(amount.value));
	}

	public Amount subtract(Amount amount) {
		return new Amount(this.value.subtract(amount.value));
	}

	@Override
	public String toString() {
		return "" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amount other = (Amount) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean isWithDrawNegativeResult(Amount amount) {

		if (this.value.subtract(amount.value).compareTo(BigDecimal.ZERO) == -1) {
			return true;
		}
		return false;
	}

	public BigDecimal getValue() {
		return value;
	}

}
