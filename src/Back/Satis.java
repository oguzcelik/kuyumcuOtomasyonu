/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Back;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Cynapsis
 */
public class Satis implements Serializable{
    private Musteri musteri;
    private Double fiyat;
    private Urun satilan;
    private String date;
    private int adet;
    
    public double getMaaliyet() {
        return adet*satilan.getMaliyet();
    }

    public int getAdet() {
        return adet;
    }
    

    public Musteri getMusteri() {
        return musteri;
    }

    public Double getFiyat() {
        return fiyat;
    }

    public Urun getSatilan() {
        return satilan;
    }
    
    public Satis(Musteri musteri, Urun satilan, Double fiyat, int adet) {
        this.musteri=musteri;
        this.fiyat = fiyat;
        this.satilan=satilan;
        this.adet=adet;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        this.date=dateFormat.format(date);
    }

    public String getDate() {
        return date;
    }
    
}
