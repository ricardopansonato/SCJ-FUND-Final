package br.com.fiap.command;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import br.com.fiap.dao.TelegramFileDAO;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.AccountUser;

public abstract class Command {

	public static final String DEFAULT = "padrão";
	public static final String KEEP = "manter";
	
	public static final String NAME_PATTERN = ".{3,}";
	public static final String CPF_PATTERN = "[0-9]{11}";
	public static final String DATE_PATTERN = "[0-9]{2}/[0-9]{2}/[0-9]{4}";
	public static final String DECIMAL_PATTERN = "[0-9.,]{1,}";
	public static final String NUMBER_PATTERN = "[0-9]{1,}";
	public static final String CPF_MODIFY_PATTERN = "(?i)([0-9]{11}|manter)";
	public static final String DATE_MODIFY_PATTERN = "(?i)([0-9]{2}/[0-9]{2}/[0-9]{4}|manter)";
	
	protected TelegramBot bot;
	protected Update update;
	protected TelegramFileDAO dao = new TelegramFileDAO();
	protected Long chatId;

	public Command(TelegramBot bot, Update update, Long chatId) {
		this.bot = bot;
		this.update = update;
		this.chatId = chatId;
	}

	public abstract void execute();

	protected String readValue(String message, String error, String pattern) {
		String toReturn = StringUtils.EMPTY;
		Boolean isFirstLoop = true;
		Long chatId = update.message().chat().id();
		while (!toReturn.matches(pattern)) {
			Pair<AccountUser, List<AccountStatement>> userInformation = dao.getUserInformation(chatId);
			AccountUser user = checkUserInformation(userInformation);
			if (isFirstLoop) {
				bot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
				isFirstLoop = false;
			} else {
				toReturn = retrieveValue(error, pattern, user);
			}
		}

		return toReturn;
	}

	private String retrieveValue(String error, String pattern, AccountUser user) {
		GetUpdatesResponse updates = bot.execute(new GetUpdates().limit(100).offset(user.getPosition()).allowedUpdates("message"));
		Long chatId = update.message().chat().id();
		String toReturn = StringUtils.EMPTY;
		if (updates.updates() != null && !updates.updates().isEmpty()) {
			for (Update u : updates.updates()) {
				if (this.chatId.equals(u.message().chat().id())) {
					toReturn = u.message().text();
					user.addPosition();
					dao.addUser(chatId, user);
					if (!toReturn.matches(pattern)) {
						bot.execute(new SendMessage(chatId, error).parseMode(ParseMode.Markdown));
					}
				}
			}
		}
		return toReturn;
	}

	private AccountUser checkUserInformation(Pair<AccountUser, List<AccountStatement>> userInformation) {
		AccountUser user = new AccountUser(update.updateId() + 1);
		if (userInformation != null) {
			user = userInformation.getKey();
		}
		dao.addUser(update.message().chat().id(), user);
		return user;
	}
}
