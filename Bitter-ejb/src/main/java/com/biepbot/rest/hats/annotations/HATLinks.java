/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biepbot.rest.hats.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Rowan
 */
@Inherited
@Target( {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HATLinks
{
    HATLink[] value() default {};
}
