/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Rowan
 */
@Entity
@Table
public class Bark implements Serializable, Comparable<Bark>
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
    
    @ManyToOne
    private Bark repliedTo;
    
    @OneToMany(mappedBy = "repliedTo")
    private List<Bark> replies;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar posttime;
    
    public Bark()
    {
    }
    
    public Bark(User poster, String content)
    {
        this.poster = poster;
        this.content = content;
        this.posttime = new GregorianCalendar();
        rebarkers = new ArrayList<>();
        likers = new ArrayList<>();
        replies = new ArrayList<>();
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
        liked.addLike(this);
    }
    
    public void rebark(User rebarked) 
    {
        rebarkers.add(rebarked);
        rebarked.rebark(this);
    }
    
    public void replyTo(User replied, String reply)
    {
        Bark b = new Bark(replied, reply);
        b.repliedTo = this;
        replies.add(b);
    }

    public List<User> getRebarkers()
    {
        return rebarkers;
    }

    public List<User> getLikers()
    {
        return likers;
    }

    public Calendar getPosttime()
    {
        return posttime;
    }

    @Override
    public int compareTo(Bark o)
    {
        return o.posttime.compareTo(posttime);
    }
}
