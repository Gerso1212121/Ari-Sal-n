package pModelo;

import java.util.Date;

public class objetoPago {

    public objetoPago(Integer idPago, String nombrePago, Integer horasTrabajadas, Date fechaPago, Double montoPago) {
        this.idPago = idPago;
        this.nombrePago = nombrePago;
        this.horasTrabajadas = horasTrabajadas;
        this.fechaPago = fechaPago;
        this.montoPago = montoPago;
    }
    
        private Integer idPago;
        private String nombrePago;
        private Integer horasTrabajadas;
        private Date fechaPago;
        private Double montoPago;

    public Integer getIdPago() {
        return idPago;
    }

    public String getNombrePago() {
        return nombrePago;
    }

    public Integer getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public Double getMontoPago() {
        return montoPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public void setNombrePago(String nombrePago) {
        this.nombrePago = nombrePago;
    }

    public void setHorasTrabajadas(Integer horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setMontoPago(Double montoPago) {
        this.montoPago = montoPago;
    }
        
    
}
