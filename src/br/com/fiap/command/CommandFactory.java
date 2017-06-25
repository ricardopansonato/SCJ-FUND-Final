package br.com.fiap.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import br.com.fiap.command.impl.AccountStatementsCommand;
import br.com.fiap.command.impl.BankDraftCommand;
import br.com.fiap.command.impl.BankFeesCommand;
import br.com.fiap.command.impl.BankPostingCommand;
import br.com.fiap.command.impl.CreateAccountCommand;
import br.com.fiap.command.impl.CreateDependentAccountCommand;
import br.com.fiap.command.impl.DepositCommand;
import br.com.fiap.command.impl.HelpCommand;
import br.com.fiap.command.impl.LoanCommand;
import br.com.fiap.command.impl.ListLoanCommand;
import br.com.fiap.command.impl.ModifyAccountCommand;
import br.com.fiap.command.impl.ShowAccountCommand;
import br.com.fiap.command.impl.WelcomeCommand;
import br.com.fiap.command.impl.WithdrawCommand;

public class CommandFactory {
	public Command createCommand(TelegramBot bot, Update update) {
		if (update.message() != null) {
			if (update.message().text().startsWith("/start")) {
				return new WelcomeCommand(bot, update);
			} else if (update.message().text().startsWith("/help")) {
				return new HelpCommand(bot, update);
		    } else if (update.message().text().startsWith("/create")) { 
			    return new CreateAccountCommand(bot, update);
		    } else if (update.message().text().startsWith("/show")) { 
			    return new ShowAccountCommand(bot, update);
		    } else if (update.message().text().startsWith("/deposit")) { 
			    return new DepositCommand(bot, update);
		    } else if (update.message().text().startsWith("/withdraw")) { 
			    return new WithdrawCommand(bot, update);
		    } else if (update.message().text().startsWith("/modify")) { 
			    return new ModifyAccountCommand(bot, update);
		    } else if (update.message().text().startsWith("/statements")) { 
			    return new AccountStatementsCommand(bot, update);
		    } else if (update.message().text().startsWith("/dependent")) { 
			    return new CreateDependentAccountCommand(bot, update);
		    } else if (update.message().text().startsWith("/loan")) { 
			    return new LoanCommand(bot, update);
		    } else if (update.message().text().startsWith("/list_loan")) { 
			    return new ListLoanCommand(bot, update);
		    } else if (update.message().text().startsWith("/bank_draft")) { 
			    return new BankDraftCommand(bot, update);
		    } else if (update.message().text().startsWith("/bank_fees")) { 
			    return new BankFeesCommand(bot, update);
		    } else if (update.message().text().startsWith("/bank_posting")) { 
			    return new BankPostingCommand(bot, update);
		    }
		}
		return new HelpCommand(bot, update);
		
	}
}