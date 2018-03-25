/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.utils;

import com.biepbot.base.Bark;

/**
 *
 * @author Rowan
 */
public abstract class Validator
{
    private static final int MAX_SIZE = 280;
    
    public static boolean IsBarkProper(Bark bark) {
        return bark.getContent().length() <= MAX_SIZE;
    }
    
    public static int GetMaxBark() {
        return MAX_SIZE;
    }
}
