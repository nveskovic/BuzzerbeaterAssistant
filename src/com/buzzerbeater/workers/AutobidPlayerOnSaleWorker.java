package com.buzzerbeater.workers;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.buzzerbeater.pages.Login;
import com.buzzerbeater.pages.Overview;
import com.buzzerbeater.pages.Player;
import com.buzzerbeater.utils.DriverInitializationHelper;
import com.buzzerbeater.utils.Sleeper;


public class AutobidPlayerOnSaleWorker extends SwingWorker<Boolean, String> {

	private static Random rand = new Random();
	
	private long maxPrice = 0; // unlimited
	private long nextBidAmount;
	private int sleepInSeconds = 70;
	private String playerID;
	
	boolean visibleBrowser=false;

	String username;
	String password;
	String teamID;

	private String statusMessage = "";

	private JLabel lblAuctionEndsIn, lblStatus;
	WebDriver driver = null;
	
	public AutobidPlayerOnSaleWorker(String username, String password,
			String teamID, String playerID, int maxPrice, JLabel lblAuctionEndsIn, JLabel lblStatus,
			boolean useVisibleBrowser) {
		this.maxPrice = maxPrice;
		this.playerID = playerID;
		this.username = username;
		this.password = password;
		this.teamID = teamID;
		this.lblAuctionEndsIn = lblAuctionEndsIn;
		this.lblStatus = lblStatus;
		this.visibleBrowser = useVisibleBrowser;
	}

	public String getStatusMessage() {
		return this.statusMessage ;
	}

	public String getPlayerID() {
		return playerID;
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		boolean bidButtonDisplayed = true, priceOverMax = false;
		boolean playerBought = false;
		publish("Starting worker ...");
		lblAuctionEndsIn.setText("N/A");
		
		driver = DriverInitializationHelper
				.initializeDriver(visibleBrowser);
		
		try { 
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			Login loginPage = PageFactory.initElements(driver, Login.class);
			Overview overviewPage = loginPage.login(username, password, Overview.class);
			Player playerPage;
			
			while(bidButtonDisplayed && !priceOverMax && !isCancelled()) {
				try{
					
					publish("checking player bid status ...");
					
					// load player page
					playerPage = overviewPage.getPlayerPageByID(playerID);
	
					if(!loginPage.isLoggedIn(1)) {
						loginPage = PageFactory.initElements(driver, Login.class);
						overviewPage = loginPage.login(username, password, Overview.class);
						if(!loginPage.isLoggedIn(1)) {
							publish("ERROR: Can't login to BB");
							return false;
						}
						playerPage = overviewPage.getPlayerPageByID(playerID);
					}
					
					
					if(playerPage.isLoggedIn(1)) {
						bidButtonDisplayed = playerPage.isTransferListed();
						if(!bidButtonDisplayed) {
							playerBought = playerPage.arePlayerSkillsVisible();
							publish((playerBought ? "" : "ERROR: ") + "auction ended - player " 
									+ (playerBought ? "" : "NOT ") + "bought");
							return playerBought;
						}
					} else {
						// re-loop
						continue;
					}
					
					String currentBidTeamID, currentBidAmount;
					try {
						currentBidTeamID = playerPage.getCurrentBidTeamID();
						currentBidAmount = playerPage.getCurrentBidAmount();
					} catch (Exception e){
						publish("WARN: Can't recognize active bid teamID or current amount. retrying ...");
						continue;
					}
					if(this.teamID.equals(currentBidTeamID)) {
						publish("your bid ($" + currentBidAmount + ") is still active.");
					} else {
						if(!currentBidTeamID.isEmpty())publish("WARN: you were overbid! trying to re-bid if possible and/or allowed.");
						
						try {
							nextBidAmount = Long.parseLong(playerPage.getNextBidAmount());
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
							playerPage = playerPage.clickOnBidButton();
							if(playerPage.isErrorMessageDisplayed()) { 
								// cant proceed due to error message
								publish("ERROR: Exiting now. Reason: " + playerPage.getErrorMessageText());
								break;
							}
							
							// check if your bid is active.
							// if yes -> sleep
							// if not -> loop again immediately
							try {
								currentBidTeamID = playerPage.getCurrentBidTeamID();
								currentBidAmount = playerPage.getCurrentBidAmount();
							} catch (Exception e){
								publish("WARN: exception while getting info from the page: " + e.getMessage() + ". try again");
								continue;
							}
							if(!this.teamID.equals(currentBidTeamID)) {
								publish("WARN: your bid is not active. retrying");
								continue;
							} else {
								publish("Your bid of $" + currentBidAmount + " is now active");
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
