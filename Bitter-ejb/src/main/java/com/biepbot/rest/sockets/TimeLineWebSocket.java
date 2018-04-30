/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.sockets;

import com.biepbot.rest.sockethandlers.TimeLineSessionHandler;
import java.io.StringReader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
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
@ApplicationScoped
@ServerEndpoint("/actions/bark")
public class TimeLineWebSocket
{
    @Inject
    TimeLineSessionHandler sessionhandler;
    
    @OnMessage
    public void handleMessage(String message, Session session)
    {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            
            // Send to everyone connected
            sessionhandler.send(jsonMessage);
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
    }

    @OnOpen
    public void open(Session session)
    {
        sessionhandler.connect(session);
    }
    
    
}
