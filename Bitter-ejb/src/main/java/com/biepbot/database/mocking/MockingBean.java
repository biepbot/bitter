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
        try
        {
            LOGGER.log(Level.WARNING, "DEPLOYING FAKE USERS");
            
            // create default user
            bie = new User("biepbot");
            bie.setAvatar("https://cdn.discordapp.com/emojis/414037045560999956.png?v=1");
            
            // create users
            User goo = new User("GoodUser5");
            goo.setAvatar("https://cdn.discordapp.com/emojis/409023321888522246.png?v=1");
            db.save(goo);
            User aba = new User("ABadMeme");
            aba.setAvatar("https://cdn.discordapp.com/emojis/414410934166093836.png?v=1");
            db.save(aba);
            User kez = new User("Kezter");
            kez.setAvatar("https://cdn.discordapp.com/emojis/382965733224611840.png?v=1");
            db.save(kez);
            User sad = new User("SadTeen99");
            sad.setAvatar("https://cdn.discordapp.com/emojis/410463270089261076.png?v=1");
            db.save(sad);
            User don = new User("DonaldBump");
            don.setAvatar("https://cdn.discordapp.com/emojis/412679200986628118.png?v=1");
            db.save(don);
            User mem = new User("MemeMester");
            mem.setAvatar("https://cdn.discordapp.com/emojis/375728803206660126.png?v=1");
            db.save(mem);
            
            // attach tweets to users
            // and rebarks as well
            goo.bark("Hey everyone! I just noticed a new feature on Bitter.com, and it's totes awesome!");
            Thread.sleep(2);
            bie.bark("I need more memes imo.");
            Thread.sleep(2);
            aba.bark("Anyone saw that new Black Panty movie?");
            Thread.sleep(2);
            kez.bark("Everything is parodied today. It makes me want to paroDI--.. paroKMS.");
            Thread.sleep(2);
            sad.bark("If only there were emojies :joy: :sob:");
            Thread.sleep(2);
            bie.bark("When was the last time I had breakfast?");
            Thread.sleep(2);
            goo.bark("-10C inside, too! Everyone pack up before going anywhere!!!");
            Thread.sleep(2);
            don.bark("Something. Sad!");
            Thread.sleep(2);
            don.getBarks().get(0).rebark(bie);
            bie.bark("My friends are gay.");
            Thread.sleep(2);
            mem.bark("LOREM IPSUM for 240 characters? Or was it 280... uhh... Why are characters limited anyway?");
            Thread.sleep(2);
            
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
        catch (InterruptedException ex)
        {
            Logger.getLogger(MockingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
