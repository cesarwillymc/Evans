package com.evans.technologies.usuario.model;

public class getPrice  {

    private TravelRate travelRate;
    private String CashPrice;

    public String getCashPrice() {
        return CashPrice;
    }

    public void setCashPrice(String cashPrice) {
        CashPrice = cashPrice;
    }

    public TravelRate getTravelRate() {
        return travelRate;
    }

    public void setTravelRate(TravelRate travelRate) {
        this.travelRate = travelRate;
    }
}
