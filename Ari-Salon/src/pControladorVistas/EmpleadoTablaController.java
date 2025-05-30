package pControladorVistas;

import pModelo.Conexion;
import pModelo.DatosEmpleado;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import pModelo.Funciones;
import java.time.LocalDate;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmpleadoTablaController{

    Funciones funcion = new Funciones();

    @FXML
    private Button btnProductos, btnCerrarSesion;
    @FXML
    private Button btnCitas;
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnVentas;

    @FXML
    private Button btnAñadirEmpleado;
    @FXML
    private Button btnEliminarEmpleado;
    @FXML
    private Button btnEditarEmpleado;
    @FXML
    private Button btnPagarEmpleado;
    @FXML
    private TableView<DatosEmpleado> tableEmpleado;
    @FXML
    private TableColumn<DatosEmpleado, Integer> tcId_Empleado;
    @FXML
    private TableColumn<DatosEmpleado, Integer> tcEdadEmpleado;
    @FXML
    private TableColumn<DatosEmpleado, String> tcNombreEmpleado;
    @FXML
    private TableColumn<DatosEmpleado, String> tcApellidoEmpleado;
    @FXML
    private TableColumn<DatosEmpleado, String> tcTelefonoEmpleado;
    @FXML
    private TableColumn<DatosEmpleado, String> tcCorreoEmpleado;
    @FXML
    private TableColumn<DatosEmpleado, LocalDate> tcFechaContratacionEmpleado;
    @FXML
    private TableColumn<DatosEmpleado, String> tcRolEmpleado;

    private ObservableList<DatosEmpleado> empleadosList;

    public void initialize() {
        // Configurar las columnas con los atributos de DatosEmpleado
        tcId_Empleado.setCellValueFactory(new PropertyValueFactory<>("Id_Empleado"));
        tcEdadEmpleado.setCellValueFactory(new PropertyValueFactory<>("Edad"));
        tcNombreEmpleado.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        tcApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<>("Apellido"));
        tcTelefonoEmpleado.setCellValueFactory(new PropertyValueFactory<>("Telefono"));
        tcCorreoEmpleado.setCellValueFactory(new PropertyValueFactory<>("CorreoElectronico"));
        tcFechaContratacionEmpleado.setCellValueFactory(new PropertyValueFactory<>("FechaContratacion"));
        tcRolEmpleado.setCellValueFactory(new PropertyValueFactory<>("Rol"));

        // Configurar eventos de los botones del sideba
        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));
        
        btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
        
        btnAñadirEmpleado.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/EmpleadoAgenda.fxml"));
        
        btnEliminarEmpleado.setOnAction(event -> eliminarEmpleado());
       
        btnEditarEmpleado.setOnAction(event -> editarEmpleado());
        btnPagarEmpleado.setOnAction(event -> pagarEmpleado());
        // Cargar los empleados desde la base de datos
        cargarEmpleadosDesdeBD();
    }

    @FXML
    public void editarEmpleado() {
        // Verifica si se ha seleccionado un empleado de la tabla
        DatosEmpleado empleadoSeleccionado = tableEmpleado.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado != null) {
            try {
                // Cargar la interfaz de editar en la misma ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pVista/EmpleadoAgenda.fxml"));
                Parent root = loader.load();

                // Obtén el controlador de la nueva vista cargada
                EmpleadoAgendaController agendaController = loader.getController();

                // Pasa los valores del empleado a la interfaz de editar
                agendaController.setValores(empleadoSeleccionado.getId_Empleado(),
                        empleadoSeleccionado.getNombre(),
                        empleadoSeleccionado.getApellido(),
                        empleadoSeleccionado.getEdad(),
                        empleadoSeleccionado.getTelefono(),
                        empleadoSeleccionado.getCorreoElectronico(),
                        empleadoSeleccionado.getFechaContratacion(),
                        empleadoSeleccionado.getRol()
                );

                // Cambia la escena actual por la nueva vista (EmpleadoAgenda)
                Scene currentScene = btnProductos.getScene();
                if (currentScene != null) {
                    currentScene.setRoot(root);  // Reemplazar la raíz de la escena actual
                } else {
                    System.out.println("No se pudo cambiar la escena, la escena actual es nula.");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("ERROR");
                error.setContentText("Hubo un error al cargar la interfaz de edición.");
                error.setHeaderText(null);
                error.showAndWait();
            }
        } else {
            // Si no se ha seleccionado ningún empleado, muestra una alerta
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("ERROR");
            error.setContentText("Por favor, selecciona un empleado para editar.");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }


public void pagarEmpleado() {
    try {
        DatosEmpleado datosEmpleado = tableEmpleado.getSelectionModel().getSelectedItem();

        if (datosEmpleado == null) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Seleccione un empleado para pagar.");
            return;
        }

        TextInputDialog dialogoDias = new TextInputDialog();
        dialogoDias.setTitle("Pago al Empleado");
        dialogoDias.setHeaderText(null);
        dialogoDias.setContentText("Ingrese los días trabajados:");

        dialogoDias.showAndWait().ifPresent(diasTexto -> {
            try {
                int diasTrabajados = Integer.parseInt(diasTexto);

                if (diasTrabajados <= 0) {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "Días inválidos", "Los días trabajados deben ser mayores a 0.");
                    return;
                }
                TextInputDialog dialogoPago = new TextInputDialog();
                dialogoPago.setTitle("Pago al Empleado");
                dialogoPago.setHeaderText(null);
                dialogoPago.setContentText("Ingrese el monto a pagar:");

                dialogoPago.showAndWait().ifPresent(pagoTexto -> {
                    try {
                        double montoPago = Double.parseDouble(pagoTexto);

                        if (montoPago <= 0) {
                            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Monto inválido", "El monto debe ser mayor a 0.");
                            return;
                        }

                        Connection conexion = new Conexion().establecerConexion();
                        try {
   
                            double horasTrabajadas = diasTrabajados * 8;

                            double totalPago = diasTrabajados * montoPago;
                            
                            String sqlSalario = "INSERT INTO Salario (Id_Empleado, HorasTrabajadas, FechaInicio, FechaFinalizacion, TotalNeto) VALUES (?, ?, CURDATE(), CURDATE(), ?)";
                            PreparedStatement stmt = conexion.prepareStatement(sqlSalario);
                            stmt.setLong(1, datosEmpleado.getId_Empleado());
                            stmt.setDouble(2, horasTrabajadas);
                            stmt.setDouble(3, totalPago); 

                           
                            stmt.executeUpdate();

                            funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Pago Realizado", "El pago se ha realizado correctamente.");
                        } catch (SQLException e) {
                            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error al realizar el pago", "No se pudo completar el pago: " + e.getMessage());
                        } finally {
                            conexion.close();
                        }
                    } catch (NumberFormatException e) {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "Monto inválido", "Debe ingresar un número válido para el monto.");
                    } catch (SQLException e) {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", "Ocurrió un error: " + e.getMessage());
                    }
                });
            } catch (NumberFormatException e) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "Días inválidos", "Debe ingresar un número válido para los días.");
            }
        });
    } catch (Exception ex) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", "Ocurrió un error inesperado: " + ex.getMessage());
    }
}


    

    private void cargarEmpleadosDesdeBD() {
        Conexion conexion = new Conexion();
        Connection con = conexion.establecerConexion();
        empleadosList = FXCollections.observableArrayList();

        if (con != null) {
            Statement stmt = null;
            ResultSet rs = null;
            try {
                String query = "SELECT id_empleado, nombre, apellido, edad, telefono, correoelectronico, fechacontratacion, rol FROM empleado";
                stmt = con.createStatement();
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    int id = rs.getInt("id_empleado");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    int edad = rs.getInt("edad");
                    String telefono = rs.getString("telefono");
                    String correoElectronico = rs.getString("correoelectronico");
                    LocalDate fechaContratacion = rs.getDate("fechacontratacion").toLocalDate();
                    String rol = rs.getString("rol");

                    DatosEmpleado empleado = new DatosEmpleado(id, nombre, apellido, edad, telefono, correoElectronico, fechaContratacion, rol);
                    empleadosList.add(empleado);
                }

                tableEmpleado.setItems(empleadosList);

            } catch (SQLException e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error de Base de Datos");
                error.setContentText("Ocurrió un error al cargar los empleados: " + e.getMessage());
                error.setHeaderText("Error al conectarse a la base de datos");
                error.showAndWait();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No se pudo conectar a la base de datos.");
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error de Conexión");
            error.setContentText("No se pudo establecer conexión con la base de datos.");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }

    private void eliminarEmpleado() {
        DatosEmpleado empleadoSeleccionado = tableEmpleado.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de que desea eliminar este empleado?");

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Conexion conexion = new Conexion();
                        Connection con = conexion.establecerConexion();
                        String sql = "DELETE FROM empleado WHERE id_empleado = ?";
                        PreparedStatement st = con.prepareStatement(sql);
                        st.setInt(1, empleadoSeleccionado.getId_Empleado());

                        int filasAfectadas = st.executeUpdate();
                        if (filasAfectadas > 0) {
                            empleadosList.remove(empleadoSeleccionado);
                            tableEmpleado.setItems(empleadosList);
                            showSuccessAlert("Eliminación Exitosa", "El empleado ha sido eliminado correctamente.");
                        } else {
                            showErrorAlert("Error", "No se pudo eliminar el empleado.");
                        }
                    } catch (SQLException e) {
                        showErrorAlert("Error al Eliminar", "Error al eliminar el empleado: " + e.getMessage());
                    }
                }
            });
        } else {
            showWarningAlert("Advertencia", "Por favor, seleccione un empleado para eliminar.");
        }
    }

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    

}