package br.com.fiap.model;

import java.util.List;

public class User {
	private String name;
	private List<Dependent> dependents;

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
}
