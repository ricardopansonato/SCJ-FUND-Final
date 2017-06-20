package br.com.fiap.command.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import br.com.fiap.command.Command;
import br.com.fiap.dao.DAO;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.User;

public class ShowAccountCommand implements Command {

	private TelegramBot bot;
	private Update update;
	
	public ShowAccountCommand(TelegramBot bot, Update update) {
		this.bot = bot;
		this.update = update;
	}
		
	@Override
	public void execute() {
		//DAO dao = new DAO();
		//User user = new User();
		//Pair<User, List<AccountStatement>> data = dao.getUserInformation(update.message().chat().id());
		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
		bot.execute(new SendMessage(update.message().chat().id(), "Consulta aos Dados de sua Conta:"));

		System.out.println("entrou show");
		
		DAO dao = new DAO();
		User user = new User();
		Pair<User, List<AccountStatement>> data = dao.getUserInformation(update.message().chat().id());
		if (data != null) {
		   System.out.println("consulta");
	        user = dao.getUser(update.message().chat().id());
	        System.out.println("capturar classe: " + user);  
	        System.out.println("CPF: " + user.getCpf());
	        System.out.println("DataNasc: " + user.getDataNasc());
	        System.out.println("Nome: " + user.getName());
			bot.execute(new SendMessage(update.message().chat().id(), "Nro Conta: " + user.getNroConta()));
	        bot.execute(new SendMessage(update.message().chat().id(), "CPF: " + user.getCpf()));
			bot.execute(new SendMessage(update.message().chat().id(), "Nome: " + user.getName()));
			bot.execute(new SendMessage(update.message().chat().id(), "Data Nasc: " + user.getDataNasc()));
		}
		else {
			bot.execute(new SendMessage(update.message().chat().id(), "Você precisa criar uma conta para acessar os seus dados " ));		}
	}	
	
}
