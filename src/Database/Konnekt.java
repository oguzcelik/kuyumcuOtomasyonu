/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Cynapsis
 */
public class Konnekt {
    EntityManagerFactory emf;
    EntityManager em;
    
    public Konnekt() {
        emf = Persistence.createEntityManagerFactory("BP1-2_Proje3PU");
        em = emf.createEntityManager();
    }
    
    public List query(String qStr) {
        Query q = em.createQuery(qStr);
        return q.getResultList();
    }
    
    public void insert(Object entity) {
        em.getTransaction().begin();
        if(entity instanceof Urun) {
            em.persist((Urun)entity);
        }
        else if(entity instanceof Satis) {
            em.persist((Satis)entity);
        }
        else {
            em.persist((Musteri)entity);
        }
        em.getTransaction().commit();
    }
    
    public void deleteUpdate(Query q) {
        em.getTransaction().begin();
        q.executeUpdate();
        em.getTransaction().commit();
    }

    public EntityManager getEm() {
        return em;
    }
    
    
    
}
