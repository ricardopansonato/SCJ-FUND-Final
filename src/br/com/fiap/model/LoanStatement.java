package br.com.fiap.model;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LoanStatement extends AccountStatement {
	
	private static final long serialVersionUID = 4604901756881921575L;

	private Integer months;
	
	public Integer getMonths() {
		return months;
	}

	public void setMonths(Integer months) {
		this.months = months;
	}

	public String installments() {
		final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale ("pt", "BR"));
		final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		final StringBuilder sb = new StringBuilder(super.toString());
		sb.append("Número de parcelas: ").append(months).append("\n");
		sb.append("------------------------\n");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate());
		Double balance = 0d;
		for (int i = 1; i <= months; i++) {
			Double installment = (getValue() / months)*1.05;
			balance += installment;
			calendar.add(Calendar.MONTH, 1);
			sb.append(i).append(". ")
			  .append(numberFormat.format(installment))
			  .append(" vencimento: ").append(dateFormat.format(calendar.getTime()))
			  .append("\n");
		}
		sb.append("------------------------\n");
		sb.append("Saldo atual: ").append(numberFormat.format(balance)).append("\n");
		return sb.toString();
	}
}
