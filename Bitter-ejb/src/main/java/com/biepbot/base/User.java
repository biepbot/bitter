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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

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
    private Validator val;
    
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String name;
    
    /**
     * Tweets of a user
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Bark> barks;           
    
    /**
     * Likes of a user
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "like_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "like_id")
    )
    private List<Bark> likes;           
    
    /**
     * Rebarks of a user of their own barks
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "rebark_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "rebark_id")
    )
    private List<Bark> rebarks;           
    
    /**
     * The user's following
     */
    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    @JoinTable(name = "follow_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follow_id")
    )
    private List<User> following;
    
    /**
     * The user's followers
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "follower_user",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private List<User> followers;
    
    /**
     * The user's blocked users
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> blockedUsers;
    
    @Column(nullable = false)
    private Role privilege = Role.user;
   
    private String bio;
    
    private String location;
    
    private String color;
    
    private String email;
    
    private String avatar;
    
    /**
    * Default constructor for reflection libraries only
    * Don't use this to create new objects!
    */
    @Deprecated
    public User() { }

    public User(String name)
    {
        this.barks = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.rebarks = new ArrayList<>();
        this.likes = new ArrayList<>();
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
    
    public Bark getLastBark() {
        return barks.get(barks.size() - 1);
    }
    
    /**
     * Adds a rebark (in case of rebarking)
     * @param bark
     */
    public void rebark(Bark bark) {
        barks.add(bark);
        
        if (bark.getPoster().equals(this))
            rebarks.add(bark);
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

    @XmlTransient
    public List<Bark> getBarks()
    {
        return barks;
    }

    @XmlTransient
    public List<User> getFollowing()
    {
        return following;
    }

    @XmlTransient
    public List<User> getFollowers()
    {
        return followers;
    }

    @XmlTransient
    public List<User> getBlockedUsers()
    {
        return blockedUsers;
    }    

    @XmlTransient
    public List<Bark> getLikes()
    {
        return likes;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
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

    @XmlAttribute
    public int getFollower_count()
    {
        return followers.size();
    }

    @XmlAttribute
    public int getBark_count()
    {
        return barks.size();
    }
    
    @XmlAttribute
    public int getFollowing_count()
    {
        return following.size();
    }

    @XmlTransient
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

    @XmlTransient
    public List<Bark> getRebarks()
    {
        return rebarks;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String Email)
    {
        this.email = Email;
    }

    public void setBarks(List<Bark> barks)
    {
        this.barks = barks;
    }

    public void setLikes(List<Bark> likes)
    {
        this.likes = likes;
    }

    public void setRebarks(List<Bark> rebarks)
    {
        this.rebarks = rebarks;
    }

    public void setFollowing(List<User> following)
    {
        this.following = following;
    }

    public void setFollowers(List<User> followers)
    {
        this.followers = followers;
    }

    public void setBlockedUsers(List<User> blockedUsers)
    {
        this.blockedUsers = blockedUsers;
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

    public void unLike(Bark b)
    {
        this.likes.remove(b);
    }
    
    public void unRebark(Bark b)
    {
        this.rebarks.remove(b);
    }
}
