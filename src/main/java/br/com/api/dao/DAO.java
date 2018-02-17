package br.com.api.dao;

import java.util.List;

public interface DAO<T, E> {
	
	public void inserir(T t);
	
	public void alterar(T t);
	
	public void remover(T t);
	
	public T consultarPorID(E e);
	
	public List<T> listar();
	
}