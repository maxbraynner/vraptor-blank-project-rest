package br.com.api.dao;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerCreator {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Produces @RequestScoped
	public EntityManager getManager() {
		return manager;
	}
}