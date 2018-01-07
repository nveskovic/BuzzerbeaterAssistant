package com.buzzerbeater.utils;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverInitializationHelper {

	public static WebDriver initializeDriver(boolean visibleBrowser, boolean enableJavascript) {
		
		WebDriver driver = null;
		
		if(visibleBrowser){
			
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
	        driver = new FirefoxDriver();
		} else {
			
			String currentDir = System.getProperty("user.dir");
			String phantomJsPath = Paths.get(currentDir,"/data/drivers").toString();
            if (OsUtils.getOsType() == OsUtils.OsType.WINDOWS) {
            	phantomJsPath = Paths.get(phantomJsPath, "phantomjs.exe").toString();
            } else {
            	phantomJsPath = Paths.get(phantomJsPath, "phantomjs").toString();
            }
			
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(enableJavascript);
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJsPath);
			
			driver = new PhantomJSDriver(caps);
		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		return driver;
	}

	public static WebDriver initializeDriver(boolean visibleBrowser) {
		return initializeDriver(visibleBrowser, false);
	}
}
