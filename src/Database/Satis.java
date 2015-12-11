/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Cynapsis
 */
@Entity
@Table(name = "SATIS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Satis.findAll", query = "SELECT s FROM Satis s"),
    @NamedQuery(name = "Satis.findByMusteriId", query = "SELECT s FROM Satis s WHERE s.musteriId = :musteriId"),
    @NamedQuery(name = "Satis.findByFiyat", query = "SELECT s FROM Satis s WHERE s.fiyat = :fiyat"),
    @NamedQuery(name = "Satis.findByUrunKodu", query = "SELECT s FROM Satis s WHERE s.urunKodu = :urunKodu"),
    @NamedQuery(name = "Satis.findByTarih", query = "SELECT s FROM Satis s WHERE s.tarih = :tarih"),
    @NamedQuery(name = "Satis.findBySatisKodu", query = "SELECT s FROM Satis s WHERE s.satisKodu = :satisKodu")})
public class Satis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "adet")
    private Integer adet;
    @Column(name = "MUSTERI_ID")
    private Integer musteriId;
    @Column(name = "FIYAT")
    private Double fiyat;
    @Column(name = "URUN_KODU")
    private String urunKodu;
    @Column(name = "TARIH")
    @Temporal(TemporalType.DATE)
    private Date tarih;
    @Id
    @Basic(optional = false)
    @Column(name = "SATIS_KODU")
    private Integer satisKodu;

    public Satis() {
    }

    public Satis(Musteri musteri, Urun satilan, Double fiyat, int adet) {
        this.musteriId=musteri.getMusteriId();
        this.fiyat = fiyat;
        this.urunKodu=satilan.getUrunPK().getUrunKodu();
        this.adet=adet;
        Date date = new Date();
        this.tarih=date;
        List<Satis> sat = new Konnekt().query("SELECT s FROM Satis s");
        satisKodu = 1;
        if(sat.size()>0) {
            satisKodu+=sat.get(sat.size()-1).satisKodu;
        }
    }

    public Integer getAdet() {
        return adet;
    }

    
    
    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(tarih);
    }

    public Musteri getMusteri() {
        Musteri m = null;
        List<Musteri> mus = new Konnekt().query("SELECT m FROM Musteri m");
        for (Musteri mu : mus) {
            if(mu.getMusteriId().equals(musteriId)) {
                m=mu;
            }
        }
        return m;
    }
    
    public Urun getSatilan() {
        Urun u = null;
        List<Urun> ur = new Konnekt().query("SELECT u FROM Urun u");
        for (Urun ur1 : ur) {
            if(ur1.getUrunPK().getUrunKodu().equals(urunKodu)) {
                u=ur1;
            }
        }
        return u;
    }
    
    public Integer getMusteriId() {
        return musteriId;
    }

    public void setMusteriId(Integer musteriId) {
        this.musteriId = musteriId;
    }

    public Double getFiyat() {
        return fiyat;
    }

    public void setFiyat(Double fiyat) {
        this.fiyat = fiyat;
    }

    public String getUrunKodu() {
        return urunKodu;
    }

    public void setUrunKodu(String urunKodu) {
        this.urunKodu = urunKodu;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public Integer getSatisKodu() {
        return satisKodu;
    }

    public void setSatisKodu(Integer satisKodu) {
        this.satisKodu = satisKodu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (satisKodu != null ? satisKodu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Satis)) {
            return false;
        }
        Satis other = (Satis) object;
        if ((this.satisKodu == null && other.satisKodu != null) || (this.satisKodu != null && !this.satisKodu.equals(other.satisKodu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Database.Satis[ satisKodu=" + satisKodu + " ]";
    }
    
}
