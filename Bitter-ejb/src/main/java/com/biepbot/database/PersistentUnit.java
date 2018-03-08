/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database;

import com.biepbot.database.mocking.LocalEntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
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
        if (db == null)
        {
            db = new DB();
        }
        em = db.getEntityManager();
    }

    /**
     * Gets a single object based on a query
     * @param <T>
     * @param c
     * @param where
     * @return
     */
    public <T> T getObjectFromQuery(Class c, Map<String, Object> where)
    {
        try
        {
            List<T> ret = getObjectsFromQuery(c, where);
            return ret.isEmpty() ? null : ret.get(0);
        }
        catch (final NoResultException nre)
        {
            return null;
        }
    }

    /**
     * Gets a single object based on a query
     * @param <T>
     * @param c
     * @param f
     * @param where
     * @return
     */
    public <T> T getObjectFromQuery(Class c, String f, Object where)
    {
        try
        {
            List<T> ret = getObjectsFromQuery(c, f, where);
            return ret.isEmpty() ? null : ret.get(0);
        }
        catch (final NoResultException nre)
        {
            return null;
        }
    }

    /**
     * Returns objects from a where and field name
     * @param <T>
     * @param c The class which to browse
     * @param f The field name as stated in the table
     * @param where The condition to compare the field to
     * @return
     */
    public <T> List<T> getObjectsFromQuery(Class c, String f, Object where)
    {
        Map<String, Object> oneMap = new HashMap<>();
        oneMap.put(f, where);
        return getObjectsFromQuery(c, oneMap);
    }

    /**
     * Returns objects from a map based where query, based on fields and names
     * @param <T>
     * @param c The class which to browse
     * @param where Fields and conditions
     * @return
     */
    public <T> List<T> getObjectsFromQuery(Class c, Map<String, Object> where)
    {
        LocalEntityManager lem = (LocalEntityManager) em;
        if (lem != null)
        {
            return lem.<T>get(c, where);
        }
        else
        {
            // set up a builder
            CriteriaBuilder builder = em.getCriteriaBuilder();

            // begin creating the query
            CriteriaQuery<T> criteria = builder.createQuery(c);

            // select the class for a root
            Root<T> from = criteria.from(c);
            criteria.select(from);

            //Constructing list of parameters
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : where.entrySet())
            {
                String key = entry.getKey();
                Object value = entry.getValue();
                predicates.add(
                        builder.equal(from.get(key), value)
                );
            }

            // create the equals statement
            criteria.where(predicates.toArray(new Predicate[]
            {
            }));
            TypedQuery<T> typed = em.createQuery(criteria);
            return typed.getResultList();
        }
    }
}
