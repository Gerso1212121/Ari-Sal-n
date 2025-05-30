package pModelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FuncionPagoDB {
    
      //Un metodo publi para crear un arraylist y recorrer los datos
    public List<objetoPago> obternerInfoPago() {
        List<objetoPago> pagos = new ArrayList<>();
        
        String sql = "SELECT s.Id_Salario, e.Nombre AS NombreEmpleado, s.HorasTrabajadas, s.FechaFinalizacion, s.TotalNeto "
                + "FROM Salario s "
                + "JOIN Empleado e ON s.Id_Empleado = e.Id_Empleado";
        
        try{Connection con;
            Conexion conexion = new Conexion();
            con = conexion.establecerConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()){
                int idPago = rs.getInt("Id_Salario");
                String nombrePago = rs.getString("NombreEmpleado");
                int horasTrabajadas = rs.getInt("HorasTrabajadas");
                Date fechaPago = rs.getDate("FechaFinalizacion");
                double montoPago = rs.getDouble("TotalNeto");
                
                objetoPago pago = new objetoPago(idPago, nombrePago, horasTrabajadas,fechaPago,montoPago);
                pagos.add(pago);
            }
            
        } catch (SQLException e){
            e.printStackTrace();
        }  
        return pagos;
    }
    
}
