package pControladorVistas;

import pModelo.DatosCita;
import pModelo.Funciones;
import pModelo.Conexion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CitaAgendaController {

    // Función de utilidad
    Funciones funcion = new Funciones();

    // Botones de navegación
    @FXML
    private Button btnProductos, btnCerrarSesion;
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnCitas;
    @FXML
    private Button btnVentas;

    // Campos de formulario
    @FXML
    private TextField txtNombreCita, txtHoraCita;
    @FXML
    private DatePicker dataReserva;
    @FXML
    private ComboBox<String> cbServicioCita;
    @FXML
    private ComboBox<String> cbEmpleadoAsignado;
    @FXML
    private Button btnAgregarCita;
    @FXML
    private ImageView btnRegresarCita;

    // Objeto de Cita
    DatosCita Cita = new DatosCita(0, "", null, null, 0, 0, 0, 0.0, "", "", "");

    // Método de inicialización
    @FXML
    public void initialize() {
        // Asegúrate de que los ComboBox tengan opciones predeterminadas
        retornarServicios();
        retornarEmpleados();
        establecerRestriccionesFecha();
        // Configurar los botones del sidebar
        btnRegresarCita.setOnMouseClicked(event -> regresarTablaCita());
        btnAgregarCita.setOnAction(event -> agregarCita());
        btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
        // Configurar acciones de los botones de navegación
        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));
    }

    // Método para regresar a la tabla de citas
    public void regresarTablaCita() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pVista/CitasTabla.fxml"));
            Parent root = loader.load();
            Scene currentScene = btnRegresarCita.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void agregarCita() {
        if (txtNombreCita.getText().isEmpty()
                || dataReserva.getValue() == null
                || txtHoraCita.getText().isEmpty()
                || cbServicioCita.getValue() == null
                || cbEmpleadoAsignado.getValue() == null) {

            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "No se pudo agregar la cita. Verifica que todos los campos estén llenos.");
            return;
        }

        // Validar que el nombre contenga solo letras
        if (!txtNombreCita.getText().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El nombre solo puede contener letras.");
            return;
        }

        try {
            // Obtener servicio y empleado seleccionados
            String servicioSeleccionado = cbServicioCita.getValue();
            String empleadoSeleccionado = cbEmpleadoAsignado.getValue();

            // Obtener los ID correspondientes
            int idServicio = obtenerIdServicio(servicioSeleccionado);
            int idEmpleado = obtenerIdEmpleado(empleadoSeleccionado);

            LocalDate fecha = dataReserva.getValue(); // Ejemplo: 2024-05-11

            // Parsear la hora del TextField con validación
            LocalTime hora;
            try {
                String textoHora = txtHoraCita.getText();
                // Si el usuario ingresa solo la hora (e.g., "16"), completamos con ":00"
                if (textoHora.matches("\\d{1,2}")) {
                    textoHora += ":00";
                }
                hora = LocalTime.parse(textoHora, DateTimeFormatter.ofPattern("HH:mm"));

                // Validar que la hora esté dentro del rango permitido
                if (hora.isBefore(LocalTime.of(7, 0)) || hora.isAfter(LocalTime.of(17, 0))) {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El horario debe estar entre 07:00 y 17:00.");
                    return;
                }
            } catch (DateTimeParseException e) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Formato de hora inválido. Usa el formato 24 horas, como '16:00'.");
                return;
            }

            // Combinar fecha y hora en un LocalDateTime
            LocalDateTime fechaReserva = LocalDateTime.of(fecha, hora);

            // Crear la nueva cita
            DatosCita nuevaCita = new DatosCita(
                    0,
                    txtNombreCita.getText(),
                    null,
                    fechaReserva,
                    idServicio,
                    idEmpleado,
                    0,
                    0.0,
                    "Pendiente",
                    servicioSeleccionado,
                    empleadoSeleccionado
            );

            // Query para insertar la cita en la base de datos
            String query = "INSERT INTO Cita (Cliente, FechaReserva, Id_Servicio, Id_Empleado, Estado, Precio) VALUES (?, ?, ?, ?, ?, ?)";

            // Conexión a la base de datos
            Conexion conexion = new Conexion();
            try (Connection conn = conexion.establecerConexion(); PreparedStatement stmt = conn.prepareStatement(query)) {

                // Establecer los parámetros de la consulta
                stmt.setString(1, nuevaCita.getCliente());
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(fechaReserva));  // Usar el LocalDateTime combinado
                stmt.setInt(3, nuevaCita.getIdServicio());
                stmt.setInt(4, nuevaCita.getIdEmpleado());
                stmt.setString(5, nuevaCita.getEstado());
                stmt.setDouble(6, 0.0);

                // Ejecutar la consulta
                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmación", "Datos guardados correctamente");
                    funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml");
                    
                } else {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "No se pudo agregar la cita.");
                }
            }
        } catch (SQLException e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error de SQL: " + e.getMessage());
        } catch (NumberFormatException e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El formato de algunos campos es incorrecto.");
        }
    }

    // Método para obtener el ID del servicio por su nombre
    private int obtenerIdServicio(String servicioNombre) {
        int id = 0;
        try {
            Connection n1;
            PreparedStatement st;
            ResultSet rs;
            Conexion conexion = new Conexion();
            n1 = conexion.establecerConexion();
            st = n1.prepareStatement("SELECT Id_Servicio FROM Servicio WHERE NombreServicio = ?");
            st.setString(1, servicioNombre);
            rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt("Id_Servicio");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    // Método para obtener el ID del empleado por su nombre
    private int obtenerIdEmpleado(String empleadoNombre) {
        int id = 0;
        try {
            Connection n1;
            PreparedStatement st;
            ResultSet rs;
            Conexion conexion = new Conexion();
            n1 = conexion.establecerConexion();
            st = n1.prepareStatement("SELECT Id_Empleado FROM Empleado WHERE Nombre = ?");
            st.setString(1, empleadoNombre);
            rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt("Id_Empleado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    
    public void setValores(int idEmpleado, int idCita, String cliente, LocalDate fechaReserva, LocalTime horaReserva,
            String servicio, String empleado, double precio, String estado) {
        
        txtNombreCita.setText(cliente);
        dataReserva.setValue(fechaReserva);
        
        if (horaReserva != null) {
            String horaFormato = horaReserva.format(DateTimeFormatter.ofPattern("HH:mm"));
            txtHoraCita.setText(horaFormato);
        } else {
            txtHoraCita.setText("");
        }

        cbServicioCita.setValue(servicio);
        cbEmpleadoAsignado.setValue(empleado);
    }

    public void retornarEmpleados() {
        ObservableList<String> empleados = FXCollections.observableArrayList();
        try {
            Connection n1;
            PreparedStatement st;
            ResultSet rs;
            Conexion conexion = new Conexion();
            n1 = conexion.establecerConexion();
            st = n1.prepareStatement("Select Nombre from Empleado");
            rs = st.executeQuery();
            while (rs.next()) {
                empleados.add(rs.getString("Nombre"));
            }
            cbEmpleadoAsignado.setItems(empleados);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void retornarServicios() {
        ObservableList<String> servicios = FXCollections.observableArrayList();
        try {
            Connection n1;
            PreparedStatement st;
            ResultSet rs;
            Conexion conexion = new Conexion();
            n1 = conexion.establecerConexion();
            st = n1.prepareStatement("Select NombreServicio from Servicio");
            rs = st.executeQuery();
            while (rs.next()) {
                servicios.add(rs.getString("NombreServicio"));
            }
            cbServicioCita.setItems(servicios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void establecerRestriccionesFecha() {

        LocalDate today = LocalDate.now();

        LocalDate twoYearsLater = today.plusYears(1);

        dataReserva.setValue(today);

        dataReserva.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(today) || item.isAfter(twoYearsLater));
            }
        });
    }
}