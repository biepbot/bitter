/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import com.biepbot.rest.hats.POJO.HATObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rowan
 */
@Entity
@NamedQueries(
        { /* FIX LATER
            @NamedQuery(name = "search.Before", 
                        query = "SELECT b FROM Bark b WHERE "
                        + "b.content LIKE :contents AND "
                        + "b.posttime <= convert(date,:before)"),
            @NamedQuery(name = "search.After", 
                        query = "SELECT b FROM Bark b WHERE "
                        + "b.content LIKE :contents AND "
                        + "b.posttime => convert(date,:after)"),
            @NamedQuery(name = "search.After.Before", 
                        query = "SELECT b FROM Bark b WHERE "
                        + "b.content LIKE :contents AND "
                        + "b.posttime => convert(date,:after) AND "
                        + "b.posttime <= convert(date,:before)"),
            @NamedQuery(name = "search", 
                        query = "SELECT b FROM Bark b WHERE "
                        + "b.content LIKE :contents AND "
                        + "b.posttime <= convert(date,:before)")
       */ }
)
public class Bark extends HATObject implements Comparable<Bark>
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
    private String content;
    
    @ManyToMany(mappedBy = "rebarks", fetch = FetchType.LAZY)
    private Set<User> rebarkers;
    
    @ManyToMany(mappedBy = "likes", fetch = FetchType.LAZY)
    private Set<User> likers;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Bark repliedTo;
    
    @OneToMany(mappedBy = "repliedTo", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Bark> replies;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar posttime;
    
    /**
    * Default constructor for reflection libraries only
    * Don't use this to create new objects!
    */
    @Deprecated
    public Bark()
    {
        super();
    }
    
    public Bark(User poster, String content)
    {
        super();
        this.poster = poster;
        this.content = content;
        this.posttime = new GregorianCalendar();
        rebarkers = new HashSet<>();
        likers = new HashSet<>();
        replies = new HashSet<>();
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
    
    public Bark replyTo(User replied, String reply)
    {
        Bark b = new Bark(replied, reply);
        b.setRepliedTo(this);
        replies.add(b);
        replied.getBarks().add(b);
        return b;
    }

    @XmlTransient
    @JsonIgnore
    public Set<User> getRebarkers()
    {
        return rebarkers;
    }

    @XmlTransient
    @JsonIgnore
    public Set<User> getLikers()
    {
        return likers;
    }

    public Calendar getPosttime()
    {
        return posttime;
    }

    public Bark getRepliedTo()
    {
        return repliedTo;
    }

    public void setRepliedTo(Bark repliedTo)
    {
        this.repliedTo = repliedTo;
    }

    @XmlTransient
    @JsonIgnore
    public Set<Bark> getReplies()
    {
        return replies;
    }

    public void setReplies(Set<Bark> replies)
    {
        this.replies = replies;
    }

    public void setPoster(User poster)
    {
        this.poster = poster;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setRebarkers(Set<User> rebarkers)
    {
        this.rebarkers = rebarkers;
    }

    public void setLikers(Set<User> likers)
    {
        this.likers = likers;
    }

    public void setPosttime(Calendar posttime)
    {
        this.posttime = posttime;
    }

    @Override
    public int compareTo(Bark o)
    {
        return -o.posttime.compareTo(posttime);
    }
}
