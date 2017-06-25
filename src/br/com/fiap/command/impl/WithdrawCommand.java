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

public class WithdrawCommand extends Command {
	
	public WithdrawCommand(TelegramBot bot, Update update) {
		super(bot, update);
	}

	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser,List<AccountStatement>> data = dao.getUserInformation(chatId);
		AccountUser user = data.getKey();
		if (!user.isEmpty()) {
			Double value = null;
			NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
			String valueText = readValue("Valor informar o valor de saque", "Valor inválido!", DECIMAL_PATTERN);
			while(value == null) {
				try {
					value = numberFormat.parse(valueText).doubleValue();
				} catch (Exception e) {
					valueText = readValue("Valor inválido!", "Valor inválido!", DECIMAL_PATTERN);
				}
			}
			
			try {
				final AccountStatement statement = new AccountStatement();
				statement.setType(AccountStatementType.WITHDRAW);
				statement.setValue(value);
				if (user.getAccountBalance() + (statement.getValue() - AccountStatementType.WITHDRAW_TAX.getValue()) < 0 ) {
					throw new OperationNotAllowed("Saldo insuficiente!");
				}
				dao.addAccountStatement(chatId, statement);

				final AccountStatement statementTax = new AccountStatement();
				statementTax.setType(AccountStatementType.WITHDRAW_TAX);
				statementTax.setValue(AccountStatementType.WITHDRAW_TAX.getValue());
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
