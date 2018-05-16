/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import com.biepbot.session.base.BarkBeanHandler;
import com.biepbot.session.security.annotations.inject.CurrentESUser;
import com.biepbot.session.security.annotations.interceptors.EasySecurity;
import com.biepbot.session.security.base.ESUser;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * FOR BARK QUERIES
 *
 * @author Rowan
 */
@Stateful
@Produces(
        {
            MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
        })
@Path("/barks")
public class BarkBean
{
    @Inject
    @CurrentESUser
    private ESUser me;
    
    @EJB
    private BarkBeanHandler bbh;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);

    /**
     * Searches through barks of last week and displays the X major ones Does
     * not show barks without likes or rebarks
     *
     * @param amount
     * @return relevant 'tweets'
     */
    @GET
    @Path("/trending/{amount}")
    public List<Bark> getTrending(@PathParam("amount") String amount)
    {
        return bbh.getTrending(amount);
    }

    /**
     *
     * @param barkID
     * @return who liked this bark
     */
    @GET
    @Path("/{bark}/likes")
    public List<User> getLikes(@PathParam("bark") String barkID)
    {
        return bbh.getLikes(barkID);
    }

    /**
     *
     * @param barkID
     * @param user
     * @return whether the user liked this bark or not
     */
    @GET
    @Path("/{bark}/likes/{username}")
    public String getLikedBy(@PathParam("bark") String barkID, @PathParam("username") String user)
    {
        return bbh.getLikedBy(barkID, user);
    }

    /**
     *
     * @param barkID
     * @param user
     * @return whether the user rebarked this bark or not
     */
    @GET
    @Path("/{bark}/rebarks/{username}")
    public String getRebarkedBy(@PathParam("bark") String barkID, @PathParam("username") String user)
    {
        return bbh.getRebarkedBy(barkID, user);
    }

    /**
     *
     * @param barkID
     * @return who rebarked this bark
     */
    @GET
    @Path("/{bark}/rebarks")
    public List<User> getRebarks(@PathParam("bark") String barkID)
    {
        return bbh.getRebarks(barkID);
    }

    /**
     *
     * @param barkID
     * @return the replies to this bark
     */
    @GET
    @Path("/{bark}/replies")
    public List<Bark> getReplies(@PathParam("bark") String barkID)
    {
        return bbh.getReplies(barkID);
    }

    /**
     *
     * @param barkID
     * @param username
     * @param content
     * @return the replies to this bark
     */
    @POST
    @EasySecurity(requiresUser = true)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/{bark}/replyAs/{username}")
    public Bark replyTo(@PathParam("bark") String barkID, @PathParam("username") String username, @FormParam("content") String content)
    {
        if (!username.equals(me.getName())) {
            throw new NotAuthorizedException(Response.status(403));
        }
        
        return bbh.replyTo(barkID, username, content);
    }

    /**
     *
     * @param barkID
     * @return details about this bark
     */
    @GET
    @Path("/{bark}")
    public Bark getBark(@PathParam("bark") String barkID)
    {
        return bbh.getBark(barkID);
    }

    /**
     * Searches through barks of last week and displays the X major ones Does
     * not show barks without likes or rebarks
     *
     * @return relevant 'tweets'
     */
    @GET
    @Path("/trending")
    public List<Bark> getTrending()
    {
        return getTrending("5");
    }

    /**
     * Searches through barks to match the queries
     *
     * @param ui
     * @return 'tweets' matching the search result
     */
    @GET
    @Path("/search")
    public List<Bark> getSearchResult(@Context UriInfo ui)
    {
        return bbh.getSearchResult(ui);
    }
}
