package br.com.fiap.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.command.Command;
import br.com.fiap.dao.TelegramFileDAO;
import br.com.fiap.model.AccountUser;

public class WelcomeCommand extends Command {
	
	public WelcomeCommand(TelegramBot bot, Update update, Long chatId) {
		super(bot, update, chatId);
	}

	@Override
	public void execute() {
		TelegramFileDAO dao = new TelegramFileDAO();
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		AccountUser user = dao.getUser(chatId);
		if (user.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("*Olá ").append(chat.firstName()).append(" ");
			sb.append(chat.lastName()).append("*, bem vindo ao banco *FIAP TRABALHO FINAL*!\n");
			sb.append("Para iniciar a utilização dos nossos serviços,\nexecute */create* realizar o cadastro.");
			SendMessage request = new SendMessage(chatId, sb.toString())
					.parseMode(ParseMode.Markdown);
			bot.execute(request);
		} else {
			SendMessage request = new SendMessage(chatId, user.welcomeMessage()).parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}

}
