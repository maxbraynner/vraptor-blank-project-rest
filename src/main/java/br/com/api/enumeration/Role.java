package br.com.api.enumeration;

import br.com.api.model.Usuario;

public enum Role {
	GERAL(Usuario.class);
	
	private final Class<? extends Usuario> classe;
	
	private Role(Class<? extends Usuario> classe) {
		this.classe = classe;
	}

	public Class<? extends Usuario> getClasse() {
		return classe;
	}
}
