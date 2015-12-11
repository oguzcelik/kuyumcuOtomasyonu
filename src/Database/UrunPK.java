/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Cynapsis
 */
@Embeddable
public class UrunPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ADI")
    private String adi;
    @Basic(optional = false)
    @Column(name = "URUN_KODU")
    private String urunKodu;

    public UrunPK() {
    }

    public UrunPK(String adi, String urunKodu) {
        this.adi = adi;
        this.urunKodu = urunKodu;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public String getUrunKodu() {
        return urunKodu;
    }

    public void setUrunKodu(String urunKodu) {
        this.urunKodu = urunKodu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (adi != null ? adi.hashCode() : 0);
        hash += (urunKodu != null ? urunKodu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UrunPK)) {
            return false;
        }
        UrunPK other = (UrunPK) object;
        if ((this.adi == null && other.adi != null) || (this.adi != null && !this.adi.equals(other.adi))) {
            return false;
        }
        if ((this.urunKodu == null && other.urunKodu != null) || (this.urunKodu != null && !this.urunKodu.equals(other.urunKodu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Database.UrunPK[ adi=" + adi + ", urunKodu=" + urunKodu + " ]";
    }
    
}
