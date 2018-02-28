/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session.call;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

/**
 * An object for sending webrequests
 * @author Rowan
 * @param <T> 
 */
public class Caller<T>
{
    private final String baseUri = "http://localhost:8080/Bitter-web/webapi/";
    final Class<T> typeParameterClass;
    private final Client client;
    
    public Caller(Class<T> typeParameterClass, Client client){
        this.client = client;
        this.typeParameterClass = typeParameterClass;
    }
    
    public T request(String path) {
        try {
            T event = client.target(baseUri)
                    .path(path)
                    .request(MediaType.APPLICATION_XML)
                    .get(typeParameterClass);
            if (event == null) {
                // response didn't have anything
                return null;
            } else {
                return event;
            }
        } catch (Exception ex) {
            // error
            return null;
        }
    }
}
