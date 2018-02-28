/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.barking;

import com.biepbot.base.Bark;
import javax.ejb.Singleton;

/**
 *
 * @author Rowan
 */
@Singleton
public class Validator
{
    private final int MAX_SIZE = 280;
    
    
    public boolean IsBarkProper(Bark bark) {
        return bark.getContent().length() <= MAX_SIZE;
    }
    
    public int GetMaxBark() {
        return MAX_SIZE;
    }
}
