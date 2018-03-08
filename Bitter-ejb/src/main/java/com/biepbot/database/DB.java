/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database;

import com.biepbot.database.mocking.LocalEntityManager;
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

    public DB()
    {
        if (em == null) 
        {
            em = new LocalEntityManager();
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
