package pModelo;

import java.time.LocalDate;

public class DatosEmpleado {
   
private int Id_Empleado;
private String Nombre;
private String Apellido;
private int Edad;
private String telefono;
private String CorreoElectronico;
private LocalDate FechaContratacion;
private String Rol;

    public int getId_Empleado() {
        return Id_Empleado;
    }

    public void setId_Empleado(int Id_Empleado) {
        this.Id_Empleado = Id_Empleado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int Edad) {
        this.Edad = Edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return CorreoElectronico;
    }

    public void setCorreoElectronico(String CorreoElectronico) {
        this.CorreoElectronico = CorreoElectronico;
    }

    public LocalDate getFechaContratacion() {
        return FechaContratacion;
    }

    public void setFechaContratacion(LocalDate FechaContratacion) {
        this.FechaContratacion = FechaContratacion;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String Rol) {
        this.Rol = Rol;
    }



    public DatosEmpleado(int Id_Empleado, String Nombre, String Apellido, int Edad, String telefono, String CorreoElectronico, LocalDate FechaContratacion, String Rol) {
        this.Id_Empleado = Id_Empleado;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Edad = Edad;
        this.telefono = telefono;
        this.CorreoElectronico = CorreoElectronico;
        this.FechaContratacion = FechaContratacion;
        this.Rol = Rol;
    }

  
}
