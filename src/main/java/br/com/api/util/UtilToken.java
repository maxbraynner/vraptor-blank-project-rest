package br.com.api.util;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;


/**
 * Classe responsável por gerar e decodificar o token
 * @author braynner
 *
 */
@ApplicationScoped
public class UtilToken {

	private static final String SECRET = "*api_*_*_*api-rest*";
	public static final String IDUSUARIO = "idUsuario";

	/**
	 * @deprecated CDI eyes only
	 */
	public UtilToken() {
	}

	/**
	 * Cria o token
	 * @param idPessoa
	 * @return
	 * @throws IllegalArgumentException
	 * @throws JWTCreationException
	 * @throws UnsupportedEncodingException
	 */
	public static String createToken(Long idPessoa) throws IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
		String token = JWT.create()
				.withSubject(idPessoa.toString())
				.withClaim("date", LocalDateTime.now().toString())
				.sign(Algorithm.HMAC512(SECRET));

		return token;

	}

	/**
	 * Decodifica o token
	 * @param token
	 * @return
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 */
	public static Long decode(String token) throws IllegalArgumentException, UnsupportedEncodingException{
		if(token==null) {
			throw new IllegalArgumentException();
		}
		
		JWTVerifier verifier = JWT
				.require(Algorithm.HMAC512(SECRET))
				.build();
		
		DecodedJWT jwt = verifier.verify(token);

		Long idUsuario = Long.parseLong(jwt.getSubject());

		return idUsuario;
	}
	
	public Long getIdToken(String token){
		Long id = null;
		
		try {
			id = (Long) UtilToken.decode(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
	}

}