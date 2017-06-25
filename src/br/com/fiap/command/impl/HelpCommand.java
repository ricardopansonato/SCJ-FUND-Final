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
		sb.append("*/create* - Criação de conta\n");
		sb.append("*/modify* - Modificação de conta\n");
		sb.append("*/dependent* - Inclusão de dependentes (conta-conjunta)\n");
		sb.append("*/show* - Criação de conta\n");
		sb.append("*/deposit* - Depósito\n");
		sb.append("*/withdraw* - Saque\n");
		sb.append("*/statements* - Solicitação de extrato\n");
		sb.append("*/loan* - Solicitação de empréstimo\n");
		sb.append("*/list_loan* - Exibição de saldo devedor do empréstimo e prazo de pagamento\n");
		sb.append("*/bank_posting* - Exibição dos lançamentos detalhada\n");
		sb.append("*/bank_draft* - Exibição das retiradas\n");
		sb.append("*/bank_fees* - Exibição das tarifas de serviço\n");
		SendMessage request = new SendMessage(chatId, sb.toString()).parseMode(ParseMode.Markdown);
		bot.execute(request);		
	}

}
