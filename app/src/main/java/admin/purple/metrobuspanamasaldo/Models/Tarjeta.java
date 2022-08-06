package admin.purple.metrobuspanamasaldo.Models;

public class Tarjeta {
    private String numero;
    private String ksi;
    private String saldo;
    private String estado;
    private String fecha;
    private String tipo;


    // Getter Methods

    public String getNumero() {
        return numero;
    }

    public String getKsi() {
        return ksi;
    }

    public String getSaldo() {
        return saldo;
    }

    public String getEstado() {
        return estado;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    // Setter Methods

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setKsi(String ksi) {
        this.ksi = ksi;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
