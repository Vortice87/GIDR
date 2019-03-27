package com.neovia.gidr.pom;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.neovia.gidr.model.Incident;
import com.neovia.gidr.utils.WaitForPageToLoad;

public class NewIncident {

	private static final Logger LOGGER = LogManager.getLogger(NewIncident.class);

	private WebDriver driver;

	private static String baseURL = "https://spl.dhl.com";

	@FindBy(how = How.XPATH, using = "//*[@name=\"basicInfo:reason\"]/option[24]")
	WebElement reason;
	
	@FindBy(how = How.NAME, using = "basicInfo:description")
	WebElement description;
	
	@FindBy(how = How.XPATH, using = "//*[@name=\"basicInfo:resolver\"]option[4]")
	WebElement resolver;
	
	@FindBy(how = How.XPATH, using = "//*[@name=\"basicInfo:orderType\"]option[2]")
	WebElement orderType;
	
	@FindBy(how = How.XPATH, using = "//*[@name=\"basicInfo:fromWarehouse\"]option[516]")
	WebElement shippedByWarehouse;
	
	@FindBy(how = How.NAME, using = "basicInfo:orderRef")
	WebElement orderReference;
	
	@FindBy(how = How.NAME, using = "basicInfo:feName")
	WebElement fieldEngineerName;
	
	@FindBy(how = How.NAME, using = "goodsTransfer:partNumber")
	WebElement partNumber;
	
	@FindBy(how = How.NAME, using = "goodsTransfer:isGoodpart")
	WebElement isGoodpart;
	
	@FindBy(how = How.NAME, using = "actions:submitButton")
	WebElement save;
	

	public NewIncident(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public List<String> addIncident(List<Incident> incidenceList, Properties properties) {

		List<String> incidentList = new ArrayList<>();
		for (int i = 0; i < incidenceList.size(); i++) {

			String incident = "";
			WaitForPageToLoad.waitForLoad(driver);
			WebElement error404 = null;
			try {
				// CONTROLAMOS EL ERROR 404
				error404 = driver.findElement(By.xpath("//h1"));
				if (error404.getText().contains("Estado HTTP 404 - /GIDR/app/incident/")) {
					LOGGER.error(
							"A 404 error has occurred, redirecting to the page : https://spl.dhl.com/GIDR/app/incident/");
					driver.get("https://spl.dhl.com/GIDR/app/incident/");
					WaitForPageToLoad.waitForLoad(driver);
				}
			} catch (NoSuchElementException e) {
				// CONTINUAMOS CON EL PROCESO
			}

			try {
				WebElement logout = driver.findElement(By.xpath("//h2"));
				if (logout.getText().contains("Cierre") || logout.getText().contains("Logout")) {
					// NOS VOLVEMOS A LOGEAR
					LOGGER.error("The session has been closed. Returning to Login");
					driver.get(baseURL);
					LoginPage loginPage = new LoginPage(driver);
					loginPage.login(properties.getProperty("username"), properties.getProperty("password"));
					WaitForPageToLoad.waitForLoad(driver);
					driver.get("https://spl.dhl.com/GIDR/app/incident/");
					WaitForPageToLoad.waitForLoad(driver);
				}
			} catch (NoSuchElementException e) {
				// CONTINUAMOS CON EL PROCESO
			}

			try {
				WebElement login = driver.findElement(By.xpath("//h3"));
				if (login.getText().contains("Login") || login.getText().contains("")) {
					// NOS VOLVEMOS A LOGEAR
					LOGGER.error("The session has been closed. Returning to Login");
					driver.get(baseURL);
					LoginPage loginPage = new LoginPage(driver);
					loginPage.login(properties.getProperty("username"), properties.getProperty("password"));
					WaitForPageToLoad.waitForLoad(driver);
					driver.get("https://spl.dhl.com/GIDR/app/incident/");
					WaitForPageToLoad.waitForLoad(driver);
				}
			} catch (NoSuchElementException e) {
				// CONTINUAMOS CON EL PROCESO
			}

			try {
				// CONTROLAMOS EL ERROR 404
				error404 = driver.findElement(By.xpath("//h1"));
				if (error404.getText().contains("Estado HTTP 404 - /GIDR/app/incident")) {
					LOGGER.error(
							"A 404 error has occurred, redirecting to the page : https://spl.dhl.com/GIDR/app/incident/");
					driver.get("https://spl.dhl.com/GIDR/app/incident/");
					WaitForPageToLoad.waitForLoad(driver);
				}
			} catch (NoSuchElementException e) {
				// CONTINUAMOS CON EL PROCESO
			}

			LOGGER.info("Processing GIDR with RMA " + incidenceList.get(i).getRma());

			// 1. SELECT REASON
			clickOnElement(reason);
			// 2. DESCRIPTION
			sendText(description, "Without RMA");
			// 3. SELECT RESOLVER
			clickOnElement(resolver);
			// 4. SELECT ORDER TYPE
			clickOnElement(orderType);
			// 5. SELECT SHIPPED BY WAREHOUSE
			clickOnElement(shippedByWarehouse);
			// 6. SEND ORDER REFERENCE
			sendText(orderReference, incidenceList.get(i).getRma());
			// 7. SEND FIELD ENGINEER NAME
			sendText(fieldEngineerName, incidenceList.get(i).getOrigin());
			// 8. SEND PART NUMBER	
			sendText(partNumber, incidenceList.get(i).getPartNumber());
			// 9. UNCHECKED ISGOODPART
			clickOnElement(isGoodpart);
			// 10. SAVE INCIDENT
			clickOnElement(save);

			//TODO: RECOGER EL NUMERO DE INCIDENCIA GENERADO
			
			incidentList.add(incident);
			LOGGER.info("Incident " + incident + " has been created.");

			// VOLVEMOS A NUEVA INCIDENCIA
			driver.get("https://spl.dhl.com/GIDR/app/incident/");

		}
		return incidentList;
	}

	private void clickOnElement(WebElement element) {
		element.click();
	}

	private void sendText(WebElement element, String text) {
		element.clear();
		element.sendKeys(text);
	}

}
