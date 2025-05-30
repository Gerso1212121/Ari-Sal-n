
package pControladorVistas;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TablaEmpleadoServicioController extends Application{

    
   @Override
        public void start(Stage primaryStage) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/pVista/EmpleadoTabla.fxml"));
        primaryStage.setTitle("Inicio de Sesión");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setMaximized(true);
        
        
    } catch (IOException e) {
        System.out.println("Error al cargar el archivo FXML: " + e.getMessage());
    }
 }

    
    
}
