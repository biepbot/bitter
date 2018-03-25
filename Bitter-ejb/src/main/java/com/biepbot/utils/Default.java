/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Rowan
 */
public abstract class Default
{
    public static Calendar now() {
        return new GregorianCalendar();
    }
}
