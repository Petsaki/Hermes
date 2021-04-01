package com.petsaki.epaketo;

public class FetchData {

    String id,odos;
    Double baros;

    public FetchData(String id, Double baros, String odos) {
        this.id = id;
        this.baros = baros;
        this.odos = odos;
    }

    public FetchData(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOdos() {
        return odos;
    }

    public void setOdos(String odos) {
        this.odos = odos;
    }

    public Double getBaros() {
        return baros;
    }

    public void setBaros(Double baros) {
        this.baros = baros;
    }
}
