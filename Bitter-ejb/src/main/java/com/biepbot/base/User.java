/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import com.biepbot.rest.hats.POJO.HATObject;
import com.biepbot.rest.hats.annotations.HATLink;
import com.biepbot.session.UserBean;
import com.biepbot.session.security.base.ESUser;
import com.biepbot.utils.Validator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Rowan
 */
@Entity
@Table(name = "Account")
public class User extends HATObject implements ESUser
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * Tweets of a user
     */
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Bark> barks;

    /**
     * Likes of a user
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "like_user",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "like_id")
    )
    private Set<Bark> likes;

    /**
     * Rebarks of a user of their own barks
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "rebark_user",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "rebark_id")
    )
    private Set<Bark> rebarks;

    /**
     * The user's following
     */
    @ManyToMany(mappedBy = "followers", cascade = CascadeType.ALL)
    @JoinTable(name = "follow_user",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "follow_id")
    )
    private Set<User> following;

    /**
     * The user's followers
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "follower_user",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<User> followers;

    /**
     * The user's blocked users
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> blockedUsers;

    @Column(nullable = false)
    private Role privilege = Role.user;

    private String bio;

    private String location;

    private String color;

    private String email;

    private String avatar;

    /**
     * Default constructor for reflection libraries only Don't use this to
     * create new objects!
     */
    @Deprecated
    public User()
    {
        super();
    }

    public User(String name)
    {
        super();
        this.barks = new HashSet<>();
        this.blockedUsers = new HashSet<>();
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.rebarks = new HashSet<>();
        this.likes = new HashSet<>();
        this.username = name;
        this.password = "password";
    }

    public User(String name, String email)
    {
        this(name);
        this.email = email;
    }

    public boolean follow(User user)
    {
        if (!isBlockedBy(user))
        {
            user.followers.add(this);
            this.following.add(user);
            return true;
        }
        return false;
    }

    public void unfollow(User user)
    {
        this.following.remove(user);
        user.followers.remove(this);
    }

    @XmlTransient
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     *
     * @param u the user
     * @return whether the user specified is blocked
     */
    public boolean isBlockedBy(User u)
    {
        return (blockedUsers.contains(u));
    }

    /**
     * Post a tweet, if valid
     *
     * @param content
     * @return
     */
    public Bark bark(String content)
    {
        Bark bark = new Bark(this, content);
        if (Validator.IsBarkProper(bark))
        {
            barks.add(bark);
            return bark;
        }
        return null;
    }

    /**
     * Adds a rebark (in case of rebarking)
     *
     * @param bark
     */
    public void rebark(Bark bark)
    {
        barks.add(bark);
        rebarks.add(bark);
    }

    public void addLike(Bark bark)
    {
        likes.add(bark);
    }

    public String getName()
    {
        return username;
    }

    public void setName(String name)
    {
        this.username = name;
    }

    @XmlTransient
    @HATLink(bean = UserBean.class, path = "/{username}/barks")
    public Set<Bark> getBarks()
    {
        return barks;
    }

    /*
        In theory, you want to define HATLinks elsewhere or dynamically. Example;
        - automatically generate HATEOAS links from unique identifiers
    
        You could give the "getFollowing" the "following" identifier, and it'd automatically
        loop through any bean to see if anything would match the identifier. Possibly
        through another annotation that defines what they return.
     */
    @XmlTransient
    @HATLink(bean = UserBean.class, path = "/{username}/following")
    @HATLink(bean = UserBean.class, path = "/{username}/follow/{other}", method = "POST")
    @HATLink(bean = UserBean.class, path = "/{username}/unfollow/{other}", method = "POST")
    public Set<User> getFollowing()
    {
        return following;
    }

    @XmlTransient
    @HATLink(bean = UserBean.class, path = "/{username}/followers")
    public Set<User> getFollowers()
    {
        return followers;
    }

    @XmlTransient
    public Set<User> getBlockedUsers()
    {
        return blockedUsers;
    }

    @XmlTransient
    @HATLink(bean = UserBean.class, path = "/{username}/likes")
    public Set<Bark> getLikes()
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
    @Override
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
    @HATLink(bean = UserBean.class, path = "/{username}/rebarks")
    public Set<Bark> getRebarks()
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

    public void setBarks(Set<Bark> barks)
    {
        this.barks = barks;
    }

    public void setLikes(Set<Bark> likes)
    {
        this.likes = likes;
    }

    public void setRebarks(Set<Bark> rebarks)
    {
        this.rebarks = rebarks;
    }

    public void setFollowing(Set<User> following)
    {
        this.following = following;
    }

    public void setFollowers(Set<User> followers)
    {
        this.followers = followers;
    }

    public void setBlockedUsers(Set<User> blockedUsers)
    {
        this.blockedUsers = blockedUsers;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.username);
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
        return Objects.equals(this.username, other.username);
    }

    public void unLike(Bark b)
    {
        this.likes.remove(b);
    }

    public void unRebark(Bark b)
    {
        if (!b.getPoster().username.equals(username))
        {
            // only delete from barks if it's not yours
            this.barks.remove(b);
        }
        this.rebarks.remove(b);
    }

    @Override
    public String toString()
    {
        return "User{" + "name=" + username + ", privilege=" + privilege + ", bio=" + bio + ", location=" + location + ", color=" + color + ", email=" + email + ", avatar=" + avatar + '}';
    }
}
