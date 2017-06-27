package br.com.fiap.command.impl;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.command.Command;
import br.com.fiap.exception.OperationNotAllowed;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.AccountStatementType;
import br.com.fiap.model.AccountUser;
import br.com.fiap.model.LoanStatement;

public class LoanCommand extends Command {
	
	public LoanCommand(TelegramBot bot, Update update, Long chatId) {
		super(bot, update, chatId);
	}

	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser,List<AccountStatement>> data = dao.getUserInformation(chatId);
		AccountUser user = data.getKey();
		if (!user.isEmpty()) {
			NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
			String valueText = readValue("Favor informar o valor de empréstimo", "Valor inválido!", DECIMAL_PATTERN);
			Double value = null;
			while(value == null) {
				try {
					value = numberFormat.parse(valueText).doubleValue();
					if (value > (user.getAccountBalance() * 40 - user.getLoanBalance())) {
						value = null;
						valueText = readValue("Valor não deve exceder 40x o saldo em conta!", "Valor inválido!", DECIMAL_PATTERN);
					}
				} catch (Exception e) {
					valueText = readValue("Valor inválido!", "Valor inválido!", DECIMAL_PATTERN);
				}
			}
			
			String installmentsText = readValue("Número de prestações", "Valor inválido!", NUMBER_PATTERN);
			Integer installments = null;
			while(installments == null) {
				try {
					installments = numberFormat.parse(installmentsText).intValue();
					if (installments > 36) {
						installments = null;
						installmentsText = readValue("Número de prestações não deve exceder 36x", "Valor inválido!", NUMBER_PATTERN);
					}
				} catch (Exception e) {
					installmentsText = readValue("Valor inválido!", "Valor inválido!", NUMBER_PATTERN);
				}
			}
			
			try {
				if (user.getAccountBalance() - AccountStatementType.LOAN_TAX.getValue() < 0 ) {
					throw new OperationNotAllowed("Saldo insuficiente!");
				}
				final LoanStatement statement = new LoanStatement();
				statement.setType(AccountStatementType.LOAN);
				statement.setValue(value);
				statement.setMonths(installments);
				dao.addAccountStatement(chatId, statement);
				
				final AccountStatement statementTax = new AccountStatement();
				statementTax.setType(AccountStatementType.LOAN_TAX);
				statementTax.setValue(AccountStatementType.LOAN_TAX.getValue());
				dao.addAccountStatement(chatId, statementTax);
				
				data = dao.getUserInformation(chatId);
				user = data.getKey();
				SendMessage request = new SendMessage(chatId, "*Operação realizada com sucesso!\nSaldo atual:* " + user.getFormattedAccountBalance()).parseMode(ParseMode.Markdown);
				bot.execute(request);
			} catch (OperationNotAllowed e) {
				StringBuilder sb = new StringBuilder();
				sb.append("*Saldo insuficiente! Seu saldo atual é ");
				sb.append(user.getFormattedAccountBalance()).append("*");
				SendMessage request = new SendMessage(chatId,  sb.toString()).parseMode(ParseMode.Markdown);
				bot.execute(request);
			}
			
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta não existe!*\nFavor executar o */create* para adicionar as informações").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}

}
