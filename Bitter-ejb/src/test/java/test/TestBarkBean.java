/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import com.biepbot.database.DB;
import com.biepbot.session.base.BarkBeanHandler;
import com.biepbot.session.base.UserBeanHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 *
 * @author Rowan
 */
public class TestBarkBean
{
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH);
    private final UserBeanHandler ubean;
    private final BarkBeanHandler bbean;
    User a;

    List<Bark> testBarks = new ArrayList<>();

    String tu = "testuser";
    String tb = "fresh";
    DB db;

    public TestBarkBean()
    {
        this.ubean = new UserBeanHandler(true);
        this.bbean = new BarkBeanHandler(true);
        a = new User(tu);
        db = new DB(true);
    }

    @Before
    public void setup()
    {
        db.save(a);
        // Have this user create a bunch of barks;
        addBark(tb + " fish1", true, false);
        addBark(tb + " fish2", false, true);
        addBark(tb + " fish3 @" + tu, false, true);

        // This one is relatively trending
        addBark(tb + " fish4 @" + tu, true, true);

        addBark(tb + " fish5", false, false);
        addBark(tb + " fish6", false, false);
        addBark(tb + " fish7", false, false);
        addBark(tb + " fish8", false, false);
        addBark(tb + " fish9", false, false);
        db.update(a);
    }

    @After
    public void tearDown()
    {
        for (Bark b : testBarks)
        {
            db.delete(b);
        }
        db.delete(a);
    }

    private void addBark(String context, boolean like, boolean rebark)
    {
        Bark b = new Bark(a, context);
        a.rebark(b);                    // non contextual add

        if (like)
        {
            b.like(a);
        }
        if (rebark)
        {
            b.rebark(a);
        }

        db.save(b);
        testBarks.add(b);
    }

    @Test
    public void getTrending()
    {
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
    public void getMentions()
    {
        List<Bark> mentions = ubean.getMentions(tu); // todo: still returns all
        Assert.assertEquals(2, mentions.size());
    }

    @Test
    public void fishyCase()
    {
        List<Bark> shouldBeOnTL = new ArrayList<>();
        List<Bark> shouldNotBeOnTL = new ArrayList<>();
        
        /*
         * test case where a user creates several fishy barks
         * where his timeline is kept up to date
        */
        // cases where the amount of barks increases:
            // bark
            // rebark
            // reply
        
        // timeline should contain
            // rebarks
            // barks
            // no replies

        // create barks
        // bark
        int c = a.getBark_count();
        a.bark("I love fish");              // Tell them. Tell them how much you love fish.
        shouldBeOnTL.add(a.getLastBark());  // should be on TL
        int newc = a.getBark_count();
        Assert.assertTrue(newc - 1 == c);

        // rebark
        c = a.getBark_count();
        a.rebark(testBarks.get(0));         // rebark fish bark
        shouldBeOnTL.add(a.getLastBark());  // should be on TL
        newc = a.getBark_count();
        Assert.assertTrue(newc - 1 == c);

        // reply
        c = a.getBark_count();
        testBarks.get(0).replyTo(a, "where's my fish???");
        shouldNotBeOnTL.add(a.getLastBark()); // should NOT be on TL
        newc = a.getBark_count();
        Assert.assertTrue(newc - 1 == c);
        
        // update for TL
        db.update(a);
        
        // verify timeline
        List<Bark> tl = ubean.getUserTimeline(tu);
        Assert.assertTrue(tl.containsAll(shouldBeOnTL));        // TL contains: fish barks
        Assert.assertFalse(tl.containsAll(shouldNotBeOnTL));    // TL does not contain: fish replies
    }

    @Test
    public void getSearch()
    {

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
        params.put("content", tb + " fish8");
        List<Bark> barks = bbean.getSearchResult(params);
        Assert.assertEquals(1, barks.size()); // I only have one tweet with this

        params.clear();
        params.put("contains", tb);
        barks = bbean.getSearchResult(params);
        Assert.assertEquals(9, barks.size()); // all my tweets

        params.clear();
        params.put("contains", tb);
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
