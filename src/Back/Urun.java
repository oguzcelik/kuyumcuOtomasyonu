package Back;

import java.io.Serializable;

public class Urun implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;
    private Materyal materyal;
    private String adi,urunKodu;
    private double maliyet,agirlik;

    public void setMateryal(Materyal materyal) {
        this.materyal = materyal;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public void setUrunKodu(String urunKodu) {
        this.urunKodu = urunKodu;
    }

    public void setMaliyet(double maliyet) {
        this.maliyet = maliyet;
    }

    public void setAgirlik(double agirlik) {
        this.agirlik = agirlik;
    }

    public String getResimPath() {
        return "veriler/urunfoto/"+urunKodu+".png";
    }

    public Materyal getMateryal() {
        return materyal;
    }

    public String getAdi() {
        return adi;
    }

    public String getUrunKodu() {
        return urunKodu;
    }

    public double getAgirlik() {
        return agirlik;
    }
    
    public Urun(String adi, double agirlik, String urunKodu, Materyal materyal) {
        this.adi=adi;
        this.agirlik=agirlik;
        this.urunKodu=urunKodu;
        this.materyal=materyal;
    }
    
    public double getMaliyet() {
        double sayi = materyal.getbirimFiyati()*agirlik;
        return sayi;
    }
}
