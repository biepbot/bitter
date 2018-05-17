/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Rowan
 */
public class SeleniumTest
{
    protected static final int WAIT_TIME = 3;
    protected static WebDriver driver;

    @BeforeClass
    public static void bsetup()
    {
        ChromeDriverManager.getInstance().setup();
        
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        
        // navigate to login page
        driver.get("http://localhost:8080/Bitter-web/index.jsp");

        // log in
        WebElement us = driver.findElement(By.name("username"));
        WebElement pw = driver.findElement(By.name("password"));
        us.sendKeys("biepbot");
        pw.sendKeys("password");
        pw.submit();
        
        WebDriverWait wait = new WebDriverWait(driver, WAIT_TIME, 100);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bark")));
    }

    
    @AfterClass
    public static void btearDown() 
    {
        driver.quit();
    }
    
    protected static WebDriverWait getWait()
    {
        return new WebDriverWait(driver, WAIT_TIME, 100);
    }
}
