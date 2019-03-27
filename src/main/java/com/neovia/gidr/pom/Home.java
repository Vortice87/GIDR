package com.neovia.gidr.pom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.neovia.gidr.utils.WaitForPageToLoad;


public class Home {
	
	private WebDriver driver;
	
	private static final Logger LOGGER = LogManager.getLogger(Home.class);
	
	@FindBy(how = How.XPATH, using = "//*[@id=\"nav-bar\"]/span[4]/a")
	WebElement gidr;
	    
    public Home(WebDriver driver) {
    	this.driver = driver;
    	PageFactory.initElements(driver, this);
    }
    
    public void navigateToNewIncident() {
    	clickOnElement(gidr);
    	WaitForPageToLoad.waitForLoad(driver);
    	WebElement newIncident = driver.findElement(By.xpath("//*[@id=\"dynContent\"]/div/div[3]/div[3]/a[1]"));
    	// driver.get("https://spl.dhl.com/GIDR/app/incident/");
     	clickOnElement(newIncident);
//    	try {
//        	// CONTROLAMOS EL ERROR 404
//        	WebElement error404 = driver.findElement(By.xpath("//h1"));
//            if(error404.getText().contains("Estado HTTP 404 - /xdock/app/returnorder")) {
//				LOGGER.error("A 404 error has occurred, redirecting to the page : https://spl.dhl.com/xdock/app/returnorder/");
//            	driver.get("https://spl.dhl.com/xdock/app/returnorder/");
//            	WaitForPageToLoad.waitForAlert(driver,500);
//            }
//		} catch (NoSuchElementException e) {
//			// CONTINUAMOS CON EL PROCESO
//		}
    }
    
    public void navigateToOpenExistingIncident() {
    	clickOnElement(gidr);
    	WaitForPageToLoad.waitForLoad(driver);
    	WebElement openExistingIncident = driver.findElement(By.xpath("//*[@id=\"dynContent\"]/div/div[3]/div[3]/a[2]"));
     	clickOnElement(openExistingIncident);
    }
    
    public void clickOnElement(WebElement element){
        element.click();
    }

}
