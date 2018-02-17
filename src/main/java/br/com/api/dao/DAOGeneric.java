package br.com.api.dao;


import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class DAOGeneric<T, E> implements DAO<T, E> {	
	private Class<T> clas;

	private EntityManager entityManager;

	/**
	 * @deprecated CDI eyes only
	 */
	public DAOGeneric() {
		this(null, null);
	}

	@Inject
	public DAOGeneric(Class<T> clas, EntityManager entityManager) {
		this.clas = clas;
		this.entityManager = entityManager;
	}

	@Override
	public void inserir(T t) {
		this.entityManager.persist(t);
	}

	@Override
	public void alterar(T t) {
		this.entityManager.merge(t);
	}

	@Override
	public void remover(T t) {
		this.entityManager.remove(t);
	}

	@Override
	public T consultarPorID(E e) {
		return (T) this.entityManager.find(clas, e);
	}

	@Override
	public List<T> listar() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clas);
		Root<T> rootEntry = cq.from(clas);
		CriteriaQuery<T> all = cq.select(rootEntry);
		TypedQuery<T> allQuery = entityManager.createQuery(all);
		return allQuery.getResultList();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}