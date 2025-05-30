package pModelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
//Esta clase es principalmente para acargar los datos a la tabla directamente desdela base de datos
public class FuncionVentasDB {
    
    //Un metodo publi para crear un arraylist y recorrer los datos
    public List<objetoVentas> obternerInfo() {
        List<objetoVentas> ventas = new ArrayList<>();
        
        String sql = "SELECT "
                + "vp.Id_Venta AS idVenta, "
                + "p.Nombre AS productoNombre, "
                + "p.Precio AS productoPrecio, "
                + "vp.Cantidad AS productoCantidad, "
                + "vp.FechaEmision AS fechaVenta, "
                + "vp.MontoTotal AS totalVenta "
                + "FROM VentaProducto vp "
                + "JOIN Productos p ON vp.Id_Producto = p.Id_Producto";
        
        try{Connection con;
            Conexion conexion = new Conexion();
            con = conexion.establecerConexion();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()){
                int idVenta = rs.getInt("idVenta");
                String productoNombre = rs.getString("productoNombre");
                double productoPrecio = rs.getDouble("productoPrecio");
                int productoCantidad = rs.getInt("productoCantidad");
                Date fechaVenta = rs.getDate("fechaVenta");
                double totalVenta = rs.getDouble("totalVenta");
                
                objetoVentas venta = new objetoVentas(idVenta, productoNombre, productoPrecio, productoCantidad, fechaVenta, totalVenta);
                ventas.add(venta);
            }
         
        } catch (SQLException e){
            e.printStackTrace();
        }  
        return ventas;
    }    
}
