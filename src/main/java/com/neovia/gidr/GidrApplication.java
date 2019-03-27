package com.neovia.gidr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.neovia.gidr.model.Incident;
import com.neovia.gidr.pom.Home;
import com.neovia.gidr.pom.IncidentList;
import com.neovia.gidr.pom.LoginPage;
import com.neovia.gidr.pom.NewIncident;
import com.neovia.gidr.utils.ReadExcel;
import com.neovia.gidr.utils.WaitForPageToLoad;




public class GidrApplication {

	private static final Logger LOGGER = LogManager.getLogger(GidrApplication.class);

	private static String PATHDRIVER = "drivers/";

	private static String baseURL = "https://spl.dhl.com";

	static WebDriver driver;

	public static Properties properties;

	public static void main(String[] args) {
		setup();
		signUp();
	}

	public static void setup() {

		System.setProperty("webdriver.chrome.driver", PATHDRIVER + "chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.addArguments("--incognito");
		driver = new ChromeDriver(options);

		properties = new Properties();
		File config = new File("config.properties");
		// CARGAMOS EL PROPERTIES
		if (config.exists()) {
			try (InputStream ins = new FileInputStream(config)) {
				properties.load(ins);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		} else {
			LOGGER.error("The file does not exist in the specified path");
			System.exit(1);
		}
		// COMPROBAMOS QUE EL CAMPO CONFIGURABLE time.loading.order ES NUMERICO
		try {
			int time = Integer.parseInt(properties.getProperty("time.loading.order"));
		} catch (NumberFormatException e) {
			LOGGER.error("Number format exception. " + properties.getProperty("time.loading.order") + " not a valid format. It must be numeric, from 2500.");
			System.exit(2);
		}

	}

	public static void signUp() {

    	LOGGER.info("--- [ INITIALIZING EXECUTION LOG CREATE GIDR PROCESS ] ---");
		driver.get(baseURL);
		// CREAMOS LOS OBJETOS DE LAS CLASES PAGE
		LoginPage loginPage = new LoginPage(driver);
		Home home = new Home(driver);
		NewIncident newIncident = new NewIncident(driver);
		IncidentList listToClose = new IncidentList(driver);
		// NOS LOGEAMOS
		LOGGER.info("Initializing access to the Login page");
		loginPage.login(properties.getProperty("username"), properties.getProperty("password"));
		// NAVEGAMOS
		LOGGER.info("Navigating to new Indicent");
		home.navigateToNewIncident();
		// LEEMOS EL FICHERO EXCEL
		LOGGER.info("Reading the excel file from the path " + properties.getProperty("excel.path"));
		ReadExcel lecture = new ReadExcel();
		List<Incident> incidentList = lecture.getIncidentList(properties.getProperty("excel.path"));
		WaitForPageToLoad.waitForLoad(driver);
		// DEVOLVEMOS TODA LA LISTA DE PRODUCTOS
		LOGGER.info("Preparing the incidents list");
		List<String> incidentIds = newIncident.addIncident(incidentList, properties);
		if (incidentIds.size() != 0) {
			// PASAMOS A CERRAR LAS INCIDENCIAS
			LOGGER.info("Navigating to Open Existing Incident");
			home.navigateToOpenExistingIncident();
			listToClose.closeIncidents(incidentIds);
		} else {
			LOGGER.warn("There are no incidents to close.");
		}

		// ELIMINANDO FICHERO A CARPETA REMOVED
		File excel = new File(properties.getProperty("excel.path"));
		if (excel.exists()) {
			Date today = new Date();
			long timeMilli = today.getTime();
			try {
				Files.copy(Paths.get(properties.getProperty("excel.path")),
						Paths.get(properties.getProperty("removed.path") + timeMilli + "_completed.xlsx"),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}

    	LOGGER.info("--- [ FINISHED EXECUTION LOG CREATE GIDR PROCESS ] ---");
		driver.close();
	}

}
