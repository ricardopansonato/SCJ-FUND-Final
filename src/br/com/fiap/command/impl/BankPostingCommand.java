package br.com.fiap.command.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.DoubleAdder;

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

public class BankPostingCommand extends Command {

	public BankPostingCommand(TelegramBot bot, Update update) {
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
			if (data.getRight().size() > 0) {
				sb.append("```------------------------\n")
				  .append("LANÇAMENTOS\n")
				  .append("------------------------\n");
				final List<Double> values = new ArrayList<>();
				data.getValue().forEach(a -> {
					if(a.getType().equals(AccountStatementType.DEPOSIT)) {
						values.add(a.getValue());
						sb.append(a.toString())
						  .append("------------------------\n");
					}
				});
				final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale ("pt", "BR"));
				final DoubleAdder sumAdder = new DoubleAdder();
				values.parallelStream().forEach(sumAdder::add);
				sb.append("Total: ").append(numberFormat.format(sumAdder.doubleValue())).append("```\n");
				SendMessage request = new SendMessage(chatId,  sb.toString()).parseMode(ParseMode.Markdown);
				bot.execute(request);
			} else {
				sb.append("Não foi realizado nenhum lançamento\n");
				SendMessage request = new SendMessage(chatId,  sb.toString()).parseMode(ParseMode.Markdown);
				bot.execute(request);
			}
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta não existe!*\nFavor executar o */create* para adicionar as informações").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}

}
