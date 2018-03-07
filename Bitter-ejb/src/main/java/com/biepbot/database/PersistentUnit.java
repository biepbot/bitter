/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database;

import com.biepbot.database.mocking.LocalEntityManager;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Rowan
 */
public class PersistentUnit
{
    protected EntityManager em;
    
    @EJB
    DB db;

    public PersistentUnit()
    {
        if (db == null) {
            db = new DB();
        }
        em = db.getEntityManager();
    }
    
    /**
     *
     * @param c The class which to browse
     * @param f The field name as stated in the table
     * @param where The condition to compare the field to
     * @return
     */
    public Object getObjectFromQuery(Class c, String f, Object where)
    {
        LocalEntityManager lem = (LocalEntityManager)em;
        if (lem != null)
        {
            List<Object> objs = lem.get(c, f, where);
            return objs.isEmpty() ? null : objs.get(0);
        } else {        
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Object> criteria = builder.createQuery(c);
            Root<Object> from = criteria.from(c);
            criteria.select(from);
            criteria.where(builder.equal(from.get(f), where));
            TypedQuery<Object> typed = em.createQuery(criteria);
            try {
                return typed.getSingleResult();
            } catch (final NoResultException nre) {
                return null;
            }
        }
    }
}
