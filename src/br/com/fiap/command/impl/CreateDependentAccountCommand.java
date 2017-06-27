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
import br.com.fiap.model.Dependent;
import br.com.fiap.model.AccountUser;

public class CreateDependentAccountCommand extends Command {

	public CreateDependentAccountCommand(TelegramBot bot, Update update, Long chatId) {
		super(bot, update, chatId);
	}

	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser,List<AccountStatement>> userInformation = dao.getUserInformation(chatId);
		AccountUser user = userInformation.getKey();
		if (!user.isEmpty()) {
			Dependent dependent = new Dependent();
			
			String name = readValue("Informe o nome completo do dependente", 
									"Tamanho de *nome* inválido! Deve ter no mínimo 3 caracteres!", NAME_PATTERN);
			dependent.setName(name);
			dependent.setCpf(readValue("Informe o CPF", 
									   "CPF inválido! Favor informar somente números!", CPF_PATTERN));
			
			final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateFormat.setLenient(false);
			
			String birthDateText = readValue("Informe a data de nascimento", 
											 "Data inválida", DATE_PATTERN);
			Date birthDate = null;
			while (birthDate == null) {
				try {
					birthDate = dateFormat.parse(birthDateText);
				} catch (Exception e) {
					birthDateText = readValue("Data de nascimento inválida", 
											  "Data de nascimento inválida", DATE_PATTERN);
				}
			}
			dependent.setBirthDate(birthDate);
			dao.addDepedent(chatId, dependent);
			
			final SendMessage request = new SendMessage(chatId, dependent.accountMessage()).parseMode(ParseMode.Markdown);
			bot.execute(request);
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta não cadastrada!*\nFavor executar o */create* para cadastrar a conta").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}

}
