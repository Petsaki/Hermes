package com.petsaki.epaketo;

import java.io.Serializable;
import java.util.Date;

//basic antikeimeno gia ta paketa pou trabaw apo thn bash
public class FetchData implements Serializable {

    String id,odos;
    String baros;
    String megethos,odos_magaziou,onoma_etairias;
    String hmerominia;

    public FetchData(String id, String odos, String baros, String megethos, String odos_magaziou, String onoma_etairias, String hmerominia) {
        this.id = id;
        this.odos = odos;
        this.baros = baros;
        this.megethos = megethos;
        this.odos_magaziou = odos_magaziou;
        this.onoma_etairias = onoma_etairias;
        this.hmerominia=hmerominia;
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

    public String getBaros() {
        return baros;
    }

    public void setBaros(String baros) {
        this.baros = baros;
    }

    public String getMegethos() {
        return megethos;
    }

    public void setMegethos(String megethos) {
        this.megethos = megethos;
    }

    public String getOdos_magaziou() {
        return odos_magaziou;
    }

    public void setOdos_magaziou(String odos_magaziou) {
        this.odos_magaziou = odos_magaziou;
    }

    public String getOnoma_etairias() {
        return onoma_etairias;
    }

    public void setOnoma_etairias(String onoma_etairias) {
        this.onoma_etairias = onoma_etairias;
    }

    public String getHmerominia() {
        return hmerominia;
    }

    public void setHmerominia(String hmerominia) {
        this.hmerominia = hmerominia;
    }
}
