package com.buzzerbeater.utils;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverInitializationHelper {

	public static WebDriver initializeDriver(boolean visibleBrowser, boolean enableJavascript, BrowserType bt) {

		/*
		 * Due to problem described here https://github.com/SeleniumHQ/selenium/issues/5453
		 * Setting http.factory to apache
		 */
		//System.setProperty("webdriver.http.factory", "apache");

		WebDriver driver = null;

		String currentDir = System.getProperty("user.dir");
		String driverPath = Paths.get(currentDir,"/data/drivers").toString();
		String driverPathSuffix = (OsUtils.getOsType() == OsUtils.OsType.WINDOWS ? ".exe" : "");

		switch(bt) {

		case FIREFOX : {
			driverPath = Paths.get(driverPath, "geckodriver"+driverPathSuffix).toString();
			System.setProperty("webdriver.gecko.driver",driverPath);

			if(!visibleBrowser){
				FirefoxBinary firefoxBinary = new FirefoxBinary();
				firefoxBinary.addCommandLineOptions("--headless");
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setBinary(firefoxBinary);
				driver = new FirefoxDriver(firefoxOptions);
			} else {
				driver = new FirefoxDriver();
			}
		} break;

		default : {
			driverPath = Paths.get(driverPath, "chromedriver"+driverPathSuffix).toString();

			System.setProperty("webdriver.chrome.driver", driverPath);
			ChromeOptions chromeOptions = new ChromeOptions();
			if(!visibleBrowser){
				chromeOptions.addArguments("--headless"); // not visible == headless
			}

			driver = new ChromeDriver(chromeOptions);
			
		}
		}

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		return driver;
	}

	public static WebDriver initializeDriver(boolean visibleBrowser, BrowserType bt) {
		return initializeDriver(visibleBrowser, false, bt);
	}
}
