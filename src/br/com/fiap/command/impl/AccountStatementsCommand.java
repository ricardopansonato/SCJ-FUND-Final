package br.com.fiap.command.impl;

import java.util.List;

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

public class AccountStatementsCommand extends Command {
	
	private static final Double ACCOUNT_STATEMENTS_TAX = 1d;
	
	public AccountStatementsCommand(TelegramBot bot, Update update) {
		super(bot, update);
	}

	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser, List<AccountStatement>> data = dao.getUserInformation(chatId);
		AccountUser user = data.getKey();
		if (!user.isEmpty()) {
			try {
				AccountStatement statement = new AccountStatement();
				statement.setType(AccountStatementType.ACCOUNT_STATEMENTS_TAX);
				statement.setValue(ACCOUNT_STATEMENTS_TAX);
				
				if (user.getAccountBalance() + (statement.getValue() - ACCOUNT_STATEMENTS_TAX) < 0 ) {
					throw new OperationNotAllowed("Saldo insuficiente!");
				}
				
				dao.addAccountStatement(chatId, statement);
				data = dao.getUserInformation(chatId);
				user = data.getKey();
				final StringBuffer sb = new StringBuffer();
				sb.append("```------------------------\n")
				  .append("EXTRATO\n")
				  .append("------------------------\n");
				data.getValue().forEach(a -> {
					sb.append(a.toString())
					  .append("------------------------\n");
				});
				sb.append("Saldo atual: ").append(user.getFormattedAccountBalance()).append("```\n");
				SendMessage request = new SendMessage(chatId,  sb.toString()).parseMode(ParseMode.Markdown);
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
