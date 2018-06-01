/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package embedded;

import fish.payara.micro.BootstrapException;
import fish.payara.micro.PayaraMicro;

/**
 *
 * @author Rowan
 */
public class Payara
{
    public static void main(String[] args) throws BootstrapException
    {
        PayaraMicro.getInstance()
                .addDeployment("F:\\Programming\\Projects\\Bitter\\Bitter-ear\\target\\Bitter-ear-1.0-SNAPSHOT.ear")
                .bootStrap();
    }
}
