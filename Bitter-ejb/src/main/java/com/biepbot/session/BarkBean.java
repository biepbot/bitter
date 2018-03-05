/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.Bark;
import com.biepbot.database.PersistentUnit;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * FOR BARK QUERIES
 * @author Rowan
 */
@Stateless
@Named
@Path("/barks")
public class BarkBean extends PersistentUnit implements Serializable 
{
    /**
     * Searches through barks of last week and displays the 5 major ones
     * @return relevant 'tweets'
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/trending")
    public List<Bark> getTrending() {
        // TODO
        return null;
    }
    
    /**
     * Searches through barks to match the queries
     * @param ui
     * @return 'tweets' matching the search result
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/search")
    public List<Bark> getSearchResult(@Context UriInfo ui) {
        // TODO
        Map parameters = ui.getQueryParameters();
        return null;
    }
    
    //@Context UriInfo ui
}
