package lab.emerson.iae.entity;

import java.util.List;

public class Usuario {

	private long id;
	private String nome;
	private String telefone;
	private String cidade;
	private String bairro;
	private List<Instrumento> usuario_toca;
	private List<GeneroMusical> generoMusical;
	private List<SubGeneroMusical> subGeneroMusical;

	public Usuario() {

	}

	/**
	 *
	 * @param nome
	 * @param telefone
	 * @param cidade
	 * @param bairro
	 * @param usuario_toca
	 * @param generoMusical
	 */
	public Usuario(String nome, String telefone, String cidade, String bairro, List<Instrumento> usuario_toca,
				   List<GeneroMusical> generoMusical, List<SubGeneroMusical> subGeneroMusical) {

		this.nome = nome;
		this.telefone = telefone;
		this.cidade = cidade;
		this.bairro = bairro;
		this.usuario_toca = usuario_toca;
		this.generoMusical = generoMusical;
		this.subGeneroMusical = subGeneroMusical;
	}

	public long getId() {
		return id;
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

	public List<Instrumento> getUsuario_toca() {
		return usuario_toca;
	}

	public void setUsuario_toca(List<Instrumento> usuario_toca) {
		this.usuario_toca = usuario_toca;
	}

	public List<GeneroMusical> getGeneroMusical() {
		return generoMusical;
	}

	public void setGeneroMusical(List<GeneroMusical> generoMusical) {
		this.generoMusical = generoMusical;
	}

	public List<SubGeneroMusical> getSubGeneroMusical() {
		return subGeneroMusical;
	}

	public void setSubGeneroMusical(List<SubGeneroMusical> subGeneroMusical) {
		this.subGeneroMusical = subGeneroMusical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + ((generoMusical == null) ? 0 : generoMusical.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((subGeneroMusical == null) ? 0 : subGeneroMusical.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result + ((usuario_toca == null) ? 0 : usuario_toca.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (generoMusical == null) {
			if (other.generoMusical != null)
				return false;
		} else if (!generoMusical.equals(other.generoMusical))
			return false;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (subGeneroMusical == null) {
			if (other.subGeneroMusical != null)
				return false;
		} else if (!subGeneroMusical.equals(other.subGeneroMusical))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (usuario_toca == null) {
			if (other.usuario_toca != null)
				return false;
		} else if (!usuario_toca.equals(other.usuario_toca))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome + ";" + telefone + ";" + cidade + ";"
				+ bairro + ";" + usuario_toca + ";" + generoMusical + ";"
				+ subGeneroMusical;
	}

}