/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import com.biepbot.session.base.UserBeanHandler;
import com.biepbot.session.security.annotations.inject.CurrentESUser;
import com.biepbot.session.security.annotations.interceptors.EasySecurity;
import com.biepbot.session.security.base.ESUser;
import java.io.File;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
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
@Stateful
@Produces(
        {
            MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
        })
@Path("/users")
public class UserBean
{
    @Inject
    @CurrentESUser
    private ESUser me;
    
    @EJB
    private UserBeanHandler bbh;

    /**
     *
     * @param username the username of the user
     * @return a specific username
     */
    @GET
    @Path("/{username}")
    public User getUser(@PathParam("username") String username)
    {
        return bbh.getUser(username);
    }
    
    /**
     *
     * @param username the username of the user
     * @param other the user to follow
     * @return 
     */
    @POST
    @EasySecurity(requiresUser = true)
    @Path("/{username}/follow/{other}")
    public String followUser(@PathParam("username") String username, @PathParam("other") String other)
    {
        if (!username.equals(me.getName())) {
            throw new NotAuthorizedException(Response.status(403));
        }
        User u = bbh.getUser(username);
        User them = bbh.getUser(other);
        u.follow(them);
        if (u.getFollowing().contains(them)) {
            return "1";
        }
        return "0";
    }
    
    /**
     *
     * @param username the username of the user
     * @param other the user to unfollow
     * @return 
     */
    @POST
    @Path("/{username}/unfollow/{other}")
    public String unfollowUser(@PathParam("username") String username, @PathParam("other") String other)
    {
        if (!username.equals(me.getName())) {
            throw new NotAuthorizedException(Response.status(403));
        }
        User u = bbh.getUser(username);
        User them = bbh.getUser(other);
        u.unfollow(them);
        if (u.getFollowing().contains(them)) {
            return "1";
        }
        return "0";
    }

    /**
     *
     * @return a specific username
     */
    @GET
    @Path("/all")
    public User getAllUsers()
    {
        return bbh.getAllUsers();
    }

    /**
     *
     * @param username the username of the user
     * @return a list of followers of a user
     */
    @GET
    @Path("/{username}/followers")
    public List<User> getUserFollowers(@PathParam("username") String username)
    {
        return getUser(username).getFollowers();
    }

    /**
     *
     * @param username the username of the user
     * @return a list of followers of a user
     */
    @GET
    @Path("/{username}/timeline")
    public List<Bark> getUserTimeline(@PathParam("username") String username)
    {
        return bbh.getUserTimeline(username);
    }

    /**
     *
     * @param username the username of the user
     * @return a list of likes of a user
     */
    @GET
    @Path("/{username}/likes")
    public List<Bark> getUserLikes(@PathParam("username") String username)
    {
        return getUser(username).getLikes();
    }

    /**
     * @param username the username of the user
     * @param file the form file to update with
     * @return whether the image could be updated or not
     */
    @POST
    @EasySecurity(requiresUser = true)
    @Path("/{username}/avatar/upload")
    public Response updateAvatar(@PathParam("username") String username, @FormParam("avatar") File file)
    {
        int ret = bbh.updateAvatar(username, file);
        if (ret == 0) {
            return Response.ok().build();
        } 
        return Response.status(500).build();
    }

    /**
     *
     * @param username the username of the user
     * @param tweet the tweet content
     * @return error code
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // default?
    @EasySecurity(requiresUser = true)
    @Path("/{username}/bark")
    public Response bark(@PathParam("username") String username, @FormParam("bark") String tweet)
    {
        if (!username.equals(me.getName())) {
            throw new NotAuthorizedException(Response.status(403));
        }
        
        return bbh.bark(username, tweet);
    }

    /**
     *
     * @param username the username of the user
     * @param tweet the tweet content
     * @return error code
     */
    @POST
    @EasySecurity(requiresUser = true)
    @Path("/{username}/like/{bark}")
    public Response like(@PathParam("username") String username, @PathParam("bark") String tweet)
    {
        if (!username.equals(me.getName())) {
            throw new NotAuthorizedException(Response.status(403));
        }
        
        return bbh.like(username, tweet);
    }

    /**
     *
     * @param username the username of the user
     * @param tweet the tweet ID
     * @return error code
     */
    @POST
    @EasySecurity(requiresUser = true)
    @Path("/{username}/rebark/{bark}")
    public Response rebark(@PathParam("username") String username, @PathParam("bark") String tweet)
    {
        if (!username.equals(me.getName())) {
            throw new NotAuthorizedException(Response.status(403));
        }
        
        return bbh.rebark(username, tweet);
    }

    /**
     * Returns all mentions to a user, if this is the user requesting them
     *
     * @param username
     * @return mentions to a user
     */
    @GET
    @Path("/{username}/mentions")
    public List<Bark> getMentions(@PathParam("username") String username)
    {
        return bbh.getMentions(username);
    }

    /**
     *
     * @param username the username of the user
     * @return a list of 'tweets' of a user
     */
    @GET
    @Path("/{username}/barks")
    public List<Bark> getUserBarks(@PathParam("username") String username)
    {
        return getUser(username).getBarks();
    }

    /**
     *
     * @param username the username of the user
     * @return a list of users the user is following
     */
    @GET
    @Path("/{username}/following")
    public List<User> getUserFollowing(@PathParam("username") String username)
    {
        return getUser(username).getFollowing();
    }
}
