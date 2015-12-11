package Back;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Materyal implements Serializable{
    private double birimFiyati;
    
    public double getbirimFiyati() {
        String aBirim = null;
        String gBirim = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("veriler/birimfiyat.txt"));
            String[] s = br.readLine().split("/");
            aBirim=s[0];
            gBirim=s[1];
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Materyal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Materyal.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(this instanceof Altın && !aBirim.equals(null)) {
            birimFiyati=Double.parseDouble(aBirim);
        } else if(this instanceof Gumus && !gBirim.equals(null)){
            birimFiyati=Double.parseDouble(gBirim);
        }
        
        return birimFiyati;
    }
}
