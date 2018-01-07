package com.buzzerbeater.workers;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.buzzerbeater.pages.Login;
import com.buzzerbeater.pages.Overview;
import com.buzzerbeater.pages.Staff;
import com.buzzerbeater.utils.DateTimeHelper;
import com.buzzerbeater.utils.DriverInitializationHelper;
import com.buzzerbeater.utils.Sleeper;


public class AutobidStaffOnSaleWorker extends SwingWorker<Boolean, String> {

	private static Random rand = new Random();
	
	private long maxPrice = 0; // unlimited
	private long nextBidAmount;
	private int sleepInSeconds = 30;
	private String staffID;
	
	boolean visibleBrowser=false;

	String username;
	String password;
	String teamID;

	
	private boolean sleepUntilAuctionEndsTime = false;
	private int numOfMinutesToStartMonitoringBeforeAuctionsEnds;
	private Date auctionEndsAt;

	private String statusMessage = "";

	private JLabel lblAuctionEndsIn, lblStatus;
	WebDriver driver = null;
	
	public AutobidStaffOnSaleWorker(String username, String password,
			String teamID, String staffID, int maxPrice, int secondsBetweenTryouts,
			boolean useAuctionEnd, int minutesBeforeAuctionToStartMonitoring, JLabel lblAuctionEndsIn, JLabel lblStatus,
			boolean useVisibleBrowser) {
		this.maxPrice = maxPrice;
		this.sleepInSeconds = secondsBetweenTryouts;
		this.staffID = staffID;
		this.username = username;
		this.password = password;
		this.teamID = teamID;
		//this.sleepUntilAuctionEndsTime = useAuctionEnd;
		this.numOfMinutesToStartMonitoringBeforeAuctionsEnds = minutesBeforeAuctionToStartMonitoring;
		this.lblAuctionEndsIn = lblAuctionEndsIn;
		this.lblStatus = lblStatus;
		this.visibleBrowser = useVisibleBrowser;
	}

	public String getStatusMessage() {
		return this.statusMessage ;
	}

	public String getStaffID() {
		return staffID;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		boolean bidButtonDisplayed = true, priceOverMax = false;
		//boolean playerBought = false;
		publish("Starting worker ...");
		lblAuctionEndsIn.setText("N/A");
		
		driver = DriverInitializationHelper
				.initializeDriver(visibleBrowser);
		
		try { 
			Login loginPage = PageFactory.initElements(driver, Login.class);
			Overview overviewPage = loginPage.login(username, password, Overview.class);
			Staff staffPage;
			
			while(bidButtonDisplayed && !priceOverMax && !isCancelled()) {
				try{
					
					publish("checking staff bid status ...");
					
					// load player page
					staffPage = overviewPage.getStaffPageByID(staffID);
	
					if(!loginPage.isLoggedIn(1)) {
						loginPage = PageFactory.initElements(driver, Login.class);
						overviewPage = loginPage.login(username, password, Overview.class);
						if(!loginPage.isLoggedIn(1)) {
							publish("ERROR: Can't login to BB");
							return false;
						}
						staffPage = overviewPage.getStaffPageByID(staffID);
					}
					
					
					if(staffPage.isLoggedIn(1)) {
						bidButtonDisplayed = staffPage.isTransferListed();
						if(!bidButtonDisplayed) {
							//playerBought = staffPage.arePlayerSkillsVisible();
							publish("INFO: auction ended");
							return /*playerBought*/ true;
						}
					} else {
						// re-loop
						continue;
					}
					
					if(sleepUntilAuctionEndsTime) {
						try {
							auctionEndsAt = staffPage.getAuctionEndsAtDate();
							int minutesToSleep = DateTimeHelper.getNumberOfMinutesBetweenDates(
									new Date(System.currentTimeMillis()), auctionEndsAt);
							if(minutesToSleep > numOfMinutesToStartMonitoringBeforeAuctionsEnds) {
								statusMessage = "Sleeping till " + 
											numOfMinutesToStartMonitoringBeforeAuctionsEnds + 
											"min before auction ends.";
								publish(statusMessage);
								lblAuctionEndsIn.setText(minutesToSleep + " minutes");
								Sleeper.sleepTightInMinutes(minutesToSleep - numOfMinutesToStartMonitoringBeforeAuctionsEnds);
								// sleeping finished, set useAuctionEnd to false
								sleepUntilAuctionEndsTime = false;
								publish("Started monitoring auction for player.");
								continue;
							} else {
								// auction end is too soon -> set to false
								sleepUntilAuctionEndsTime = false;
							}
						} catch (Exception e) {
							publish("WARN: can't get auction ends datetime. continuing without it.");
							sleepUntilAuctionEndsTime = false;
						}
					}
					
					String currentBidTeamID;
					try {
						currentBidTeamID = staffPage.getCurrentBidTeamID();
					} catch (Exception e){
						publish("WARN: Can't recognize active bid teamID or current amount. retrying ...");
						continue;
					}
					if(this.teamID.equals(currentBidTeamID)) {
						publish("your bid is still active.");
					} else {
						if(!currentBidTeamID.isEmpty())publish("WARN: you were overbid! trying to re-bid if possible and/or allowed.");
						
						try {
							nextBidAmount = Long.parseLong(staffPage.getNextBidAmount());
						} catch (Exception e) {
							publish("WARN: exception while parsing long from next bid value: " + e.getMessage() + ". try again");
							continue;
						}
						publish("next bid should be: $" + nextBidAmount);
						
						if(maxPrice > 0 && nextBidAmount > maxPrice) {
							publish("ERROR: next bid price is greater than max price. exiting now...");
							priceOverMax = true;
							break;
						} else if(nextBidAmount < 1) {
							publish("WARN: wrongly collected nex bid amount ($" + nextBidAmount + ") ... restarting");
							continue;
						} else {
							// place a bid
							staffPage = staffPage.clickOnBidButton();
							
							// check if your bid is active.
							// if yes -> sleep
							// if not -> loop again immediately
							try {
								currentBidTeamID = staffPage.getCurrentBidTeamID();
							} catch (Exception e){
								publish("WARN: exception while getting info from the page: " + e.getMessage() + ". try again");
								continue;
							}
							if(!this.teamID.equals(currentBidTeamID)) {
								publish("WARN: your bid is not active. retrying");
								continue;
							} else {
								publish("Your bid is now active");
							}
						}
						
					}
					
					// now sleep interval between tryouts +/- 5 seconds
					Sleeper.sleepTightInSeconds(sleepInSeconds -5 + rand.nextInt(10));
					
				} catch(Exception e) {
					statusMessage = "Fatal error " + e.getMessage();
					publish("ERROR: " + statusMessage);
					break;
				}
			}
		} finally {
			driver.quit();
		}
		return false;
	}

	protected void process(List<String> statuses ) {
		for(String status : statuses) {
			if(status.startsWith("ERROR: ")) {
				lblStatus.setForeground(Color.RED);
				lblStatus.setText(status.replaceFirst("ERROR: ", ""));
			} else if(status.startsWith("WARN: ")) {
				lblStatus.setForeground(Color.MAGENTA);
				lblStatus.setText(status.replaceFirst("WARN: ", ""));
			} else {
				lblStatus.setForeground(Color.BLUE);
				lblStatus.setText(status);
			}
		}
	}
}
