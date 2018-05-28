/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.hats.POJO;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Rowan
 */
public class HATLink implements Serializable
{
    private String name;
    private Set<Link> methods;

    @Deprecated
    public HATLink() {}
    
    public HATLink(String name)
    {
        this.name = name;
        this.methods = new HashSet<>();
    }

    public Set<Link> getMethods()
    {
        return methods;
    }

    public void setMethods(Set<Link> links)
    {
        this.methods = links;
    }
    
    public void addMethod(Link link) 
    {
        this.methods.add(link);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
