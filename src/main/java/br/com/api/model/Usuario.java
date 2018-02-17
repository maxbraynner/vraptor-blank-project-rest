package br.com.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = -3556751655672609629L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
	private String sobrenome;
	
	@Column(unique=true, nullable=true)
	private String email;
	
	private String senha;
	
	/**
	 * chave usada para confirmar a alteração da senha
	 */
	@Column(length=16)
	private String chaveAlteracaoSenha;
	
	/**
	 * Atributo não persistido em BD
	 * Possui a função de ser serializado para o front-end
	 */
	@Transient
	private String token;
	
	@OneToMany(mappedBy="usuario")
	private List<Token> tokens;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getChaveAlteracaoSenha() {
		return chaveAlteracaoSenha;
	}

	public void setChaveAlteracaoSenha(String chaveAlteracaoSenha) {
		this.chaveAlteracaoSenha = chaveAlteracaoSenha;
	}
	
}