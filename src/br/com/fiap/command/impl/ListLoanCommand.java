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
import br.com.fiap.model.LoanStatement;

public class ListLoanCommand extends Command {
	
	public ListLoanCommand(TelegramBot bot, Update update) {
		super(bot, update);
	}

	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser, List<AccountStatement>> data = dao.getUserInformation(chatId);
		AccountUser user = data.getKey();
		if (!user.isEmpty()) {
			final StringBuffer sb = new StringBuffer();
			if (user.getLoanBalance() > 0) {
				sb.append("```------------------------\n")
				  .append("EMPRÉSTIMOS\n")
				  .append("------------------------\n");
				data.getValue().forEach(a -> {
					if (a instanceof LoanStatement) {
						LoanStatement l = (LoanStatement) a;
						sb.append(l.toString())
						  .append("------------------------\n");
					}
				});
				sb.append("```\n");
				SendMessage request = new SendMessage(chatId,  sb.toString()).parseMode(ParseMode.Markdown);
				bot.execute(request);
			}
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta não existe!*\nFavor executar o */create* para adicionar as informações").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}

}
