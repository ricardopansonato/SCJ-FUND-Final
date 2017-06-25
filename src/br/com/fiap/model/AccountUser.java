package br.com.fiap.model;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import br.com.fiap.exception.OperationNotAllowed;

public class AccountUser extends Person {
	
	private static final long serialVersionUID = 6727348100801993533L;

	private Integer position;
	private List<Dependent> dependents;
	private Double accountBalance;
	private Double loanBalance;
	
	public AccountUser(Integer position) {
		this.create = true;
		this.position = position;
		this.accountBalance = 0d;
		this.loanBalance = 0d;
	}

	public List<Dependent> getDependents() {
		return dependents;
	}

	public void setDependents(List<Dependent> dependents) {
		this.dependents = dependents;
	}
	
	public Integer getPosition() {
		return position;
	}

	public void addPosition() {
		this.position += 1;
	}
	
	public String getFormattedAccountBalance() {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale ("pt", "BR"));
		return numberFormat.format(accountBalance);
	}
	
	public Double getAccountBalance() {
		return this.accountBalance;
	}

	public Double getLoanBalance() {
		return this.loanBalance;
	}
	
	public String getFormattedLoanBalance() {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale ("pt", "BR"));
		return numberFormat.format(loanBalance);
	}
	
	public void addLoanBalance(Double value) {
		this.loanBalance += value;
	}
	
	public void addAmount(Double amount) throws OperationNotAllowed {
		if ((this.accountBalance + amount) < 0) {
			throw new OperationNotAllowed("Saldo insuficiente!");
		}
		this.accountBalance += amount;
	}

	public boolean isEmpty() {
		return StringUtils.isBlank(getName());
	}
	
	public String welcomeMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Olá *").append(getName())
		  .append("*, bem vindo!\n");
		sb.append("Sua conta é *")
		  .append(getAccountNumber()).append("*.\n");
		sb.append("*Saldo atual:* ").append(getFormattedAccountBalance()).append("\n");
		sb.append("*Saldo de empréstimo:* ").append(getFormattedLoanBalance()).append("\n");
		sb.append("Em caso de dúvidas, execute */help*. Estamos aqui para ajudá-lo!");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("*Informações do titular da ").append(personType()).append("*\n");
		sb.append(super.toString());
		sb.append("*Saldo atual:* ").append(getFormattedAccountBalance()).append("\n");
		sb.append("*Saldo de empréstimo:* ").append(getFormattedLoanBalance()).append("\n");
		if (dependents != null) {
			sb.append("--------------------\n");
			sb.append("*DEPENDENTES*\n");
			sb.append("--------------------\n");
			
			dependents.forEach(d -> {
				sb.append(d.toString());
				sb.append("--------------------\n");
			});
		}
		return sb.toString();
	}

	@Override
	public String personType() {
		return "CONTA";
	}
}
