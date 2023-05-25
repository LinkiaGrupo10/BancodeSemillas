package com.example.bancodesemillas.Modelo;

import java.io.Serializable;

public class Historial implements Serializable {

    private String mailGuardian;
    private int idSemilla;
    private String fechaGuarda;

    private String fechaCierre;
    private String observaciones;
    private boolean guardaCerrada;

    public Historial(String mailGuardian, int idSemilla, String fechaGuarda, String fechaCierre, boolean guardaCerrada, String observaciones) {
        this.mailGuardian = mailGuardian;
        this.idSemilla = idSemilla;
        this.fechaGuarda = fechaGuarda;
        this.fechaCierre = fechaCierre;
        this.guardaCerrada = guardaCerrada;
        this.observaciones = observaciones;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Historial() {
    }

    public String getMailGuardian() {
        return mailGuardian;
    }

    public void setMailGuardian(String mailGuardian) {
        this.mailGuardian = mailGuardian;
    }

    public int getIdSemilla() {
        return idSemilla;
    }

    public void setObservaciones(String observaciones){this.observaciones = observaciones;}

    public String getObservaciones(){return observaciones;}

    public void setIdSemilla(int idSemilla) {
        this.idSemilla = idSemilla;
    }

    public String getFechaGuarda() {
        return fechaGuarda;
    }

    public void setFechaGuarda(String fechaGuarda) {
        this.fechaGuarda = fechaGuarda;
    }

    public boolean isGuardaCerrada() {
        return guardaCerrada;
    }

    public void setGuardaCerrada(boolean guardaCerrada) {
        this.guardaCerrada = guardaCerrada;
    }
}
