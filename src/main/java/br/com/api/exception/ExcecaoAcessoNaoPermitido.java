package br.com.api.exception;

public class ExcecaoAcessoNaoPermitido extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -180090369136243249L;

	public ExcecaoAcessoNaoPermitido(String mensagem) {
		super(mensagem);
	}
}
