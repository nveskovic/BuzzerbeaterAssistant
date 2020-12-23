package com.buzzerbeater.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Page {
	
	protected WebDriver driver;
	protected String title;
	protected static String baseUrl = "https://www2.buzzerbeater.com";
	protected static String username;
	protected static String password;
	
	public Page(WebDriver driver) {
		this.driver = driver;
	}
	
	public boolean isLoggedIn(int timeout){
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		try{
			return driver.findElement(By.className("menuList")).isDisplayed();
		} catch(Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}
	
	public String getTeamID() {
		String id = "";
		try {
			String href = driver.findElement(
					By.id("menuTeamName")).getAttribute("href");
			// parse href
			Pattern p = Pattern.compile(".*team/([0-9]+?)/overview.*");
			Matcher m = p.matcher(href);
			if(m.matches())
				id = m.group(1);
			
		} catch (Exception e){}
		return id;
	}
	
	

	public String getCountryID() {
		String id = "";
		try {
			
			List<WebElement> countryLinks = driver.findElements(
					By.xpath("//*[@id='mainContent']//a[contains(@href, 'country/')]"));
			
			if(countryLinks.size() == 0) { // if empty, try menu on the left
				// try with other div
				countryLinks = driver.findElements(
						By.xpath("//*[@id='menuBox']//a[contains(@href, 'country/')]"));
			}
		
			String href = countryLinks.get(0).getAttribute("href");
			
			// parse href
			Pattern p = Pattern.compile(".*country/([0-9]+?)/.*");
			Matcher m = p.matcher(href);
			if(m.matches())
				id = m.group(1);
			
		} catch (Exception e){}
		return id;
	}
}
