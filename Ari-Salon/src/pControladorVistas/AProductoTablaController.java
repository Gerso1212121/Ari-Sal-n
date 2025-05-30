package pControladorVistas;

import pModelo.Conexion;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pModelo.DatosProducto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

import pModelo.Funciones;

public class AProductoTablaController {

    Funciones funcion = new Funciones();

    @FXML
    private Button btnCitas, btnCerrarSesion;
    @FXML
    private Button btnServicios;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnVentas;
    @FXML
    private Button btnAñadirProducto;
    @FXML
    private Button btnEditarProducto;
    @FXML
    private TextField txtBuscarProducto;
    @FXML
    private TableView<DatosProducto> tableProducto;
    @FXML
    private TableColumn<DatosProducto, Integer> tcId_Producto;
    @FXML
    private TableColumn<DatosProducto, String> tcNombreProducto;
    @FXML
    private TableColumn<DatosProducto, Integer> tcExistenciasProducto;
    @FXML
    private TableColumn<DatosProducto, String> tcCategoriaProducto;
    @FXML
    private TableColumn<DatosProducto, Double> tcPrecioProducto;
    @FXML
    private TableColumn<DatosProducto, String> tcEstadoProducto;
    @FXML
    private TableColumn<DatosProducto, String> tcDescripcionProducto;
    @FXML
    private Button btnEliminarProducto;
    @FXML
    private Button btnVenderProductos;
    @FXML
    private ComboBox cbCategoriaFiltro;
    private ObservableList<DatosProducto> productosList;

    @FXML
    public void initialize() {

        btnCitas.setOnAction(event -> funcion.verRuta(btnCitas, "/pVista/CitasTabla.fxml"));
        btnServicios.setOnAction(event -> funcion.verRuta(btnServicios, "/pVista/DServiciosTabla.fxml"));
        btnEmpleados.setOnAction(event -> funcion.verRuta(btnEmpleados, "/pVista/EmpleadoTabla.fxml"));
        btnVentas.setOnAction(event -> funcion.verRuta(btnVentas, "/pVista/TablaVentas.fxml"));
        btnCerrarSesion.setOnAction(event-> funcion.salirForm(btnCerrarSesion));
        txtBuscarProducto.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarProductos(newValue); // Llama al método buscarProductos con el texto ingresado
        });
        // Acción para abrir la interfaz de agregar producto
        btnAñadirProducto.setOnAction(event -> funcion.verRuta(btnAñadirProducto, "/pVista/AProductoAgregar.fxml"));
        btnEliminarProducto.setOnAction(event -> eliminarProducto());

        // Llamar a la función para cargar los productos desde la base de datos
        cargarProductosDesdeBD();

        // Configurar las columnas de la tabla
        tcId_Producto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        tcNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcExistenciasProducto.setCellValueFactory(new PropertyValueFactory<>("existencias"));
        tcCategoriaProducto.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        tcPrecioProducto.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcEstadoProducto.setCellValueFactory(new PropertyValueFactory<>("estado"));
        tcDescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        btnVenderProductos.setOnAction(event -> venderProducto());
        btnEditarProducto.setOnAction(event -> editarProducto());

        cbCategoriaFiltro.getItems().addAll(
                "Ordenar por Defecto",
                "No Disponible",
                "Disponible",
                "Productos Mayores de $50.00",
                "Productos Menores de $50.00"
        );

        cbCategoriaFiltro.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                filtrarProductos(newValue.toString());
            }
        });

    }

    public void agregarProductoATabla(DatosProducto nuevoProducto) {
        productosList.add(nuevoProducto);
        tableProducto.setItems(productosList);
    }

    public void eliminarProducto() {
        // Obtener la fila seleccionada en el TableView
        DatosProducto productoSeleccionado = tableProducto.getSelectionModel().getSelectedItem();

        // Verificar si se ha seleccionado alguna fila
        if (productoSeleccionado != null) {
            // Mostrar un mensaje de confirmación antes de eliminar
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de que desea eliminar este producto?");

            // Mostrar el cuadro de diálogo de confirmación
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Proceder con la eliminación en la base de datos
                    try {
                        // Establecer conexión a la base de datos
                        Conexion conexion = new Conexion();
                        Connection con = conexion.establecerConexion();
                        String sql = "DELETE FROM productos WHERE id_Producto = ?";
                        PreparedStatement st = con.prepareStatement(sql);

                        // Establecer el id del producto a eliminar
                        st.setInt(1, productoSeleccionado.getIdProducto());

                        int filasAfectadas = st.executeUpdate();

                        if (filasAfectadas > 0) {
                            // Eliminar el producto de la lista en la interfaz
                            productosList.remove(productoSeleccionado);
                            tableProducto.setItems(productosList);

                            // Mostrar una alerta de éxito
                            funcion.mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminación Exitosa", "El producto ha sido eliminado correctamente.");
                        } else {
                            // Mostrar alerta si no se pudo eliminar
                            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el producto.");
                        }
                    } catch (SQLException e) {
                        // Mostrar alerta si ocurre un error al eliminar
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al eliminar el producto: " + e.getMessage());
                    }
                }
            });
        } else {
            // Mostrar alerta si no se ha seleccionado ningún producto
            funcion.mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Por favor, seleccione un producto para eliminar.");
        }
    }

    private void cargarProductosDesdeBD() {
        // Obtenemos la conexión a la base de datos
        Conexion conexion = new Conexion();
        Connection con = conexion.establecerConexion();

        // Lista para almacenar los productos
        productosList = FXCollections.observableArrayList();

        if (con != null) {
            try {
                // Consulta SQL para obtener los productos
                String query = "SELECT * FROM productos"; // Cambia esto si el nombre de tu tabla es diferente
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                // Recorrer los resultados y agregarlos a la lista
                while (rs.next()) {
                    int id = rs.getInt("id_producto");
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    double precio = rs.getDouble("precio");
                    int existencias = rs.getInt("existencias");
                    String categoria = rs.getString("categoria");
                    String estado = rs.getString("estado");

                    // Crear el objeto DatosProducto
                    DatosProducto producto = new DatosProducto(id, nombre, descripcion, precio, existencias, categoria, estado);
                    productosList.add(producto);
                }

                // Asignar la lista observable a la tabla
                tableProducto.setItems(productosList);

            } catch (SQLException e) {
                e.printStackTrace();  // En caso de error, mostrar el error
            }
        } else {
            System.out.println("No se pudo conectar a la base de datos.");
        }

    }

    public void buscarProductos(String buscar) {
        try {
            Conexion conexion = new Conexion();
            Connection con = conexion.establecerConexion();

            if (con != null) {
                // Consulta SQL con filtro por nombre
                String query = "SELECT * FROM productos WHERE Nombre LIKE ? OR Id_Producto LIKE ?";
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, "%" + buscar + "%");
                st.setString(2, "%" + buscar + "%");

                ResultSet rs = st.executeQuery();

                // Crear una nueva lista para los productos encontrados
                ObservableList<DatosProducto> productosFiltrados = FXCollections.observableArrayList();

                while (rs.next()) {
                    int id = rs.getInt("id_producto");
                    String nombre = rs.getString("nombre");
                    String descripcion = rs.getString("descripcion");
                    double precio = rs.getDouble("precio");
                    int existencias = rs.getInt("existencias");
                    String categoria = rs.getString("categoria");
                    String estado = rs.getString("estado");

                    // Crear el objeto DatosProducto y agregarlo a la lista filtrada
                    DatosProducto producto = new DatosProducto(id, nombre, descripcion, precio, existencias, categoria, estado);
                    productosFiltrados.add(producto);
                }

                // Actualizar la tabla con los resultados filtrados
                tableProducto.setItems(productosFiltrados);
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (SQLException e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al buscar productos: " + e.getMessage());
        }

    }

    public void venderProducto() {
        try {
            DatosProducto datosProducto = tableProducto.getSelectionModel().getSelectedItem();

            if (datosProducto == null) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Seleccione un producto para vender.");
                return;
            }

            // Solicitar cantidad al usuario
            TextInputDialog dialogoCantidad = new TextInputDialog();
            dialogoCantidad.setTitle("Venta de Producto");
            dialogoCantidad.setHeaderText(null);
            dialogoCantidad.setContentText("Ingrese la cantidad de productos a vender:");

            dialogoCantidad.showAndWait().ifPresent(cantidadTexto -> {
                try {
                    int cantidadVenta = Integer.parseInt(cantidadTexto);

                    if (cantidadVenta <= 0) {
                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "Cantidad inválida", "La cantidad debe ser mayor a 0.");
                        return;
                    }

                    Connection conexion = new Conexion().establecerConexion();
                    try {
                        // Verificar existencias del producto
                        String consultaExistencias = "SELECT Existencias FROM Productos WHERE Id_Producto = ?";
                        PreparedStatement consulta = conexion.prepareStatement(consultaExistencias);
                        consulta.setInt(1, datosProducto.getIdProducto());

                        ResultSet resultado = consulta.executeQuery();

                        if (resultado.next()) {
                            int existenciasActuales = resultado.getInt("Existencias");

                            if (cantidadVenta > existenciasActuales) {
                                funcion.mostrarAlerta(Alert.AlertType.ERROR, "Stock insuficiente",
                                        "No hay suficientes existencias. Disponibles: " + existenciasActuales);
                                return;
                            }

                            // Calcular el total de la venta
                            double totalVenta = cantidadVenta * datosProducto.getPrecio();

                            // Confirmar la venta
                            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmacion.setTitle("Confirmar Venta");
                            confirmacion.setHeaderText(null);
                            confirmacion.setContentText(
                                    "ID Producto: " + datosProducto.getIdProducto() + "\n"
                                    + "Cantidad: " + cantidadVenta + "\n"
                                    + "Total: $" + totalVenta + "\n\n"
                                    + "¿Desea realizar esta venta?");

                            confirmacion.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.OK) {
                                    try {
                                        conexion.setAutoCommit(false);

                                        // Insertar la venta en la tabla VentaProducto
                                        String sqlVenta = "INSERT INTO VentaProducto (Cantidad, MontoTotal, FechaEmision, Id_Producto) VALUES (?, ?, CURDATE(), ?)";
                                        PreparedStatement ventaStmt = conexion.prepareStatement(sqlVenta);
                                        ventaStmt.setInt(1, cantidadVenta);
                                        ventaStmt.setDouble(2, totalVenta);
                                        ventaStmt.setInt(3, datosProducto.getIdProducto());
                                        ventaStmt.executeUpdate();

                                        // Actualizar existencias en la tabla Productos
                                        int nuevasExistencias = existenciasActuales - cantidadVenta;
                                        String actualizarProducto;
                                        PreparedStatement actualizarStmt;

                                        if (nuevasExistencias == 0) {
                                            // Cambiar estado a "No Disponible" si las existencias llegan a 0
                                            actualizarProducto = "UPDATE Productos SET Existencias = ?, Estado = ? WHERE Id_Producto = ?";
                                            actualizarStmt = conexion.prepareStatement(actualizarProducto);
                                            actualizarStmt.setInt(1, nuevasExistencias);
                                            actualizarStmt.setString(2, "No Disponible");
                                        } else {
                                            // Solo actualizar existencias
                                            actualizarProducto = "UPDATE Productos SET Existencias = ? WHERE Id_Producto = ?";
                                            actualizarStmt = conexion.prepareStatement(actualizarProducto);
                                            actualizarStmt.setInt(1, nuevasExistencias);
                                        }

                                        actualizarStmt.setInt(actualizarStmt.getParameterMetaData().getParameterCount(), datosProducto.getIdProducto());
                                        actualizarStmt.executeUpdate();

                                        conexion.commit();
                                        funcion.mostrarAlerta(Alert.AlertType.CONFIRMATION, "Venta Exitosa", "La venta se realizó correctamente.");
                                        cargarProductosDesdeBD(); // Recargar tabla
                                    } catch (Exception e) {
                                        funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo completar la venta: " + e.getMessage());
                                    }
                                } else {
                                    funcion.mostrarAlerta(Alert.AlertType.INFORMATION, "Cancelado", "La venta ha sido cancelada.");
                                }
                            });
                        } else {
                            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "El producto no se encontró en la base de datos.");
                        }
                    } finally {
                        conexion.close();
                    }
                } catch (NumberFormatException e) {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "Cantidad inválida", "Debe ingresar un número válido.");
                } catch (Exception e) {
                    funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", "Ocurrió un error: " + e.getMessage());
                }
            });
        } catch (Exception ex) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error inesperado", "Ocurrió un error inesperado: " + ex.getMessage());
        }
    }

    public void editarProducto() {
        try {
            DatosProducto producto = tableProducto.getSelectionModel().getSelectedItem();
            if (producto == null) {
                funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Seleccione un dato primero para editar");
            } else {

                FXMLLoader carga = new FXMLLoader(getClass().getResource("/pVista/AProductoAgregar.fxml"));

                Parent Vista = carga.load();

                producto.getIdProducto();
                producto.getNombre();   // Si 'nombre' es un atributo
                producto.getDescripcion(); // Si 'descripcion' es un atributo
                producto.getPrecio();
                producto.getExistencias();
                producto.getCategoria();
                producto.getEstado();

                AProductoAgregarController controlador = carga.getController();

                controlador.setValores(
                        producto.getIdProducto(),
                        producto.getNombre(), // Si 'nombre' es un atributo
                        producto.getDescripcion(), // Si 'descripcion' es un atributo
                        producto.getPrecio(),
                        producto.getExistencias(),
                        producto.getCategoria(),
                        producto.getEstado()
                );

                Scene vistaActual = btnVentas.getScene();
                vistaActual.setRoot(Vista);
            }
        } catch (IOException e) {
            funcion.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar el archivo FXML");
        }
    }

    public void filtrarProductos(String criterio) {
        ObservableList<DatosProducto> productosFiltrados = FXCollections.observableArrayList();

        // Filtrar según el criterio seleccionado en el ComboBox
        switch (criterio) {
            case "No Disponible":
                productosFiltrados.addAll(filtrarPorEstado("No Disponible"));
                break;
            case "Disponible":
                productosFiltrados.addAll(filtrarPorEstado("Disponible"));
                break;
            case "Productos Mayores de $50.00":
                productosFiltrados.addAll(filtrarPorPrecio(50.00, true));
                break;
            case "Productos Menores de $50.00":
                productosFiltrados.addAll(filtrarPorPrecio(50.00, false));
                break;
            default:
                productosFiltrados.addAll(productosList);
                break;
        }

        // Actualizar la tabla con los productos filtrados
        tableProducto.setItems(productosFiltrados);
    }

// Métodos separados para los diferentes filtros
    private List<DatosProducto> filtrarPorEstado(String estado) {
        List<DatosProducto> filtrados = new ArrayList<>();
        for (DatosProducto producto : productosList) {
            if (producto.getEstado().equals(estado)) {
                filtrados.add(producto);
            }
        }
        return filtrados;
    }

    private List<DatosProducto> filtrarPorPrecio(double precio, boolean esMayor) {
        List<DatosProducto> filtrados = new ArrayList<>();
        for (DatosProducto producto : productosList) {
            if ((esMayor && producto.getPrecio() > precio) || (!esMayor && producto.getPrecio() < precio)) {
                filtrados.add(producto);
            }
        }
        return filtrados;
    }
}