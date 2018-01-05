package com.buzzerbeater.utils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverInitializationHelper {

	public static WebDriver initializeDriver(boolean visibleBrowser, boolean enableJavascript) {
		
		WebDriver driver = null;
		
		if(visibleBrowser){
			//driver = new FirefoxDriver();
			
			boolean is64bit = false;
			if (System.getProperty("os.name").contains("Windows")) {
			    is64bit = (System.getenv("ProgramFiles(x86)") != null);
			} else {
			    is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
			}
			
			String currentDir = System.getProperty("user.dir");
	        String marionetteDriverLocation = currentDir + 
	        		"/data/drivers/geckodriver_" + (is64bit ? "x64" : "x86") + ".exe";
	        System.setProperty("webdriver.gecko.driver", marionetteDriverLocation);
	        driver = new MarionetteDriver();
		} else {
			if (System.getProperty("os.name").contains("Windows") && enableJavascript){
				String currentDir = System.getProperty("user.dir");
		        String phantomJsPath = currentDir + 
		        		"/data/drivers/phantomjs.exe";
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setJavascriptEnabled(enableJavascript);
				caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJsPath);
				
				driver = new PhantomJSDriver(caps);
			} else { // no js needed - we can use htmlunit
			
				driver = new HtmlUnitDriver(false);
				//java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
				//java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
			}
		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		return driver;
	}

	public static WebDriver initializeDriver(boolean visibleBrowser) {
		return initializeDriver(visibleBrowser, false);
	}
}
