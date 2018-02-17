package br.com.api.auth;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.api.dao.UsuarioDAO;
import br.com.api.enumeration.Role;
import br.com.api.enumeration.TipoRestricao;
import br.com.api.exception.ExcecaoAcessoNaoPermitido;
import br.com.api.exception.ExcecaoAcessoNegado;
import br.com.api.model.Usuario;
import br.com.api.model.Token;
import br.com.api.util.UtilResult;
import br.com.api.util.UtilToken;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe com finalidade de interceptar as requisições e autenticar o token
 */
@Intercepts
public class InterceptAuth {

	@Inject private HttpServletRequest request;
	@Inject private Result result;
	@Inject private UsuarioDAO usuarioDAO;

	/**
	 * Qualquer requisição que não tenha a restrição "ABERTO" terá o token validado
	 * @param stack
	 * @param method
	 */
	@AroundCall
	public void intercept(SimpleInterceptorStack stack, ControllerMethod method) {

		Method metodo = method.getMethod();

		Restricao restricao = metodo.getAnnotation(Restricao.class);

		if (restricao==null || restricao.tipoRestricao() == TipoRestricao.RESTRITO) {
			validarToken(stack, restricao);
		} else{
			stack.next();
		}
	}

	/**
	 * Valida o token da requisição
	 * @param stack
	 */
	private void validarToken(SimpleInterceptorStack stack, Restricao restricao) {
		Long idUsuario = null;

		// recupera o token da requisição
		String token = request.getHeader("authorization");

		try {
			try {
				idUsuario = UtilToken.decode(token);
			} catch (IllegalArgumentException | UnsupportedEncodingException | JWTVerificationException
					| NullPointerException e) {
				throw new ExcecaoAcessoNaoPermitido("Token inválido ou não informado");
			}

			// consulta o usuario no banco
			Usuario usuario = usuarioDAO.consultarPorID(idUsuario);

			// verifica se o token existe
			if(!this.tokenExistente(token, usuario)) {
				throw new ExcecaoAcessoNaoPermitido("Token não existe na base de dados");
			}

			// verifica se possui acesso ao método
			if(!possuiAcesso(restricao, usuario)){
				throw new ExcecaoAcessoNegado("Não possui acesso ao método");
			}

			// adiciona o token no result
			result.use(Results.http()).addHeader("authorization", token);

			// segue com a requisição
			stack.next();
		}catch (ExcecaoAcessoNaoPermitido e) {

			UtilResult.retornarMensagem(result, e.getMessage(), 401);

		}catch (ExcecaoAcessoNegado e) {

			UtilResult.retornarMensagem(result, e.getMessage(), 403);

		} catch (Exception e) {

			e.printStackTrace();

			UtilResult.retornarMensagem(result,
					"Ocorreu um erro inesperado (Erro: " + (e.getMessage() == null ? "" : e.getMessage()) + " )", 
					500);

		}
	}

	private boolean possuiAcesso(Restricao restricao, Usuario usuario) {
		// possui acesso caso o método não tenha restrição ou não tenha roles
		if (restricao == null || restricao.roles() == null) {
			return true;
		}

		// verifica se o usuário está autorizado a acessar o método
		Role busca = Arrays.asList(restricao.roles()).stream()
				.filter( role -> role.getClasse().isAssignableFrom(usuario.getClass()) )
				.findAny().orElse(null);

		return busca != null;
	}

	/**
	 * Método responsável por verificar se a usuario possui o token passado
	 * @param jwt
	 * @param usuario
	 * @return
	 */
	private boolean tokenExistente(String jwt, Usuario usuario) {
		if (usuario == null || jwt == null || jwt.isEmpty() || usuario.getTokens().isEmpty()) {
			return false;
		}

		Token busca = usuario.getTokens().stream()
				.filter(token -> token.getJwt().equals(jwt))
				.findAny().orElse(null);

		return busca != null;
	}
}
