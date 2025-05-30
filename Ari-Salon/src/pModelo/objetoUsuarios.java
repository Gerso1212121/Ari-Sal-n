package pModelo;

import java.time.LocalDate;

public class objetoUsuarios {
    private int id_empleado;
    private String nombre;
    private String apellido;
    private int edad;
    private String telefono;
    private String correoelectronico;
    private String contra;
    private LocalDate FechaContratacion ;
    private String Rol;

    public objetoUsuarios(int id_empleado, String nombre, String apellido, int edad, String telefono, String correoelectronico, String contra, LocalDate FechaContratacion, String Rol) {
        this.id_empleado = id_empleado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.telefono = telefono;
        this.correoelectronico = correoelectronico;
        this.contra = contra;
        this.FechaContratacion = FechaContratacion;
        this.Rol = Rol;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoelectronico() {
        return correoelectronico;
    }

    public void setCorreoelectronico(String correoelectronico) {
        this.correoelectronico = correoelectronico;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
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

}
