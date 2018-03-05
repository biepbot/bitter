/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import com.biepbot.barking.Validator;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rowan
 */
@Entity(name="Account")
public class User implements Serializable
{
    Validator val;
    
    @Id 
    @XmlTransient
    private String name;
    @XmlTransient
    private String avatar;
    
    @XmlTransient
    @ManyToMany(mappedBy = "likes")
    private List<Bark> barks;
    
    @OneToMany
    @XmlTransient
    private List<User> following;
    
    @OneToMany
    @XmlTransient
    private List<User> followers;
    
    @OneToMany
    @XmlTransient
    private List<User> blockedUsers;

    public User()
    {
    }

    public User(String name)
    {
        this.name = name;
    }
    
    public boolean Follow(User user) {
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
     * Updates the database
     */
    public void revalidateUser() {
        // todo ?
    }
    
    /**
     * Post a tweet, if valid
     * @param bark
     * @return 
     */
    public boolean Bark(Bark bark) {
        if (val.IsBarkProper(bark)) {
            barks.add(bark);
            return true;
        }
        return false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAvatar()
    {
        return avatar;
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
    
}
