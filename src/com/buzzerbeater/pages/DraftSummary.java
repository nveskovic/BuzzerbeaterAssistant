package com.buzzerbeater.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DraftSummary extends Page{

	public DraftSummary(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(id = "ctl00_cphContent_draft")
	private WebElement contentDraft;
	
	public List<String> getAllPlayersFromDraft() {
		List<WebElement> playersWebElements = contentDraft.findElements(By.xpath("table/tbody/tr/td[1]/a"));
		ArrayList<String> playerUrls = new ArrayList<String>();
		for (WebElement we : playersWebElements) {
			playerUrls.add(we.getAttribute("href"));
		}
		return playerUrls;
	}

}
