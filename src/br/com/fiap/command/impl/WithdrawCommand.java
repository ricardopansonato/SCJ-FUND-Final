package br.com.fiap.command.impl;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.command.Command;
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
		Pair<AccountUser,List<AccountStatement>> userInformation = dao.getUserInformation(chatId);
		AccountUser user = userInformation.getKey();
		if (!user.isEmpty()) {
			Double value = null;
			String valueText = readValue("Valor informar o valor que deve ser depositado", "Valor inválido!", NUMBER_PATTERN);
			while(value == null) {
				try {
					value = Double.valueOf(valueText);
				} catch (Exception e) {
					valueText = readValue("Valor inválido!", "Valor inválido!", NUMBER_PATTERN);
				}
			}
			AccountStatement statement = new AccountStatement();
			statement.setType(AccountStatementType.DEPOSIT);
			statement.setValue(value);
			dao.addAccountStatement(chatId, statement);
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta não existe!*\nFavor executar o */create* para adicionar as informações").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}

}
