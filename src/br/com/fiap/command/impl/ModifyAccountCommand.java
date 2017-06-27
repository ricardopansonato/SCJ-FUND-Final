package br.com.fiap.command.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class ModifyAccountCommand extends Command {

	public ModifyAccountCommand(TelegramBot bot, Update update, Long chatId) {
		super(bot, update, chatId);
	}
		
	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser,List<AccountStatement>> userInformation = dao.getUserInformation(chatId);
		AccountUser user = userInformation.getKey();
		if (!user.isEmpty()) {
			String name = readValue("Informe seu nome completo (Para continuar com o nome *" + user.getName() + "*, basta mandar o texto *manter*)", 
									"Tamanho de *nome* inválido! Deve ter no mínimo 3 caracteres!", NAME_PATTERN);
			if (KEEP.equalsIgnoreCase(name)) {
				name = user.getName();
			}
			user.setName(name);
			
			String cpf = readValue("Informe o CPF (Para continuar com o cpf *" + user.getCpf() + "*, basta mandar o texto *manter*)", 
								   "CPF inválido! Favor informar somente números!", CPF_MODIFY_PATTERN);
			if (KEEP.equalsIgnoreCase(cpf)) {
				cpf = user.getCpf();
			}
			user.setCpf(cpf);
			
			final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateFormat.setLenient(true);
			
			String birthDateText = readValue("Informe a data de nascimento (Para continuar com a data *" + dateFormat.format(user.getBirthDate()) + "*, basta mandar o texto *manter*)", 
											 "Data inválida! Favor informar no formato DD/MM/AAAA", DATE_MODIFY_PATTERN);
			Date birthDate = null;
			if (KEEP.equalsIgnoreCase(birthDateText)) {
				birthDate = user.getBirthDate();
			} else {
				while (birthDate == null) {
					try {
						birthDate = dateFormat.parse(birthDateText);
					} catch (Exception e) {
						birthDateText = readValue("Data de nascimento inválida", "Data de nascimento inválida", DATE_MODIFY_PATTERN);
					}
				}
			}

			user.setBirthDate(birthDate);
			dao.addUser(chatId, user);
			
			final SendMessage request = new SendMessage(chatId, user.accountMessage()).parseMode(ParseMode.Markdown);
			bot.execute(request);
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta não existe!*\nFavor executar o */create* para adicionar as informações").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}	
}