package br.com.fiap.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import br.com.fiap.model.AccountStatement;
import br.com.fiap.model.Dependent;
import br.com.fiap.model.User;

public class DAO {
	private static Map<Long, Pair<User, List<AccountStatement>>> REPOSITORY = new HashMap<>();
	
	public Pair<User, List<AccountStatement>> getUserInformation(Long chatId) {
		synchronized (REPOSITORY) {
			return REPOSITORY.get(chatId);
		}
	}
	
	public void addAccountStatement(Long chatId, AccountStatement statement) {
		synchronized (REPOSITORY) {
			List<AccountStatement> statements = REPOSITORY.get(chatId).getRight();
			statements.add(statement);
			REPOSITORY.get(chatId).setValue(statements);
		}
	}
	
	public void addUser(Long chatId, User user) {
		synchronized (REPOSITORY) {
			if (REPOSITORY.get(chatId) == null) {
				Pair<User, List<AccountStatement>> data = Pair.of(user, new ArrayList<>());
				REPOSITORY.put(chatId, data);
			}
		}
	}
	
	public void addDepedent(Long chatId, Dependent dependent) {
		synchronized (REPOSITORY) {
			if (REPOSITORY.get(chatId) != null) {
				User user = REPOSITORY.get(chatId).getKey();
				user.getDependents().add(dependent);
				Pair<User, List<AccountStatement>> data = Pair.of(user, REPOSITORY.get(chatId).getValue());
				REPOSITORY.put(chatId, data);
			}
		}
	}
}
