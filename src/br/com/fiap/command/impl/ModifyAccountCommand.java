package br.com.fiap.command.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import br.com.fiap.command.impl.ShowAccountCommand;

import br.com.fiap.command.Command;
import br.com.fiap.dao.DAO;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.User;

public class ModifyAccountCommand implements Command {

	private TelegramBot bot;
	private Update update;
	private String texto;
	
	public ModifyAccountCommand(TelegramBot bot, Update update, String texto) {
		this.bot = bot;
		this.update = update;
		this.texto = texto;
	}
	
    public boolean validarCpf(String cpf) {
		if (cpf.length() != 11) {
			return false;
		}
		return true;
	}

		
	@Override
	public void execute() {
		DAO dao = new DAO();
		User user = new User();
		Pair<User, List<AccountStatement>> data = dao.getUserInformation(update.message().chat().id());
		if (data == null) {
			bot.execute(new SendMessage(update.message().chat().id(), "Você precisa criar uma conta para alterar os seus dados " ));
			return;
		}

        bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
		ShowAccountCommand showAccountCommand = new ShowAccountCommand(bot, update);
	    showAccountCommand.execute();

		int k = texto.indexOf(" ");
		System.out.println("k: " + k);
		if ( (k == -1) || (texto.equals("/modify")) ) {
			   bot.execute(new SendMessage(update.message().chat().id(), "Para alterar os seus dados, por favor informe o comando referente ao dado que deseja alterar, conforme os exemplos abaixo:"));
			   bot.execute(new SendMessage(update.message().chat().id(), "Para alterar o CPF, digite: /modify/cpf novo_cpf. Exemplo: /modify/cpf 12345678900"));
			   bot.execute(new SendMessage(update.message().chat().id(), "Para alterar o Nome, digite: /modify/nome novo_nome. Exemplo: /modify/cpf Venus Rodrigues Amaral"));
    		return;
		}

		if (texto.startsWith("/modify/cpf")) {

    		String cpf = texto.substring(texto.indexOf(" ") + 1);

    		if (!validarCpf(cpf)) {
        		bot.execute(new SendMessage(update.message().chat().id(), "Para alterar o CPF, informe corretamente o comando e CPF. Exemplo /modify/cpf 1234567800"));
        		return;
    		}

    		bot.execute(new SendMessage(update.message().chat().id(), "Alterando o CPF"));

    		System.out.println("entrou /modify/cpf: " + cpf);
    	    user = dao.getUser(update.message().chat().id());
    	    user.setCpf(cpf);
    	    dao.addUserAccount(update.message().chat().id(), user);
    		System.out.println("inserindo dados no dao");    	
    			
		}

		if (texto.startsWith("/modify/nome")) {

    		String nome = texto.substring(texto.indexOf(" ") + 1);
    		
    		if (nome.equals(null)) {
        		bot.execute(new SendMessage(update.message().chat().id(), "Para alterar o Nome, informe corretamente o comando e Nome. Exemplo /modify/cpf José Maria da Silva"));

        		
        		return;
    		}

    		bot.execute(new SendMessage(update.message().chat().id(), "Alterando o Nome"));

    		System.out.println("entrou /modify/nome: " + nome);
    	    user = dao.getUser(update.message().chat().id());
    	    user.setName(nome);
    	    dao.addUserAccount(update.message().chat().id(), user);
    		System.out.println("inserindo dados no dao");    	
    			
		}
		
		bot.execute(new SendMessage(update.message().chat().id(), "Dados após a alteração:"));    		
		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
		showAccountCommand = new ShowAccountCommand(bot, update);
	    showAccountCommand.execute();

		
		
		
		/*DAO dao = new DAO();
		User user = new User();
		Pair<User, List<AccountStatement>> data = dao.getUserInformation(update.message().chat().id());
		if (data != null) {
		   System.out.println("modify");
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
			*/
			
	}	
	
}
