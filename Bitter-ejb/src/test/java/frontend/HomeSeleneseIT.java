/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import static frontend.SeleniumTest.driver;
import java.util.UUID;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 *
 * @author Rowan
 */
public class HomeSeleneseIT extends SeleniumTest
{

    @Test
    public void testLogin() throws Exception
    {
        // check if on home page
        assertNotNull(driver.findElement(By.id("bark")));
    }

    @Test
    public void testBark() throws Exception
    {
        // check if can bark
        WebElement ele = driver.findElement(By.id("bark"));
        assertNotNull(ele);

        
        final String bark = UUID.randomUUID().toString();
        ele.sendKeys(bark);

        WebElement send = driver.findElement(By.id("bark-button"));
        assertNotNull(send);

        send.click();

        getWait().until(new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver f)
            {
                try
                {
                    driver.findElement(By.xpath("//div[contains(text(), '"+bark+"')]/.."));
                    return true;
                }
                catch (Exception e)
                {
                }
                return false;
            }
        });

    }
}
