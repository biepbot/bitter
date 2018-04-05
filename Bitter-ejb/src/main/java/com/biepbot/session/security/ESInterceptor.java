/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.session.security;

import com.biepbot.base.Role;
import com.biepbot.base.User;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

/**
 *
 * @author Rowan
 */
@Interceptor
@EasySecurity
public class ESInterceptor
{
    @Inject
    private HttpServletRequest context;

    public ESInterceptor()
    {
    }

    @AroundInvoke
    public Object checkSecurity(InvocationContext ctx) throws Exception
    {
        // get the context parameters
        EasySecurity example = ctx.getMethod().getAnnotation(EasySecurity.class);
        Role[] roles = example.grantedRoles();
        boolean requiresUser = example.requiresUser();

        if (requiresUser || roles.length > 0)
        {
            User u = (User) context.getSession().getAttribute("user");
            boolean NOAUTH = true; // by default: no access
            if (u != null)
            {
                for (Role r : roles)
                {
                    if (u.getPrivilege() == r)
                    {
                        NOAUTH = false; // access granted
                        break;
                    }
                }
            }
            else
            {
                NOAUTH = requiresUser;
            }
            if (NOAUTH)
            {
                throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED));
            }
        }

        /* invoke the proceed() method to call the original method */
        Object ret = ctx.proceed();

        /* perform any post method call work */
        return ret;
    }
}
