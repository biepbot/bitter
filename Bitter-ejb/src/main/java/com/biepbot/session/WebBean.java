/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Rowan
 */
@Stateless
@Named
@Produces(
        {
            MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
        })
@Path("/website")
public class WebBean
{
    @POST
    @Path("/string")
    public String getSite(@FormParam("url") String website)
    {
        String site = "";
        BufferedReader br = null;
        
        if (!website.startsWith("http")) {
            website = "http://" + website;
        }

        try
        {

            URL url = new URL(website);
            br = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            site = sb.toString();
        }
        catch (Exception e)
        {
            return "";
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(WebBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return site;
    }
}
