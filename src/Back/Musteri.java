/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Back;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Cynapsis
 */
public class Musteri implements Serializable{
    private static final long serialVersionUID = 6529623098267757690L;
    private String adi,telefonNumarasi,adres;
    
    public Musteri(String adi, String telefonNo, String adres) {
        this.adi = adi;
        this.telefonNumarasi = telefonNo;
        this.adres = adres;
    }

    public String getAdi() {
        return adi;
    }

    public String getTelefonNumarasi() {
        return telefonNumarasi;
    }

    public String getAdres() {
        return adres;
    }
    
    @Override
    public String toString() {
        return this.adi;
    }
}
