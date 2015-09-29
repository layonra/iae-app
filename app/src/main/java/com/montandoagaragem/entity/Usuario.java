package com.montandoagaragem.entity;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = -4678347457644872616L;
	private long id;
	private String nome;
	private String telefone;
	private String email;
	private String senha;
	private String instrumento;
	private String genero;
	private String subGenero;
	private String cidade;
	private String bairro;

	public Usuario() {

	}

	public Usuario(String nome, String telefone, String email, String senha, String instrumento, String genero, String subGenero, String cidade, String bairro) {
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.senha = senha;
		this.instrumento = instrumento;
		this.genero = genero;
		this.subGenero = subGenero;
		this.cidade = cidade;
		this.bairro = bairro;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
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

	public String getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(String instrumento) {
		this.instrumento = instrumento;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getSubGenero() {
		return subGenero;
	}

	public void setSubGenero(String subGenero) {
		this.subGenero = subGenero;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Usuario usuario = (Usuario) o;

		return id == usuario.id && !(nome != null ? !nome.equals(usuario.nome) : usuario.nome != null) && !(telefone != null ? !telefone.equals(usuario.telefone) : usuario.telefone != null) && !(email != null ? !email.equals(usuario.email) : usuario.email != null) && !(senha != null ? !senha.equals(usuario.senha) : usuario.senha != null) && !(instrumento != null ? !instrumento.equals(usuario.instrumento) : usuario.instrumento != null) && !(genero != null ? !genero.equals(usuario.genero) : usuario.genero != null) && !(subGenero != null ? !subGenero.equals(usuario.subGenero) : usuario.subGenero != null) && !(cidade != null ? !cidade.equals(usuario.cidade) : usuario.cidade != null) && !(bairro != null ? !bairro.equals(usuario.bairro) : usuario.bairro != null);

	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (nome != null ? nome.hashCode() : 0);
		result = 31 * result + (telefone != null ? telefone.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (senha != null ? senha.hashCode() : 0);
		result = 31 * result + (instrumento != null ? instrumento.hashCode() : 0);
		result = 31 * result + (genero != null ? genero.hashCode() : 0);
		result = 31 * result + (subGenero != null ? subGenero.hashCode() : 0);
		result = 31 * result + (cidade != null ? cidade.hashCode() : 0);
		result = 31 * result + (bairro != null ? bairro.hashCode() : 0);
		return result;
	}


	@Override
	public String toString() {
		return "Usuario{" +
				"id=" + id +
				", nome='" + nome + '\'' +
				", telefone='" + telefone + '\'' +
				", email='" + email + '\'' +
				", senha='" + senha + '\'' +
				", instrumento='" + instrumento + '\'' +
				", genero='" + genero + '\'' +
				", subGenero='" + subGenero + '\'' +
				", cidade='" + cidade + '\'' +
				", bairro='" + bairro + '\'' +
				'}';
	}
}