package br.com.api.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2018-02-17T17:07:19.385-0300")
@StaticMetamodel(Token.class)
public class Token_ {
	public static volatile SingularAttribute<Token, Long> id;
	public static volatile SingularAttribute<Token, String> jwt;
	public static volatile SingularAttribute<Token, String> sistemaOperacional;
	public static volatile SingularAttribute<Token, String> browser;
	public static volatile SingularAttribute<Token, String> ip;
	public static volatile SingularAttribute<Token, Usuario> usuario;
}