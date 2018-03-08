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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
    private final UserBean ubean;
    private final BarkBean bbean;
    User a;
    Bark b, b1, b2, b3, b4, b5, b6, b7, b8;
    String tu = "testuser";
    
    public TestBarkBean()
    {
        this.ubean = new UserBean();        
        this.bbean = new BarkBean();
        a = new User(tu);
    }
    
    @Before
    public void setup() {
        // todo: no pasta
        save(a);
        // Have this user create a bunch of barks;
        b = new Bark(a, "Fresh fish");
        b1 = new Bark(a, "Fresh fish1 @" + tu); // b1 mentions
        b2 = new Bark(a, "Fresh fish2");
        b3 = new Bark(a, "Fresh fish3");
        b4 = new Bark(a, "Fresh fish4 @" + tu); // b4 mentions
        b5 = new Bark(a, "Fresh fish5");
        b6 = new Bark(a, "Fresh fish6");
        b7 = new Bark(a, "Fresh fish7");
        b8 = new Bark(a, "Fresh fish8");
        a.rebark(b);
        save(b);
        a.rebark(b1);
        save(b1);
        a.rebark(b2);
        save(b2);
        a.rebark(b3);
        save(b3);
        a.rebark(b4);
        save(b4);
        a.rebark(b5);
        save(b5);
        a.rebark(b6);
        save(b6);
        a.rebark(b7);
        save(b7);
        a.rebark(b8);
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
        delete(b);
        delete(b1);
        delete(b2);
        delete(b3);
        delete(b4);
        delete(b5);
        delete(b6);
        delete(b7);
        delete(b8);
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
        
        Calendar days = new GregorianCalendar();
        days.add(Calendar.DAY_OF_MONTH, 5);
        
        /*
        * test on:
        * 1. any field
        * 2. before - date
        * 3. after - date SKIP
        * 4. orderBy - "rebarks", "likes"
        * 5. contains - string
        * 6. limit - amount
        */
        Map<String, String> params = new HashMap<>();
        params.put("content", "Fresh fish8");
        List<Bark> barks = bbean.getSearchResult(params);
        Assert.assertEquals(1, barks.size()); // I only have one tweet with this
        
        params.clear();
        params.put("contains", "Fresh");
        barks = bbean.getSearchResult(params);
        Assert.assertEquals(9, barks.size()); // all my tweets
        
        params.clear();
        params.put("contains", "Fresh");
        params.put("limit", "5");
        barks = bbean.getSearchResult(params);
        Assert.assertEquals(5, barks.size()); // all my tweets, but limited to 5

        params.clear();        
        params.put("before", sdf.format(days.getTime()));
        barks = bbean.getSearchResult(params);
        Assert.assertEquals(0, barks.size()); // My tweets weren't posted 5 days ago

        params.clear();        
        params.put("after", sdf.format(days.getTime()));
        params.put("limit", "5");
        barks = bbean.getSearchResult(params);
        Assert.assertEquals(5, barks.size()); // My tweets were posted after 5 days ago
    }
}
