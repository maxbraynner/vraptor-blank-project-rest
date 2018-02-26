package br.com.api.dao;


import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

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
		return entityManager.createQuery(buildQuery()).getResultList();
	}
	
	public List<T> listar(int inicioPaginacao, int qtdRegistros) {
		return entityManager.createQuery(buildQuery())
				.setFirstResult(inicioPaginacao)
				.setMaxResults(qtdRegistros)
				.getResultList();
	}

	private CriteriaQuery<T> buildQuery() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<T> query = builder.createQuery(clas);
		Root<T> rootEntry = query.from(clas);
		
		return query.select(rootEntry);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> consultarPor(@SuppressWarnings("rawtypes") SingularAttribute atributo, Object valor){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> query = builder.createQuery(clas);
		Root<T> rootEntry = query.from(clas);
		
		query.select(rootEntry).where(builder.and(builder.equal(rootEntry.get(atributo), valor)));
		
		return entityManager.createQuery(query).getResultList();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}