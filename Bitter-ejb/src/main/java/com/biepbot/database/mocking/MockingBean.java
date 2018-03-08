/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database.mocking;

import com.biepbot.base.Bark;
import com.biepbot.base.User;
import com.biepbot.database.DB;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Rowan
 */
@Singleton
@Startup
public class MockingBean
{
    private static final Logger LOGGER = Logger.getLogger( MockingBean.class.getName() );
    
    private User bie;
    
    @EJB
    private DB db;
    
    @PostConstruct
    public void init() {
        LOGGER.log(Level.WARNING, "DEPLOYING FAKE USERS");
        
        // create default user
        bie = new User("biepbot");
        
        // create users
        User goo = new User("GoodUser5");
        db.save(goo);
        User aba = new User("ABadMeme");
        db.save(aba);
        User kez = new User("Kezter");
        db.save(kez);
        User sad = new User("SadTeen99");
        db.save(sad);
        User don = new User("DonaldBump");
        db.save(don);
        User mem = new User("MemeMester");
        db.save(mem);
        
        // attach tweets to users
        don.bark("Something. Sad!");
        
        // and rebarks as well
        goo.bark("Hey everyone! I just noticed a new feature on Bitter.com, and it's totes awesome!");
        bie.bark("I need more memes imo.");
        aba.bark("Anyone saw that new Black Panty movie?");
        kez.bark("Everything is parodied today. It makes me want to paroDI--.. paroKMS.");
        sad.bark("If only there were emojies :joy: :sob:");
        bie.bark("When was the last time I had breakfast?");
        goo.bark("-10C inside, too! Everyone pack up before going anywhere!!!");
        don.getBarks().get(0).rebark(bie);
        bie.bark("My friends are gay.");
        mem.bark("LOREM IPSUM for 240 characters? Or was it 280... uhh... Why are characters limited anyway?");
        
        // attach likes
        Bark parody = kez.getBarks().get(0);
        parody.like(bie);
        parody.rebark(bie);
        
        Bark emoji = sad.getBarks().get(0);
        emoji.like(bie);
        
        Bark cold = goo.getBarks().get(1);
        cold.like(bie);
        cold.rebark(bie);
        
        Bark something = don.getBarks().get(0);
        something.like(bie);
        
        // attach follows
        bie.follow(goo);
        bie.follow(aba);
        bie.follow(kez);
        bie.follow(sad);
        bie.follow(mem);
        
        goo.follow(bie);
        goo.follow(kez);
        
        mem.follow(sad);
        
        // save
        db.save(bie);
    }
}
