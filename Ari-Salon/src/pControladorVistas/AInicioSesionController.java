package pControladorVistas;
import pModelo.Conexion;
import pModelo.objetoUsuarios;
//Maneja excepiones de archivos FXML
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

//Es un inicializador de aplicacion javaFX esta libreria añade una extencion 
//a la clase para que inicialice como aplicacio javaFX

import javafx.application.Application;

//Esta libreria nos permite poder utilizar o enlazar las vistas FXML para poder usar los elementos dentro de estas.
import javafx.fxml.FXML;
//Lee el archivo y procesa archivos FXML donde estructura todo el archivo luego de leer cada posicion de estos.
import javafx.fxml.FXMLLoader;

//El javafx.sceneParent almacena la estructura del fxml de la libreria de arriba es un almacenador de nodos secundarios
import javafx.scene.Parent;
//muestra escenas de los archivos fxml almacenados
import javafx.scene.Scene;
//Manejador de alertas como el "javax.swing.JOptionPane;"
import javafx.scene.control.Alert;

//Manjador de eventos de botones, passwordFields, textfields y demas elementos que se pueden usar en el sceneBuilder
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

//Maneja ventanas antes de inicializar este puede configurar tanto el titulo el logo y tamaño de como se quiere mostrar la ventana
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pModelo.Funciones;


public class AInicioSesionController {
    
    objetoUsuarios usuarios = new objetoUsuarios(0,"","",0,"","","", null, ""); 
    Funciones alerta = new Funciones();
    
    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContra;

    @FXML
    private Button btnIniciar;
    
    @FXML
    private Button btnEmpleado;
    
    @FXML
    private ImageView btnSalir;


     @FXML
    public void initialize() {
        btnEmpleado.setOnAction(event -> IniciarEmpleado());
        btnIniciar.setOnAction(event -> IniciarSesion());
        btnSalir.setOnMouseClicked(event -> alerta.salirForm(btnSalir));
    }
     
        
        //1 - Creamos una funcion para asinarsela al boton btnEmpleado para que muestre el form de los empleados
        public void IniciarEmpleado(){
            try{
            Parent vista = FXMLLoader.load(getClass().getResource("/pVista/AInicioSesionEmpleado.fxml"));
            Scene vistaNueva = txtCorreo.getScene();
            vistaNueva.setRoot(vista);
            }
            catch(IOException e){
                alerta.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Error archivo no encontrado: "+ e.getMessage());
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
                    String admin = "Admin";
                    Conexion conexion = new Conexion();
                n1 = conexion.establecerConexion();
                st = n1.prepareStatement("SELECT Contraseña, Rol, Nombre FROM Empleado WHERE CorreoElectronico = ?");
                st.setString(1, usuarios.getCorreoelectronico());
            
                rs = st.executeQuery();
            
                if (rs.next()) {
                String VerificarContra= rs.getString(1);
                String VerificarRool = rs.getString(2);
                String empleado = rs.getString(3);
                    if(VerificarContra.equals(usuarios.getContra()) && VerificarRool.equals(admin)){
                        
                        alerta.mostrarAlerta(Alert.AlertType.CONFIRMATION,"Sesion Iniciada", "Inicio de sesion exitoso \nQue tenga un buen dia :D " + empleado);
                        
                        Parent vistaVer = FXMLLoader.load(getClass().getResource("/pVista/AProductoTabla.fxml"));
                        Scene vistaActual = btnIniciar.getScene();
                        
                        vistaActual.setRoot(vistaVer);
                }
                    
                else if(VerificarContra.equals(usuarios.getContra()) && !VerificarRool.equals(admin)){         
                        alerta.mostrarAlerta(Alert.AlertType.CONFIRMATION,"El usuario ingresado es un empleado", "Usuario ingresado es un empleado");
                    }
                
                    else{
                    alerta.mostrarAlerta(Alert.AlertType.ERROR,"Contraseña Invalida", "Contraseña incorrecta");
                    }
            }
            else {
                alerta.mostrarAlerta(Alert.AlertType.ERROR,"Error de inicio de sesión", "Correo o contraseña incorrectos.");
            }
         }
        } //Fin del try
        
        catch (Exception e) {
            alerta.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Error al conectar a la base de datos: " + e.getMessage());
        }
    }

}
