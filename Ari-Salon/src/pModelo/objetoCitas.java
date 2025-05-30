package pModelo;

import java.time.LocalDateTime;
import java.util.Date;

public class objetoCitas {
    
    private Integer idCita;
    private String citaEmpleado;
    private Double citaPrecio; // Cambio a Double
    private String citaServicio;
    private LocalDateTime fechaCita;

    public objetoCitas(Integer idCita, String citaEmpleado, Double citaPrecio, String citaServicio, LocalDateTime fechaCita) {
        this.idCita = idCita;
        this.citaEmpleado = citaEmpleado;
        this.citaPrecio = citaPrecio;
        this.citaServicio = citaServicio;
        this.fechaCita = fechaCita;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public String getCitaEmpleado() {
        return citaEmpleado;
    }

    public void setCitaEmpleado(String citaEmpleado) {
        this.citaEmpleado = citaEmpleado;
    }

    public Double getCitaPrecio() { // Cambio a Double
        return citaPrecio;
    }

    public void setCitaPrecio(Double citaPrecio) { // Cambio a Double
        this.citaPrecio = citaPrecio;
    }

    public String getCitaServicio() {
        return citaServicio;
    }

    public void setCitaServicio(String citaServicio) {
        this.citaServicio = citaServicio;
    }

    public LocalDateTime getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }
}