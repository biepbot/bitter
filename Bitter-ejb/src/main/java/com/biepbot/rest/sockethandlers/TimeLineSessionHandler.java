/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.sockethandlers;

import com.biepbot.base.SessionUser;
import com.biepbot.base.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Rowan
 */
// todo:
// find out why applicationscoped here causes it to be null
public class TimeLineSessionHandler
{
    protected final List<SessionUser> sessions = new ArrayList<>();

    private TimeLineSessionHandler()
    {
    }

    private static TimeLineSessionHandler instance;

    public static TimeLineSessionHandler getInstance()
    {
        if (instance == null)
        {
            instance = new TimeLineSessionHandler();
        }
        return instance;
    }

    /**
     * Connects a session
     *
     * @param session
     * @param who
     */
    public void connect(Session session, User who)
    {
        sessions.add(new SessionUser(session, who));
    }

    /**
     * Disconnects a session
     *
     * @param session
     */
    public void disconnect(Session session)
    {
        SessionUser su = null;
        for (SessionUser u : sessions)
        {
            if (u.session.equals(session))
            {
                su = u;
            }
        }
        if (su == null)
        {
            return;
        }
        sessions.remove(su);
    }

    /**
     * Sends a JsonObject to all sessions
     *
     * @param message
     */
    public void send(JsonObject message, Session from)
    {
        User u = null;
        for (SessionUser s : sessions)
        {
            if (s.session.equals(from))
            {
                u = s.user;
                // for now; send to self
                sendTo(from, message);
            }
        }
        if (u == null)
        {
            return;
        }

        // get their followers
        List<User> followers = u.getFollowers();

        for (SessionUser s : sessions)
        {
            if (followers.contains(s.getUser()))
            {
                sendTo(s.session, message);
            }
        }
    }

    public void sendTo(Session session, JsonObject message)
    {
        try
        {
            session.getBasicRemote().sendText(message.toString());
        }
        catch (IOException ex)
        {
            disconnect(session);
            Logger.getLogger(TimeLineSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
