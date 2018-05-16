/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.sockets;

import com.biepbot.base.User;
import com.biepbot.rest.sockethandlers.GetHttpSessionConfigurator;
import com.biepbot.rest.sockethandlers.TimeLineSessionHandler;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Rowan
 */
@ServerEndpoint(value = "/actions/bark", configurator = GetHttpSessionConfigurator.class)
public class TimeLineWebSocket
{
    private static final Logger LOG = Logger.getLogger(TimeLineWebSocket.class.getName());

    TimeLineSessionHandler sessionhandler = TimeLineSessionHandler.getInstance();

    @OnMessage
    public void handleMessage(String message, Session session)
    {
        try (JsonReader reader = Json.createReader(new StringReader(message)))
        {
            JsonObject jsonMessage = reader.readObject();

            // Send to everyone connected
            sessionhandler.send(jsonMessage, session);
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @OnClose
    public void close(Session session)
    {
        sessionhandler.disconnect(session);
    }

    @OnError
    public void onError(Throwable error)
    {
        LOG.log(Level.SEVERE, "Error");
        error.printStackTrace();
    }

    @OnOpen
    public void open(Session session, EndpointConfig config)
    {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User u = (User)httpSession.getAttribute("ESUser");
        sessionhandler.connect(session, u);
    }

}
