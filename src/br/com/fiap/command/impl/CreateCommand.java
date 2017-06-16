package br.com.fiap.command.impl;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.command.Command;
import br.com.fiap.dao.DAO;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.User;

public class CreateCommand implements Command {
	
	private TelegramBot bot;
	private Update update;

	public CreateCommand(TelegramBot bot, Update update) {
		this.bot = bot;
		this.update = update;
	}
	
	@Override
	public void execute() {
		DAO dao = new DAO();
		User user = new User();
		Pair<User, List<AccountStatement>> data = dao.getUserInformation(update.message().chat().id());
		if (data == null) {
			user.setName(update.message().chat().firstName() + " " + update.message().chat().lastName());
			dao.addUser(update.message().chat().id(), user);
		}
		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
		StringBuilder sb = new StringBuilder();
		if (data == null) {
			sb.append("Olá ").append(user.getName()).append(", p");
		} else {
			sb.append("P");
		}
		sb.append("ara que possa ajudá-lo, digite /help e veja a opção que melhor te atende");
		bot.execute(new SendMessage(update.message().chat().id(), sb.toString()));
	}

}
