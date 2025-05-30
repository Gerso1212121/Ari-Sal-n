package pModelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Timestamp;
public class FuncionCitaBD {
    
     //Un metodo publi para crear un arraylist y recorrer los datos
    public List<objetoCitas> obternerInfoCitas() {
        List<objetoCitas> citas = new ArrayList<>();
        
        String sql = "SELECT "
                + "c.Id_Cita, "
                + "e.Nombre AS Empleado, "
                + "s.NombreServicio AS Servicio, "
                + "c.FechaReserva AS FechaCita, "
                + "s.Precio "
                + "FROM Cita c "
                + "JOIN Empleado e ON c.Id_Empleado = e.Id_Empleado "
                + "JOIN Servicio s ON c.Id_Servicio = s.Id_Servicio" ;
        
        try{Connection con;
            Conexion conexion = new Conexion();
            con = conexion.establecerConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()){
                int idCita = rs.getInt("Id_Cita");
                String citaEmpleado = rs.getString("Empleado");
                String citaServicio = rs.getString("Servicio");
                Timestamp timestamp = rs.getTimestamp("FechaCita");
                LocalDateTime fechaCita = timestamp.toLocalDateTime();
                double citaPrecio = rs.getDouble("Precio");
                
                objetoCitas cita = new objetoCitas(idCita, citaEmpleado, citaPrecio, citaServicio, fechaCita);
                citas.add(cita);
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }  
        return citas;
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
            Timestamp fechaReserva = resultado.getTimestamp("FechaReserva");
            LocalDateTime fechaCita = fechaReserva.toLocalDateTime();
            String servicio = resultado.getString("servicio");
            String empleado = resultado.getString("empleado");
            double precio = resultado.getDouble("Precio");
            String estado = resultado.getString("Estado");

            DatosCita cita = new DatosCita(idCita, cliente, null,fechaCita, 0, 0, 0, precio, estado, servicio, empleado);
            citas.add(cita);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return citas;
}
    
    
    


    //Un metodo publi para crear un arraylist y recorrer los datos
    public List<objetoCitas> validarCitas() {
        List<objetoCitas> citas = new ArrayList<>();

        String sql = "SELECT Cita.Id_Cita, Cita.Cliente, Cita.FechaReserva, Servicio.NombreServicio AS servicio, "
                + "Empleado.Nombre AS empleado, Servicio.Precio, Cita.Estado "
                + "FROM Cita "
                + "JOIN Servicio ON Cita.Id_Servicio = Servicio.Id_Servicio "
                + "JOIN Empleado ON Cita.Id_Empleado = Empleado.Id_Empleado "
                + "WHERE Cita.Estado = 'Finalizada'";

        try {
            Connection con;
            Conexion conexion = new Conexion();
            con = conexion.establecerConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idCita = rs.getInt("Id_Cita");
                String citaEmpleado = rs.getString("Empleado");
                String citaServicio = rs.getString("Servicio");
                Timestamp timestamp = rs.getTimestamp("FechaReserva");
                LocalDateTime fechaCita = timestamp.toLocalDateTime();
                double citaPrecio = rs.getDouble("Precio");

                objetoCitas cita = new objetoCitas(idCita, citaEmpleado, citaPrecio, citaServicio, fechaCita);
                citas.add(cita);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return citas;
    }

    public static ObservableList<DatosCita> validarCitas2() {
        ObservableList<DatosCita> citas = FXCollections.observableArrayList(); // Cambiado a ObservableList
        String consultaSQL = "SELECT Cita.Id_Cita, Cita.Cliente, Cita.FechaReserva, Servicio.NombreServicio AS servicio, "
                + "Empleado.Nombre AS empleado, Servicio.Precio, Cita.Estado "
                + "FROM Cita "
                + "JOIN Servicio ON Cita.Id_Servicio = Servicio.Id_Servicio "
                + "JOIN Empleado ON Cita.Id_Empleado = Empleado.Id_Empleado "
                + "WHERE Cita.Estado = 'Finalizada'"; // Filtro agregado

        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.establecerConexion();
            PreparedStatement stmt = con.prepareStatement(consultaSQL);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                int idCita = resultado.getInt("Id_Cita");
                String cliente = resultado.getString("Cliente");
                Timestamp fechaReserva = resultado.getTimestamp("FechaReserva");
                LocalDateTime fechaCita = fechaReserva.toLocalDateTime();
                String servicio = resultado.getString("servicio");
                String empleado = resultado.getString("empleado");
                double precio = resultado.getDouble("Precio");
                String estado = resultado.getString("Estado");

                DatosCita cita = new DatosCita(idCita, cliente, null, fechaCita, 0, 0, 0, precio, estado, servicio, empleado);
                citas.add(cita);
            }

            // Cerrar recursos
            resultado.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return citas;
    }

}
