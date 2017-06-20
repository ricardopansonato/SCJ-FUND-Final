package br.com.fiap.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import br.com.fiap.command.Command;
import br.com.fiap.command.CommandFactory;
import br.com.fiap.dao.DAO;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.User;

public class FiapTelegramBot {

	
	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("fiap.properties"));

		TelegramBot bot = TelegramBotAdapter.build(properties.getProperty("TOKEN"));
		GetUpdatesResponse updatesResponse;
			
		CommandFactory factory = new CommandFactory();

		int position = 0;
		while (true) {
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(position));
			List<Update> updates = updatesResponse.updates();
			updates.forEach(u -> {
				System.out.println(u);
			});
			
			for (Update update : updates) {
				position = update.updateId() + 1;
				Pair<User, List<AccountStatement>> data = new DAO().getUserInformation(update.message().chat().id());
				if (data != null) {
					data.getKey().setPosition(position);
				}
				Command command = factory.createCommand(bot, update);
				command.execute();
			}
		}
	}

}
