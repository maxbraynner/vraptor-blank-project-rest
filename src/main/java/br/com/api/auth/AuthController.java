package br.com.api.auth;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.commons.mail.Email;

import com.auth0.jwt.exceptions.JWTCreationException;

import br.com.api.dao.UsuarioDAO;
import br.com.api.dao.TokenDAO;
import br.com.api.enumeration.Role;
import br.com.api.enumeration.TipoRestricao;
import br.com.api.model.Usuario;
import br.com.api.model.Token;
import br.com.api.util.CustomValidator;
import br.com.api.util.UtilClientInfo;
import br.com.api.util.UtilGeradorChaveNovaSenha;
import br.com.api.util.UtilPassword;
import br.com.api.util.UtilResult;
import br.com.api.util.UtilToken;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.HeaderParam;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.simplemail.AsyncMailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;

@Controller
public class AuthController {

    @Inject private Result result;
    @Inject private HttpServletRequest request;
    @Inject private UsuarioDAO usuarioDAO;
    @Inject private CustomValidator validator;
    @Inject private TokenDAO tokenDAO;
    @Inject private AsyncMailer mailer;
    @Inject private TemplateMailer templates;

    @Post("/login")
    @Restricao(tipoRestricao=TipoRestricao.ABERTO)
    @Transactional
    @Consumes("application/json")
    public void logIn(String email, String senha) throws IllegalArgumentException, JWTCreationException, UnsupportedEncodingException {
    	validator.addIfIsEmpty(email, "Email", "Email não preenchido");
    	validator.addIfIsEmpty(senha, "Senha", "Senha não preenchida");
    	validator.onErrorSendBadRequest();
    	
    	Usuario usuario = usuarioDAO.consultarPorEmail(email);
    	
    	// valida se email foi cadastrado
    	validator.addIf( usuario == null, "Email", "Email não cadastrado");
    	validator.onErrorSendForbidden();
    	
    	// valida a senha
    	boolean isSenhaCorreta = UtilPassword.check(senha, usuario.getSenha());
    	
    	validator.addIf( !isSenhaCorreta, "Senha", "Senha incorreta");
    	validator.onErrorSendForbidden();
    	
    	// gera o jwt
    	String jwt = UtilToken.createToken(usuario.getId());
    	
    	String clientIpAddr = UtilClientInfo.getClientIpAddr(request);
    	String clientOS = UtilClientInfo.getClientOS(request);
    	String clientBrowser = UtilClientInfo.getClientBrowser(request);
    	
    	// preenche as informações do cliente
    	Token token = new Token(jwt, clientOS, clientBrowser, clientIpAddr);
    	
    	// define o dono do token
    	token.setPessoa(usuario);
    	
    	// inclui o registro
    	tokenDAO.inserir(token);
    	
    	usuario.setToken(jwt);
    	
    	// os atributos senha, CPF e RG não serão serializados
		UtilResult.retornarObjetoExclude(result, usuario, "senha", "cpf", "rg", "chaveAlteracaoSenha", "telefone");
    }
    
    @Delete("/logout")
    @Transactional
    @Consumes("application/json")
    @Restricao(tipoRestricao=TipoRestricao.RESTRITO, roles={Role.GERAL})
    public void logOut(@HeaderParam(value = "authorization") String jwt) throws IllegalArgumentException, UnsupportedEncodingException {
    	Long idPessoa = UtilToken.decode(jwt);
    	
    	Usuario usuario = usuarioDAO.consultarPorID(idPessoa);
    	
    	// remove o token passado
    	for (Token token : usuario.getTokens()) {
			if (token.getJwt().equals(jwt)) {
				tokenDAO.remover(token);
				break;
			}
		}
    	
        UtilResult.retornarCodigoHttp(result, 200);
    }
    
    @Post("/esqueci-senha")
    @Transactional
    @Consumes("application/json")
    @Restricao(tipoRestricao=TipoRestricao.ABERTO)
    public void esqueciSenha(String email) {
    	validator.addIfIsEmpty(email, "E-mail", "E-mail não informado");
    	validator.onErrorSendBadRequest();
    	
    	// consulta por cpf ou cnpj
    	Usuario usuario = usuarioDAO.consultarPorEmail(email);

    	// acesso negado
    	validator.addIf(usuario==null, "E-mail", "E-mail não encontrado");
    	validator.onErrorSendBadRequest();
    	
		// cria chave de recuperação
		String chave = UtilGeradorChaveNovaSenha.getChave();
		
    	// altera o o fornecedo com a nova chave
		usuario.setChaveAlteracaoSenha(chave);
		
		usuarioDAO.alterar(usuario);
		
		Email emailSender = this.templates
				.template("esqueciSenha")
				.with("nome", usuario.getNome() + " " + usuario.getSobrenome())
				.with("chave", usuario.getChaveAlteracaoSenha())
				.to("Recuperação de senha", usuario.getEmail())
				.setSubject("Recuperação de senha");
		
		mailer.asyncSend(emailSender);
		
		UtilResult.retornarCodigoHttp(result, 200);
    }
    
    @Post("/recuperar-senha")
    @Transactional
    @Consumes("application/json")
    @Restricao(tipoRestricao=TipoRestricao.ABERTO)
    public void recuperarSenha(String email, String chave, String senha) {
    	validator.addIfIsEmpty(email, "E-mail", "E-mail não informado");
    	validator.addIfIsEmpty(chave, "Chave", "Chave não informada");
    	validator.addIfIsEmpty(senha, "Senha", "Senha não informada");
    	
    	validator.onErrorSendBadRequest();
    	
    	// consulta por cpf ou cnpj
    	Usuario usuario = usuarioDAO.consultarPorEmail(email);

    	
    	// bad request caso não encontre o fornecedor ou a chave não seja igual a passada na requisição
    	validator.addIf(usuario == null || 
    			usuario.getChaveAlteracaoSenha() == null ||
    			!usuario.getChaveAlteracaoSenha().equals(chave.toLowerCase()), 
    		"Dados", "Dados incorretos, favor verificar seu e-mail.");
    	validator.onErrorSendBadRequest();
		
		// altera a senha
    	usuario.setSenha(UtilPassword.crypt(senha));
    	usuario.setChaveAlteracaoSenha("");
		
    	usuarioDAO.alterar(usuario);
		
		// ok
		UtilResult.retornarCodigoHttp(result, 200);
    }
    
    @Post("/alterar-senha")
    @Transactional
    @Consumes("application/json")
    @Restricao(tipoRestricao=TipoRestricao.RESTRITO, roles={Role.GERAL})
    public void alterarSenha(@HeaderParam("authorization") String jwt, 
    		String senhaAntiga, String senhaNova) throws IllegalArgumentException, UnsupportedEncodingException {
    	
    	validator.addIfIsEmpty(senhaAntiga, "Senha Atual", "Senha Atual não informada");
    	validator.addIfIsEmpty(senhaNova, "Nova Senha", "Nova Senha não informada");    	
    	validator.onErrorSendBadRequest();
    	
    	Long id = UtilToken.decode(jwt);
    	
    	// consulta por cpf ou cnpj
    	Usuario usuario = usuarioDAO.consultarPorID(id);
    	
    	validator.addIf(usuario==null, "Registro", "Registro não econtrado");
    	validator.onErrorSendBadRequest();
    	
    	// verifica se a senha atual é válida
    	boolean isSenhaValida = UtilPassword.check(senhaAntiga, usuario.getSenha());
    	
    	validator.addIf(!isSenhaValida, "", "");
    	validator.onErrorSendForbidden();
    	
    	// altera a senha
    	usuario.setSenha(UtilPassword.crypt(senhaNova));
    	
    	usuarioDAO.alterar(usuario);
    	
    	// ok
    	UtilResult.retornarCodigoHttp(result, 200);
    }
    
    
}
