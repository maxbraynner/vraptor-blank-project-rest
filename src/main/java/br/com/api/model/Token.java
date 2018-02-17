package br.com.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Token {
	
	public Token() {}
	
	public Token(String jwt, String sistemaOperacional, String browser, String ip) {
		this.jwt = jwt;
		this.browser = browser;
		this.sistemaOperacional = sistemaOperacional;
		this.ip = ip;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Usuario usuario;
	
	/**
	 * Token JWT
	 */
	private String jwt;
	
	private String sistemaOperacional;
	
	private String browser;
	
	private String ip;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getSistemaOperacional() {
		return sistemaOperacional;
	}

	public void setSistemaOperacional(String sistemaOperacional) {
		this.sistemaOperacional = sistemaOperacional;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Usuario getPessoa() {
		return usuario;
	}

	public void setPessoa(Usuario usuario) {
		this.usuario = usuario;
	}
	
}