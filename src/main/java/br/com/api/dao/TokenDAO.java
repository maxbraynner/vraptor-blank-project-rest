package br.com.api.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.api.model.Token;


public class TokenDAO extends DAOGeneric<Token, Long> {
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected TokenDAO() {
		this(null);
	}
	
	@Inject
	public TokenDAO(EntityManager manager) {
		super(Token.class, manager);
	}
}
