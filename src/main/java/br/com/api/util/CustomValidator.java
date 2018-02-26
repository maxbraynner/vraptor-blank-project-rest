package br.com.api.util;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	public void add(String categoria, String mensagem) {
		validator.add(new SimpleMessage(categoria, mensagem));
	}
	
	/**
	 * 
	 * @param condicao
	 * @param categoria
	 * @param mensagem
	 */
	public void addIf(boolean condicao, String categoria, String mensagem) {
		validator.addIf(condicao, new SimpleMessage(categoria, mensagem));
	}
	
	/**
	 * 
	 * @param condicao
	 * @param categoria
	 * @param mensagem
	 */
	public void addIfIsNull(Object arg, String categoria, String mensagem) {
		validator.addIf(arg == null, new SimpleMessage(categoria, mensagem));
	}
	
	/**
	 * Recupera o id do objeto e verifica se está nulo ou se é zero 
	 * Caso ID não exista na classe: não adiciona a mensagem
	 * @param arg
	 * @param categoria
	 * @param mensagem
	 */
	public void addIfIsIDNull(Object arg, String categoria, String mensagem) {
		validator.addIf(isIdInValido(arg), new SimpleMessage(categoria, mensagem));
	}

	/**
	 * chama o método getId do arg e verifica o resultado
	 * @param arg
	 * @return
	 */
	private boolean isIdInValido(Object arg) {
		boolean condicao = false;

		try {
			
			Method method = arg.getClass().getMethod("getId");
			Long id = (Long) method.invoke(arg);
			
			if (id == null || id == 0) {
				condicao = true;
			}
				
		} catch (NoSuchMethodException | SecurityException e) {
			// não encontrou o método, segue a requisição
			condicao = false;
		} catch (NullPointerException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// não pode chamar o método
			condicao = true;
		}

		return condicao;
	}
	
	/**
	 * 
	 * @param arg
	 * @param categoria
	 * @param mensagem
	 */
	public void addIfIsEmpty(String arg, String categoria, String mensagem) {
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
	public void onErrorSendBadRequest() {
		sendError(HttpServletResponse.SC_BAD_REQUEST);	
	}
	
	/**
	 * 
	 */
	public void onErrorSendNotFound() {
		sendError(HttpServletResponse.SC_NOT_FOUND);	
	}
	
	/**
	 * 
	 */
	public void onErrorSendForbidden() {
		sendError(HttpServletResponse.SC_FORBIDDEN);
	}
	
}