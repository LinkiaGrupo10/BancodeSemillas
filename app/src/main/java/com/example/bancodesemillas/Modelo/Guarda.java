package com.example.bancodesemillas.Modelo;

import java.io.Serializable;

public class Guarda implements Serializable {

    private String mailGuardian;
    private int idSemilla;
    private String fechaGuarda;

    private String observaciones;
    private boolean guardaCerrada;

    public Guarda(String mailGuardian, int idSemilla, String fechaGuarda, boolean guardaCerrada, String observaciones) {
        this.mailGuardian = mailGuardian;
        this.idSemilla = idSemilla;
        this.fechaGuarda = fechaGuarda;
        this.guardaCerrada = guardaCerrada;
        this.observaciones = observaciones;
    }


    public Guarda() {
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
