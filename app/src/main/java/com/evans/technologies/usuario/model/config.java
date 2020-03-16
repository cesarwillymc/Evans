package com.evans.technologies.usuario.model;

public class config {
    Boolean accountActivate,tripActivate;

    Boolean informationCar;
    String response;
    Double lat,log;


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLog() {
        return log;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getAccountActivate() {
        return accountActivate;
    }

    public void setAccountActivate(Boolean accountActivate) {
        this.accountActivate = accountActivate;
    }

    public Boolean getTripActivate() {
        return tripActivate;
    }

    public void setTripActivate(Boolean tripActivate) {
        this.tripActivate = tripActivate;
    }


    public Boolean getInformationCar() {
        return informationCar;
    }

    public void setInformationCar(Boolean informationCar) {
        this.informationCar = informationCar;
    }
}
