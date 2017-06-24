package br.com.fiap.model;

import java.io.Serializable;

public class AccountStatement implements Serializable {

	private static final long serialVersionUID = -2910473057541978965L;
	
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
