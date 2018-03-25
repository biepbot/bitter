/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.utils;

/**
 *
 * @author Rowan
 */
public abstract class Test
{
    public static boolean isNumber(String amount)
    {
        return amount.matches("-?\\d+(\\.\\d+)?");
    }

    public static int asNumber(String amount)
    {
        return Integer.parseInt(amount);
    }
}
