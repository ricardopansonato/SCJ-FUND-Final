package br.com.fiap.model;

public enum AccountStatementType {
	WITHDRAW(-1, "Saque"),
	WITHDRAW_TAX(-1, "Taxa do saque"),
	ACCOUNT_STATEMENTS_TAX(-1, "Taxa do extrato"),
	LOAN_TAX(-1, "Taxa do empréstimo"),
	DEPOSIT(1, "Depósito"),
	LOAN(1, "Empréstimo"),
	PAYMENT_LOAN(-1, "Pagamento do emprestimo");
	
	private Integer sign;
	private String operation;
	
	AccountStatementType(Integer sign, String operation) {
		this.sign = sign;
		this.operation = operation;
	}

	public Integer getSign() {
		return sign;
	}

	public String getOperation() {
		return operation;
	}
}
