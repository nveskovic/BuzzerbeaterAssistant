package com.buzzerbeater.pages;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.buzzerbeater.utils.DateTimeHelper;
import com.buzzerbeater.utils.Sleeper;

public class Player extends Page{

	public Player(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//div[@id='titlebar']//td/h1")
	private WebElement playerNameHeader;

	@FindBy(id = "ctl00_cphContent_teamName")
	private WebElement ownerLink;

	@FindBy(id = "ctl00_cphContent_btnBid")
	private WebElement bidForPlayerButton;

	@FindBy(id = "ctl00_cphContent_tbOffer")
	private WebElement bidNewOfferField;

	@FindBy(id = "ctl00_cphContent_linkBid")
	private WebElement bidCurrentTeamLink;
	
	@FindBy(id = "ctl00_cphContent_bidsDiv")
	private WebElement bidsDiv;
	
	@FindBy(id = "ctl00_cphContent_ltlAuctionEnds")
	private WebElement auctionEndsAtText;

	@FindBy(id = "ctl00_ltlFailure")
	private WebElement errorMassage;
	
	
	// -- NT SECTION --
	
	/*@FindBy(id = "ctl00_cphContent_btnNTRecruitOrDismiss")
	private WebElement recruitOrDissmisNTButton;*/
	
	// recruit
	//@FindBy(id = "ctl00_cphContent_btnNTRecruit")
	@FindBy(id = "ctl00_cphContent_btnNTRecruitPopup2")
	private WebElement recruitToNTButton;
	
	@FindBy(id = "ctl00_cphContent_btnRecruitYes2")
	private WebElement recruitToNTButtonConfirm;
	
	// dismiss 
	// TODO: move this id to some configuration file
	private final String dissmissFromNTButtonByID = "ctl00_cphContent_btnNTDismissPopup2";
	@FindBy(id = dissmissFromNTButtonByID)
	private WebElement dissmissFromNTButton;
	
	// TODO: move this id to some configuration file
	private final String dismissFromNTPanelConfirmationID = "ctl00_cphContent_pnlDismissNT2";
	@FindBy(id = dismissFromNTPanelConfirmationID)
	private WebElement dismissFromNTPanelConfirmation;
	
	// TODO: move this id to some configuration file
	private final String dissmissFromNTPopupConfirmationID = "cbDismissPopup2";
	@FindBy(id = dissmissFromNTPopupConfirmationID)
	private WebElement dissmissFromNTPopupConfirmation;
	
	// TODO: move this id to some configuration file
	private final String dissmissFromNTButtonConfirmationYesByID = "ctl00_cphContent_btnDismissYes2";
	@FindBy(id = dissmissFromNTButtonConfirmationYesByID)
	private WebElement dissmissFromNTButtonConfirmationYes;
	
	@FindBy(id = "ctl00_cphContent_lblNT")
	private WebElement ntStatusTextForNTManager;
	
	@FindBy(id = "ctl00_cphContent_lblNotYourNT")
	private WebElement ntStatusTextNotYourNT;
	
	// ----------------
	
	// general info
	@FindBy (id = "playerPersonalInfo")
	private WebElement playerPersonalInfo;
	
	@FindBy (id = "ctl00_cphContent_potential_linkDen")
	private WebElement potentialLink;
	
	// player skills
	@FindBy(id = "ctl00_cphContent_skillTable_sdJumpShot_linkDen")
	private WebElement skillJSlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdJumpRange_linkDen")
	private WebElement skillJRlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdperimDef_linkDen")
	private WebElement skillODlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdhandling_linkDen")
	private WebElement skillHAlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sddriving_linkDen")
	private WebElement skillDRlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdpassing_linkDen")
	private WebElement skillPAlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdinsideShot_linkDen")
	private WebElement skillISlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdinsideDef_linkDen")
	private WebElement skillIDlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdrebound_linkDen")
	private WebElement skillRBlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdshotBlock_linkDen")
	private WebElement skillSBlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdstamina_linkDen")
	private WebElement skillSTlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdfreeThrow_linkDen")
	private WebElement skillFTlink;
	@FindBy(id = "ctl00_cphContent_skillTable_sdexperience_linkDen")
	private WebElement skillEXlink;

	public String getPlayerName() {
		return playerNameHeader.getText();
	}

	public Team clickOnOwnerLink() {
		ownerLink.click();
		return PageFactory.initElements(driver, Team.class);
	}

	public boolean isOwnerRetired() {
		return ownerLink.getAttribute("href").equals(baseUrl+"/manage/transferlist.aspx");
	}

	public boolean isTransferListed() {
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		try {
			return bidForPlayerButton.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}

	public Date getAuctionEndsAtDate() throws ParseException {
		String text = auctionEndsAtText.getText();
		// text is: 11/30/2014 3:13:24 PM
		// format is: MM/dd/YYYY h:mm:ss a
		return DateTimeHelper.getDateFromString(text, "MM/dd/yyyy hh:mm:ss a");
	}

	// TODO: update to use matcher instead of split
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

	public Player clickOnBidButton() {
		this.bidForPlayerButton.click();
		return PageFactory.initElements(driver, Player.class);
	}

	public boolean isErrorMessageDisplayed() {
		try {
			return this.errorMassage.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	} 

	public String getErrorMessageText() {
		return this.errorMassage.getText();
	}

	public String getCurrentBidAmount() {
		return this.bidsDiv.findElement(By.tagName("b")).getText().replaceAll("[ \\$]", "");
	}
	
	public boolean arePlayerSkillsVisible() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			return skillJSlink.isDisplayed();
		} catch(NoSuchElementException nse) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}

	/*public boolean isRecruitOrDismissButtonDisplayed() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			return recruitOrDissmisNTButton.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}*/
	

	public boolean isRecruitToNTButtonDisplayed() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			return this.recruitToNTButton.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}
	
	public boolean isDismissFromNTButtonDisplayed() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			return this.dissmissFromNTButton.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}
	
	public boolean isConfirmInviteToNTButtonDisplayed() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			return this.recruitToNTButtonConfirm.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}
	
	public boolean isDismissFromNTConfirmationYesButtonDisplayed() {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			return this.dissmissFromNTButtonConfirmationYes.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}
	
	public boolean isDissmissFromNTPopupConfirmation(){
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			return this.dissmissFromNTPopupConfirmation.isDisplayed();
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
	}

	/*public Player clickOnRecruitOrDissmissButton() {
		this.recruitOrDissmisNTButton.click();
		return PageFactory.initElements(driver, Player.class);
	}*/
	
	public void clickOnRecruitToNTButton() {
		this.recruitToNTButton.click();
		//return PageFactory.initElements(driver, Player.class);
	}
	
	public Player clickOnRecruitToNTButtonConfirm() {
		for(int i=0;i<10;i++) {
			if(!isConfirmInviteToNTButtonDisplayed()) {
				Sleeper.sleepTightInMillis(200);
				System.out.println("invite to NT confirm button is not displayed ... ");
			} else {
				this.recruitToNTButtonConfirm.click();
				break;
			}
		}
		
		return PageFactory.initElements(driver, Player.class);
	}
	
	public Player dismissFromNT() {
		System.out.println("\n----\nbefore click: playerPage.isDismissFromNTButtonDisplayed(): " 
				+ this.isDismissFromNTButtonDisplayed());
		System.out.println("before click: playerPage.isDissmissFromNTPopupConfirmation(): " 
				+ this.isDissmissFromNTPopupConfirmation());
		
		/*WebElement element1 = driver.findElement(By.id(dissmissFromNTButtonByID));
		JavascriptExecutor executor = (JavascriptExecutor)driver;
		executor.executeScript("arguments[0].click();", element1);*/
		
		WebElement dismissButton = driver.findElement(By.id(dissmissFromNTButtonByID));
		System.out.println("dismissButton is displayed before clicking: " + dismissButton.isDisplayed());
		System.out.println("dismissButton is enabled before clicking: " + dismissButton.isEnabled());
		System.out.println("dismissButton click");
		
		dismissButton.click();
		
		System.out.println("dismissButton is displayed after clicking: " + dismissButton.isDisplayed());
		System.out.println("dismissButton is enabled after clicking: " + dismissButton.isEnabled());
		
		WebElement yesButton = null, dismissPanel = null;		
		
		for(int i=0;i<5;i++) {
			try {
				Thread.sleep(300);
				
				yesButton = driver.findElement(By.id(dissmissFromNTButtonConfirmationYesByID));
				dismissPanel = driver.findElement(By.id(dismissFromNTPanelConfirmationID));
				
				if(!dismissPanel.isDisplayed()) {
					System.out.println("waiting another 300ms for panel to become displayed");
					continue;
				} else {
					break;
				}
			} catch(Exception ex){ex.printStackTrace();}
		}
				
		System.out.println("dismissPanel is displayed: " + dismissPanel.isDisplayed());
		System.out.println("dismissPanel is enabled: " + dismissPanel.isEnabled());
		System.out.println("dismissPanel attribute style: " + dismissPanel.getAttribute("style"));
		System.out.println("\nyesButton is displayed: " + yesButton.isDisplayed());
		System.out.println("yesButton is enabled: " + yesButton.isEnabled());
		System.out.println("yesButton attribute style: " + yesButton.getAttribute("style"));
				
		/* <div id="ctl00_cphContent_pnlDismissNT" 
		 * style="display: block; position: fixed; z-index: 100001; 
		 * top: 33.0149%; left: 34.2447%; margin: 0px;">
		 */
		// set panel to be visible
		if(!dismissPanel.isDisplayed()) {
			System.out.println("using javascriptexecutor to set panel to visible");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("document.getElementById('" + dismissFromNTPanelConfirmationID + "')"
					+ ".setAttribute('style', 'display: block;')");
			
			try {
				Thread.sleep(500);
			} catch (Exception e) {e.printStackTrace();}

			System.out.println("dismissPanel is displayed: " + dismissPanel.isDisplayed());
			System.out.println("dismissPanel is enabled: " + dismissPanel.isEnabled());
			System.out.println("dismissPanel attribute style: " + dismissPanel.getAttribute("style"));
			System.out.println("yesButton is displayed: " + yesButton.isDisplayed());
			System.out.println("yesButton is enabled: " + yesButton.isEnabled());
			System.out.println("yesButton attribute style: " + yesButton.getAttribute("style"));
		}
		
		
		// refresh the elements
		
		System.out.println("\nyes button click");
		yesButton.click();
		
		return PageFactory.initElements(driver, Player.class);
	}

	public com.buzzerbeater.model.Player getPlayerInfo() {
		HashMap<String, String> personalInfo = this.getPersonalInfo();
		return new com.buzzerbeater.model.Player(
				Long.parseLong(this.getPlayerID()), 
				this.getPlayerName(),
				(this.isOwnerRetired() ? "RETIRED" : ""),
				personalInfo.get("age"), 
				personalInfo.get("dmi"),
				personalInfo.get("salary"),
				personalInfo.get("height"),
				this.getPotentialValue(), 
				this.getPlayerSkills());
		
	}

	private HashMap<String, String> getPersonalInfo() {
		HashMap<String, String> toReturn = new HashMap<String, String>();
		// add initial empty values
		toReturn.put("age", "");
		toReturn.put("height", "");
		toReturn.put("salary", "");
		toReturn.put("dmi", "");
		
		try { // try because this is parsing the text and that is unpredictable
			String[] personalInfo = playerPersonalInfo.getText().split("\n");
			for(int i=0; i<personalInfo.length; i++) {
				if(personalInfo[i].contains("/")) {
					// this should be the line with height: 6'3" / 190 cm
					toReturn.put("height",
							personalInfo[i].split("/")[1].trim() // take '190 cm'
							.split(" ")[0].trim()); // take '190'
					
					// age should be 2 lines before: 21
					toReturn.put("age",
							personalInfo[i-1].trim()
							.split(" ")[1].trim()); // take '21'					
				} else if(personalInfo[i].contains("$")) {
					// this should be the line with salary: Weekly salary: $ 2 448
					toReturn.put("salary",
							personalInfo[i].trim()
							.split("\\$")[1].trim().replace(" ", "")); // take '2448'
				} else if(personalInfo[i].toLowerCase().contains("dmi")) {
					// this should be the line with dmi: DMI: 7700
					toReturn.put("dmi",
							personalInfo[i].trim()
							.split(":")[1].trim().replace(" ", "")); // take '7700'
				}
			}
		} catch(Exception e){};
		return toReturn;
	}

	private HashMap<String, Integer> getPlayerSkills() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		if(this.arePlayerSkillsVisible()) {
			for(String skill : com.buzzerbeater.model.Player.skillNames) {
				switch (skill) {
					case "js": map.put(skill, getJSSkillValue());break;
					case "jr": map.put(skill, getJRSkillValue());break;
					case "od": map.put(skill, getODSkillValue());break;
					case "ha": map.put(skill, getHASkillValue());break;
					case "dr": map.put(skill, getDRSkillValue());break;
					case "pa": map.put(skill, getPASkillValue());break;
					case "is": map.put(skill, getISSkillValue());break;
					case "id": map.put(skill, getIDSkillValue());break;
					case "rb": map.put(skill, getRBSkillValue());break;
					case "sb": map.put(skill, getSBSkillValue());break;
					case "st": map.put(skill, getSTSkillValue());break;
					case "ft": map.put(skill, getFTSkillValue());break;
					case "ex": map.put(skill, getEXSkillValue());break;
				}
			}
		}
		return map;
	}
	
	public int getJSSkillValue() {
		String title = this.skillJSlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}

	public int getJRSkillValue() {
		String title = this.skillJRlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getODSkillValue() {
		String title = this.skillODlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getHASkillValue() {
		String title = this.skillHAlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getDRSkillValue() {
		String title = this.skillDRlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getPASkillValue() {
		String title = this.skillPAlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getISSkillValue() {
		String title = this.skillISlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getIDSkillValue() {
		String title = this.skillIDlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getRBSkillValue() {
		String title = this.skillRBlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getSBSkillValue() {
		String title = this.skillSBlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getSTSkillValue() {
		String title = this.skillSTlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getFTSkillValue() {
		String title = this.skillFTlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getEXSkillValue() {
		String title = this.skillEXlink.getAttribute("title");
		if(title.equals("20+"))
			return 21;
		else
			return Integer.parseInt(title);
	}
	
	public int getPotentialValue() {
		String potValue = this.potentialLink.getAttribute("title");
		if(potValue.equals("10+")) 
			return 11;
		else
			return Integer.parseInt(potValue);
	}

	private String getPlayerID() {
		return driver.getCurrentUrl()
				.replace("http://www.buzzerbeater.com/player/", "")
				.replace("/overview.aspx", "");
	}
}
