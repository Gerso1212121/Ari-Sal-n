package pControladorVistas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pModelo.Conexion;
import pModelo.FuncionCitaBD;
import pModelo.objetoCitas;
import pModelo.objetoPago;
import pModelo.objetoVentas;
import pModelo.FuncionPagoDB;
import pModelo.FuncionVentasDB;
import pModelo.Funciones;
import java.sql.Connection;

public class TablaVentasController{
Funciones funcion = new Funciones();
    
    @FXML private Button btnProductos, btnCerrarSesion;
    @FXML private Button btnCitas;
    @FXML private Button btnServicios;
    @FXML private Button btnEmpleados;
    @FXML private Button exportPagosButton;
    @FXML private Button exportVentasButton;
    @FXML private Button exportCitasButton;
    
   
    
    @FXML
    private TableView<objetoCitas> tableCitas;

    @FXML
    private TableColumn<objetoCitas, String> cEmpleado;

    @FXML
    private TableColumn<objetoCitas, Double> cPrecio;

    @FXML
    private TableColumn<objetoCitas, String> cServicio;

    @FXML
    private TableColumn<objetoCitas, Integer> idCita;
    
     @FXML
    private TableColumn<objetoVentas, Integer> idVentas;

    @FXML
    private TableView<objetoVentas> tableVentas;

    @FXML
    private TableColumn<objetoVentas, Integer> vCantidad;

    @FXML
    private TableColumn<objetoVentas, Date> vFecha;

    @FXML
    private TableColumn<objetoVentas, String> vNombre;

    @FXML
    private TableColumn<objetoVentas, Double> vPrecio;

    @FXML
    private TableColumn<objetoVentas, Double> vTotal;
    
     @FXML
    private TableColumn<objetoPago, Integer> idPago;
    
    @FXML
    private TableColumn<objetoPago, String> nombrePago;
    
    @FXML
    private TableColumn<objetoPago, Integer> horasTrabajdas;

    
    @FXML
    private TableColumn<objetoPago, Date> fechaPago;

    @FXML
    private TableColumn<objetoPago, Double> montoPago;
    
    @FXML
    private TableView<objetoPago> tablePagos;


 
    @FXML
    public void initialize() {
        btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
        idCita.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        cEmpleado.setCellValueFactory(new PropertyValueFactory<>("citaEmpleado"));
        cPrecio.setCellValueFactory(new PropertyValueFactory<>("citaPrecio"));
        cServicio.setCellValueFactory(new PropertyValueFactory<>("citaServicio"));
        
         idVentas.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        vNombre.setCellValueFactory(new PropertyValueFactory<>("productoNombre"));
        vPrecio.setCellValueFactory(new PropertyValueFactory<>("productoPrecio"));
        vCantidad.setCellValueFactory(new PropertyValueFactory<>("productoCantidad"));
        vFecha.setCellValueFactory(new PropertyValueFactory<>("fechaVenta"));
        vTotal.setCellValueFactory(new PropertyValueFactory<>("totalVenta"));

         idPago.setCellValueFactory(new PropertyValueFactory<>("idPago"));
        nombrePago.setCellValueFactory(new PropertyValueFactory<>("nombrePago"));
        horasTrabajdas.setCellValueFactory(new PropertyValueFactory<>("horasTrabajadas"));
        fechaPago.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
        montoPago.setCellValueFactory(new PropertyValueFactory<>("montoPago"));
        
        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        
        
        
        cargarDatosPagos();
        cargarDatosCitas();
        cargarDatosVentas();
        
        
        exportVentasButton.setOnAction(event -> generarPDF(
                "SELECT "
                + "vp.Id_Venta AS idVenta, "
                + "p.Nombre AS productoNombre, "
                + "p.Precio AS productoPrecio, "
                + "vp.Cantidad AS productoCantidad, "
                + "vp.FechaEmision AS fechaVenta, "
                + "vp.MontoTotal AS totalVenta "
                + "FROM VentaProducto vp "
                + "JOIN Productos p ON vp.Id_Producto = p.Id_Producto"));
        
        exportPagosButton.setOnAction(event -> generarPDF(
                "SELECT s.Id_Salario, e.Nombre AS NombreEmpleado, s.HorasTrabajadas, s.FechaFinalizacion, s.TotalNeto "
                + "FROM Salario s "
                + "JOIN Empleado e ON s.Id_Empleado = e.Id_Empleado"
        ));
        exportCitasButton.setOnAction(event -> generarPDF(
                "SELECT Cita.Id_Cita, Cita.Cliente, Cita.FechaReserva, Servicio.NombreServicio AS servicio, "
                + "Empleado.Nombre AS empleado, Servicio.Precio, Cita.Estado "
                + "FROM Cita "
                + "JOIN Servicio ON Cita.Id_Servicio = Servicio.Id_Servicio "
                + "JOIN Empleado ON Cita.Id_Empleado = Empleado.Id_Empleado "
                + "WHERE Cita.Estado = 'Finalizada'"
        ));
        
    }
    private void generarPDF(String consulta) {
        try {
            // Crear la conexión a la base de datos
            Conexion Conexion = new Conexion();
            Connection connection = Conexion.establecerConexion();

            // Consulta SQL (por ejemplo, empleados)
            String sqlQuery = consulta;



            // Llamar a la función 'crearPdfTabla'
            funcion.crearPdfTabla(sqlQuery, connection, (Stage) exportVentasButton.getScene().getWindow());
        } catch (Exception e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo generar el PDF: " + e.getMessage());
        }
    }
    private void cargarDatosCitas(){
        FuncionCitaBD Cita = new FuncionCitaBD();
        ObservableList<objetoCitas> citasList = FXCollections.observableArrayList(Cita.validarCitas());
        tableCitas.setItems(citasList);
    }
    
     private void cargarDatosPagos() {
        FuncionPagoDB pagoDB = new FuncionPagoDB();
        ObservableList<objetoPago> pagosList = FXCollections.observableArrayList(pagoDB.obternerInfoPago());
        tablePagos.setItems(pagosList);
    }
    
    private void cargarDatosVentas(){
        FuncionVentasDB ventasDB = new FuncionVentasDB();
        ObservableList<objetoVentas> ventasList = FXCollections.observableArrayList(ventasDB.obternerInfo());
        tableVentas.setItems(ventasList);
    }    
    
}
