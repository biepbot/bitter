/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.database.mocking;

import javax.persistence.EntityTransaction;

/**
 *
 * @author Rowan
 */
public class LocalTransaction implements EntityTransaction
{
    @Override
    public void begin()
    {
    }

    @Override
    public void commit()
    {
    }

    @Override
    public void rollback()
    {
    }

    @Override
    public void setRollbackOnly()
    {
    }

    @Override
    public boolean getRollbackOnly()
    {
        return false;
    }

    @Override
    public boolean isActive()
    {
        return true;
    }
    
}
