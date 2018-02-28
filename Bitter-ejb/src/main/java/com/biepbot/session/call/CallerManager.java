/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session.call;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * A class for more efficient client calling managing
 * @author Rowan
 */
@Named
@SessionScoped
public class CallerManager implements Serializable 
{
    private Client client;
    
    @PostConstruct
    private void init() {
        this.client = ClientBuilder.newClient();
    }
    
    @PreDestroy
    private void clean() {
        client.close();
    }
    
    /**
     * Send a webrequest to a specific path
     * @param path the path
     * @param c the class to request
     * @return the object from the class
     */
    public Object request(String path, Class c) {
        Caller<Object> caller = new Caller(c, client);
        return caller.request(path);
    }
}
