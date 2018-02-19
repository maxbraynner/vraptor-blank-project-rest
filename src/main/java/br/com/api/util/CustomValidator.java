
package br.com.api.util;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável por facilitar o uso do Validaor e diminuir a repetição de código
 *
 */
@RequestScoped
public class CustomValidator {
	
	
	@Inject private Validator validator;
	@Inject private HttpServletResponse response;

	/**
	 * 
	 * @param categoria
	 * @param mensagem
	 */
	public void add(String categoria, String mensagem){
		validator.add(new SimpleMessage(categoria, mensagem));
	}
	
	/**
	 * 
	 * @param condicao
	 * @param categoria
	 * @param mensagem
	 */
	public void addIf(boolean condicao, String categoria, String mensagem){
		validator.addIf(condicao, new SimpleMessage(categoria, mensagem));
	}
	
	/**
	 * 
	 * @param arg
	 * @param categoria
	 * @param mensagem
	 */
	public void addIfIsEmpty(String arg, String categoria, String mensagem){
		if (arg!=null && !isNullOrEmpty(arg.trim())) {
			return;
		}
		
		addMessage(categoria, mensagem);
	}
	
	/**
	 * 
	 * @param arg
	 * @param categoria
	 * @param mensagem
	 */
	public void addIfIsEmpty(@SuppressWarnings("rawtypes") List arg, String categoria, String mensagem){
		if (arg!=null && !arg.isEmpty()) {
			return;
		}
		
		addMessage(categoria, mensagem);
	}

	private void addMessage(String categoria, String mensagem) {
		validator.add(new SimpleMessage(categoria, mensagem));
	}

	
	/**
	 * 
	 * @param error
	 */
	private void sendError(int error) {
		response.setStatus(error);
		validator.onErrorUse(Results.json()).from(validator.getErrors(), "errors").serialize();
	}
	
	/**
	 * 
	 */
	public void onErrorSendBadRequest(){
		sendError(HttpServletResponse.SC_BAD_REQUEST);	
	}
	
	/**
	 * 
	 */
	public void onErrorSendForbidden(){
		sendError(HttpServletResponse.SC_FORBIDDEN);
	}
	
}