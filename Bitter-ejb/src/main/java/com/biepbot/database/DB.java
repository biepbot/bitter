/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database;

import com.biepbot.database.mocking.LocalEntityManager;
import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rowan
 */
@Stateful
public class DB
{
    @PersistenceContext(name = "bitter")
    private EntityManager em;
    
    private boolean isLocal = false;
    protected int changes = 0;

    public DB()
    {
    }
    
    public DB(boolean local) 
    {
        // todo:
        // move all local functions to wrapper
        
        this.isLocal = local;
        if (isLocal)
        {
            em = new LocalEntityManager();
        }
    }
    
    public EntityManager getEntityManager() 
    {
        return em;
    }
    
    
    public void save(Object o) 
    {
        if (isLocal) em.getTransaction().begin();
        em.persist(o);
        if (isLocal) em.getTransaction().commit();
    }
    
    public void save(List<Object> o) 
    {
        if (isLocal) em.getTransaction().begin();
        for (Object obj : o) 
        {
            em.persist(o);
        }
        if (isLocal) em.getTransaction().commit();
    }
    
    public void update(Object o) 
    {
        if (isLocal) em.getTransaction().begin();
        em.merge(o);
        if (isLocal) em.getTransaction().commit();
    }
    
    public void update(List<Object> o) 
    {
        if (isLocal) em.getTransaction().begin();
        for (Object obj : o) 
        {
            em.merge(o);
        }
        if (isLocal) em.getTransaction().commit();
    }
    
    public void delete(Object o) 
    {
        if (isLocal) em.getTransaction().begin();
        em.remove(em.merge(o));
        if (isLocal) em.getTransaction().commit();
    }
    
    public void delete(List<Object> o) 
    {
        if (isLocal) em.getTransaction().begin();
        for (Object obj : o) 
        {
            em.remove(em.merge(o));
        }
        if (isLocal) em.getTransaction().commit();
    }

    @Override
    protected void finalize() throws Throwable
    {
        if (em.isOpen())
            em.close();
        super.finalize();
    }
}
