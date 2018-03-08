/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.Bark;
import com.biepbot.database.PersistentUnit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 * FOR BARK QUERIES
 *
 * @author Rowan
 */
@Stateless
@Named
@Path("/barks")
public class BarkBean extends PersistentUnit
{
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
    
    /**
     * Searches through barks of last week and displays the X major ones
     * Does not show barks without likes or rebarks
     *
     * @param amount
     * @return relevant 'tweets'
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
    @Path("/trending/{amount}")
    public List<Bark> getTrending(@PathParam("amount") String amount)
    {
        int amnt = 5;
        if (amount.matches("-?\\d+(\\.\\d+)?")) {
            amnt = Integer.parseInt(amount);
            
        }
        
        Calendar days = new GregorianCalendar();
        days.add(Calendar.DAY_OF_MONTH, -5);

        Map<String, String> params = new HashMap<>();
        params.put("before", sdf.format(days.getTime()));
        params.put("orderBy", "rebarks");
        params.put("min_likes", "1");
        params.put("min_rebarks", "1");
        params.put("limit", String.valueOf(amnt));
        return getSearchResult(params);
    }
    /**
     * Searches through barks of last week and displays the X major ones
     * Does not show barks without likes or rebarks
     *
     * @return relevant 'tweets'
     */
    @GET
    @Produces(
            {
                MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
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
    @Produces(
            {
                MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
            })
    @Path("/search")
    public List<Bark> getSearchResult(@Context UriInfo ui)
    {
        // returns a Map<String, LinkedList<String>>
        MultivaluedMap<String, String> parameters = ui.getQueryParameters();
        Map<String, String> params = new HashMap<>();
        
        // Turn into usable Map<String, String>
        Set<String> keys = parameters.keySet();
        for (String key : keys)
        {
            List<String> list = parameters.get(key);
            for (String value : list)
            {
                params.put(key, value);
            }
        }        
        
        return getSearchResult(params);
    }

    /**
     * Searches through barks to match the queries ACCEPTED: 
     * 1. any field 
     * 2. before - date 
     * 3. after - date 
     * 4. orderBy - "rebarks", "likes", "value" 
     * 5. contains - string 6. limit - amount
     * 6. min_rebarks - minimum
     * 7. min_likes - minimum likes
     *
     * @param parameters
     * @return
     */
    public List<Bark> getSearchResult(Map<String, String> parameters)
    {
        Map<String, Object> par = new HashMap<>();
        int limit = Integer.MAX_VALUE;

        for (Map.Entry<String, String> object : parameters.entrySet())
        {
            String key = object.getKey();
            String value = object.getValue();
            switch (key)
            {
                case "before":
                case "after":
                case "orderBy":
                case "contains":
                case "min_likes":
                case "min_rebarks":
                    break;
                case "limit":
                    if (value.matches("-?\\d+(\\.\\d+)?")) {
                        limit = Integer.parseInt(value);
                        if (limit == 0)
                            return new ArrayList<>();
                    }
                    break;
                default:
                    par.put(key, value);
                    break;
            }
        }

        // todo: migrate to seperate functions to remove database load
        // Get all the barks
        List<Bark> ret = this.<Bark>getObjectsFromQuery(Bark.class, par);
        
        // Barks to remove from list later
        Set<Bark> rem = new HashSet<>();
        String order = "";
        
        for (Map.Entry<String, String> object : parameters.entrySet())
        {
            String key = object.getKey();
            String value = object.getValue();

            switch (key)
            {
                case "before":
                    try {
                        // parse value to calendar
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(value));

                        // for loop, remove anything before calender now
                        for (Bark b : ret) 
                        {
                            if (b.getPosttime().before(cal))
                            {
                                rem.add(b);
                            }
                        }
                    } 
                    catch (Exception e) 
                    {} // not interested in errors
                    break;
                case "after":
                    try {
                        // parse value to calendar
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(value));

                        // for loop, remove anything after calender now
                        for (Bark b : ret) 
                        {
                            if (b.getPosttime().after(cal))
                            {
                                rem.add(b);
                            }
                        }
                    } 
                    catch (Exception e) 
                    {} // not interested in errors
                    break;
                case "orderBy":
                    order = value;
                    break;
                case "contains":
                    // check if contains
                    for (Bark b : ret) 
                    {
                        if (!b.getContent().contains(value))
                        {
                             rem.add(b);
                        }
                    }
                    break;
                case "min_likes":
                    // check if enough likes
                    if (value.matches("-?\\d+(\\.\\d+)?")) {
                        int amnt = Integer.parseInt(value);
                        for (Bark b : ret) 
                        {
                            if (b.getLikers().size() < amnt)
                            {
                                 rem.add(b);
                            }
                        }
                    }
                    break;
                case "min_rebarks":
                    // check if enough rebarks
                    if (value.matches("-?\\d+(\\.\\d+)?")) {
                        int amnt = Integer.parseInt(value);
                        for (Bark b : ret) 
                        {
                            if (b.getRebarkers().size() < amnt)
                            {
                                 rem.add(b);
                            }
                        }
                    }
                    break;
            }
        }
        
        // remove all removed
        if (rem.size() > 0)
            ret.removeAll(rem);

        if (!"".equals(order))
        {
            switch (order)
            {
                case "rebarks":
                    // sort on rebarks
                    Collections.sort(ret, new Comparator<Bark>()
                             {
                                 @Override
                                 public int compare(Bark lhs, Bark rhs)
                                 {
                                     return lhs.getRebarkers().size() - rhs.getRebarkers().size();
                                 }
                             });
                    break;

                case "likes":
                    // sort on likes
                    Collections.sort(ret, new Comparator<Bark>()
                             {
                                 @Override
                                 public int compare(Bark lhs, Bark rhs)
                                 {
                                     return lhs.getLikers().size() - rhs.getLikers().size();
                                 }
                             });
                    break;
                case "value":
                    // sort on weighted balance
                    Collections.sort(ret, new Comparator<Bark>()
                             {
                                 @Override
                                 public int compare(Bark lhs, Bark rhs)
                                 {
                                     int rebarkers = lhs.getRebarkers().size() - rhs.getRebarkers().size();
                                     int likers = lhs.getLikers().size() - rhs.getLikers().size();
                                     return likers + rebarkers * 2; // rebarks are twice as heavy as likes
                                 }
                             });
                    break;
            }
        }

        if (limit - 1 < ret.size() && ret.size() != 1)
             ret = ret.subList(0, limit);
        return ret;
    }
}
