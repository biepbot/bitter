/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rowan
 */
@Singleton
public class DB
{
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bitter");
    
    @PersistenceContext
    private EntityManager em;

    public DB()
    {
        if (em == null) 
        {
            em = emf.createEntityManager();
        }
    }
    
    public EntityManager getEntityManager() 
    {
        return em;
    }

    @Override
    protected void finalize() throws Throwable
    {
        if (em.isOpen())
            em.close();
        super.finalize();
    }
}
