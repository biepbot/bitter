/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database.mocking;

import com.biepbot.session.BarkBean;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author Rowan
 */
public class LocalEntityManager implements EntityManager
{
    private static final Logger LOGGER = Logger.getLogger(BarkBean.class.getName());

    // private static list of all
    private final static List<Object> localCache = new ArrayList<>();

    public LocalEntityManager()
    {
        LOGGER.log(Level.WARNING, "Local Entity Manager used");
    }

    @Override
    public void persist(Object entity)
    {
        localCache.add(entity);
    }

    @Override
    public <T> T merge(T entity)
    {
        return entity;
    }

    @Override
    public void remove(Object entity)
    {
        localCache.remove(entity);
    }

    /**
     * Gets an instance of a class where the field matches the value & name
     *
     * @param <T>
     * @param entityClass
     * @param fieldmap
     * @return
     */
    public <T> List<T> get(Class<T> entityClass, Map<String, Object> fieldmap, boolean like)
    {
        List<T> ret = new ArrayList<>();
        try
        {
            Map<Field, Object> fields = new HashMap<>();

            for (Map.Entry<String, Object> object : fieldmap.entrySet())
            {
                String key = object.getKey();
                Object value = object.getValue();

                Field field = entityClass.getDeclaredField(key);
                field.setAccessible(true);
                fields.put(field, value);

            }

            for (Object o : localCache)
            {
                T ot = (T) o;
                if (ot.getClass().equals(entityClass))
                {
                    boolean add = true;
                    for (Map.Entry<Field, Object> entry : fields.entrySet())
                    {
                        Field key = entry.getKey();
                        Object value = entry.getValue();

                        if (like && value instanceof String)
                        {
                            if (!((String) key.get(ot)).contains((String) value))
                            {
                                add = false;
                                break;
                            }
                        }
                        else
                        {
                            if (!key.get(ot).equals(value))
                            {
                                add = false;
                                break;
                            }
                        }

                    }
                    if (add)
                    {
                        ret.add(ot);
                    }

                }
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return ret;
    }

    /**
     * Gets an instance of a class where the field matches the value & name
     *
     * @param <T>
     * @param entityClass
     * @param fieldname
     * @param fieldvalue
     * @param like
     * @return
     */
    public <T> List<T> get(Class<T> entityClass, String fieldname, Object fieldvalue, boolean like)
    {
        List<T> ret = new ArrayList<>();
        try
        {
            Field field = entityClass.getDeclaredField(fieldname);
            field.setAccessible(true);

            for (Object o : localCache)
            {
                T ot = (T) o;
                if (ot.getClass().equals(entityClass))
                {
                    // USE REFLECTION TO SEE IF FIELDNAME IS THE SAME
                    if (like && fieldvalue instanceof String)
                    {
                        if (((String) field.get(ot)).contains((String) fieldvalue))
                        {
                            ret.add(ot);
                        }
                    }
                    else
                    {
                        if (field.get(ot).equals(fieldvalue))
                        {
                            ret.add(ot);
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return ret;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey)
    {
        try
        {
            for (Object o : localCache)
            {
                T ot = (T) o;
                if (o.getClass().equals(entityClass))
                {
                    // USE REFLECTION TO SEE IF FIELD HAS ANNOTATION @ID
                    Field[] fs = entityClass.getDeclaredFields();
                    for (Field field : fs)
                    {
                        field.setAccessible(true);
                        if (field.isAnnotationPresent(Id.class))
                        {
                            if (field.get(ot).equals(primaryKey))
                            {
                                return ot;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey)
    {
        return find(entityClass, primaryKey);
    }

    @Override
    public void flush()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFlushMode(FlushModeType flushMode)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FlushModeType getFlushMode()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void lock(Object entity, LockModeType lockMode)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh(Object entity)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh(Object entity, Map<String, Object> properties)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void detach(Object entity)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(Object entity)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LockModeType getLockMode(Object entity)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setProperty(String propertyName, Object value)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Object> getProperties()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Query createQuery(String qlString)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Query createQuery(CriteriaUpdate updateQuery)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Query createQuery(CriteriaDelete deleteQuery)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Query createNamedQuery(String name)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Query createNativeQuery(String sqlString)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void joinTransaction()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isJoinedToTransaction()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T unwrap(Class<T> cls)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getDelegate()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close()
    {
        //
    }

    @Override
    public boolean isOpen()
    {
        return false;
    }

    @Override
    public EntityTransaction getTransaction()
    {
        return new LocalTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Metamodel getMetamodel()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityGraph<?> createEntityGraph(String graphName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityGraph<?> getEntityGraph(String graphName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
