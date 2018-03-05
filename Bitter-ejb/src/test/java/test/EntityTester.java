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
    
    protected int changes = 0;

    public EntityTester()
    {
        db = new DB();
        em = db.getEntityManager();
    }
    
    protected void save(Object o) 
    {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }
    
    protected void save(List<Object> o) 
    {
        em.getTransaction().begin();
        for (Object obj : o) 
        {
            em.persist(o);
        }
        em.getTransaction().commit();
    }
    
    protected void update(Object o) 
    {
        em.getTransaction().begin();
        em.merge(o);
        em.getTransaction().commit();
    }
    
    protected void update(List<Object> o) 
    {
        em.getTransaction().begin();
        for (Object obj : o) 
        {
            em.merge(o);
        }
        em.getTransaction().commit();
    }
    
    protected void delete(Object o) 
    {
        em.getTransaction().begin();
        em.remove(em.merge(o));
        em.getTransaction().commit();
    }
    
    protected void delete(List<Object> o) 
    {
        em.getTransaction().begin();
        for (Object obj : o) 
        {
            em.remove(em.merge(o));
        }
        em.getTransaction().commit();
    }
}
