
package br.com.api.util;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Classe responsável por facilitar o uso do Validaor e diminuir a repetição de código
 *
 */
public class CustomValidator {
	
	
	@Inject Validator validator;
	@Inject Result result;

	/**
	 * 
	 * @param categoria
	 * @param mensagem
	 */
	public void add(String categoria, String mensagem){
		setCodigoErro();
		validator.add(new SimpleMessage(categoria, mensagem));
	}
	
	/**
	 * 
	 * @param condicao
	 * @param categoria
	 * @param mensagem
	 */
	public void addIf(boolean condicao, String categoria, String mensagem){
		setCodigoErro();
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
		setCodigoErro();
		
		validator.add(new SimpleMessage(categoria, mensagem));
	}

	private void setCodigoErro() {
		if (!validator.hasErrors()) {
			result.use(Results.http()).setStatusCode(400);
		}
	}
	
	/**
	 * 
	 */
	public void onErrorSendBadRequest(){
		validator.onErrorUse(Results.json()).from(validator.getErrors(), "errors").serialize();	
	}
	
	/**
	 * 
	 */
	public void onErrorSendForbidden(){
		validator.onErrorUse(Results.status()).forbidden("Acesso não permitido");
	}
	
}