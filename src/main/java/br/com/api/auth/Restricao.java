package br.com.api.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.api.enumeration.Role;
import br.com.api.enumeration.TipoRestricao;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Restricao {
	TipoRestricao tipoRestricao() default TipoRestricao.RESTRITO;
	Role[] roles() default Role.GERAL;
}