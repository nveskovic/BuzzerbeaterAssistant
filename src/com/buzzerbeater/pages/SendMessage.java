package com.buzzerbeater.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SendMessage extends Page{

	public SendMessage(WebDriver driver) {
		super(driver);
	}

	@FindBy(id = "ctl00_cphContent_tbSubject")
	WebElement subjectFiled;
	
	@FindBy(id = "ctl00_cphContent_tbMessage")
	WebElement messageArea;
	
	@FindBy(id = "ctl00_cphContent_btnSend")
	WebElement sendMessageButton;
	
	public void setSubject(String subject) {
		this.subjectFiled.clear();this.subjectFiled.sendKeys(subject);
	}
	
	public void setMessage(String message) {
		this.messageArea.clear();this.messageArea.sendKeys(message);
	}
	
	public SentMessage clickOnSendButton() {
		this.sendMessageButton.click();
		return PageFactory.initElements(driver, SentMessage.class);
	}
}
