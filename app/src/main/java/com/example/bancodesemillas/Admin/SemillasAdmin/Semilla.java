package com.example.bancodesemillas.Admin.SemillasAdmin;

import java.io.Serializable;

public class Semilla implements Serializable {

    private String ImagenSemilla;
    private String NombreCientificoSemilla;
    private String NombreVulgarSemilla;
    private String VariedadSemilla;
    private int IdSemilla;
    private String ProcedenciaSemilla;
    private double GramosMinimosSemilla;
    private String Viavilidad;
    private String MesRepartoSemilla;
    private String MesDevolucionSemilla;
    private String ManejoSemilla;
    private String ConsumirSemilla;
    private String DescripcionSemilla;
    private String OtrosDatosSemilla;
    private String Disponibilidad;


    public Semilla() {
    }

    public Semilla(String imagenSemilla, String nombreCientificoSemilla, String nombreVulgarSemilla, String variedadSemilla, int idSemilla, String procedenciaSemilla, double gramosMinimosSemilla, String viabilidad, String mesRepartoSemilla, String mesDevolucionSemilla, String manejoSemilla, String consumirSemilla, String descripcionSemilla, String otrosDatosSemilla, String disponibilidad) {
        ImagenSemilla = imagenSemilla;
        NombreCientificoSemilla = nombreCientificoSemilla;
        NombreVulgarSemilla = nombreVulgarSemilla;
        VariedadSemilla = variedadSemilla;
        IdSemilla = idSemilla;
        ProcedenciaSemilla = procedenciaSemilla;
        GramosMinimosSemilla = gramosMinimosSemilla;
        Viavilidad = viabilidad;
        MesRepartoSemilla = mesRepartoSemilla;
        MesDevolucionSemilla = mesDevolucionSemilla;
        ManejoSemilla = manejoSemilla;
        ConsumirSemilla = consumirSemilla;
        DescripcionSemilla = descripcionSemilla;
        OtrosDatosSemilla = otrosDatosSemilla;
        Disponibilidad = disponibilidad;
    }

    public String getImagenSemilla() {
        return ImagenSemilla;
    }

    public void setImagenSemilla(String imagenSemilla) {
        ImagenSemilla = imagenSemilla;
    }

    public String getNombreCientificoSemilla() {
        return NombreCientificoSemilla;
    }

    public void setNombreCientificoSemilla(String nombreCientificoSemilla) {
        NombreCientificoSemilla = nombreCientificoSemilla;
    }

    public String getNombreVulgarSemilla() {
        return NombreVulgarSemilla;
    }

    public void setNombreVulgarSemilla(String nombreVulgarSemilla) {
        NombreVulgarSemilla = nombreVulgarSemilla;
    }

    public String getVariedadSemilla() {
        return VariedadSemilla;
    }

    public void setVariedadSemilla(String variedadSemilla) {
        VariedadSemilla = variedadSemilla;
    }

    public int getIdSemilla() {
        return IdSemilla;
    }

    public void setIdSemilla(int idSemilla) {
        IdSemilla = idSemilla;
    }

    public String getProcedenciaSemilla() {
        return ProcedenciaSemilla;
    }

    public void setProcedenciaSemilla(String procedenciaSemilla) {
        ProcedenciaSemilla = procedenciaSemilla;
    }

    public double getGramosMinimosSemilla() {
        return GramosMinimosSemilla;
    }

    public void setGramosMinimosSemilla(double gramosMinimosSemilla) {
        GramosMinimosSemilla = gramosMinimosSemilla;
    }

    public String getViavilidad() {
        return Viavilidad;
    }

    public void setViavilidad(String viavilidad) {
        Viavilidad = viavilidad;
    }

    public String getMesRepartoSemilla() {
        return MesRepartoSemilla;
    }

    public void setMesRepartoSemilla(String mesRepartoSemilla) {
        MesRepartoSemilla = mesRepartoSemilla;
    }

    public String getMesDevolucionSemilla() {
        return MesDevolucionSemilla;
    }

    public void setMesDevolucionSemilla(String mesDevolucionSemilla) {
        MesDevolucionSemilla = mesDevolucionSemilla;
    }

    public String getManejoSemilla() {
        return ManejoSemilla;
    }

    public void setManejoSemilla(String manejoSemilla) {
        ManejoSemilla = manejoSemilla;
    }

    public String getConsumirSemilla() {
        return ConsumirSemilla;
    }

    public void setConsumirSemilla(String consumirSemilla) {
        ConsumirSemilla = consumirSemilla;
    }

    public String getDescripcionSemilla() {
        return DescripcionSemilla;
    }

    public void setDescripcionSemilla(String descripcionSemilla) {
        DescripcionSemilla = descripcionSemilla;
    }

    public String getOtrosDatosSemilla() {
        return OtrosDatosSemilla;
    }

    public void setOtrosDatosSemilla(String otrosDatosSemilla) {
        OtrosDatosSemilla = otrosDatosSemilla;
    }

    public String getDisponibilidad() {
        return Disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {

        Disponibilidad = disponibilidad;
    }

}
