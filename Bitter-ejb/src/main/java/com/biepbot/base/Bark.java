/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Rowan
 */
@Entity
@Table
public class Bark implements Serializable
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The person who posted this 'tweet'
     */
    @ManyToOne
    private User poster;
    
    /**
     * the content of the 'tweet'
     */
    @Column(length = 280)
    private String content;
    
    @ManyToMany(mappedBy = "rebarks")
    private List<User> rebarkers;
    
    @ManyToMany(mappedBy = "likes")
    private List<User> likers;
    
    public Bark()
    {
    }
    
    public Bark(User poster, String content)
    {
        this.poster = poster;
        this.content = content;
        rebarkers = new ArrayList<>();
        likers = new ArrayList<>();
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

    public User getPoster()
    {
        return poster;
    }
    
    public void like(User liked) 
    {
        likers.add(liked);
    }
    
    public void rebark(User rebarked) 
    {
        rebarkers.add(rebarked);
    }
}
