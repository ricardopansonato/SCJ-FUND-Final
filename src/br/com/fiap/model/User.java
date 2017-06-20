package br.com.fiap.model;

import java.util.Date;
import java.util.List;

public class User {
	private String name;
	private Integer position;
	private List<Dependent> dependents;
	private String cpf;
	private Date dataNasc; 
	private String nroConta;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Dependent> getDependents() {
		return dependents;
	}
	public void setDependents(List<Dependent> dependents) {
		this.dependents = dependents;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Date getDataNasc() {
		return dataNasc;
	}
	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}
	public String getNroConta() {
		return nroConta;
	}
	public void setNroConta(String nroConta) {
		this.nroConta = nroConta;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
}
