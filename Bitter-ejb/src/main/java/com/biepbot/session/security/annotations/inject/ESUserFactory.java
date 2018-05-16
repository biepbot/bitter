/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session.security.annotations.inject;

import com.biepbot.base.User;
import com.biepbot.database.PersistentUnit;
import static com.biepbot.session.security.auth.ESAuth.key;
import com.biepbot.session.security.base.ESUser;
import io.jsonwebtoken.Jwts;
import javax.ejb.EJB;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Rowan
 */
public class ESUserFactory 
{
    @Inject
    private HttpServletRequest context;
    
    @EJB
    protected PersistentUnit pu;    
    
    @Dependent
    @Produces
    @CurrentESUser
    public ESUser createESUser() {
        String h = context.getHeader("Authorization");
        
        String who;
        if (h == null) {
            return null;
        } else {
            who = Jwts.parser().setSigningKey(key).parseClaimsJws(h).getBody().getSubject();
        }
        return pu.<User>getObjectFromQuery(User.class, "name", who, false);
    }
}
