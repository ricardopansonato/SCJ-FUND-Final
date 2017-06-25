package br.com.fiap.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.command.Command;

public class HelpCommand extends Command {
		
	public HelpCommand(TelegramBot bot, Update update) {
		super(bot, update);
	}

	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		StringBuilder sb = new StringBuilder();
		sb.append("*/start* - Tela de boas-vindas do banco\n");
		sb.append("*/create* - Cria��o de conta\n");
		sb.append("*/modify* - Modifica��o de conta\n");
		sb.append("*/dependent* - Inclus�o de dependentes (conta-conjunta)\n");
		sb.append("*/show* - Cria��o de conta\n");
		sb.append("*/deposit* - Dep�sito\n");
		sb.append("*/withdraw* - Saque\n");
		sb.append("*/statements* - Solicita��o de extrato\n");
		sb.append("*/loan* - Solicita��o de empr�stimo\n");
		sb.append("*/list_loan* - Exibi��o de saldo devedor do empr�stimo e prazo de pagamento\n");
		sb.append("*/bank_posting* - Exibi��o dos lan�amentos detalhada\n");
		sb.append("*/bank_draft* - Exibi��o das retiradas\n");
		sb.append("*/bank_fees* - Exibi��o das tarifas de servi�o\n");
		SendMessage request = new SendMessage(chatId, sb.toString()).parseMode(ParseMode.Markdown);
		bot.execute(request);		
	}

}
