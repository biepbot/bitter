/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.biepbot.base.User;
import com.biepbot.session.UserBean;
import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rowan
 */
public class TestUserBean extends EntityTester
{
    private final UserBean bean;
    
    public TestUserBean()
    {
        this.bean = new UserBean();
    }
    
    @Test
    @Transactional
    public void GetUserTest() 
    {
        User a = new User("testuser");   
        persist(a);
        
        User b = bean.getUser("testuser");   
        Assert.assertNotNull(b);
        Assert.assertTrue((a.getName() == null ? b.getName() == null : a.getName().equals(b.getName())));
        rollback();
    }
    
    @Test
    public void GetFakeUserTest() 
    {
        User a = bean.getUser("nouser");
        Assert.assertNull(a);
    }
    
    @Test
    public void GetUnsupportedUserTest() 
    {
        User a = bean.getUser("#*");
        Assert.assertNull(a);
    }
}
