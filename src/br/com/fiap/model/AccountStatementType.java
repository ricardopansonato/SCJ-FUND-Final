package br.com.fiap.model;

public enum AccountStatementType {
	WITHDRAW(-1, "Saque", 0d),
	WITHDRAW_TAX(-1, "Taxa do saque", 2.5d),
	ACCOUNT_STATEMENTS_TAX(-1, "Taxa do extrato", 1d),
	LOAN_TAX(-1, "Taxa do empr�stimo", 15d),
	DEPOSIT(1, "Dep�sito", 0d),
	LOAN(1, "Empr�stimo", 0d),
	PAYMENT_LOAN(-1, "Pagamento do empr�stimo", 0d);
	
	private Integer sign;
	private String operation;
	private Double value;
	
	AccountStatementType(Integer sign, String operation, Double value) {
		this.sign = sign;
		this.operation = operation;
		this.value = value;
	}

	public Integer getSign() {
		return sign;
	}

	public String getOperation() {
		return operation;
	}

	public Double getValue() {
		return value;
	}
}
