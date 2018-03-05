/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.User;
import java.io.Serializable;
import java.util.UUID;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rowan
 */
@SessionScoped
@Path("/sessions")
public class SessionBean implements Serializable
{
    private User user;
    private String OATH;
    
    /**
     *
     * @param username
     * @param password
     * @return logs a user in and returns a OATH code
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/logon")
    public String logon(@QueryParam("username") String username, @QueryParam("password") String password) {
        // TODO
        // verify
        // get from database if possible
        user = new User(username);
        UUID uuid = UUID.randomUUID();
        OATH = uuid.toString();
        return OATH;
    }
    
    /**
     *
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/logout")
    public void logout() {
        user = null;
        OATH = null;
    }
    
    /**
     *
     * @return logs a user in and returns a OATH code
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/whoami")
    public String getOATH() {
        return OATH;
    }
    
    @PreDestroy
    public void cleanUp() {
        logout();
    }
}
