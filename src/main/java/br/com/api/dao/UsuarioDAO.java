package br.com.api.dao;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.api.model.Usuario;
import br.com.api.util.UtilToken;


public class UsuarioDAO extends DAOGeneric<Usuario, Long> {
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected UsuarioDAO() {
		this(null);
	}
	
	@Inject
	public UsuarioDAO(EntityManager manager) {
		super(Usuario.class, manager);
	}
	
	public Usuario consultarPorToken(String jwt) {
		try {
			return super.consultarPorID(UtilToken.decode(jwt));
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * Consulta usuario por email
	 * @param email
	 * @param senha
	 * @return
	 */
	public Usuario consultarPorEmail(String email) {
		try {
			return (Usuario) this.getEntityManager()
					.createQuery("select p from Usuario p where p.email = :email")
					.setParameter("email", email)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
