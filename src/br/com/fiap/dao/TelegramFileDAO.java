package br.com.fiap.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.Dependent;
import br.com.fiap.model.AccountUser;

@SuppressWarnings("unchecked")
public class TelegramFileDAO {
	private static Map<Long, Pair<AccountUser, List<AccountStatement>>> REPOSITORY = new HashMap<>();
	private static final String FILE = "repository.txt";

	static {
		try (FileInputStream fi = new FileInputStream(new File(FILE));
			 ObjectInputStream oi = new ObjectInputStream(fi);) {
			REPOSITORY = (Map<Long, Pair<AccountUser, List<AccountStatement>>>) oi.readObject();
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo não encontrado!");
		} catch (IOException e) {
			System.err.println("Não foi possível realizar essa operação!");
		} catch (ClassNotFoundException e) {
			System.err.println("Não foi possível encontrar a classe!");
		}
	}

	private void writeFile() {
		File file = new File(FILE);
		if (file.delete()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("Não foi possível realizar essa operação!");
			}
		}
		try (FileOutputStream f = new FileOutputStream(file); 
			 ObjectOutputStream o = new ObjectOutputStream(f);) {
			o.writeObject(REPOSITORY);
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo não encontrado!");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Não foi possível realizar essa operação!");
		}
	}

	public Pair<AccountUser, List<AccountStatement>> getUserInformation(Long chatId) {
		synchronized (REPOSITORY) {
			return REPOSITORY.get(chatId);
		}
	}

	public void addAccountStatement(Long chatId, AccountStatement statement) {
		synchronized (REPOSITORY) {
			Pair<AccountUser, List<AccountStatement>> data = REPOSITORY.get(chatId);
			if (data != null) {
				AccountUser user = data.getKey();
				List<AccountStatement> statements = data.getValue();
				if (statements == null || statements.isEmpty()) {
					statements = new ArrayList<>(Arrays.asList(statement));
				} else {
					statements.add(statement);
				}
				data = Pair.of(user, statements);
				REPOSITORY.put(chatId, data);
				writeFile();
			}
		}
	}

	public void addUser(Long chatId, AccountUser user) {
		synchronized (REPOSITORY) {
			Pair<AccountUser, List<AccountStatement>> data = REPOSITORY.get(chatId);
			if (data != null) {
				data = Pair.of(user, data.getValue());
			} else {
				data = Pair.of(user, new ArrayList<>());
			}
			REPOSITORY.put(chatId, data);
			writeFile();
		}
	}

	public void addDepedent(Long chatId, Dependent dependent) {
		synchronized (REPOSITORY) {
			Pair<AccountUser, List<AccountStatement>> data = REPOSITORY.get(chatId);
			if (data != null) {
				AccountUser user = REPOSITORY.get(chatId).getKey();
				List<Dependent> dependents = user.getDependents();
				if (user.getDependents() == null || user.getDependents().isEmpty()) {
					user.setDependents(new ArrayList<>(Arrays.asList(dependent)));
				} else {
					dependents.add(dependent);
					user.setDependents(dependents);
				}
				data = Pair.of(user, data.getValue());
				REPOSITORY.put(chatId, data);
				writeFile();
			}
		}
	}

	public AccountUser getUser(Long chatId) {
		Pair<AccountUser, List<AccountStatement>> userInformation = getUserInformation(chatId);
		return userInformation.getKey();
	}

	public void incrementPosition(Long chatId, Integer position) {
		Pair<AccountUser, List<AccountStatement>> data = REPOSITORY.get(chatId);
		AccountUser user = null;
		if (data != null) {
			user = data.getKey();
			user.addPosition();
			addUser(chatId, user);
		} else {
			user = new AccountUser(position + 1);
			addUser(chatId, user);
		}
	}

	public Integer getPosition(Long chatId, Integer updateId) {
		Pair<AccountUser, List<AccountStatement>> data = REPOSITORY.get(chatId);
		if (data != null) {
			return data.getKey().getPosition();
		}

		return updateId;
	}
}
