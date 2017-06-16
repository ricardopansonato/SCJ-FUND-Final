package br.com.fiap.model;

public class AccountStatement {
	private AccountStatementType type;
	private Double value;

	public AccountStatementType getType() {
		return type;
	}

	public void setType(AccountStatementType type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
