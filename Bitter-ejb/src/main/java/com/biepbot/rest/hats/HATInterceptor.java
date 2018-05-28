/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.hats;

import com.biepbot.rest.hats.POJO.HATObject;
import com.biepbot.rest.hats.POJO.Link;
import com.biepbot.rest.hats.annotations.HATLink;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.Path;

/**
 *
 * @author Rowan
 */
@Interceptor
public class HATInterceptor
{
    private ObjectMapper mapper = new ObjectMapper();

    public HATInterceptor()
    {
    }

    @AroundInvoke
    public Object applyHats(InvocationContext ctx) throws Exception
    {
        Object ret = ctx.proceed();

        // if list, do this in list
        if (ret instanceof List)
        {
            List<Object> al = (List) ret;
            for (Object a : al)
            {
                parseObject(a);
            }
        }
        else
        {
            if (ret.getClass().isArray())
            {
                Object[] al = (Object[]) ret;
                for (int i = 0; i < al.length; i++)
                {
                    Object a = al[i];
                    parseObject(a);
                }
            }
            else
            {
                parseObject(ret);
            }
        }
        return ret;
    }

    private void parseObject(Object ret) throws IllegalArgumentException, SecurityException
    {
        if (!(ret instanceof HATObject))
        {
            return;
        }

        for (Method m : ret.getClass().getMethods())
        {
            // the point of these getters is that they are always XMLTransient / JSONIgnored
            Annotation[] annos = m.getAnnotations();
            
            //Annotation n = JsonIgnore.class.
            //annos[annos.length+1] = JsonIgnore;
            
            HATLink[] ls = m.getAnnotationsByType(HATLink.class);
            for (HATLink l : ls)
            {
                // Init
                String path = "";

                // get bean
                Class b = l.bean();

                // get path of bean
                Path p = (Path) b.getAnnotation(Path.class);
                path += p.value();

                // get remainder of url
                path += l.path();
                
                // get method
                Link link = new Link(path, l.method());

                // get method name
                String key = downGrade(m.getName());
                
                // add to object
                HATObject a = (HATObject) ret;
                a.addLink(link, key);
            }
        }
    }

    /**
     * Turns a camelCase function name into a shorter version Used for JSON
     * ("getFollowers" -> followers)
     *
     * @param string
     * @return
     */
    private static String downGrade(String string)
    {
        boolean firstword = false;
        boolean t = true;
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < string.length(); i++)
        {
            Character c = string.charAt(i);
            if (Character.isUpperCase(c) || firstword)
            {
                if (firstword)
                {
                    if (t)
                    {
                        s.append(c);
                    }
                    else
                    {
                        t = false;
                    }
                }
                else
                {
                    s.append(Character.toLowerCase(c));
                    firstword = true;
                }
            }
            else
            {
            }
        }
        return s.toString();
    }
}
