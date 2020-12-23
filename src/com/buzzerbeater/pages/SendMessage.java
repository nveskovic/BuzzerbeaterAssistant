package com.buzzerbeater.pages;

import com.buzzerbeater.utils.Sleeper;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class SendMessage extends Page{

	public SendMessage(WebDriver driver) {
		super(driver);
	}

	@FindBy(id = "cphContent_tbSubject")
	WebElement subjectFiled;
	
	@FindBy(id = "cphContent_tbMessage")
	WebElement messageArea;
	
	@FindBy(id = "cphContent_btnSend")
	WebElement sendMessageButton;
	
	public void setSubject(String subject) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofMillis(500))
				.ignoring(NoSuchElementException.class);

		wait.until(ExpectedConditions.elementToBeClickable(subjectFiled));
		for(int i=0; i<=5; i++) {
			this.subjectFiled.clear();
			this.subjectFiled.sendKeys(subject);

			if(subjectFiled.getText().equals(subject)) {
				break;
			} else {
				Sleeper.sleepTightInMillis(500);
			}
		}
	}
	
	public void setMessage(String message) {
		this.messageArea.clear();this.messageArea.sendKeys(message);
	}
	
	public SentMessage clickOnSendButton() {
		this.sendMessageButton.click();
		return PageFactory.initElements(driver, SentMessage.class);
	}
}
