package pModelo;

import java.util.Date;
import javafx.scene.control.TableColumn;

public class objetoVentas {

    public objetoVentas(Integer idVenta, String productoNombre, double productoPrecio, Integer productoCantidad, Date fechaVenta, double totalVenta) {
        this.idVenta = idVenta;
        this.productoNombre = productoNombre;
        this.productoPrecio = productoPrecio;
        this.productoCantidad = productoCantidad;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
    }
    private Integer idVenta;
    private String productoNombre;
    private double productoPrecio;
    private Integer productoCantidad;
    private Date fechaVenta;
    private double totalVenta;

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public double getProductoPrecio() {
        return productoPrecio;
    }

    public void setProductoPrecio(double productoPrecio) {
        this.productoPrecio = productoPrecio;
    }

    public Integer getProductoCantidad() {
        return productoCantidad;
    }

    public void setProductoCantidad(Integer productoCantidad) {
        this.productoCantidad = productoCantidad;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }
    
    

}
