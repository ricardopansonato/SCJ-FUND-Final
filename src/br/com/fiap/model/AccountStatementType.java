package br.com.fiap.model;

public enum AccountStatementType {
	WITHDRAW(-1),
	DEPOSIT(1),
	LOAN(1),
	PAYMENT_LOAN(-1);
	
	private Integer sign;
	
	AccountStatementType(Integer sign) {
		this.sign = sign;
	}

	public Integer getSign() {
		return sign;
	}

}
