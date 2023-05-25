package com.example.bancodesemillas.Modelo;

import java.io.Serializable;

public class Guardian implements Serializable {

    String CORREO;
    String NOMBRE;
    String TELEFONO;
    String DIRECCION;
    String ASAMBLEA;
    String IMAGEN;
    String UID;
    String MUNICIPIO;
    String PASSWORD;

    public Guardian() {

    }

    public Guardian(String CORREO, String NOMBRE, String TELEFONO, String DIRECCION, String ASAMBLEA, String IMAGEN, String UID, String MUNICIPIO, String PASSWORD) {
        this.CORREO = CORREO;
        this.NOMBRE = NOMBRE;
        this.TELEFONO = TELEFONO;
        this.DIRECCION = DIRECCION;
        this.ASAMBLEA = ASAMBLEA;
        this.IMAGEN = IMAGEN;
        this.UID = UID;
        this.MUNICIPIO = MUNICIPIO;
        this.PASSWORD = PASSWORD;
    }

    public String getCORREO() {
        return CORREO;
    }

    public void setCORREO(String CORREO) {
        this.CORREO = CORREO;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getTELEFONO() {
        return TELEFONO;
    }

    public void setTELEFONO(String TELEFONO) {
        this.TELEFONO = TELEFONO;
    }

    public String getDIRECCION() {
        return DIRECCION;
    }

    public void setDIRECCION(String DIRECCION) {
        this.DIRECCION = DIRECCION;
    }

    public String getASAMBLEA() {
        return ASAMBLEA;
    }

    public void setASAMBLEA(String ASAMBLEA) {
        this.ASAMBLEA = ASAMBLEA;
    }

    public String getIMAGEN() {
        return IMAGEN;
    }

    public void setIMAGEN(String IMAGEN) {
        this.IMAGEN = IMAGEN;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getMUNICIPIO() {
        return MUNICIPIO;
    }

    public void setMUNICIPIO(String MUNICIPIO) {
        this.MUNICIPIO = MUNICIPIO;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}

