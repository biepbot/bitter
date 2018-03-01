/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rowan
 */
@Stateless
@Named
@Path("/username")
public class UserBean implements Serializable 
{
    private List<User> allUsers;
    private static final Logger logger = Logger.getLogger("javaeetutorial.rsvp.ejb.StatusBean");

    @PersistenceContext
    private EntityManager em;
    
    /**
     *
     * @param username
     * @return a specific username
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/")
    public User getUser(@PathParam("username") String username) {
        return new User("test");
    }
    
    /**
     *
     * @param username
     * @return a list of followers of a user
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/followers/")
    public List<User> getUserFollowers(@PathParam("username") String username) {
        User a = null;
        
        return a.getFollowers();
    }
    
    /**
     *
     * @param username
     * @return a list of 'tweets' of a user
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/tweets/")
    public List<Bark> getUserBarks(@PathParam("username") String username) {
        User a = null;
        
        return a.getBarks();
    }
    
    /**
     *
     * @param username
     * @return a list of users the user is following
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("{username}/following/")
    public List<User> getUserFollowing(@PathParam("username") String username) {
        User a = null;
        
        return a.getFollowing();
    }
}
