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
import br.com.fiap.model.AccountUser;

public class ShowAccountCommand extends Command {

	public ShowAccountCommand(TelegramBot bot, Update update, Long chatId) {
		super(bot, update, chatId);
	}

	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser,List<AccountStatement>> userInformation = dao.getUserInformation(chatId);
		AccountUser user = userInformation.getKey();
		if (!user.isEmpty()) {
			SendMessage request = new SendMessage(chatId, user.toString()).parseMode(ParseMode.Markdown);
			bot.execute(request);
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta não cadastrada!*\nFavor executar o */create* para cadastrar a conta").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}	
	
}
