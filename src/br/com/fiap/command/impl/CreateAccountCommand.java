package br.com.fiap.command.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import br.com.fiap.command.Command;
import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.AccountUser;

public class CreateAccountCommand extends Command {

	public CreateAccountCommand(TelegramBot bot, Update update) {
		super(bot, update);
	}

	@Override
	public void execute() {
		Chat chat = update.message().chat();
		Long chatId = chat.id();
		Pair<AccountUser,List<AccountStatement>> userInformation = dao.getUserInformation(chatId);
		AccountUser user = userInformation.getKey();
		if (user.isEmpty()) {
			String name = readValue("Informe seu nome completo (Para considerar o nome *" + chat.firstName() + " " + chat.lastName() + "*, basta mandar o texto *padrão*)", 
									"Tamanho de *nome* inválido! Deve ter no mínimo 3 caracteres!", NAME_PATTERN);
			if (DEFAULT.equalsIgnoreCase(name)) {
				name = chat.firstName() + " " + chat.lastName();
			}
			user.setName(name);
			user.setCpf(readValue("Informe o CPF", "CPF inválido! Favor informar somente números!", CPF_PATTERN));
			
			final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			dateFormat.setLenient(false);
			
			String birthDateText = readValue("Informe a data de nascimento", "Data inválida", DATE_PATTERN);
			Date birthDate = null;
			while (birthDate == null) {
				try {
					birthDate = dateFormat.parse(birthDateText);
				} catch (Exception e) {
					birthDateText = readValue("Data de nascimento inválida", "Data de nascimento inválida", DATE_PATTERN);
				}
			}
			user.setBirthDate(birthDate);
			
			Random rand = new Random();
			user.setAccountNumber(StringUtils.leftPad(Integer.toString(rand.nextInt(999)), 3, '0') + "-" + StringUtils.leftPad(Integer.toString(rand.nextInt(99)), 2, '0'));
			dao.addUser(chatId, user);
			
			final SendMessage request = new SendMessage(chatId, user.accountMessage()).parseMode(ParseMode.Markdown);
			bot.execute(request);
		} else {
			SendMessage request = new SendMessage(chatId, "*Conta já existe!*\nFavor executar o */modify* para editar as informações").parseMode(ParseMode.Markdown);
			bot.execute(request);
		}
	}

}
