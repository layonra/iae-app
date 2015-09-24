package lab.emerson.iae.entity;

public class Usuario {

	private long id;
	private String nome;
	private String telefone;

	Usuario() {

	}

	public Usuario(String nome, String telefone) {
		super();
		this.nome = nome;
		this.telefone = telefone;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Usuario usuario = (Usuario) o;

		return id == usuario.id && !(nome != null ? !nome.equals(usuario.nome) : usuario.nome != null) && !(telefone != null ? !telefone.equals(usuario.telefone) : usuario.telefone != null);

	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (nome != null ? nome.hashCode() : 0);
		result = 31 * result + (telefone != null ? telefone.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return id + ";" + nome + ";" + telefone + ";";
	}
}