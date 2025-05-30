package pControladorVistas;

import pModelo.Conexion;
import pModelo.DatosEmpleado;
import pModelo.Funciones;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class EmpleadoAgendaController{

    Funciones funcion = new Funciones();

    @FXML
    private Button btnProductos;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnCitas;
    @FXML
    private Button btnVentas, btnCerrarSesion;
    @FXML
    private Button btnAgregarEmpleado;
    @FXML
    private TextField txtNombreEmpleado, txtRolEmpleado;
    @FXML
    private TextField txtApellidoEmpleado;
    @FXML
    private TextField txtCorreoEmpleado;
    @FXML
    private TextField txtTelefonoEmpleado;
    @FXML
    private TextField txtEdadEmpleado;
    @FXML
    private DatePicker dataContratacion;
    @FXML
    private ImageView btnRegresarEmpleado;

    DatosEmpleado Empleado = new DatosEmpleado(0, "", "", 0, "", "", null, "");

    @FXML
    public void initialize() {
        btnAgregarEmpleado.setOnAction(event -> agregarNuevoEmpleado());
        btnRegresarEmpleado.setOnMouseClicked(event -> funcion.verRuta(btnRegresarEmpleado, "/pVista/EmpleadoTabla.fxml"));

        // Configuración de botones de navegación
        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));
        
         btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
    }

    // Método para agregar o actualizar un empleado
    @FXML
   private void agregarNuevoEmpleado() {
    PreparedStatement st = null;

    // Validaciones
    String nombre = txtNombreEmpleado.getText().trim();
    String apellido = txtApellidoEmpleado.getText().trim();
    String rol = txtRolEmpleado.getText().trim();
    String telefono = txtTelefonoEmpleado.getText().trim();
    String correo = txtCorreoEmpleado.getText().trim();
    String edadStr = txtEdadEmpleado.getText().trim();

    // Obtener fecha actual
    LocalDate fechaHoy = LocalDate.now();

    // Asignar la fecha de hoy directamente (sin necesidad de que el usuario la seleccione)
    LocalDate fechaSeleccionada = fechaHoy;

    // Validación de nombre, apellido y rol (solo texto, sin caracteres especiales)
    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚÑñ]+")) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El nombre solo debe contener letras.");
        return;
    }
    if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚÑñ]+")) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El apellido solo debe contener letras.");
        return;
    }
    if (!rol.matches("[a-zA-ZáéíóúÁÉÍÓÚÑñ]+")) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El rol solo debe contener letras.");
        return;
    }

    // Validación de teléfono (solo números válidos de El Salvador)
    if (!telefono.matches("^[2-7][0-9]{7}$")) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El teléfono debe ser un número válido de El Salvador (8 dígitos, comenzando con 2-7).");
        return;
    }

    // Validación de edad (solo números, mayor de 18 y menor de 65)
    int edad = 0;
    try {
        edad = Integer.parseInt(edadStr);
        if (edad < 18 || edad > 65) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "La edad debe ser entre 18 y 65 años.");
            return;
        }
    } catch (NumberFormatException e) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "La edad debe ser un número.");
        return;
    }

    // Validación de correo (solo @gmail.com o @hotmail.com)
    if (!correo.matches("^[a-zA-Z0-9._%+-]+@(gmail\\.com|hotmail\\.com)$")) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El correo debe ser válido y pertenecer a gmail.com o hotmail.com.");
        return;
    }

    // Asignar valores a la instancia de DatosEmpleado
    Empleado.setNombre(nombre);
    Empleado.setApellido(apellido);
    Empleado.setEdad(edad);
    Empleado.setCorreoElectronico(correo);
    Empleado.setTelefono(telefono);
    Empleado.setFechaContratacion(fechaSeleccionada);  // Aquí asignamos la fecha actual automáticamente
    Empleado.setRol(rol);

    try {
        Conexion conexion = new Conexion();
        Connection con = conexion.establecerConexion();

        if (Empleado.getId_Empleado() == 0) { // Nuevo empleado
            String insertar = "INSERT INTO Empleado (Nombre, Apellido, Edad, Telefono, CorreoElectronico, FechaContratacion, Rol, Contraseña) VALUES (?,?,?,?,?,?,?,?)";
            st = con.prepareStatement(insertar);

            st.setString(1, Empleado.getNombre());
            st.setString(2, Empleado.getApellido());
            st.setInt(3, Empleado.getEdad());
            st.setString(4, Empleado.getTelefono());
            st.setString(5, Empleado.getCorreoElectronico());
            st.setDate(6, Date.valueOf(Empleado.getFechaContratacion())); // LocalDate a Date
            st.setString(7, Empleado.getRol());
            st.setString(8, "default");

            int filas = st.executeUpdate();
            if (filas > 0) {
                funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmación", "El empleado se añadió correctamente.");
                funcion.verRuta(btnRegresarEmpleado, "/pVista/EmpleadoTabla.fxml");
            }

        } else { // Empleado existente, actualizar
            String actualizar = "UPDATE Empleado SET Nombre = ?, Apellido = ?, Edad = ?, Telefono = ?, CorreoElectronico = ?, FechaContratacion = ?, Rol = ? WHERE Id_Empleado = ?";
            st = con.prepareStatement(actualizar);

            st.setString(1, Empleado.getNombre());
            st.setString(2, Empleado.getApellido());
            st.setInt(3, Empleado.getEdad());
            st.setString(4, Empleado.getTelefono());
            st.setString(5, Empleado.getCorreoElectronico());
            st.setDate(6, Date.valueOf(Empleado.getFechaContratacion()));
            st.setString(7, Empleado.getRol());
            st.setInt(8, Empleado.getId_Empleado());

            int filas = st.executeUpdate();
            if (filas > 0) {
                funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmación", "Empleado Actualizado");
                funcion.verRuta(btnRegresarEmpleado, "/pVista/EmpleadoTabla.fxml");
            }
        }

    } catch (SQLException e) {
        if (e.getSQLState().equals("23505")) { // SQLState para duplicado en PostgreSQL
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "ERROR");
        } else {
            e.printStackTrace();
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "ERROR");
        }
    }
}


    // Método para cargar los datos de un empleado y establecer los valores en los campos
    public void setValores(int id, String nombre, String apellido, int edad, String telefono, String correo, LocalDate fechaContratacion, String rol) {
        this.Empleado.setId_Empleado(id); // Aseguramos que el ID se actualice correctamente
        txtNombreEmpleado.setText(nombre);
        txtApellidoEmpleado.setText(apellido);
        txtEdadEmpleado.setText(String.valueOf(edad));
        txtRolEmpleado.setText(rol);
        txtTelefonoEmpleado.setText(telefono);
        txtCorreoEmpleado.setText(correo);
        dataContratacion.setValue(fechaContratacion);
    }
}