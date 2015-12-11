/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Cynapsis
 */
@Entity
@Table(name = "MUSTERI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Musteri.findAll", query = "SELECT m FROM Musteri m"),
    @NamedQuery(name = "Musteri.findByAdi", query = "SELECT m FROM Musteri m WHERE m.adi = :adi"),
    @NamedQuery(name = "Musteri.findByTelNo", query = "SELECT m FROM Musteri m WHERE m.telNo = :telNo"),
    @NamedQuery(name = "Musteri.findByAdres", query = "SELECT m FROM Musteri m WHERE m.adres = :adres"),
    @NamedQuery(name = "Musteri.findByMusteriId", query = "SELECT m FROM Musteri m WHERE m.musteriId = :musteriId")})
public class Musteri implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "ADI")
    private String adi;
    @Column(name = "TEL_NO")
    private String telNo;
    @Column(name = "ADRES")
    private String adres;
    @Id
    @Basic(optional = false)
    @Column(name = "MUSTERI_ID")
    private Integer musteriId;

    public Musteri(String adi, String telefonNo, String adres) {
        this.adi=adi;
        this.telNo=telefonNo;
        this.adres=adres;
        List<Musteri> must = new Konnekt().query("SELECT s FROM Musteri s");
        musteriId = 1;
        if(must.size()>0) {
            musteriId+=must.get(must.size()-1).musteriId;
        }
    }

    public Musteri(Integer musteriId) {
        this.musteriId = musteriId;
    }
    
    public Musteri() {
        
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public Integer getMusteriId() {
        return musteriId;
    }

    public void setMusteriId(Integer musteriId) {
        this.musteriId = musteriId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (musteriId != null ? musteriId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Musteri)) {
            return false;
        }
        Musteri other = (Musteri) object;
        if ((this.musteriId == null && other.musteriId != null) || (this.musteriId != null && !this.musteriId.equals(other.musteriId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.adi;
    }
    
}
