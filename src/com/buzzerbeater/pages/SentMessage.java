package com.buzzerbeater.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SentMessage extends Page{
	
	@FindBy(id = "stamp")
	WebElement messageStamp;

	public SentMessage(WebDriver driver) {
		super(driver);
	}

	public boolean isDisplayed() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			return messageStamp.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}
}
