/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.biepbot.rest.DB;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Rowan
 */
public class EntityTester
{
    protected DB db;
    protected EntityManager em;
    
    private int changes = 0;

    public EntityTester()
    {
        db = new DB();
        em = db.getEntityManager();
    }
    
    protected void persist(Object o) 
    {
        em.getTransaction().begin();
        em.persist(o);
        changes++;
        em.getTransaction().commit();
        em.close();  
    }
    
    protected void persist(List<Object> o) 
    {
        em.getTransaction().begin();
        for (Object obj : o) 
        {
            em.persist(o);
            changes++;
        }
        em.getTransaction().commit();
        em.close();  
    }
    
    protected void rollback() 
    {
        em.getTransaction().begin();
        for (int i = 0; i < changes; i++) {
            em.getTransaction().rollback();
        }
        em.close();  
    }
}
