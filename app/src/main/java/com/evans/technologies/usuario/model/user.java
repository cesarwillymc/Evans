package com.evans.technologies.usuario.model;

public class user {

    private String id;
    String token,message,viajeId,mensaje;
    Boolean success,imgUpdate,imgActivation,accountActivate,isReferred,emailexist;
    private String name, surname, email, city, cellphone, numDocument, imageProfile,user,darMensaje,price,pricediscount;

    public Boolean getEmailexist() {
        return emailexist;
    }

    public void setEmailexist(Boolean emailexist) {
        this.emailexist = emailexist;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getReferred() {
        return isReferred;
    }

    public void setReferred(Boolean referred) {
        isReferred = referred;
    }

    public Boolean getIsReferred() {
        return isReferred;
    }

    public void setIsReferred(Boolean referred) {
        isReferred = referred;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricediscount() {
        return pricediscount;
    }

    public void setPricediscount(String pricediscount) {
        this.pricediscount = pricediscount;
    }

    public String getDarMensaje() {
        return darMensaje;
    }

    public void setDarMensaje(String darMensaje) {
        this.darMensaje = darMensaje;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public user(String message) {
        this.message = message;
    }
    public user(String id, String token, String message) {
        this.id = id;
        this.token = token;
        this.message = message;
    }

    public user(String name, String surname, String email, String city, String celphone, String numDocument, Boolean accountActivate, String imageProfile) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.city = city;
        this.cellphone = celphone;
        this.numDocument = numDocument;
        this.accountActivate = accountActivate;
        this.imageProfile = imageProfile;
    }

    public Boolean getImgUpdate() {
        return imgUpdate;
    }

    public void setImgUpdate(Boolean imgUpdate) {
        this.imgUpdate = imgUpdate;
    }

    public Boolean getImgActivation() {
        return imgActivation;
    }

    public void setImgActivation(Boolean imgActivation) {
        this.imgActivation = imgActivation;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCelphone() {
        return cellphone;
    }

    public void setCelphone(String celphone) {
        this.cellphone = celphone;
    }

    public String getNumDocument() {
        return numDocument;
    }

    public void setNumDocument(String numDocument) {
        this.numDocument = numDocument;
    }

    public Boolean getAccountActivate() {
        return accountActivate;
    }

    public void setAccountActivate(Boolean accountActivate) {
        this.accountActivate = accountActivate;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
