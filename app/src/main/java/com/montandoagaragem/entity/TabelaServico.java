package com.montandoagaragem.entity;

public class TabelaServico {

    private long id;
    private String servico;
    private String ip;
    private int porta;

    public TabelaServico(){}

    public TabelaServico(String servico, String ip, int porta) {
        this.servico = servico;
        this.ip = ip;
        this.porta = porta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabelaServico that = (TabelaServico) o;

        return id == that.id && porta == that.porta && !(servico != null ? !servico.equals(that.servico) : that.servico != null) && !(ip != null ? !ip.equals(that.ip) : that.ip != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (servico != null ? servico.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + porta;
        return result;
    }

    @Override
    public String toString() {
        return "TabelaServico{" +
                "id=" + id +
                ", servico='" + servico + '\'' +
                ", ip='" + ip + '\'' +
                ", porta=" + porta +
                '}';
    }
}
