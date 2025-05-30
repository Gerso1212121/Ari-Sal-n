package pControladorVistas;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import pModelo.objetoervicios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import pModelo.Conexion;
import pModelo.Funciones;

public class DServiciosTablaController {
    

    Funciones funcion = new Funciones();
    // ObservableList que mantendrá los servicios
    ObservableList<objetoervicios> listaServicios = FXCollections.observableArrayList();
    
    @FXML
    private TableView<objetoervicios> tableViewServicios;

    @FXML
    private TableColumn<objetoervicios, Integer> colId_servicio;
    @FXML
    private TableColumn<objetoervicios, String> colNombreServicio;
    @FXML
    private TableColumn<objetoervicios, String> colDescripcionServicio;
    @FXML
    private TableColumn<objetoervicios, Double> colPrecioServicio, colDuracionServicio;
    @FXML
    private Button btnAñadirServicio;
    @FXML
    private Button btnEliminarServicio;
    
    @FXML
    private Button btnEditarServicio;
    
    
    @FXML 
    private Button btnProductos;
    @FXML
    private Button btnCitas;
    @FXML
    private Button btnEmpleados;
    @FXML 
    private Button btnVentas;
    @FXML 
    private Button btnCerrarSesion;
   
    
    @FXML
    public void initialize() {
        cargarDatosBase();
        colId_servicio.setCellValueFactory(new PropertyValueFactory<>("Id_Servicio"));
        colNombreServicio.setCellValueFactory(new PropertyValueFactory<>("NombreServicio"));
        colDescripcionServicio.setCellValueFactory(new PropertyValueFactory<>("DescripcionServicio"));
        colPrecioServicio.setCellValueFactory(new PropertyValueFactory<>("PrecioServicio"));
        colDuracionServicio.setCellValueFactory(new PropertyValueFactory<>("DuracionServicio"));
                
        btnAñadirServicio.setOnAction(event -> funcion.verRuta(btnAñadirServicio,"/pVista/DAñadirServicios.fxml"));
        btnEliminarServicio.setOnAction(event->eliminarServicio());
        btnEditarServicio.setOnAction(event->editarServicio());
        
        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));
        
        btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
    } 

    
    
    public void cargarDatosBase() {
        try {
            Connection n1;
            PreparedStatement st;
            ResultSet rs;
            Conexion conexion = new Conexion();
            n1 = conexion.establecerConexion();
            String sql = "SELECT Id_Servicio, NombreServicio, DescripcionServicio, Precio, Duracion FROM Servicio";
            st = n1.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                objetoervicios servicio = new objetoervicios(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getDouble(4),
                    rs.getDouble(5)
                );
                //Añadimos los datos al arraylist
                listaServicios.add(servicio);
                
            }
            //Asignandole los valores del arraylist a la tabla
            tableViewServicios.setItems(listaServicios);
            
        } catch (Exception e) {
             funcion.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Error al cargar el archivo FXML " + e.getMessage());
        }
    }
    
    
    
    public void agregarServicio(){
        try{
        Parent Vista = FXMLLoader.load(getClass().getResource("/pVista/BAñadirServicios.fxml"));  
        Scene vistaActual = btnAñadirServicio.getScene();
        vistaActual.setRoot(Vista);
        }
        catch(IOException e){
            funcion.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Error al cargar el archivo FXML");
        }
    }
    
    public void editarServicio(){
        try{
        objetoervicios servicioSeleccionado = tableViewServicios.getSelectionModel().getSelectedItem();
            if(servicioSeleccionado == null){
                funcion.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Seleccione un dato primero para editar");
            }
                else{
                
                FXMLLoader carga = new FXMLLoader(getClass().getResource("/pVista/DAñadirServicios.fxml"));
                
                Parent Vista = carga.load();
                
                
                    servicioSeleccionado.getId_Servicio();
                    servicioSeleccionado.getNombreServicio();   // Si 'nombre' es un atributo
                    servicioSeleccionado.getDescripcionServicio(); // Si 'descripcion' es un atributo
                    servicioSeleccionado.getPrecioServicio();
                    servicioSeleccionado.getDuracionServicio();
                    
                    DAñadirServiciosController controlador = carga.getController();
                    
                    controlador.setValores(servicioSeleccionado.getId_Servicio(),
                                           servicioSeleccionado.getNombreServicio(),
                                           servicioSeleccionado.getDescripcionServicio(),
                                           servicioSeleccionado.getPrecioServicio(),
                                           servicioSeleccionado.getDuracionServicio()
                    );
                    
                Scene vistaActual = btnAñadirServicio.getScene();  
                vistaActual.setRoot(Vista);
            }
        }
        
        catch(IOException e){
             funcion.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Error al cargar el archivo FXML");
        }
    }
    
    public void eliminarServicio() {
    // Obtener la fila seleccionada en el TableView
    objetoervicios servicioSeleccionado = tableViewServicios.getSelectionModel().getSelectedItem();
    
    // Verificar si se ha seleccionado alguna fila
    if (servicioSeleccionado != null) {
        // Mostrar un mensaje de confirmación antes de eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar este servicio?");
        
        // Mostrar el cuadro de diálogo de confirmación
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Proceder con la eliminación en la base de datos
                try {
                    // Establecer conexión a la base de datos
                    Connection n1 = new Conexion().establecerConexion();
                    String sql = "DELETE FROM Servicio WHERE Id_Servicio = ?";
                    PreparedStatement st = n1.prepareStatement(sql);
                    
                    st.setInt(1, servicioSeleccionado.getId_Servicio());
                    
                    int filasAfectadas = st.executeUpdate();
                    
                    if (filasAfectadas > 0) {
                        listaServicios.remove(servicioSeleccionado);
                        
                        tableViewServicios.setItems(listaServicios);
                        
                        funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION,"Eliminación Exitosa", "El servicio ha sido eliminado correctamente.");
                        
                    } 
                    else {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR,"Error", "No se pudo eliminar el servicio");
                    }
                } catch (Exception e) {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Error al eliminar el servicio:" + e.getMessage());
                }
            }
        });
    } 
    else {
        funcion.mostrarAlerta(Alert.AlertType.WARNING,"Advertencia", "Por favor, seleccione un servicio para eliminar.");
        }
    }
    
}
