package pModelo;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Conexion {
    Connection conectar;
    String base = "PruebaAriSalon";
    String usuario = "root";
    String contra = "";
    String url= "jdbc:mysql://localhost:3306/" + base ;
    
    public Connection establecerConexion(){
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        conectar = DriverManager.getConnection(url , usuario, contra);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + toString() );
        }
    return conectar;
    }
    public static void main(String [] args){
        
        Conexion conexion = new Conexion();
        conexion.establecerConexion();
    
    }
}
