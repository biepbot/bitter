/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.base;

import com.biepbot.session.security.base.ESRole;

/**
 *
 * @author Rowan
 */
public enum Role implements ESRole
{
    user("user"),
    admin("admin");

    public String value;
    
    Role(String value) {
        this.value = value;
    }
    
    @Override
    public String getValue()
    {
        return this.value;
    }
}
