/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.biepbot.database.EntityHolder;
import com.biepbot.base.Role;
import com.biepbot.base.User;
import com.biepbot.session.UserBean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Rowan
 */
public class TestUserBean extends EntityHolder
{
    private final UserBean bean;
    User a;
    String tu = "testuser";
    
    public TestUserBean()
    {
        this.bean = new UserBean();        
        if ((a=bean.getUser(tu)) != null) {
            delete(a);
        }
        a = new User(tu);
        changes++;
    }
    
    @Before
    public void setupUser() {
        save(a);
    }
    
    @After
    public void tearDownUser() {
        delete(a);
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
        boolean res = a.Bark("hello world!");
        update(a);
        User b = bean.getUser(tu);
        Assert.assertTrue(res);                             // Not too long
        Assert.assertFalse(b.getBarks().isEmpty());         // Posted
        
    }
}
