package pControladorVistas;

import java.io.File;
import java.io.IOException;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pModelo.Funciones;
import java.sql.SQLException;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import pModelo.FuncionCitaBD;
import pModelo.Conexion;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;  // Para formatear la fecha y hora
import javafx.scene.control.TableCell;  // Para personalizar las celdas de la tabla
import javafx.scene.control.TableColumn;
import java.time.LocalDateTime;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pModelo.DatosCita;

public class CitaTablaController{

    Funciones funcion = new Funciones();

    @FXML
    private Button btnEditarCita, btnActualizarCita;
    @FXML
    private Button btnAgendarCitaPDF, btnCerrarSesion;
    @FXML
    private ComboBox cbFiltrarFechas;
    @FXML
    private Button btnEliminarCita;
    @FXML
    private Button btnProductos;
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnVentas;
    @FXML
    private Button btnAgendarCita;
    @FXML
    private TableView<DatosCita> tableCita;  // Asegúrate de que la tabla sea de tipo DatosCita
    @FXML
    private TableColumn<DatosCita, Integer> tcId_cita;
    @FXML
    private TableColumn<DatosCita, String> tcCitaNombre;
    @FXML
    private TableColumn<DatosCita, LocalDateTime> tcCitaFecha;
    @FXML
    private TableColumn<DatosCita, String> tcCitaServicio;
    @FXML
    private TableColumn<DatosCita, String> tcCitaEmpleado;
    @FXML
    private TableColumn<DatosCita, Double> tcCitaTotal;
    @FXML
    private TableColumn<DatosCita, String> tcCitaEstado;
    @FXML
    private TextField txtBuscarCitas;

    public ObservableList<DatosCita> citas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Inicializar el ComboBox con las opciones
        cbFiltrarFechas.setItems(FXCollections.observableArrayList(
                "Ordenar por Defecto",
                "Pendiente",
                "Finalizado",
                "Hoy"
        ));

// Manejar el cambio de selección en el ComboBox
        cbFiltrarFechas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filtrarCitas(newValue.toString());
            }
        });

        tableCita.setItems(citas);

        txtBuscarCitas.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarCita(newValue); // Llama al método buscarProductos con el texto ingresado
        });

        tcId_cita.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        tcCitaNombre.setCellValueFactory(new PropertyValueFactory<>("Cliente"));

        // Formatear fecha y hora con LocalDateTime
        tcCitaFecha.setCellValueFactory(new PropertyValueFactory<>("horaReserva"));
        tcCitaFecha.setCellFactory(col -> new TableCell<DatosCita, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));  // Formato completo
                }
            }
        });

        tcCitaServicio.setCellValueFactory(new PropertyValueFactory<>("servicio"));
        tcCitaEmpleado.setCellValueFactory(new PropertyValueFactory<>("empleado"));
        tcCitaTotal.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcCitaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Cargar datos desde la base de datos
        FuncionCitaBD funcionCitaBD = new FuncionCitaBD();
        List<DatosCita> listaCitas = funcionCitaBD.obtenerCitas();  // Llamamos al método correctamente
        citas.setAll(listaCitas);  // Asignar los datos a la lista 'citas'
        tableCita.setItems(citas);  // Asignar la lista a la tabla

        // Agregar los manejadores de eventos
        btnAgendarCita.setOnAction(event -> abrirCitaAgendar());

        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));

        btnEliminarCita.setOnAction(event -> eliminarCita());
        btnEditarCita.setOnAction(event -> editarCita());
        btnAgendarCitaPDF.setOnAction(event -> importarCitaDesdePDF());

        btnActualizarCita.setOnAction(event -> pagarEmpleados());
        btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
        // Formatear fecha y hora con LocalDateTime
        tcCitaFecha.setCellValueFactory(new PropertyValueFactory<>("horaReserva"));
        tcCitaFecha.setCellFactory(col -> new TableCell<DatosCita, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        });

        // Personalizar el color de las filas según el estado de la cita
        tableCita.setRowFactory(tv -> {
            TableRow<DatosCita> row = new TableRow<DatosCita>() {
                @Override
                protected void updateItem(DatosCita item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setStyle("");
                    } else if ("Finalizada".equals(item.getEstado())) {
                        // Cambiar el color de fondo a verde lima
                        setStyle("-fx-background-color: #6BFF9C;");
                    } else {
                        setStyle("");  // Restablecer el estilo si no está finalizada
                    }
                }
            };
            return row;
        });

        // Asignar la lista a la tabla
    }

    private void filtrarCitas(String filtro) {
        // Crear una lista filtrada en función del filtro seleccionado
        ObservableList<DatosCita> citasFiltradas = FXCollections.observableArrayList();

        switch (filtro) {
            case "Ordenar por Defecto":
                // Mostrar todas las citas
                citasFiltradas.setAll(citas);
                break;

            case "Pendiente":
                // Filtrar citas con estado "Pendiente"
                for (DatosCita cita : citas) {
                    if ("Pendiente".equalsIgnoreCase(cita.getEstado())) {
                        citasFiltradas.add(cita);
                    }
                }
                break;

            case "Finalizado":
                // Filtrar citas con estado "Finalizado"
                for (DatosCita cita : citas) {
                    if ("Finalizada".equalsIgnoreCase(cita.getEstado())) {
                        citasFiltradas.add(cita);
                    }
                }
                break;

            case "Hoy":
                // Filtrar citas programadas para hoy
                LocalDateTime hoy = LocalDateTime.now();
                for (DatosCita cita : citas) {
                    if (cita.getHoraReserva() != null
                            && cita.getHoraReserva().toLocalDate().isEqual(hoy.toLocalDate())) {
                        citasFiltradas.add(cita);
                    }
                }
                break;

            default:
                // En caso de un filtro no reconocido, no hacer nada
                return;
        }

        // Asignar la lista filtrada a la tabla
        tableCita.setItems(citasFiltradas);
    }

    public void abrirCitaAgendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pVista/CitasAgenda.fxml"));
            Parent root = loader.load();

            Scene currentScene = btnAgendarCita.getScene();
            currentScene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editarCita() {
        // Verifica si se ha seleccionado un empleado de la tabla
        DatosCita citaSeleccionada = tableCita.getSelectionModel().getSelectedItem();
        if (citaSeleccionada != null) {
            try {
                // Cargar la interfaz de editar en la misma ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pVista/CitasAgenda.fxml"));
                Parent root = loader.load();

                // Obtén el controlador de la nueva vista cargada
                CitaAgendaController agendaController = loader.getController();

                // Pasa los valores del empleado a la interfaz de editar
                agendaController.setValores(
                        citaSeleccionada.getIdEmpleado(),
                        citaSeleccionada.getIdCita(),
                        citaSeleccionada.getCliente(),
                        citaSeleccionada.getFechaReserva(),
                        null,
                        citaSeleccionada.getServicio(),
                        citaSeleccionada.getEmpleado(),
                        citaSeleccionada.getPrecio(),
                        citaSeleccionada.getEstado()
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

    private void eliminarCita() {
        DatosCita citaSeleccionada = tableCita.getSelectionModel().getSelectedItem();

        if (citaSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de que desea eliminar esta cita?");

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Conexion conexion = new Conexion();
                        java.sql.Connection con = conexion.establecerConexion();
                        String sql = "DELETE FROM Cita WHERE id_cita = ?";
                        PreparedStatement st = con.prepareStatement(sql);
                        st.setInt(1, citaSeleccionada.getIdCita());

                        int filasAfectadas = st.executeUpdate();
                        if (filasAfectadas > 0) {
                            citas.remove(citaSeleccionada);  // Solo eliminar de la lista
                            funcion.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La cita fue eliminada correctamente.");
                        } else {
                            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar la cita.");
                        }
                    } catch (SQLException e) {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al eliminar la cita: " + e.getMessage());
                    }
                }
            });
        } else {
            funcion.mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Por favor, seleccione una cita para eliminar.");
        }
    }

    public void importarCitaDesdePDF() {
        // Configurar el FileChooser para seleccionar archivos PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(btnAgendarCitaPDF.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Cargar y extraer texto del PDF
                PDDocument document = PDDocument.load(selectedFile);
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                document.close();

                // Procesar el texto del PDF
                String[] lineas = text.split("\n");

                String nombre = null,
                        servicio = null,
                        fecha = null;
                double precio = 0;
                int idEmpleado = -1, idServicio = -1;

                for (String linea : lineas) {
                    if (linea.startsWith("Nombre:")) {
                        nombre = linea.replace("Nombre:", "").trim();
                    } else if (linea.startsWith("Servicio:")) {
                        servicio = linea.replace("Servicio:", "").trim();
                    } else if (linea.startsWith("Fecha y hora:")) {
                        fecha = linea.replace("Fecha y hora:", "").trim();

                        // Verificar y corregir formato de fecha y hora
                        if (fecha.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                            // La fecha y hora están en el formato correcto (yyyy-MM-dd HH:mm:ss)
                        } else if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Formato de fecha/hora inválido en el PDF.");
                            return;
                        }
                    } else if (linea.startsWith("Empleado:")) {
                        idEmpleado = Integer.parseInt(linea.replace("Empleado:", "").trim());
                    }
                }

                // Obtener ID del servicio desde el nombre del servicio
                if (servicio != null) {
                    Conexion conexion = new Conexion();
                    Connection n1 = conexion.establecerConexion();

                    String consultaServicio = "SELECT Id_Servicio, Precio FROM servicio WHERE NombreServicio = ?";
                    PreparedStatement psServicio = n1.prepareStatement(consultaServicio);
                    psServicio.setString(1, servicio);

                    ResultSet rs = psServicio.executeQuery();
                    if (rs.next()) {
                        idServicio = rs.getInt("Id_Servicio");
                        precio = rs.getInt("Precio");
                    } else {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El servicio '" + servicio + "' no existe en la base de datos.");
                        return;
                    }
                }

                // Validar los datos extraídos
                if (nombre != null && servicio != null && fecha != null && precio > 0 && idEmpleado > 0 && idServicio > 0) {
                    Conexion conexion = new Conexion();
                    Connection n1 = conexion.establecerConexion();

                    // Estado por defecto
                    String estado = "Pendiente";

                    String sql = "INSERT INTO cita (Cliente, Estado, Precio, FechaReserva, Id_Servicio, Id_Empleado) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = n1.prepareStatement(sql);
                    ps.setString(1, nombre);  // Cliente
                    ps.setString(2, estado);  // Estado
                    ps.setDouble(3, precio);  // Precio
                    ps.setString(4, fecha);   // FechaReserva (ya normalizada con fecha y hora)
                    ps.setInt(5, idServicio); // Id_Servicio
                    ps.setInt(6, idEmpleado); // Id_Empleado

                    // Ejecutar la consulta
                    int filasInsertadas = ps.executeUpdate();
                    if (filasInsertadas > 0) {
                        FuncionCitaBD funcionCitaBD = new FuncionCitaBD();
                        List<DatosCita> listaCitas = funcionCitaBD.obtenerCitas();
                        citas.setAll(listaCitas);
                        tableCita.setItems(citas);
                        funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "CONFIRMACIÓN", "Cita Importada Exitosamente");
                    } else {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "No se pudo importar la cita.");
                    }
                } else {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Datos incompletos o no válidos en el PDF.");
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error en la conexión a la base de datos: " + e.getMessage());
            }
        } else {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "No se seleccionó un documento PDF.");
        }
    }

    public void buscarCita(String buscar) {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.establecerConexion();

            if (con != null) {
                // Determinar si la búsqueda es un número o texto
                boolean esNumerico = buscar.matches("\\d+"); // Verifica si la búsqueda es numérica

                String query = "SELECT Cita.Id_Cita, Cita.Cliente, Cita.FechaReserva, Servicio.NombreServicio AS servicio, "
                        + "Empleado.Nombre AS empleado, Servicio.Precio, Cita.Estado "
                        + "FROM Cita "
                        + "JOIN Servicio ON Cita.Id_Servicio = Servicio.Id_Servicio "
                        + "JOIN Empleado ON Cita.Id_Empleado = Empleado.Id_Empleado "
                        + "WHERE Cita.Cliente LIKE ? " + (esNumerico ? "OR Cita.Id_Cita = ?" : "");

                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, "%" + buscar + "%");
                if (esNumerico) {
                    st.setInt(2, Integer.parseInt(buscar)); // Asigna el valor como entero
                }

                ResultSet resultado = st.executeQuery();

                // Crear una nueva lista para las citas encontradas
                ObservableList<DatosCita> citasFiltradas = FXCollections.observableArrayList();

                while (resultado.next()) {
                    int idCita = resultado.getInt("Id_Cita");
                    String cliente = resultado.getString("Cliente");
                    Timestamp fechaReserva = resultado.getTimestamp("FechaReserva");
                    LocalDateTime fechaCita = fechaReserva.toLocalDateTime();
                    String servicio = resultado.getString("servicio");
                    String empleado = resultado.getString("empleado");
                    double precio = resultado.getDouble("Precio");
                    String estado = resultado.getString("Estado");

                    DatosCita cita = new DatosCita(idCita, cliente, null, fechaCita, 0, 0, 0, precio, estado, servicio, empleado);
                    citasFiltradas.add(cita);
                }

                // Actualizar la tabla con los resultados filtrados
                tableCita.setItems(citasFiltradas);
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (SQLException e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al buscar citas: " + e.getMessage());
        }
    }

    public void pagarEmpleados() {
        DatosCita empleadopago = tableCita.getSelectionModel().getSelectedItem();

        if (empleadopago != null) {
            if ("Finalizada".equals(empleadopago.getEstado())) {
                funcion.mostrarAlerta(Alert.AlertType.WARNING, "WARNING", "El estado ya fue actualizado a FINALIZADO");
                return;
            }

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Cita Actualizar");
            confirmacion.setContentText("¿Estas seguro de actualizar esta cita a FINALIZADA?");
            confirmacion.setHeaderText(null);

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Connection n1;
                    Conexion conexion = new Conexion();
                    PreparedStatement st;
                    ResultSet rs;
                    try {
                        n1 = conexion.establecerConexion();
                        st = n1.prepareStatement("UPDATE Cita SET Estado = ? WHERE Id_Cita = ?");
                        st.setString(1, "Finalizada");
                        st.setInt(2, empleadopago.getIdCita());

                        int filas = st.executeUpdate();
                        if (filas > 0) {
                            funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Actualizado", "La cita ha sido actualizada correctamente");
                            FuncionCitaBD funcionCitaBD = new FuncionCitaBD();
                            List<DatosCita> listaCitas = funcionCitaBD.obtenerCitas();  
                            citas.setAll(listaCitas); 
                            tableCita.setItems(citas);
                        }
                    } catch (Exception e) {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error al actualizar la cita: " + e.getMessage());
                    }
                } else {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "Cancelado", "Acción sin cambios.");
                }
            });
        } else {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Seleccione una cita a actualizar");
        }
    }

    private int id;

    public void setIdEmpleado(int id) {
        this.id = id;

    }

    
}