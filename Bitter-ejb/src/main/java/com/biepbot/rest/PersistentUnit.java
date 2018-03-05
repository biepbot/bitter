/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rowan
 */
public class PersistentUnit
{
    @PersistenceContext
    protected EntityManager em;
    
    @EJB
    DB db;

    public PersistentUnit()
    {
        if (db == null) {
            db = new DB();
            em = db.getEntityManager();
        }
    }
}
