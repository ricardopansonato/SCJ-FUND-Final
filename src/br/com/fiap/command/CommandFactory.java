package br.com.fiap.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import br.com.fiap.command.impl.CreateCommand;
import br.com.fiap.command.impl.HelpCommand;

public class CommandFactory {
	public Command createCommand(TelegramBot bot, Update update) {
		if (update.message().text().startsWith("/start")) {
			return new CreateCommand(bot, update);
		} else if (update.message().text().startsWith("/help")) {
			return new HelpCommand(bot, update);
		} else {
			return new HelpCommand(bot, update);
		}
	}
}
