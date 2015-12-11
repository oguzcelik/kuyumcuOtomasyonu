/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Cynapsis
 */
@Entity
@Table(name = "URUN")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Urun.findAll", query = "SELECT u FROM Urun u"),
    @NamedQuery(name = "Urun.findByMateryal", query = "SELECT u FROM Urun u WHERE u.materyal = :materyal"),
    @NamedQuery(name = "Urun.findByAdi", query = "SELECT u FROM Urun u WHERE u.urunPK.adi = :adi"),
    @NamedQuery(name = "Urun.findByUrunKodu", query = "SELECT u FROM Urun u WHERE u.urunPK.urunKodu = :urunKodu"),
    @NamedQuery(name = "Urun.findByAgirlik", query = "SELECT u FROM Urun u WHERE u.agirlik = :agirlik")})
public class Urun implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UrunPK urunPK;
    @Column(name = "MATERYAL")
    private String materyal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AGIRLIK")
    private Double agirlik;

    public Urun() {
    }

    public Urun(String adi, double agirlik, String urunKodu, Materyal materyal) {
        this.urunPK = new UrunPK(adi, urunKodu);
        this.agirlik=agirlik;
        this.materyal=materyal.getTur();
    }

    public Urun(String adi, String urunKodu) {
        this.urunPK = new UrunPK(adi, urunKodu);
    }

    public UrunPK getUrunPK() {
        return urunPK;
    }

    public void setUrunPK(UrunPK urunPK) {
        this.urunPK = urunPK;
    }

    public String getMateryal() {
        return materyal;
    }

    public void setMateryal(String materyal) {
        this.materyal = materyal;
    }

    public Double getAgirlik() {
        return agirlik;
    }

    public void setAgirlik(Double agirlik) {
        this.agirlik = agirlik;
    }
    
    public String getResimPath() {
        return "veriler/urunfoto/"+urunPK.getUrunKodu()+".png";
    }
    
    public double getMaliyet() {
        double birimF = 0;
        List<Materyal> mat = new Konnekt().query("SELECT m FROM Materyal m");
        for (Materyal mat1 : mat) {
            if(mat1.getTur().equals(materyal)) {
                birimF = mat1.getFiyat();
            }
        }
        double sayi = birimF*agirlik;
        return sayi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (urunPK != null ? urunPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Urun)) {
            return false;
        }
        Urun other = (Urun) object;
        if ((this.urunPK == null && other.urunPK != null) || (this.urunPK != null && !this.urunPK.equals(other.urunPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Database.Urun[ urunPK=" + urunPK + " ]";
    }
    
}
