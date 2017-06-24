package br.com.fiap.command.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

import br.com.fiap.command.Command;

public class HelpCommand extends Command {
		
	public HelpCommand(TelegramBot bot, Update update) {
		super(bot, update);
	}

	public void execute() {
		System.out.println(readValue("Digite um n�mero", "Formato inv�lido!", "[0-9]{1,}"));
		System.out.println(readValue("Digite um texto", "Formato de texto inv�lido!", ".{1,}"));
	}

}
