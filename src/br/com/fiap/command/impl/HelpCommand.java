package br.com.fiap.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.command.Command;

public class HelpCommand implements Command {
	
	private TelegramBot bot;
	private Update update;

	public HelpCommand(TelegramBot bot, Update update) {
		this.bot = bot;
		this.update = update;
	}
	
	@Override
	public void execute() {
		System.out.println(update.message().chat().id());
		bot.execute(new SendMessage(update.message().chat().id(), "Help"));
		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

}
