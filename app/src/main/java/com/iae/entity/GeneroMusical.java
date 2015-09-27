package com.iae.entity;

import java.util.List;

/**
 * @author layon
 */
public class GeneroMusical {
	private long id;
	private String nome;
	private List<SubGeneroMusical> subGenero;

	public GeneroMusical() {
		
	}

	public GeneroMusical(String nome, List<SubGeneroMusical> subGenero) {

		this.nome = nome;
		this.subGenero = subGenero;
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

	public List<SubGeneroMusical> getSubGenero() {
		return subGenero;
	}

	public void setSubGenero(List<SubGeneroMusical> subGenero) {
		this.subGenero = subGenero;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "GeneroMusical [id=" + id + ", nome=" + nome + ", subGenero=" + subGenero + "]";
	}
	
	

}
