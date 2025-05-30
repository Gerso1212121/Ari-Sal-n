
package pControladorVistas;


import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import pModelo.objetoervicios;

import pModelo.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import pModelo.Funciones;

public class DAñadirServiciosController{
    
    objetoervicios servicios = new objetoervicios(0, "", "", 0, 0);
    Funciones funcion = new Funciones();
    
    Funciones alerta = new Funciones();
    @FXML private TextField txtNombreServicio;
    @FXML private TextField txtPrecioServicio, txtDuracionServicio;
    @FXML private TextArea txtDescripcionServicio;
    @FXML private Button btnAgregarServicio;
    @FXML private ImageView btnRegresarServicios;
    @FXML private Button btnProductos;
    @FXML private Button btnCitas;
    @FXML private Button btnServicios;
    @FXML private Button btnEmpleados;
    @FXML private Button btnVentas, btnCerrarSesion ;
    
    private int id;
    @FXML public void initialize(){
        btnAgregarServicio.setOnAction(event -> agregarServicio());
        btnRegresarServicios.setOnMouseClicked(event -> funcion.verRuta(btnRegresarServicios, "/pVista/DServiciosTabla.fxml"));
        btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
        //Navegacion navbar
        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));        
    }

    public void setValores(int id, String nombre, String descripcion, double precio, double duracion) {
        this.id = id;
        txtNombreServicio.setText(nombre);
        txtDescripcionServicio.setText(descripcion);
        txtPrecioServicio.setText(String.valueOf(precio));
        txtDuracionServicio.setText(String.valueOf(duracion));
    }
                
        public void agregarServicio(){
            if(txtNombreServicio.getText().trim().isEmpty() || txtPrecioServicio.getText().trim().isEmpty() || txtDescripcionServicio.getText().trim().isEmpty() || txtDuracionServicio.getText().trim().isEmpty()) {
                alerta.mostrarAlerta(Alert.AlertType.ERROR,"Campos Obligatorios", "Ingrese los datos requeridos para agregar el servicio.");
                return;
            }
            servicios.setNombreServicio(txtNombreServicio.getText());
            servicios.setPrecioServicio(Double.parseDouble(txtPrecioServicio.getText()));
            servicios.setDescripcionServicio(String.valueOf(txtDescripcionServicio.getText()));
            servicios.setDuracionServicio(Double.parseDouble(txtDuracionServicio.getText()));
            
            if (servicios.getPrecioServicio()<=0.0 || servicios.getDuracionServicio() <= 0.0){
                alerta.mostrarAlerta(Alert.AlertType.ERROR,"Precaucion", "El precio y la duracion del servicio no debe ser un numero menor o igual a 0.");
            }
            else if(servicios.getPrecioServicio()>100 || servicios.getDuracionServicio() > 5){
                alerta.mostrarAlerta(Alert.AlertType.ERROR,"Precaucion", "El precio y la duracion del servicio no debe ser mayor a 100 dolares ni a 5 horas.");
                
            }
            else{
                try{
                Connection n1;
                PreparedStatement st;
                Conexion conexion = new Conexion();
                n1 = conexion.establecerConexion();
                if(id == 0){
                String sql ="INSERT INTO Servicio (NombreServicio, DescripcionServicio, Precio, Duracion) VALUES (?,?,?,?)";
                st = n1.prepareStatement(sql);
                st.setString(1, servicios.getNombreServicio());
                st.setString(2, servicios.getDescripcionServicio());
                st.setDouble(3, servicios.getPrecioServicio());
                st.setDouble(4, servicios.getDuracionServicio());
                
                int filasAfectadas = st.executeUpdate();
                if(filasAfectadas != -1){
                    alerta.mostrarAlerta(Alert.AlertType.CONFIRMATION,"Campos Agregados", "Los datos fuero ingresados correctamente a la base de datos");
                    
                    txtDescripcionServicio.setText("");
                    txtPrecioServicio.setText("");
                    txtNombreServicio.setText("");
                    txtDuracionServicio.setText("");
                    Parent vistaservicio = FXMLLoader.load(getClass().getResource("/pVista/DServiciosTabla.fxml"));
                    Scene vista = txtDescripcionServicio.getScene();
                    
                    vista.setRoot(vistaservicio);
                    
                }
                 else{
                    alerta.mostrarAlerta(Alert.AlertType.ERROR,"ERROR", "Datos no ingresados, intente de nuevo");
                    }
                }
                else{
                    servicios.setNombreServicio(txtNombreServicio.getText());
                    servicios.setDescripcionServicio(txtDescripcionServicio.getText());
                    servicios.setPrecioServicio(Double.parseDouble(txtPrecioServicio.getText()));
                    servicios.setDuracionServicio(Double.parseDouble(txtDuracionServicio.getText()));
                    String sql = "UPDATE Servicio SET NombreServicio = ?, DescripcionServicio = ?, Precio = ?, Duracion = ? WHERE Id_Servicio = ?";
                    st = n1.prepareStatement(sql);
                    st.setString(1, servicios.getNombreServicio());
                    st.setString(2, servicios.getDescripcionServicio());
                    st.setDouble(3, servicios.getPrecioServicio());
                    st.setDouble(4, servicios.getDuracionServicio());
                    st.setInt(5, id);
                    int filas = st.executeUpdate();
                    
                    if(filas > 0){
                    alerta.mostrarAlerta(Alert.AlertType.CONFIRMATION,"Confirmacion", "Datos Actualizados correctamente");
                    Parent vistaVer = FXMLLoader.load(getClass().getResource("/pVista/DServiciosTabla.fxml"));
                    Scene vistaActual = btnAgregarServicio.getScene();
                    vistaActual.setRoot(vistaVer);

                    }
                    else{
                    alerta.mostrarAlerta(Alert.AlertType.ERROR,"ERROR", "Los datos no fueron actualizados, intente de nuevo");
                    }
                
                }
           }
           catch(Exception e){
                    alerta.mostrarAlerta(Alert.AlertType.ERROR,"ERROR", "ERROR EN LA BASE DE DATOS" + e.getMessage());
                }
            }
        
        }   
}
