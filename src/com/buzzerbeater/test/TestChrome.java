package com.buzzerbeater.test;

import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.buzzerbeater.utils.OsUtils;

public class TestChrome {

	public static void main(String[] args) {
		
		System.setProperty("webdriver.http.factory", "apache");
		
		String currentDir = System.getProperty("user.dir");
		String driverPath = Paths.get(currentDir,"/data/drivers").toString();
		String driverPathSuffix = (OsUtils.getOsType() == OsUtils.OsType.WINDOWS ? ".exe" : "");
		driverPath = Paths.get(driverPath, "chromedriver"+driverPathSuffix).toString();
        
        System.setProperty("webdriver.chrome.driver", driverPath);
        
		WebDriver driver = null;
		
		ChromeOptions chromeOptions = new ChromeOptions();


        try {
        	driver = new ChromeDriver(chromeOptions);
        } catch (Exception e) {
        	e.printStackTrace();
        	e.getMessage();
        }
        driver.get("http://www.google.com");
	}
}
