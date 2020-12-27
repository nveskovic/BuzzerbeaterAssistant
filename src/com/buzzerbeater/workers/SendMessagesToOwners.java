package com.buzzerbeater.workers;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.buzzerbeater.pages.*;
import com.buzzerbeater.utils.BrowserType;
import com.buzzerbeater.utils.DriverInitializationHelper;
import com.buzzerbeater.utils.Messages;

public class SendMessagesToOwners extends SwingWorker<Boolean, Integer> {
	
	List<String> playerIDs;
	HashMap<String, String> messagesAndSubjects;
	JTextArea outputArea;
	boolean visibleBrowser=true;
	boolean skipTLPlayers=true;

	String username;
	String password;
	String teamID;
	
	List<String> playersWithMessagesSent, playersWithMessagesNotSent, playersFromRetiredAndBot;
	private BrowserType browserType;
	
	public SendMessagesToOwners(String username, String password, String teamID,
			List<String> playerIDs, HashMap<String, String> messagesMap,
			JTextArea output, boolean skipTLPlayers, 
			boolean visibleBrowser, BrowserType browserType) {
		
		this.playerIDs =playerIDs;
		this.messagesAndSubjects = messagesMap;
		this.outputArea = output;
		this.username=username;
		this.password=password;
		this.teamID = teamID;
		this.visibleBrowser = visibleBrowser;
		this.skipTLPlayers = skipTLPlayers;
		this.browserType = browserType;
		
		// initialize output lists
		this.playersWithMessagesNotSent = new ArrayList<String>();
		this.playersFromRetiredAndBot = new ArrayList<String>();
		this.playersWithMessagesSent = new ArrayList<String>();
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		// send messages
		if(this.playerIDs.size()==0) {
			this.outputArea.append("Players list is empty!" + System.getProperty("line.separator"));
			return false;
		}

		for (String pID : playerIDs) {
			if(!pID.matches("[0-9]+")) {
				outputArea.setForeground(Color.RED);
				outputArea.setText("ERROR: invalid playerID found in the list: " + pID + System.getProperty("line.separator"));
				return false;
			}
		}

		outputArea.setText("");

		WebDriver driver = DriverInitializationHelper
				.initializeDriver(visibleBrowser, browserType);
		
		this.outputArea.setText("Trying to send messages for " + playerIDs.size()
					+ " player(s)" + System.getProperty("line.separator"));
		
		try {
		
			Overview overviewPage = PageFactory.initElements(driver, Overview.class);
			Login loginPage = PageFactory.initElements(driver, Login.class);
			if(!loginPage.isLoggedIn(1)) {
				overviewPage = loginPage.login(username, password, Overview.class);
				if(!loginPage.isLoggedIn(1)) {
					outputArea.append("Can't login to BB" + System.getProperty("line.separator"));
					return false;
				}
			}
			
			// authenticate team
			String teamIdInThePage = overviewPage.getTeamIDFromMenu();
			if(!this.teamID.equals(teamIdInThePage)) {
				outputArea.setForeground(Color.RED);
				outputArea.setText("ERROR: Your team ID ("+this.teamID+") does not match the one in the Overview page ("+teamIdInThePage+")");
				return false;
			}
			
			// go to manage my team to get the country id
			driver.get(overviewPage.url);
			overviewPage = PageFactory.initElements(driver, Overview.class);
				
			String domesticCountryID = overviewPage.getCountryID();
			if(domesticCountryID.isEmpty()) {
				outputArea.append("WARNING: Can't determine local country. "
						+ "Messages will be sent in English" + System.getProperty("line.separator"));
			}
			
			for(int i = 0; i< playerIDs.size(); i++) {
				
				setProgress(100 * (i) / playerIDs.size());
				
				try {

					// Check if the ID is the number
					String playerID = playerIDs.get(i);
					// TODO: use Player page to get the proepr URL based on the ID
					String playerURL = Page.baseUrl + "/player/" + playerID + "/overview.aspx";
					String okMessageSuffix = "";
					
					String messageTemplate, subjectTemplate;
					outputArea.append((i+1) + "/" + playerIDs.size() +
							": Sending message for player ID='" + playerID + "' ... ");
					driver.get(playerURL);
					Player playerPage = PageFactory.initElements(driver, Player.class);
					if(!playerPage.isLoggedIn(1)) {
						loginPage.login(username, password, Overview.class);
						driver.get(playerURL);
						playerPage = PageFactory.initElements(driver, Player.class);
						if(!this.teamID.equals(playerPage.getTeamIDFromMenu())) {
							outputArea.setForeground(Color.RED);
							outputArea.setText("ERROR: teamID provided in User Info tab is not the same as the one in the page!");
							return false;
						}
						
					}
					
					if(playerPage.isOwnerRetired()) {
						this.playersFromRetiredAndBot.add(playerID);
						outputArea.append("FAIL - Owner retired" + System.getProperty("line.separator"));
						continue;
					}
					
					if(this.skipTLPlayers && playerPage.isTransferListed()) {
						outputArea.append("SKIP - TL player" + System.getProperty("line.separator"));
						continue;
					}
					
					String playerName = playerPage.getPlayerName();
					Team teamPage = playerPage.clickOnOwnerLink();
					
					if(!teamPage.isHuman()) {
						this.playersFromRetiredAndBot.add(playerID);
						outputArea.append("FAIL - Owner is BOT" + System.getProperty("line.separator"));
						continue;
					}
					
					if(teamPage.isDomesticTeam(domesticCountryID)) {
						messageTemplate=messagesAndSubjects.get(Messages.MESSAGE_DOMESTIC_KEY);
						subjectTemplate=messagesAndSubjects.get(Messages.SUBJECT_DOMESTIC_KEY);
					} else {
						messageTemplate=messagesAndSubjects.get(Messages.MESSAGE_ENGLISH_KEY);
						subjectTemplate=messagesAndSubjects.get(Messages.SUBJECT_ENGLISH_KEY);
					}

					Thread.sleep(1000);
					SendMessage sendMessagePage = teamPage.clickOnSendMessageLink();


					String playeNamePlaceholder = messagesAndSubjects.get(Messages.PLAYER_NAME_PLACEHOLDER_KEY);
					String playeIDPlaceholder = messagesAndSubjects.get(Messages.PLAYER_ID_PLACEHOLDER_KEY);

					String subject = subjectTemplate
							.replaceAll(playeNamePlaceholder, playerName)
							.replaceAll(playeIDPlaceholder, playerID);
					String content = messageTemplate
							.replaceAll(playeNamePlaceholder, playerName)
							.replaceAll(playeIDPlaceholder, playerID);
					
					sendMessagePage.setSubject(subject);
					sendMessagePage.setMessage(content);
					SentMessage sentMessagePage = sendMessagePage.clickOnSendButton();
					Thread.sleep(1000);
					
					boolean msgSent = sentMessagePage.isDisplayed();
					
					if(msgSent) {
						this.playersWithMessagesSent.add(playerID);
						outputArea.append("OK" + okMessageSuffix + System.getProperty("line.separator"));
					} else {
						outputArea.append("FAIL" + System.getProperty("line.separator"));
					}
						
				} catch(Exception e) {
					outputArea.append("FAIL - " + e.getMessage() + System.getProperty("line.separator"));
				}
				
				setProgress(100 * (i+1) / playerIDs.size());
			}
		} catch(Exception e) {
			System.err.println("FAIL - " + e.getMessage());
		} finally {
			driver.quit();
		}
		
		// update the lists
		for(String s : this.playerIDs) {
			if(!this.playersWithMessagesSent.contains(s))
				this.playersWithMessagesNotSent.add(s);
		}
		
		return false;
	}
	
	public List<String> getPlayersWithMessagesNotSent() {
		return this.playersWithMessagesNotSent;
	}
	
	public List<String> getPlayersFromRetiredAndBot() {
		return this.playersFromRetiredAndBot;
	}
}
