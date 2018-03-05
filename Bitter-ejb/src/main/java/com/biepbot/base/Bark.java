/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rowan
 */
@Entity
public class Bark implements Serializable
{

    /**
     * the content of the 'tweet'
     */
    @XmlTransient
    private String content;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long id;

    /**
     * The person who posted this 'tweet'
     */
    @ManyToOne
    private User poster;
    
    /**
     * The people who liked this 'tweet'
     */
    @ManyToMany
    private List<User> likes;
    
    
    /**
     * The person who 'retweeted' this 'tweet'
     */
    @ManyToMany
    private List<User> barks;
    
    public Bark()
    {
    }
    
    public Bark(User poster, String content)
    {
        this.poster = poster;
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
}
