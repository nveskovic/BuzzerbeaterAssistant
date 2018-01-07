package com.buzzerbeater.utils;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverInitializationHelper {

	public static WebDriver initializeDriver(boolean visibleBrowser, boolean enableJavascript) {
		
		WebDriver driver = null;

		String currentDir = System.getProperty("user.dir");
		String driverPath = Paths.get(currentDir,"/data/drivers").toString();
        if (OsUtils.getOsType() == OsUtils.OsType.WINDOWS) {
        	driverPath = Paths.get(driverPath, "chromedriver.exe").toString();
        } else {
        	driverPath = Paths.get(driverPath, "chromedriver").toString();
        }
        
        System.setProperty("webdriver.chrome.driver", driverPath);
        ChromeOptions chromeOptions = new ChromeOptions();
        if(!visibleBrowser){
        	chromeOptions.addArguments("--headless");
        }

        driver = new ChromeDriver(chromeOptions);
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		return driver;
	}

	public static WebDriver initializeDriver(boolean visibleBrowser) {
		return initializeDriver(visibleBrowser, false);
	}
}
