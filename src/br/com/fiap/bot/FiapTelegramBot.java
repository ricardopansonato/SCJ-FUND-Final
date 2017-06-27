package br.com.fiap.bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import br.com.fiap.command.Command;
import br.com.fiap.command.CommandFactory;
import br.com.fiap.dao.TelegramFileDAO;

public class FiapTelegramBot {

	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("fiap.properties"));

		TelegramBot bot = TelegramBotAdapter.build(properties.getProperty("TOKEN"));
		GetUpdatesResponse updatesResponse;

		CommandFactory factory = new CommandFactory();
		TelegramFileDAO dao = new TelegramFileDAO();

		final int numThreads = 15;
		ExecutorService exec = Executors.newFixedThreadPool(numThreads);

		final Map<Long, Long> users = new HashMap<>();
		int position = 0;
		while (true) {
			bot.execute(new GetUpdates().limit(100).offset(position));
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(position));
			List<Update> updates = updatesResponse.updates();
			for (Update update : updates) {
				Long chatId = update.message().chat().id();
				if (!users.containsKey(chatId)) {
					position = update.updateId() + 1;
					exec.execute(new Runnable() {
						public void run() {
							long threadId = Thread.currentThread().getId();
							int localPosition = update.updateId() - 1;
							users.put(chatId, threadId);
							
							Command command = factory.createCommand(bot, update);
							command.execute();
							
							GetUpdatesResponse updatesResponse;
							while (true) {
								bot.execute(new GetUpdates().limit(100).offset(localPosition));
								updatesResponse = bot.execute(new GetUpdates().limit(100).offset(localPosition));
								List<Update> updatesLocal = updatesResponse.updates();
								for (Update updateLocal : updatesLocal) {
									Long chatIdLocal = updateLocal.message().chat().id();
									if (chatId.equals(chatIdLocal)) {
										dao.incrementPosition(chatId, updateLocal.updateId());
										command = factory.createCommand(bot, updateLocal);
										command.execute();
										localPosition = dao.getPosition(chatId, updateLocal.updateId());
									}
								}
								
							}
						}
					});
				}
			}
		}
	}

}
