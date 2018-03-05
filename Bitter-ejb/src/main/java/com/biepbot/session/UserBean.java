/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import com.biepbot.database.PersistentUnit;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
public class UserBean extends PersistentUnit implements Serializable 
{    
    /**
     *
     * @param username the username of the user
     * @return a specific username
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}")
    public User getUser(@PathParam("username") String username) {
        return (User)getObjectFromQuery(User.class, "name", username);
    }
    
    /**
     *
     * @param username the username of the user
     * @return a list of followers of a user
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/followers")
    public List<User> getUserFollowers(@PathParam("username") String username) {
        return getUser(username).getFollowers();
    }
    
    /**
     *
     * @param username the username of the user
     * @return a list of likes of a user
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/likes")
    public List<Bark> getUserLikes(@PathParam("username") String username) {
        return getUser(username).getLikes();
    }
    
    /**
     * RETURN CODES:
     * 200: OK
     * 501: BASIC FAIL
     * 502: TOO BIG
     * 503: NOT A FILE
     * 504: COULD NOT REMOVE OLD IMAGE
     * 
     * @param username the username of the user
     * @param file the form file to update with
     * @return whether the image could be updated or not
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/avatar/upload")
    public int updateAvatar(@PathParam("username") String username, @FormParam("avatar") File file) {
        // TODO
        // verify user
        // check if image
        // check if not too big
        // create new image
        // check if exists
        // remove old one
        // replace with new one
        return 1;
    }    
    
    /**
     * Returns all mentions to a user, if this is the user requesting them
     * @param username
     * @return mentions to a user
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/mentions")
    public List<Bark> getMentions(@PathParam("username") String username) {
        // TODO: query barks for @name in content
        return null;
    }
    
    /**
     *
     * @param username the username of the user
     * @return a list of 'tweets' of a user
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/barks")
    public List<Bark> getUserBarks(@PathParam("username") String username) {
        return getUser(username).getBarks();
    }
    
    /**
     *
     * @param username the username of the user
     * @return a list of users the user is following
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/following")
    public List<User> getUserFollowing(@PathParam("username") String username) {
        return getUser(username).getFollowing();
    }
}
