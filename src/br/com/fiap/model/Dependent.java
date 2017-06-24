package br.com.fiap.model;

public class Dependent extends Person {
	
	private static final long serialVersionUID = 3426637587039915894L;
	
	public Dependent() {
		this.create = true;
	}

	@Override
	public String personType() {
		return "DEPENDENTE";
	}
}
