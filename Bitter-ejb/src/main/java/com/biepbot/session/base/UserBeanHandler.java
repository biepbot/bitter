/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session.base;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rowan
 */
@Named(value="userBeanHandler")
@RequestScoped
@Stateful
public class UserBeanHandler extends BeanHandler
{
    @EJB
    private BarkBeanHandler bbean;

    /**
     * Reflective code only
     */
    @Deprecated    
    public UserBeanHandler()
    {
    }
    
    /**
     * SE fallback
     * @param useLocal
     */
    @Deprecated
    public UserBeanHandler(boolean useLocal)
    {
        super(useLocal);
        bbean = new BarkBeanHandler(useLocal);
    }
    
    /**
     *
     * @return a specific username
     */
    public User getAllUsers() {
        return pu.<User>getObjectFromQuery(User.class, new HashMap<String, Object>(), false);
    }
    
    /**
     *
     * @param username the username of the user
     * @return a list of followers of a user
     */
    public List<Bark> getUserTimeline(String username) {
        // todo: optimise
        // get barks from following
        // get rebarks from following
        // but no replies
        User u = getUser(username);
        
        List<User> following = u.getFollowing();
        List<Bark> tl = new ArrayList<>(); // accept doubles for rebarks - list, not a set
        for (User ur : following) 
        {
            tl.addAll(ur.getBarks());
        }
        tl.addAll(u.getBarks());
        
        // Filter out replies
        List<Bark> ret = new ArrayList<>();
        for (Bark b : tl) {
            if (b.getRepliedTo() == null) {
                ret.add(b);
            }
        }
        Collections.sort(ret);
        return ret;
    }
    
    /**
     * @param username the username of the user
     * @param file the form file to update with
     * @return whether the image could be updated or not
     */
    public int updateAvatar(String username, File file) {
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
    public Response bark(String username, String tweet) {
        User u = getUser(username);
        boolean succ = u.bark(tweet);
        if (succ) {
            pu.save(u);
            return Response.ok(u.getLastBark()).build();
        }
        return Response.status(500).build();
    }
    
    /**
     *
     * @param username the username of the user
     * @param tweet the tweet content
     * @return error code
     */
    public Response like(String username, String tweet) {
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
    public Response rebark(String username, String tweet) {
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
    public List<Bark> getMentions(String username) {
        Map<String, String> params = new HashMap<>();
        params.put("contains", "@" + username);
        return bbean.getSearchResult(params);
    }
}
