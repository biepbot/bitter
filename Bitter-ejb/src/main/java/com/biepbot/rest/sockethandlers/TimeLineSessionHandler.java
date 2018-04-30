/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.sockethandlers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author Rowan
 */
@ApplicationScoped
public class TimeLineSessionHandler
{    
    protected final Set<Session> sessions = new HashSet<>();

    /**
     * Connects a session
     * @param session
     */
    public void connect(Session session)
    {
        sessions.add(session);
    }

    /**
     * Disconnects a session
     * @param session
     */
    public void disconnect(Session session)
    {
        sessions.remove(session);
    }

    /**
     * Sends a JsonObject to all sessions
     * @param message
     */
    public void send(JsonObject message)
    {
        for (Session s : sessions) 
        {
            sendTo(s, message);
        }
    }
    
    public void sendTo(Session session, JsonObject message)
    {        
        // send to all CONNECTED followers
        // todo; check if it's a follower
        try
        {            
            session.getBasicRemote().sendText(message.toString());
        }
        catch (IOException ex)
        {
            sessions.remove(session);
            Logger.getLogger(TimeLineSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
