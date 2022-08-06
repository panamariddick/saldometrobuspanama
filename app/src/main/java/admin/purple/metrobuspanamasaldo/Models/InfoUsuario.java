package admin.purple.metrobuspanamasaldo.Models;

public class InfoUsuario {

    private String status;
    Tarjeta tarjeta;


    // Getter Methods

    public String getStatus() {
        return status;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    // Setter Methods

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }
}
