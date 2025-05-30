package pModelo;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import pModelo.objetoCitas;
/**
 * Clase para manejar los datos de las citas.
 * 
 * @author Zura
 */
public class DatosCita {

    private int idCita;
    private String cliente;
    private LocalDate fechaReserva;
    private LocalDateTime horaReserva;
    private int idServicio;
    private int idEmpleado;
    private int idEstado;
    private Double precio;
    private String estado;
    private String servicio;
    private String empleado;

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDate fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public LocalDateTime getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(LocalDateTime horaReserva) {
        this.horaReserva = horaReserva;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    
    public DatosCita(int idCita, String cliente, LocalDate fechaReserva, LocalDateTime horaReserva, int idServicio, int idEmpleado, int idEstado, Double precio, String estado, String servicio, String empleado) {
        this.idCita = idCita;
        this.cliente = cliente;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.idServicio = idServicio;
        this.idEmpleado = idEmpleado;
        this.idEstado = idEstado;
        this.precio = precio;
        this.estado = estado;
        this.servicio = servicio;
        this.empleado = empleado;
    }

   
    
    
    
    public static ObservableList<DatosCita> obtenerCitas() {
    ObservableList<DatosCita> citas = FXCollections.observableArrayList(); // Cambiado a ObservableList
    String consultaSQL = "SELECT Cita.Id_Cita, Cita.Cliente, Cita.FechaReserva, Servicio.NombreServicio AS servicio, "
            + "Empleado.Nombre AS empleado, Servicio.Precio, Cita.Estado "
            + "FROM Cita "
            + "JOIN Servicio ON Cita.Id_Servicio = Servicio.Id_Servicio "
            + "JOIN Empleado ON Cita.Id_Empleado = Empleado.Id_Empleado";

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.establecerConexion();
        PreparedStatement stmt = con.prepareStatement(consultaSQL);
        ResultSet resultado = stmt.executeQuery();

        while (resultado.next()) {
            int idCita = resultado.getInt("Id_Cita");
            String cliente = resultado.getString("Cliente");
            java.sql.Timestamp fechaReserva = resultado.getTimestamp("FechaReserva");
            // Convertir a LocalDateTime para incluir la hora
            LocalDateTime fechaReservaLocalDateTime = fechaReserva.toLocalDateTime();
            String servicio = resultado.getString("servicio");
            String empleado = resultado.getString("empleado");
            double precio = resultado.getDouble("Precio");
            String estado = resultado.getString("Estado");

            // Crear el objeto DatosCita pasando el valor LocalDateTime para fechaReserva
            DatosCita cita = new DatosCita(idCita, cliente, fechaReservaLocalDateTime.toLocalDate(), fechaReservaLocalDateTime, 0, 0, 0, precio, estado, servicio, empleado);
            citas.add(cita);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return citas;
}

    
    
    
    
    
    

}
   


