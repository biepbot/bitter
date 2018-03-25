/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.biepbot.base.Bark;
import com.biepbot.base.Role;
import com.biepbot.base.User;
import com.biepbot.database.DB;
import com.biepbot.session.base.UserBeanHandler;
import com.biepbot.utils.Validator;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Rowan
 */
public class TestUserBean
{
    private final UserBeanHandler bean;
    User a;
    String tu = "testuser2";
    DB db;
    
    public TestUserBean()
    {
        this.bean = new UserBeanHandler(true);
        a = new User(tu);
        db = new DB(true);
    }
    
    @Before
    public void setupUser() {
        db.save(a);
    }
    
    @After
    public void tearDownUser() {
        db.delete(a);
    }
    
    // basic user testing
    @Test
    public void checkDatabaseUser() 
    {        
        User b = bean.getUser(tu);   
        Assert.assertNotNull(b);                            // Should exist
        Assert.assertEquals(a, b);                          // Should be the same user
    }
    
    @Test
    public void checkDefaultPrivilege() 
    {
        User b = bean.getUser(tu);   
        Assert.assertNotNull(b);                            // Should exist
        Assert.assertEquals(b.getPrivilege(), Role.user);   // Should be default
    }
    
    @Test
    public void checkNoUser() 
    {
        Assert.assertNull(bean.getUser("nouser"));
    }
    
    @Test
    public void checkUnsupportedUser() 
    {
        Assert.assertNull(bean.getUser("#*"));
    }
    
    // barking
    @Test
    public void checkValidTweet() 
    {        
        boolean res = a.bark("hello world!");
        db.update(a);
        User b = bean.getUser(tu);
        Assert.assertTrue(res);                             // Not too long
        Assert.assertFalse(b.getBarks().isEmpty());         // Posted
    }
    
    @Test
    public void userScenario1() 
    {        
        User tofollow = new User("follow");
        tofollow.follow(a);
        db.update(a);        
        
        // look at followers
        List<User> afollowers = bean.getUser(tu).getFollowers();
        Assert.assertFalse(afollowers.isEmpty());           // No followers
        
        // get a follower
        User follower = afollowers.get(0);
        Assert.assertEquals("follow", follower.getName());
        
        // follow this follower
        a.follow(follower);
        db.update(a);
        
        List<User> afollowing = bean.getUser(tu).getFollowing();
        Assert.assertFalse(afollowing.isEmpty());           // No following
        follower = afollowing.get(0);                       // get the user "a" is following
        
        Assert.assertEquals(tofollow, follower);            // check if the user "a" is following is the same user as the user we followed a with (mutuals)
    }
    
    // barking
    @Test
    public void tweetScenario1() 
    {        
        User b; boolean res;
        
        // Post a too long tweet
        String tl = ">";
        for (int i = 0; i < Validator.GetMaxBark(); i++)
        {
            tl += "a";
        }
        
        res = a.bark(tl);
        Assert.assertFalse(res);                           // Too long
        
        db.update(a); // update
        b = bean.getUser(tu);
        Assert.assertTrue(b.getBarks().isEmpty());         // Not posted
        Assert.assertTrue(a.getBarks().isEmpty());         // Not posted
        
        // Post a ok tweet
        res = a.bark("hello world!");
        db.update(a);
        b = bean.getUser(tu);
        Assert.assertTrue(res);                             // Not too long
        Assert.assertFalse(b.getBarks().isEmpty());         // Posted
        
        Bark barkA = a.getBarks().get(0);
        
        // Like own tweet
        barkA.like(a);
        db.update(a);
        b = bean.getUser(tu);
        Bark barkB = b.getBarks().get(0);
        Assert.assertFalse(barkB.getLikers().isEmpty());     // Liked
        Assert.assertFalse(bean.getUser(tu).getLikes().isEmpty());     // Liked
        
        // Rebark own tweet
        barkA.rebark(a);
        db.update(a);
        b = bean.getUser(tu);
        barkB = b.getBarks().get(0);
        Assert.assertFalse(barkB.getRebarkers().isEmpty());  // Rebarked
        Assert.assertFalse(bean.getUser(tu).getRebarks().isEmpty());     // Rebarked
    }
    
    
}
