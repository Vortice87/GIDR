package com.neovia.gidr.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitForPageToLoad {
	
    private static final Logger LOGGER = LogManager.getLogger(WaitForPageToLoad.class);
	
      // METODO QUE ESPERA A QUE SE CARGUE LA PAGINA(ESTE LISTA)
	  public static void waitForLoad(WebDriver driver) {
	        ExpectedCondition<Boolean> pageLoadCondition = new
	                ExpectedCondition<Boolean>() {
	                    public Boolean apply(WebDriver driver) {
	                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
	                    }
	                };
	        WebDriverWait wait = new WebDriverWait(driver, 30);
	        wait.until(pageLoadCondition);
	    }
	  
	    // METODO QUE ESPERA UN TIEMPO A QUE SE CARGUE LA PAGINA
	    public static void waitForAlert(WebDriver driver,int time)
	    {
	    	   int i=0;
	    	   while(i++<5)
	    	   {
	    	        try
	    	        {
	    	            Alert alert = driver.switchTo().alert();
	    	            break;
	    	        }
	    	        catch(NoAlertPresentException e)
	    	        {
	    	          try {
						Thread.sleep(time);
					} catch (InterruptedException e1) {
						LOGGER.error(e1.getMessage());
					}
	    	          continue;
	    	        }
	    	   }
	    	}
	    
	    // METODO QUE PREGUNTA SI EL ELEMENTO SPINNER DE CARGA ES VISIBLE
	    public static boolean isElementDisplayed(WebElement element, WebDriver driver) {
	        try {
	            WebDriverWait wait = new WebDriverWait(driver, 2);
	            wait.until(ExpectedConditions.visibilityOf(element));
	            return element.isDisplayed();
	        } catch (org.openqa.selenium.NoSuchElementException
	                | org.openqa.selenium.StaleElementReferenceException
	                | org.openqa.selenium.TimeoutException e) {
	            return false;
	        }
	    }
	    
	    // METODO QUE ESPERA A QUE EL ELEMENTO SPINNER DE CARGA DESAPAREZCA
	    public static void waitForElementToBeGone(WebElement element, int timeout, WebDriver driver) {
	        if (isElementDisplayed(element, driver)) {
	            new WebDriverWait(driver, timeout).until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
	        }
	    }

}
