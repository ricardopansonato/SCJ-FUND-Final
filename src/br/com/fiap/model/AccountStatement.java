package br.com.fiap.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccountStatement implements Serializable {

	private static final long serialVersionUID = -2910473057541978965L;
	
	private Date date;
	private AccountStatementType type;
	private Double value;

	public AccountStatement() {
		this.date = new Date();
	}
	
	public Date getDate() {
		return date;
	}

	public AccountStatementType getType() {
		return type;
	}

	public void setType(AccountStatementType type) {
		this.type = type;
	}

	public Double getValue() {
		return value * type.getSign();
	}

	public String getFormattedValue() {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale ("pt", "BR"));
		return numberFormat.format(getValue());
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		final StringBuilder sb = new StringBuilder();
		sb.append("Data: ").append(dateFormat.format(date)).append("\n");
		sb.append("Descrição: ").append(type.getOperation()).append("\n");
		sb.append("Valor: ").append(getFormattedValue()).append("\n");
		return sb.toString();
	}
}
