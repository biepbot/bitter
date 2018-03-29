/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session.base;

import com.biepbot.utils.Default;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Rowan
 */
@Provider
@PreMatching
public class RESTResponseFilter implements ContainerResponseFilter
{
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestCtx, ContainerResponseContext responseCtx) throws IOException
    {
        responseCtx.getHeaders().add("Access-Control-Allow-Origin", "*"); // OR: our domain
        responseCtx.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseCtx.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        responseCtx.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        responseCtx.getHeaders().add("Access-Control-Max-Age", "1209600");

        // IF GET
        Method m = resourceInfo.getResourceMethod();
        if (m.getName().equals("getLikedBy") || m.getName().equals("getRebarkedBy"))
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            Calendar expires = Default.now();
            expires.add(Calendar.HOUR, 1);

            //Tue, 24 Apr 2018 13:31:12 GMT
            String date = sdf.format(expires.getTime());
            responseCtx.getHeaders().add("Expires", date);
        }
    }
}
