package br.com.api.util;

import br.com.api.model.Usuario;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe reponsável por facilitar o uso do Result e diminuir a repetição de código
 *
 */
public class UtilResult {

	/**
	 * @param result
	 * @param mensagem
	 * @param idMensagem
	 * @param codigoHttp
	 */
	public static void retornarMensagem(Result result, String mensagem, int codigoHttp){
		result.use(Results.http()).setStatusCode(codigoHttp);
		result.use(Results.json()).from(mensagem, "message").serialize();
	}
	
	/**
	 * 
	 * @param result
	 * @param object
	 */
	public static void retornarObjeto(Result result, Object object){
		UtilResult.retornarObjeto(result, object, isWithoutRoot(object));
	}

	private static boolean isWithoutRoot(Object object) {
//		return object instanceof List || object instanceof Map || object instanceof Set;
		return !(object instanceof Usuario);
	}
	
	/**
	 * withoutRoot caso seja true: não serializa o objeto pai (list, hasMap...)
	 * @param result
	 * @param object
	 * @param withoutRoot
	 */
	public static void retornarObjeto(Result result, Object object, boolean withoutRoot){
		result.use(Results.http()).setStatusCode(200);
		if (withoutRoot) {
			result.use(Results.json()).withoutRoot().from(object).serialize();
		}else {
			result.use(Results.json()).from(object).serialize();
		}
	}
	
	/**
	 * 
	 * @param result
	 * @param object
	 */
	public static void retornarObjeto(Result result, Object object, String[] include, String[] exclude){
		result.use(Results.http()).setStatusCode(200);
		if (isWithoutRoot(object)){
			result.use(Results.json()).withoutRoot().from(object).include(include).exclude(exclude).serialize();
		}else {
			result.use(Results.json()).from(object).include(include).exclude(exclude).serialize();
		}
	}
	
	/**
	 * 
	 * @param result
	 * @param object
	 * @param include campos não primitivos que devem ser serializados
	 */
	public static void retornarObjetoInclude(Result result, Object object, String... include){
		result.use(Results.http()).setStatusCode(200);
		if (isWithoutRoot(object)){
			result.use(Results.json()).withoutRoot().from(object).include(include).serialize();
		}else {
			result.use(Results.json()).from(object).include(include).serialize();
		}
	}
	
	/**
	 * 
	 * @param result
	 * @param object
	 * @param exclude campos que não devem ser serializados
	 */
	public static void retornarObjetoExclude(Result result, Object object, String... exclude){
		result.use(Results.http()).setStatusCode(200);
		if (isWithoutRoot(object)){
			result.use(Results.json()).withoutRoot().from(object).exclude(exclude).serialize();
		}else {
			result.use(Results.json()).from(object).exclude(exclude).serialize();
		}
	}
	
	/**
	 * 
	 * @param result
	 * @param mensagem
	 * @param idMensagem
	 * @param excecao
	 */
	public static void mensagemAoLevantarExcecao(Result result, String mensagem, String idMensagem,
			Class<? extends Exception> excecao) {
		result.on(excecao).use(Results.json()).from(mensagem, idMensagem).serialize();
	}
	
	/**
	 * 
	 * @param result
	 * @param codigo
	 */
	public static void retornarCodigoHttp(Result result, int codigo) {
		result.use(Results.http()).setStatusCode(codigo);
	}
	
	public static void retornarJsonDeString(Result result, String json, int codigo) {
		result.use(Results.http()).setStatusCode(codigo);
		result.use(Results.http()).addHeader("Content-Type", "application/json").body(json);
	}
}