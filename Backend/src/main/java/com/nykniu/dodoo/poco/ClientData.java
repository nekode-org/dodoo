package com.nykniu.dodoo.poco;

import java.util.Date;

/**
 * class ClientData
 * Clase que contiene los datos de facturación de los clientes
 */
public class ClientData {
    private String rfc;
    private String razonSocial;
    private String name;
    private String mSurname;
    private String pSurname;
    private Date birthday;
    private String address;
    private String postalCode;

    public ClientData(String rfc, String razonSocial, String nombre, String apPaterno, String apMaterno,
            Date nacimiento, String domicilio, String codigoPostal) {
        this.rfc = rfc;
        this.razonSocial = razonSocial;
        this.name = nombre;
        this.mSurname = apPaterno;
        this.pSurname = apMaterno;
        this.birthday = nacimiento;
        this.address = domicilio;
        this.postalCode = codigoPostal;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String apPaterno) {
        this.mSurname = apPaterno;
    }

    public String getpSurname() {
        return pSurname;
    }

    public void setpSurname(String apMaterno) {
        this.pSurname = apMaterno;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date nacimiento) {
        this.birthday = nacimiento;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String domicilio) {
        this.address = domicilio;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String codigoPostal) {
        this.postalCode = codigoPostal;
    }

}
