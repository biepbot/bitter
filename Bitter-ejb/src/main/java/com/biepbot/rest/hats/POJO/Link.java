/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.hats.POJO;

import java.io.Serializable;

/**
 *
 * @author Rowan
 */
public class Link implements Serializable
{
    private String url;
    private String method;
    
    @Deprecated
    public Link() {}

    public Link(String url, String method)
    {
        this.url = url;
        this.method = method;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }
}
