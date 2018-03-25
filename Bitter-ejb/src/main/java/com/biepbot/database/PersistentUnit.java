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
import javax.ejb.Stateful;
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
@Stateful
public class PersistentUnit
{
    @EJB
    DB db;

    public PersistentUnit(boolean createDB)
    {
        if (createDB)
        {
            db = new DB(true);
        }
    }

    public PersistentUnit()
    {

    }

    /**
     * Gets a single object based on a query
     *
     * @param <T>
     * @param c
     * @param where
     * @param like
     * @return
     */
    public <T> T getObjectFromQuery(Class c, Map<String, Object> where, boolean like)
    {
        try
        {
            List<T> ret = getObjectsFromQuery(c, where, like);
            return ret.isEmpty() ? null : ret.get(0);
        }
        catch (final NoResultException nre)
        {
            return null;
        }
    }

    /**
     * Gets a single object based on a query
     *
     * @param <T>
     * @param c
     * @param f
     * @param where
     * @param like
     * @return
     */
    public <T> T getObjectFromQuery(Class c, String f, Object where, boolean like)
    {
        try
        {
            List<T> ret = getObjectsFromQuery(c, f, where, like);
            return ret.isEmpty() ? null : ret.get(0);
        }
        catch (final NoResultException nre)
        {
            return null;
        }
    }

    /**
     * Returns objects from a where and field name
     *
     * @param <T>
     * @param c The class which to browse
     * @param f The field name as stated in the table
     * @param where The condition to compare the field to
     * @param like
     * @return
     */
    public <T> List<T> getObjectsFromQuery(Class c, String f, Object where, boolean like)
    {
        Map<String, Object> oneMap = new HashMap<>();
        oneMap.put(f, where);
        return getObjectsFromQuery(c, oneMap, like);
    }

    /**
     * Returns objects from a map based where query, based on fields and names
     *
     * @param <T>
     * @param c The class which to browse
     * @param where Fields and conditions
     * @param like
     * @return
     */
    public <T> List<T> getObjectsFromQuery(Class c, Map<String, Object> where, boolean like)
    {
        if (db.getEntityManager() instanceof LocalEntityManager)
        {
            LocalEntityManager lem = (LocalEntityManager) db.getEntityManager();
            return lem.<T>get(c, where, like);
        }
        else
        {
            // set up a builder
            CriteriaBuilder builder = db.getEntityManager().getCriteriaBuilder();

            // begin creating the query
            CriteriaQuery<T> criteria = builder.createQuery(c);

            // select the class for a root
            Root<T> from = criteria.from(c);
            criteria.select(from);

            // LIKE?
            //Constructing list of parameters
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, Object> entry : where.entrySet())
            {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (like && value instanceof String)
                {
                    predicates.add(like(builder, from, key, (String)value));
                }
                else
                {
                    predicates.add(
                            builder.equal(from.get(key), value)
                    );
                }
            }

            // create the equals statement
            criteria.where(predicates.toArray(new Predicate[]
            {
            }));
            TypedQuery<T> typed = db.getEntityManager().createQuery(criteria);
            return typed.getResultList();
        }
    }

    private Predicate like(CriteriaBuilder builder, Root root, String key, String match)
    {
        return builder.or(
                builder.like(
                        builder.lower(root.get(key)
                        ), "%" + match + "%"
                )
        );
    }

    public void save(Object o)
    {
        db.save(o);
    }
}
