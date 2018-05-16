/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import javax.websocket.Session;

/**
 *
 * @author Rowan
 */
public class SessionUser
{
    public Session session;
    public User user;

    public SessionUser(Session session, User user)
    {
        this.session = session;
        this.user = user;
    }

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
