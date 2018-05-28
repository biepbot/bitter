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
public abstract class HATObject implements Serializable
{
    protected Set<HATLink> links;

    @Deprecated
    public HATObject()
    {
        this.links = new HashSet<>();
    }

    public Set<HATLink> getLinks()
    {
        return links;
    }

    public void setLinks(Set<HATLink> links)
    {
        this.links = links;
    }

    public void addLink(Link link, String name)
    {
        if (links == null)
        {
            this.links = new HashSet<>();
        }

        boolean found = false;
        for (HATLink hat : links)
        {
            if (hat.getName().equals(name))
            {
                found = true;
                hat.addMethod(link);
                break;
            }
        }

        if (found)
        {
            return;
        }

        HATLink l = new HATLink(name);
        l.addMethod(link);
        links.add(l);
    }
}
