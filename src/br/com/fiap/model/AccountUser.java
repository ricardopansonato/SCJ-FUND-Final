package br.com.fiap.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class AccountUser extends Person {
	
	private static final long serialVersionUID = 6727348100801993533L;

	private Integer position;
	private List<Dependent> dependents;

	public AccountUser(Integer position) {
		this.create = true;
		this.position = position;
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
	
	public boolean isEmpty() {
		return StringUtils.isBlank(getName());
	}
	
	public String welcomeMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("Olá *").append(getName()).append("*, bem vindo!\n").append("Sua conta é *").append(getAccountNumber());
		sb.append("*.\nEm caso de dúvidas,\nexecute */help*.\nEstamos aqui para ajudá-los!");
		return sb.toString();
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("*Informações do titular da ").append(personType()).append("*\n");
		sb.append(super.toString());
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
