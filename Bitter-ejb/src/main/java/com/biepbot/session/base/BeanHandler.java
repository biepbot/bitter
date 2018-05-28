/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session.base;

import com.biepbot.base.User;
import com.biepbot.database.PersistentUnit;
import javax.ejb.EJB;

/**
 *
 * @author Rowan
 */
public abstract class BeanHandler
{
    @EJB
    protected PersistentUnit pu;    
    
    /**
     * Reflective code only
     */
    @Deprecated
    public BeanHandler() {
    }
    
    /**
     * SE fallback only
     */
    @Deprecated
    public BeanHandler(boolean useLocal) {
        pu = new PersistentUnit(useLocal);
    }
    
    /**
     *
     * @param username the username of the user
     * @return a specific username
     */
    public User getUser(String username) {
        return pu.<User>getObjectFromQuery(User.class, "username", username, false);
    }
}
