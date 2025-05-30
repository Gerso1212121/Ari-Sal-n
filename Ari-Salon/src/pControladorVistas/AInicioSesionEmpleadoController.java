package pControladorVistas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pModelo.Funciones;
import pModelo.Conexion;
import pModelo.objetoUsuarios;

public class AInicioSesionEmpleadoController {  // Implementa Initializable
    
    objetoUsuarios usuarios = new objetoUsuarios(0,"","",0,"","","", null, ""); 
    Funciones alerta = new Funciones();
    
    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContra;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnAdmin;

    @FXML
    private ImageView btnSalir;

    @FXML
    public void initialize() {
        btnAdmin.setOnAction(event -> IniciarAdmin());
        btnSalir.setOnMouseClicked(event-> alerta.salirForm(btnSalir));
        btnIniciar.setOnAction(event->IniciarSesion());
    }
    


    public void IniciarAdmin() {
        try {
            Parent vistanueva = FXMLLoader.load(getClass().getResource("/pVista/AInicioSesion.fxml"));
           
            Scene vistavieja = txtCorreo.getScene();
            
            vistavieja.setRoot(vistanueva);
        } catch (IOException e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setContentText("Error: " + e.getMessage());
            error.showAndWait();
        }
    }
    
   public void IniciarSesion() {
        try {
           
            usuarios.setCorreoelectronico(txtCorreo.getText());
            usuarios.setContra(txtContra.getText());
            
            if (usuarios.getCorreoelectronico().isEmpty() || usuarios.getContra().isEmpty()) {
                alerta.mostrarAlerta(Alert.AlertType.ERROR,"Campos requeridos", "Ingrese los campos requeridos");
            }
            else if(!alerta.validarCorreo(usuarios.getCorreoelectronico())){
                alerta.mostrarAlerta(Alert.AlertType.ERROR,"Correo Invalido", "Ingrese un correo electronico valido");
            }
            else{
            
            Connection n1;
            PreparedStatement st;
            ResultSet rs;
            String  admin = "Admin";
            Conexion conexion = new Conexion();
            n1 = conexion.establecerConexion();
            st = n1.prepareStatement("SELECT Contraseña, Rol, nombre FROM Empleado WHERE CorreoElectronico = ?");
            st.setString(1, usuarios.getCorreoelectronico());
            rs = st.executeQuery();
            
            if (rs.next()) {
                String VerificarContra= rs.getString(1);
                String VerificarRool = rs.getString(2);
                String empleado = rs.getString(3);
                
                    if(VerificarContra.equals(usuarios.getContra()) && !VerificarRool.equals(admin)){
                        alerta.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Sesion Iniciada", "Inicio de sesion exitoso \nQue tenga un buen dia " + empleado);
                      
                
                        Parent vistaVer = FXMLLoader.load(getClass().getResource("/pVista/zEmpleadoInterfaz.fxml"));
                        Scene vistaActual = btnIniciar.getScene();
                        
                        vistaActual.setRoot(vistaVer);
                }
                    
                    else if(VerificarContra.equals(usuarios.getContra()) && VerificarRool.equals(admin)){
                        alerta.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Usuario ingresado es invalido");
                    }
                    else{
                    alerta.mostrarAlerta(Alert.AlertType.ERROR, "Contraseña Invalida", "Contraseña incorrecta");
                    }
            }
            else {
                alerta.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "EL archivo no ha sido encontrado");
            }
         
        } 
            
            
        } 
        //Fin del try
        catch (Exception e) {
            alerta.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al conectar a la base de datos: " + e.getMessage());
        }
    }

}
