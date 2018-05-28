/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session;

import com.biepbot.base.User;
import com.biepbot.database.DB;
import com.biepbot.rest.hats.HATInterceptor;
import com.biepbot.session.base.UserBeanHandler;
import com.biepbot.session.security.annotations.inject.CurrentESUser;
import com.biepbot.session.security.annotations.interceptors.EasySecurity;
import com.biepbot.session.security.auth.ESAuth;
import static com.biepbot.session.security.auth.ESAuth.key;
import com.biepbot.session.security.base.ESUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
@Path("/auth")
@Interceptors({HATInterceptor.class})
public class SessionBean implements Serializable
{
    @Inject
    @CurrentESUser
    private ESUser currentUser;

    @EJB
    private UserBeanHandler bbh;

    @EJB
    private DB db;

    @GET
    @Path("ping")
    @EasySecurity(requiresUser = true)
    public String ping()
    {
        ESUser who = currentUser;
        return who.toString();
    }

    /**
     *
     * @param username
     * @param password
     * @param req
     * @return accepted or refused
     */
    @POST
    @Path("/logon")
    public Response logon(@FormParam("username") String username, @FormParam("password") String password, @Context HttpServletRequest req)
    {
        Object o = req.getSession().getAttribute("ESuser");
        // not logged in
        if (o == null)
        {
            User u = bbh.getUser(username);
            if (u != null)
            {
                if (u.getPassword().equals(password))
                {
                    long now = new Date().getTime();
                    long validity = 3 * 24 * 60 * 60 * 1000;

                    // log the user in
                    ESAuth.logon(req, u);

                    // generate key
                    String compactJws = Jwts.builder()
                            .setSubject(u.getName())
                            .signWith(SignatureAlgorithm.HS512, key)
                            .setExpiration(new Date(now + validity))
                            .compact();

                    // return approved status and give auth header
                    return Response.accepted(u).header("Authorization", compactJws).build();
                }
            }
            throw new NotAcceptableException();
        }
        return Response.ok(o).build();
    }

    /**
     *
     * @param req
     * @return success or failure
     */
    @POST
    @Path("/logout")
    public void logout(@Context HttpServletRequest req)
    {
        if (ESAuth.logout(req))
        {
            // Logged out
        }
    }

    /**
     *
     * @param username
     * @param password
     * @param email
     * @param req
     * @return success or failure
     */
    @POST
    @Path("/register")
    public Response register(@FormParam("username") String username, @FormParam("password") String password, @FormParam("email") String email, @Context HttpServletRequest req)
    {
        if (password == null || username == null || email == null)
        {
            throw new BadRequestException();
        }

        String resp = "OK";
        if (username.length() < 3)
        {
            resp = "Username was shorter than three characters";
        }
        if (password.length() < 5)
        { // validate on other fields
            resp = "Password was shorter than five characters";
        }
        // todo: 
        // verify mail
        // send email
        // finish up user

        User n = bbh.getUser(username);
        if (n != null)
        {
            resp = "Username was already taken";
        }

        if (resp.equals("OK"))
        {
            n = new User(username, email);
            n.setPassword(password);
            db.save(n);
            return logon(username, password, req);
        }
        throw new NotAcceptableException(resp);
    }
}
