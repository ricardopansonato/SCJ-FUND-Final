package br.com.fiap.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import br.com.fiap.command.impl.CreateAccountCommand;
import br.com.fiap.command.impl.CreateCommand;
import br.com.fiap.command.impl.HelpCommand;
import br.com.fiap.command.impl.ModifyAccountCommand;
import br.com.fiap.command.impl.ShowAccountCommand;
import br.com.fiap.command.impl.WelcomeCommand;

public class CommandFactory {
	public Command createCommand(TelegramBot bot, Update update) {
		if (update.message() != null) {
			if (update.message().text().startsWith("/start")) {
				return new CreateCommand(bot, update);
			} else if (update.message().text().startsWith("/help")) {
				return new HelpCommand(bot, update);
			} else if (update.message().text().startsWith("/welcome")) {
				return new WelcomeCommand(bot, update);
		    } else if (update.message().text().startsWith("/create")) { 
			    return new CreateAccountCommand(bot, update);
		    } else if (update.message().text().startsWith("/show")) { 
			    return new ShowAccountCommand(bot, update);
		    } else if (update.message().text().startsWith("/modify")) { 
			    return new ModifyAccountCommand(bot, update, update.message().text());
		    }    
		}
		return new HelpCommand(bot, update);
		
	}
}
