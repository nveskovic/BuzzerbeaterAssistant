package com.buzzerbeater.test;

import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.buzzerbeater.utils.OsUtils;

public class TestFirefox {
	
	public static void main(String[] args) {
	
		System.setProperty("webdriver.http.factory", "apache");
		
		String currentDir = System.getProperty("user.dir");
		String driverPath = Paths.get(currentDir,"/data/drivers").toString();
		String driverPathSuffix = (OsUtils.getOsType() == OsUtils.OsType.WINDOWS ? ".exe" : "");
		driverPath = Paths.get(driverPath, "geckodriver"+driverPathSuffix).toString();
		System.setProperty("webdriver.gecko.driver",driverPath);
		WebDriver driver = null;
		
		try {
        	driver = new FirefoxDriver();
        } catch (Exception e) {
        	e.printStackTrace();
        	e.getMessage();
        }
        driver.get("http://www.google.com");
	}
}
