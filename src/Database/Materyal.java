/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.Serializable;
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
@Table(name = "MATERYAL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materyal.findAll", query = "SELECT m FROM Materyal m"),
    @NamedQuery(name = "Materyal.findByFiyat", query = "SELECT m FROM Materyal m WHERE m.fiyat = :fiyat"),
    @NamedQuery(name = "Materyal.findByTur", query = "SELECT m FROM Materyal m WHERE m.tur = :tur")})
public class Materyal implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FIYAT")
    private Double fiyat;
    @Id
    @Basic(optional = false)
    @Column(name = "TUR")
    private String tur;

    public Materyal() {
    }

    public Materyal(String tur) {
        this.tur = tur;
    }

    public Double getFiyat() {
        return fiyat;
    }

    public void setFiyat(Double fiyat) {
        this.fiyat = fiyat;
    }

    public String getTur() {
        return tur;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tur != null ? tur.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materyal)) {
            return false;
        }
        Materyal other = (Materyal) object;
        if ((this.tur == null && other.tur != null) || (this.tur != null && !this.tur.equals(other.tur))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Database.Materyal[ tur=" + tur + " ]";
    }
    
}
