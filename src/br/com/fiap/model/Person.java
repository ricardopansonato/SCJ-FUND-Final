package br.com.fiap.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public abstract class Person implements Serializable {

	private static final long serialVersionUID = 3087939653744046069L;

	private String name;
	private String cpf;
	private Date birthDate;
	private String accountNumber;
	protected Boolean create;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String accountMessage() {
		DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		StringBuilder sb = new StringBuilder();
		if (this.create) {
			this.create = false;
			sb.append("*").append(personType()).append(" criada(o) com sucesso!*\n");
		} else {
			sb.append("*").append(personType()).append(" modificada(o) com sucesso!*\n");
		}
		
		if (StringUtils.isNotBlank(accountNumber)) {
			sb.append("*Número da conta:* ").append(getAccountNumber()).append("\n");
		}
		
		sb.append("*Nome completo:* ").append(getName()).append("\n");
		sb.append("*CPF:* ").append(getCpf()).append("\n");
		sb.append("*Data de nascimento:* ").append(dt.format(getBirthDate())).append("\n");
		return sb.toString();
	}
	
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		StringBuilder sb = new StringBuilder();
		sb.append("*Nome:* ").append(name).append("\n");
		sb.append("*CPF:* ").append(cpf).append("\n");
		if (birthDate != null) {
			sb.append("*Data de nascimento:* ").append(dateFormat.format(birthDate)).append("\n");
		}
		if (StringUtils.isNotBlank(accountNumber)) {
			sb.append("*Número da conta:* ").append(accountNumber).append("\n");
		}
		return sb.toString();
	}

	public abstract String personType();
}
