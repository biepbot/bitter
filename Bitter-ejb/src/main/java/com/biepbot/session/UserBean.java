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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rowan
 */
@Stateless
@Named
@Path("/users")
public class UserBean  
{    
    @EJB
    private BarkBean bbean;
    
    @EJB
    private PersistentUnit pu;
    
    public static UserBean getTestBarkBean()
    {
        UserBean b = new UserBean();
        b.loadPersistanceUnit();
        return b;
    }
    
    private void loadPersistanceUnit()
    {
        if (pu == null)
        {
            pu = new PersistentUnit(true);
        }
        if (bbean == null) { // fallback
            bbean = new BarkBean();
            bbean.loadPersistanceUnit();
        }
    }
    
    /**
     *
     * @param username the username of the user
     * @return a specific username
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}")
    public User getUser(@PathParam("username") String username) {
        return pu.<User>getObjectFromQuery(User.class, "name", username);
    }
    
    /**
     *
     * @return a specific username
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/all")
    public User getAllUsers() {
        return pu.<User>getObjectFromQuery(User.class, new HashMap<String, Object>());
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
     * @return a list of followers of a user
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/timeline")
    public List<Bark> getUserTimeline(@PathParam("username") String username) {
        // todo: optimise
        User u = getUser(username);
        
        List<User> following = u.getFollowing();
        Set<Bark> tl = new HashSet<>();
        for (User ur : following) 
        {
            tl.addAll(ur.getBarks());
        }
        tl.addAll(u.getBarks());
        List<Bark> ret = new ArrayList<>(tl);
        Collections.sort(ret);
        return ret;
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
     *
     * @param username the username of the user
     * @param tweet the tweet content
     * @return error code
     */
    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/bark")
    public Response bark(@PathParam("username") String username, @FormParam("bark") String tweet) {
        User u = getUser(username);
        boolean succ = u.bark(tweet);
        if (succ) {
            pu.save(u);
            return Response.ok(u.getLastBark()).build();
        }
        return Response.status(500, "No tweet could be made").build();
    }
    
    /**
     *
     * @param username the username of the user
     * @param tweet the tweet content
     * @return error code
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/like/{bark}")
    public Response like(@PathParam("username") String username, @PathParam("bark") String tweet) {
        User u = getUser(username);
        Bark b = bbean.getBark(tweet);
        boolean succ = b != null;
        if (succ) {
            boolean liked = u.getLikes().contains(b);
            if (liked) {
                u.unLike(b);
            } else  {
                u.addLike(b);
            }
            pu.save(u);
            String response = liked ? "0" : "1";
            return Response.ok().entity(response).build();
        }
        return Response.serverError().build();
    }
    
    /**
     *
     * @param username the username of the user
     * @param tweet the tweet ID
     * @return error code
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{username}/rebark/{bark}")
    public Response rebark(@PathParam("username") String username, @PathParam("bark") String tweet) {
        User u = getUser(username);
        Bark b = bbean.getBark(tweet);
        boolean succ = b != null;
        if (succ) {
            boolean liked = u.getBarks().contains(b);
            if (liked) {
                u.unRebark(b);
            } else  {
                u.rebark(b);
            }
            pu.save(u);
            String response = liked ? "0" : "1";
            return Response.ok().entity(response).build();
        }
        return Response.serverError().build();
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
        Map<String, String> params = new HashMap<>();
        params.put("contains", "@" + username);
        return bbean.getSearchResult(params);
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
