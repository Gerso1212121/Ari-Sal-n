package pControladorVistas;

import pModelo.Conexion;
import pModelo.DatosProducto;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import pModelo.Funciones;

public class AProductoAgregarController {

    Funciones funcion = new Funciones();

    DatosProducto Producto = new DatosProducto(0, "", "", 0.0, 0, "", "");

    @FXML
    private TextField txtNombreProducto;
    @FXML
    private TextField txtCantidadProducto;
    @FXML
    private TextField txtPrecioProducto;
    @FXML
    private TextArea txaDescripcionProducto;
    @FXML
    private ComboBox<String> cbCategoriaProducto;
    @FXML
    private ImageView btnRegresarProducto;
    @FXML
    private Button btnAgregarProducto;
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

    @FXML
    public void initialize() {
        btnProductos.setOnAction(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));
         btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
        cbCategoriaProducto.getItems().addAll(
                "Cuidado Capilar",
                "Cuidado Facial",
                "Cuidado Corporal",
                "Manicura y Pedicura",
                "Estética Avanzada",
                "Servicios para Eventos Barbería",
                "Productos y Venta"
        );

        btnAgregarProducto.setOnAction(event -> agregarProducto());
        btnRegresarProducto.setOnMouseClicked(event -> funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml"));
    }

    public void agregarProducto() {
    try {
        // Validar que todos los campos estén completos
        if (txtNombreProducto.getText().isEmpty()
                || txtCantidadProducto.getText().isEmpty()
                || txtPrecioProducto.getText().isEmpty()
                || txaDescripcionProducto.getText().isEmpty()
                || cbCategoriaProducto.getValue() == null) {

            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Por favor, complete todos los campos.");
            return;
        }
        
        // Validar que la cantidad sea un número entero y no mayor a 50
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidadProducto.getText());
            if (cantidad <= 0 || cantidad > 50) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "La cantidad debe ser un número entero entre 0 y 50.");
                return;
            }
        } catch (NumberFormatException e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "La cantidad debe ser un número entero.");
            return;
        }

        // Validar que el precio sea un número decimal y no mayor a 200
        double precio;
        try {
            precio = Double.parseDouble(txtPrecioProducto.getText());
            if (precio <= 0) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El precio no puede ser negativo.");
                return;
            }
            if (precio > 200) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El precio no puede ser mayor a 200.");
                return;
            }
        } catch (NumberFormatException e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "El precio debe ser un número decimal.");
            return;
        }

        // Asignar valores del formulario al objeto Producto
        Producto.setNombre(txtNombreProducto.getText());
        Producto.setExistencias(cantidad);
        Producto.setDescripcion(txaDescripcionProducto.getText());
        Producto.setCategoria(cbCategoriaProducto.getValue());
        Producto.setPrecio(precio);

        // Crear un nuevo objeto DatosProducto con los valores ingresados
        DatosProducto nuevoProducto = new DatosProducto(
                idProducto, // ID del producto, si es 0 es un nuevo producto
                Producto.getNombre(),
                Producto.getDescripcion(),
                Producto.getPrecio(),
                Producto.getExistencias(),
                Producto.getCategoria(),
                Producto.getEstado()
        );

        // Insertar o actualizar producto en la base de datos
        if (idProducto == 0) {
            insertarProductoEnBD(nuevoProducto);
            // Insertar nuevo producto si el id es 0
        } else {
            actualizarProductoEnBD(nuevoProducto);
            // Actualizar el producto si ya tiene un id
        }

    } catch (Exception e) {
        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Ocurrió un error inesperado: " + e.getMessage());
    }
}


    private void insertarProductoEnBD(DatosProducto producto) {
        Conexion conexion = new Conexion();
        Connection con = conexion.establecerConexion();
        if (con != null) {
            try {
                String query = "INSERT INTO productos (id_producto, Nombre, Descripcion, precio, Existencias, categoria, estado) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                // Preparar la consulta para insertar
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setInt(1, producto.getIdProducto());
                pstmt.setString(2, producto.getNombre());
                pstmt.setString(3, producto.getDescripcion());
                pstmt.setDouble(4, producto.getPrecio());
                pstmt.setInt(5, producto.getExistencias());
                pstmt.setString(6, producto.getCategoria());
                pstmt.setString(7, "Disponible");

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Datos Añadidos", "Producto agregado correctamente");
                    funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml");
                } else {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error no se pudo guardar.");
                }

            } catch (SQLException e) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Hubo un error al guardar el producto en la base de datos.");
            }
        } else {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error al conectar a la base de datos");
        }
    }

    private void actualizarProductoEnBD(DatosProducto producto) {
        Conexion conexion = new Conexion();
        Connection con = conexion.establecerConexion();
        if (con != null) {
            try {
                if (Producto.getExistencias() <= 0) {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Valores menores o iguales a 0 invalido");
                } else {
                    String query = "UPDATE productos SET Nombre = ?, Descripcion = ?, precio = ?, Existencias = ?, categoria = ?, estado = ? WHERE id_producto = ?";

                    // Preparar la consulta para actualizar
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setString(1, producto.getNombre());
                    pstmt.setString(2, producto.getDescripcion());
                    pstmt.setDouble(3, producto.getPrecio());
                    pstmt.setInt(4, producto.getExistencias());
                    pstmt.setString(5, producto.getCategoria());
                    pstmt.setString(6, "Disponible");
                    pstmt.setInt(7, producto.getIdProducto());

                    int filas = pstmt.executeUpdate();
                    if (filas > 0) {
                        funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Datos Actualizados", "Producto actualizado correctamente");
                        funcion.verRuta(btnProductos, "/pVista/AProductoTabla.fxml");
                    } else {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error al actualizar el producto.");
                    }
                }
            } catch (SQLException e) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Hubo un error al actualizar el producto en la base de datos.");
            }
        } else {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error al conectar a la base de datos");
        }
    }

    private AProductoTablaController productoTablaController;

    public void setProductoTablaController(AProductoTablaController productoTablaController) {
        this.productoTablaController = productoTablaController;
    }

    private int idProducto;

    public void setValores(int idProducto, String nombre, String descripcion, double precio, int existencias, String categoria, String estado) {
        this.idProducto = idProducto;
        txtNombreProducto.setText(nombre);
        txaDescripcionProducto.setText(descripcion);
        txtPrecioProducto.setText(String.valueOf(precio));
        txtCantidadProducto.setText(String.valueOf(existencias));
        cbCategoriaProducto.setValue(categoria);
    }
}