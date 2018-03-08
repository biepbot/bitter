/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import com.biepbot.session.BarkBean;
import com.biepbot.session.UserBean;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test.base.TestEntityHolder;

/**
 *
 * @author Rowan
 */
public class TestBarkBean extends TestEntityHolder
{
    private final UserBean ubean;
    private final BarkBean bbean;
    User a;
    String tu = "testuser";
    
    public TestBarkBean()
    {
        this.ubean = new UserBean();        
        this.bbean = new BarkBean();
        a = new User(tu);
        changes++;
    }
    
    @Before
    public void setup() {
        save(a);
        // Have this user create a bunch of barks;
        Bark b = new Bark(a, "Fresh fish");
        Bark b1 = new Bark(a, "Fresh fish1 @" + tu); // b1 mentions
        Bark b2 = new Bark(a, "Fresh fish2");
        Bark b3 = new Bark(a, "Fresh fish3");
        Bark b4 = new Bark(a, "Fresh fish4 @" + tu); // b4 mentions
        Bark b5 = new Bark(a, "Fresh fish5");
        Bark b6 = new Bark(a, "Fresh fish6");
        Bark b7 = new Bark(a, "Fresh fish7");
        Bark b8 = new Bark(a, "Fresh fish8");
        a.bark(b);
        save(b);
        a.bark(b1);
        save(b1);
        a.bark(b2);
        save(b2);
        a.bark(b3);
        save(b3);
        a.bark(b4);
        save(b4);
        a.bark(b5);
        save(b5);
        a.bark(b6);
        save(b6);
        a.bark(b7);
        save(b7);
        a.bark(b8);
        save(b8);
        
        // Create trending barks
        b.like(a);
        b1.like(a);
        b2.like(a);
        b2.rebark(a);
        b3.rebark(a);
        update(a);
    }
    
    @After
    public void tearDown() {
        delete(a);
    }
    
    @Test 
    public void getTrending() {
        List<Bark> trends = bbean.getTrending(); // todo: still returns 0
        
        // From all tweets, only 4 have interaction and are relatively trending
        // ONLY 1 has both likes and barks
        Assert.assertEquals(1, trends.size());
        
        // Default amount
        List<Bark> trends2 = bbean.getTrending("gahd");
        Assert.assertEquals(trends.size(), trends2.size());
        
        // No trending
        List<Bark> trends3 = bbean.getTrending("0");
        Assert.assertEquals(0, trends3.size());
    }
    
    @Test 
    public void getMentions() {
        List<Bark> mentions = ubean.getMentions(tu); // todo: still returns all
        Assert.assertEquals(2, mentions.size());
    }
    
    @Test
    public void getSearch() {
        /*
        * test on:
        * 1. any field
        * 2. before - date
        * 3. until - date
        * 4. orderBy - "rebarks", "likes"
        * 5. contains - string
        * 6. limit - amount
        */
        //bbean.getSearchResult(null);
    }
}
