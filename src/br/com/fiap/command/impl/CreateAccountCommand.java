package br.com.fiap.command.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import br.com.fiap.command.Command;
import br.com.fiap.dao.DAO;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.User;

public class CreateAccountCommand implements Command {
	
	private TelegramBot bot;
	private Update update;
	boolean sairCreate = false;

	public CreateAccountCommand(TelegramBot bot, Update update) {
		this.bot = bot;
		this.update = update;
	}
	
    public boolean validarCpf(String cpf) {
		if (cpf.length() != 11) {
			return false;
		}
		return true;
	}
	
    public boolean validarDataNasc(String dataNasc) {
    	if (dataNasc.length() != 10) {
    		System.out.println("data invalida");
    		return false;
    	}
		//implementar validação de data
		return true;
	}
  
    public StringBuilder criarNroConta(String cpf, Date dataNasc) {
		//Nro da Conta - 4 primeiros digitos do CPF e dia do Nascimento como dígito 
    	StringBuilder conta = new StringBuilder();
    	conta.append(cpf.substring(0,4));
    	conta.append("-01");
    	System.out.println("conta: " + conta);
		return conta;
	}

    public String insereCpf() {
		GetUpdatesResponse updatesResponseCreate;
        boolean firstTime = true;
        int positionCreate = 0;
        Pair<User, List<AccountStatement>> data = new DAO().getUserInformation(update.message().chat().id());
		if (data != null) {
			positionCreate = data.getKey().getPosition();
		}
        
		String cpf = null;
		
        // Loop para recuperar CPF
		boolean executaLoop = true;
		while (executaLoop) {
			updatesResponseCreate = bot.execute(new GetUpdates().limit(100).offset(positionCreate));
			List<Update> updatesCreate = updatesResponseCreate.updates();
			updatesCreate.forEach(u -> {
				System.out.println(u);
			});
			for (Update update_for : updatesCreate) {
				positionCreate = update_for.updateId() + 1;

				cpf = update_for.message().text();
				System.out.println("Recebendo mensagem:"+ cpf);
			
				if (cpf.equals("/sair")) {
					System.out.println("digitado sair");
					executaLoop = false;
					sairCreate = true;
					break;
				}
				
				System.out.println("continua");
				if (firstTime) { 
			        bot.execute(new SendMessage(update_for.message().chat().id(), "Obrigado por ter escolhido o Banco FIAP. Para abrir sua conta corrente, por favor informe os dados solicitados a seguir: "));
					bot.execute(new SendMessage(update_for.message().chat().id(), "Informe seu CPF (11 numeros sem pontos ou traços): "));
					
					firstTime = false;
				}
				else {

    				if (!validarCpf(cpf)) {
	    				bot.execute(new SendMessage(update_for.message().chat().id(), "Por favor, insira um CPF válido. (ou /sair para cancelar a Criacao da Conta"));
			    	} // falta tecla para Sair
    				else {

    					executaLoop = false;
    				}
				}
  			
			}
		}
		
				
		System.out.println("saiu loop CPF");
    	return cpf;
    }

    
    public Date insereDataNasc() {
        // Loop para recuperar Data de Nascimento
		GetUpdatesResponse updatesResponseNome;
		Date dataNasc = null; // passar para Date
		String sdataNasc = null;
		boolean executaLoop = true;
        int positionCreate = 0;
        Pair<User, List<AccountStatement>> data = new DAO().getUserInformation(update.message().chat().id());
		if (data != null) {
			positionCreate = data.getKey().getPosition();
		}
		sdataNasc = update.message().text();
        
		while (executaLoop) {
			updatesResponseNome = bot.execute(new GetUpdates().limit(100).offset(positionCreate));
			List<Update> updatesCreate = updatesResponseNome.updates();
			for (Update update_for : updatesCreate) {
				positionCreate = update_for.updateId() + 1;

				System.out.println("position create" + positionCreate);
				sdataNasc = update_for.message().text();

				if (sdataNasc.equals("/sair")) {
					System.out.println("digitado sair");
					executaLoop = false;
					sairCreate = true;
					break;
				}
				
				System.out.println("datanasc: " + sdataNasc);
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				try {
					dataNasc = formato.parse(sdataNasc);
					executaLoop = false;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					bot.execute(new SendMessage(update.message().chat().id(), "Informe sua Data de Nascimento(DD/MM/YYYY) ou /sair para Cancelar: "));
				}
			    				
				System.out.println("Recebendo mensagem:"+ dataNasc);
				
			}
		}
		System.out.println("saiu loop dataNasc");
    	return dataNasc;
    }
    
    
	@Override
	public void execute() {
		GetUpdatesResponse updatesResponseCreate;
        boolean firstTime = true;
        sairCreate = false;
		
        int positionCreate = 0;
		String cpf = null;
		Date dataNasc = null;
		String endereco = null;
		
		DAO dao = new DAO();
        User user = new User();
        User left = new User();
		
        cpf = insereCpf();
        if (!sairCreate){dataNasc = insereDataNasc(); }

        if (!sairCreate) {
        
		System.out.println("criar conta");
		StringBuilder conta = criarNroConta(cpf, dataNasc);
		bot.execute(new SendMessage(update.message().chat().id(), "Sua conta corrente foi criada: " + conta));		
		
        user.setCpf(cpf);
        user.setDataNasc(dataNasc);
        user.setNroConta(conta.toString());
		user.setName(update.message().chat().firstName() + " " + update.message().chat().lastName());
		dao.addUserAccount(update.message().chat().id(), user);
        System.out.println("inserindo dados no dao");    	
		
//        User left = new User();
        left = dao.getUser(update.message().chat().id());
        
        System.out.println("capturar classe: " + left);  
        System.out.println("CPF: " + left.getCpf());
        System.out.println("DataNasc: " + left.getDataNasc());
        System.out.println("Nome: " + left.getName());
        
        }
        else {
    		bot.execute(new SendMessage(update.message().chat().id(), "Você saiu da opção /create "));		
        }
        
	}	

    
}
