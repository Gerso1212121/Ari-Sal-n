package ApInicializador;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BenjaProyecto extends Application{
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ruta completa desde src 
        Parent vista = FXMLLoader.load(getClass().getResource("/pVista/AInicioSesion.fxml"));
        primaryStage.setScene(new Scene(vista));
        
        primaryStage.initStyle(StageStyle.UNDECORATED); 
        
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
    
}