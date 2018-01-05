package com.buzzerbeater.pages;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class Staff  extends Page {

	@FindBy (id = "ctl00_cphContent_btnBid")
	WebElement bidForStaffButton;
	
	@FindBy (xpath = "//div[@id='subcontent']/div/div[@class='boxcontent']//a[contains(@href, 'team')]")
	WebElement bidCurrentTeamLink;
	
	@FindBy(id = "ctl00_cphContent_txtBidAmount")
	private WebElement bidNewOfferField;


	public Staff(WebDriver driver) {
		super(driver);
	}

	public boolean isTransferListed() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			return bidForStaffButton.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}

	public Date getAuctionEndsAtDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentBidTeamID() {
		// http://www.buzzerbeater.com/team/100438/overview.aspx
		String teamUrl;
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			teamUrl = this.bidCurrentTeamLink.getAttribute("href");
		} catch (NoSuchElementException nsee) {
			return ""; // no active bids => current bid team is empty
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		String toReturn = "";
		if(teamUrl.matches(".+/team/[0-9]+/overview\\.aspx")) {
			try {
				toReturn = teamUrl.split("/team/")[1].replaceAll("/overview.aspx", "");
			}catch(Exception e){}
		}
		return toReturn;
	}

	public String getNextBidAmount() {
		return this.bidNewOfferField.getAttribute("value").replaceAll(" ", "");
	}

	public Staff clickOnBidButton() {
		this.bidForStaffButton.click();
		return PageFactory.initElements(driver, Staff.class);
	}

}
