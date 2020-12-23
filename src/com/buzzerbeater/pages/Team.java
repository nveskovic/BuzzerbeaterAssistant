package com.buzzerbeater.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Team extends Page{

	public Team(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//*[@id='teammenu']//img[contains(@src, 'sendmail')]")
	WebElement sendMessageIcon;

	@FindBy(id = "cphContent_SubMenu_Label3")
	WebElement sendMessageLink;
	
	@FindBy(id = "cphContent_ltlCountryName")
	WebElement countryLink;
	
	public SendMessage clickOnSendMessageIcon() {
		sendMessageIcon.click();
		return PageFactory.initElements(driver, SendMessage.class);
	}

	public SendMessage clickOnSendMessageLink() {
		sendMessageLink.click();
		return PageFactory.initElements(driver, SendMessage.class);
	}
	
	public boolean isDomesticTeam(String domesticCountryID) {
		String href = countryLink.getAttribute("href");
		
		// if utopia, get another country
		if(href.equals(baseUrl+"/country/99/overview.aspx")) {
			try {
				href = driver.findElement(By.id("ctl00_cphContent_link2ndCountryName")).getAttribute("href");
			} catch(Exception e){
				// if exception caught while searching for another country, assume its a foreign team
				return false;
			}
		}
		return href.equals(baseUrl+"/country/" + domesticCountryID + "/overview.aspx");
	}
	
	public boolean isHuman() {
		String parentTag = sendMessageIcon.findElement(By.xpath("..")).getTagName();
		if(parentTag.equals("a"))
			return true;
		else
			return false;
	}
}
