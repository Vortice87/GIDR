package com.neovia.gidr.pom;

import java.util.List;

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

import com.neovia.gidr.utils.WaitForPageToLoad;

public class IncidentList {

	private static final Logger LOGGER = LogManager.getLogger(IncidentList.class);

	private WebDriver driver;

	@FindBy(how = How.NAME, using = "//*[@name=\"filters\"]/option[4]")
	WebElement selectFilter;

	@FindBy(how = How.XPATH, using = "//*[@id=\"dynContent\"]/div/div[3]/table/tbody/tr[1]/td[1]/div[1]/a[1]")
	WebElement filter;

	public IncidentList(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void closeIncidents(List<String> incidentList) {

		WaitForPageToLoad.waitForLoad(driver);
		LOGGER.info("Closing incidents...");

		for (int i = 0; i < incidentList.size(); i++) {
			// CLICKEAMOS SOBRE EL FILTRO OPEN GIDRS - PENDING ACTION CATERPILLAR
			clickOnElement(selectFilter);
			WaitForPageToLoad.waitForLoad(driver);
			// CLICKEAMOS SBORE EL FILTRO DE BUSQUEDA POR ID
			clickOnElement(filter);
			// INTRODUCIMOS EL NUMERO DE ID DE INCIDENCIA Y PULSAMOS ENTER
			WebElement filterById = driver
					.findElement(By.name("tabs:panel:form:idExpression:singleTextFieldContainer:singleTextField"));
			sendText(filterById, incidentList.get(i));
			WaitForPageToLoad.waitForLoad(driver);
			// SELECCIONAMOS LA OPCION CLOSED DE INCIDENT STATUS
			WebElement incidentStatus = driver.findElement(By.xpath("//*[@name=\"basicInfo:status\"]/option[1]"));
			clickOnElement(incidentStatus);
			WaitForPageToLoad.waitForLoad(driver);
			// SELECCIONAMOS LA OPCION GR49 – SHIPMENT / PART RECEIVED
			WebElement closureReason = driver
					.findElement(By.xpath("//*[@name=\"basicInfo:closureReasonContainer:closureReason\"]/option[30]"));
			clickOnElement(closureReason);
			// GUARDAMOS LA MODIFICACION DE LA INCIDENCIA
			WebElement save = driver.findElement(By.name("actions:submitButton"));
			clickOnElement(save);
			LOGGER.info("Incident " + incidentList.get(i) + " closed.");
			driver.get("https://spl.dhl.com/GIDR/app/incident/");
			WaitForPageToLoad.waitForLoad(driver);
		}
		LOGGER.info("Finished close incidents process");
	}

	private void clickOnElement(WebElement element) {
		element.click();
	}

	private void sendText(WebElement element, String text) {
		element.clear();
		element.sendKeys(text);
		element.sendKeys(Keys.ENTER);
	}

}
