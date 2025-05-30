
package pModelo;

public class objetoervicios {
    private int Id_Servicio;
    private String NombreServicio;
    private String DescripcionServicio;
    private double PrecioServicio;
    private double DuracionServicio;
    public objetoervicios(int Id_Servicio, String NombreServicio, String DescripcionServicio, double PrecioServicio, double DuracionServicio) {
        this.Id_Servicio = Id_Servicio;
        this.NombreServicio = NombreServicio;
        this.DescripcionServicio = DescripcionServicio;
        this.PrecioServicio = PrecioServicio;
        this.DuracionServicio = DuracionServicio;
    }

    public int getId_Servicio() {
        return Id_Servicio;
    }

    public void setId_Servicio(int Id_Servicio) {
        this.Id_Servicio = Id_Servicio;
    }

    public String getNombreServicio() {
        return NombreServicio;
    }

    public void setNombreServicio(String NombreServicio) {
        this.NombreServicio = NombreServicio;
    }

    public String getDescripcionServicio() {
        return DescripcionServicio;
    }

    public void setDescripcionServicio(String DescripcionServicio) {
        this.DescripcionServicio = DescripcionServicio;
    }

    public double getPrecioServicio() {
        return PrecioServicio;
    }

    public void setPrecioServicio(double PrecioServicio) {
        this.PrecioServicio = PrecioServicio;
    }

    public double getDuracionServicio() {
        return DuracionServicio;
    }

    public void setDuracionServicio(double DuracionServicio) {
        this.DuracionServicio = DuracionServicio;
    }
    
}
