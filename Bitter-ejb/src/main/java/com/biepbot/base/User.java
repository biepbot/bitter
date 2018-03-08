/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import com.biepbot.barking.Validator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Rowan
 */
@Entity
@Table(name="Account")
public class User implements Serializable
{
    @EJB
    @Transient
    Validator val;
    
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Bark> barks = new ArrayList<>();           // tweets
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "like_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "like_id")
    )
    private List<Bark> likes = new ArrayList<>();           // likes
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "rebark_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "rebark_id")
    )
    private List<Bark> rebarks = new ArrayList<>();           // retweets
    
    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    @JoinTable(name = "follow_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follow_id")
    )
    private List<User> following = new ArrayList<>();
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "follower_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> blockedUsers = new ArrayList<>();
    
    @Column(nullable = false)
    private Role privilege = Role.user;
    
    private String bio;
    
    private String location;
    
    private String color;
    
    public User()
    {
    }

    public User(String name)
    {
        this.name = name;
    }
    
    public boolean follow(User user) {
        if (!isBlockedBy(user)) {
            user.followers.add(this);
            this.following.add(user);
            return true;
        }
        return false;
    }
    
    /**
     *
     * @param u the user
     * @return whether the user specified is blocked
     */
    public boolean isBlockedBy(User u) {
        return (blockedUsers.contains(u));
    }
    
    /**
     * Post a tweet, if valid
     * @param content
     * @return 
     */
    public boolean bark(String content) {
        Bark bark = new Bark(this, content);
        
        if (val == null) {
            val = new Validator();
        }
        
        if (val.IsBarkProper(bark)) {
            barks.add(bark);
            return true;
        }
        return false;
    }
    
    /**
     * Adds a bark (in case of rebarking)
     * @param bark
     */
    public void bark(Bark bark) {
        barks.add(bark);
    }
    
    public void addLike(Bark bark) {
        likes.add(bark);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Bark> getBarks()
    {
        return barks;
    }

    public List<User> getFollowing()
    {
        return following;
    }

    public List<User> getFollowers()
    {
        return followers;
    }

    public List<User> getBlockedUsers()
    {
        return blockedUsers;
    }    

    public List<Bark> getLikes()
    {
        return likes;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }    

    public Role getPrivilege()
    {
        return privilege;
    }

    public void setPrivilege(Role privilege)
    {
        this.privilege = privilege;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public List<Bark> getRebarks()
    {
        return rebarks;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.name, other.name);
    }    
}
