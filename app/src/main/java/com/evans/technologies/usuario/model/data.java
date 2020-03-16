package com.evans.technologies.usuario.model;

import java.util.ArrayList;

public class data {
    String driverId;
    String viajeId;
    String response;
    String chatId;
    String body;
    String title;
    ArrayList<DataCupon> messageCoupon;


    public ArrayList<DataCupon> getMessageCoupon() {
        return messageCoupon;
    }

    public void setMessageCoupon(ArrayList<DataCupon> messageCoupon) {
        this.messageCoupon = messageCoupon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }



    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

}
