package br.com.api.exception;

public class ExcecaoAcessoNegado extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5594761417377991953L;

	public ExcecaoAcessoNegado(String mensagem) {
		super(mensagem);
	}

}
