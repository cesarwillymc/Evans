package com.evans.technologies.usuario.model;

public class DataCupon {
    String fourNumberT;
    String Propietario,tipePay,direccion,ciudad,departamento;
    String value, saldo,expire,name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataCupon(String propietario, String tipePay, String direccion, String ciudad, String departamento) {
        Propietario = propietario;
        this.tipePay = tipePay;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.departamento = departamento;
    }

    public String getFourNumberT() {
        return fourNumberT;
    }

    public void setFourNumberT(String fourNumberT) {
        this.fourNumberT = fourNumberT;
    }

    public String getPropietario() {
        return Propietario;
    }

    public void setPropietario(String propietario) {
        Propietario = propietario;
    }

    public String getTipePay() {
        return tipePay;
    }

    public void setTipePay(String tipePay) {
        this.tipePay = tipePay;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
