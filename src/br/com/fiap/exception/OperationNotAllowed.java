package br.com.fiap.exception;

public class OperationNotAllowed extends Exception {

	private static final long serialVersionUID = -4315957175937166477L;

	public OperationNotAllowed(String string) {
		super(string);
	}
}
