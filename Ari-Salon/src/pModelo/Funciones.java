package pModelo;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.sql.ResultSetMetaData;



import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import java.time.format.DateTimeFormatter; // Para formatear la fecha
import java.time.LocalDateTime; // Para obtener la fecha y hora actual

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class Funciones {

    public void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public void salirForm(Node vistaActual) {
        Stage vistaVer = (Stage) vistaActual.getScene().getWindow();
        vistaVer.close();
    }

    public boolean validarCorreo(String correo) {
        String emailcodigo = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailcodigo, correo);
    }

    public void verRuta(Node elementoVista, String rutaFXML) {
        try {
            Parent cargarVista = FXMLLoader.load(getClass().getResource(rutaFXML));
            Scene vista = elementoVista.getScene();
            vista.setRoot(cargarVista);
        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "ERROR", "Error al cargar: " + rutaFXML + " Verifique la ruta de la imagen");
        }
    }
    
    
    
    public void crearPdfTabla(String sqlQuery, Connection connection, Stage stage) {
      try {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar reporte como");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        fileChooser.setInitialFileName("reporte.pdf");

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String outputPath = file.getAbsolutePath();

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            document.add(new Paragraph("Reporte de las ultimas realizadas \n\n" ));
            
            String fechaGeneracion = "Fecha de generación: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            document.add(new Paragraph(fechaGeneracion + "\n\n"));

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sqlQuery)) {

                if (!rs.isBeforeFirst()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "No hay resultados", "No se encontraron datos en la consulta SQL.");
                    return;
                }

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                PdfPTable pdfTable = new PdfPTable(columnCount);
                pdfTable.setWidthPercentage(100);

                float[] columnWidths = new float[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    pdfTable.addCell(new PdfPCell(new Phrase(columnName)));
                    columnWidths[i - 1] = columnName.length();
                }
                pdfTable.setWidths(columnWidths);

                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        String cellValue = rs.getString(i);
                        PdfPCell cell = new PdfPCell(new Phrase(cellValue != null ? cellValue : "N/A"));
                        pdfTable.addCell(cell);

                        if (cellValue != null) {
                            columnWidths[i - 1] = Math.max(columnWidths[i - 1], cellValue.length());
                        }
                    }
                }

                pdfTable.setWidths(columnWidths);
                document.add(pdfTable);
            }

            document.close();
            mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmacion", "PDF Guardado correctamente como: " + document );
        } else {
            System.out.println("El usuario canceló la operación.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta(Alert.AlertType.ERROR, "Error", "Hubo un problema generando el PDF: " + e.getMessage());
    }
}

    
    
    
}
