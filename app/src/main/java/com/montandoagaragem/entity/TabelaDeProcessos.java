package com.montandoagaragem.entity;

public class TabelaDeProcessos {

	private String nomeProcesso;
	private String endereco;
	private int porta;

	public TabelaDeProcessos() {
	}

	public TabelaDeProcessos(String processo, String endereco, int porta) {
		this.nomeProcesso = processo;
		this.endereco = endereco;
		this.porta = porta;
	}

	public String getProcesso() {
		return nomeProcesso;
	}

	public void setProcesso(String processo) {
		nomeProcesso = processo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((nomeProcesso == null) ? 0 : nomeProcesso.hashCode());
		result = prime * result + porta;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TabelaDeProcessos))
			return false;
		TabelaDeProcessos other = (TabelaDeProcessos) obj;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (nomeProcesso == null) {
			if (other.nomeProcesso != null)
				return false;
		} else if (!nomeProcesso.equals(other.nomeProcesso))
			return false;
		if (porta != other.porta)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TabelaDeProcessos [nomeProcesso=" + nomeProcesso + ", endereco=" + endereco + ", porta=" + porta + "]";
	}

	
}
