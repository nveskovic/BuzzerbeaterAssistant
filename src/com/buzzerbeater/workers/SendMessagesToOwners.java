package com.buzzerbeater.workers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.buzzerbeater.pages.*;
import com.buzzerbeater.utils.DriverInitializationHelper;
import com.buzzerbeater.utils.Messages;

public class SendMessagesToOwners extends SwingWorker<Boolean, Integer> {
	
	List<String> playersURLs;
	HashMap<String, String> messagesAndSubjects;
	JTextArea outputArea;
	boolean visibleBrowser=true;
	boolean skipTLPlayers=true;

	String username;
	String password;
	String teamID;
	
	List<String> playersWithMessagesSent, playersWithMessagesNotSent, playersFromRetiredAndBot;
	
	public SendMessagesToOwners(String username, String password, String teamID,
			List<String> players, HashMap<String, String> messagesMap,
			JTextArea output, boolean skipTLPlayers, boolean visibleBrowser) {
		
		this.playersURLs=players;
		this.messagesAndSubjects = messagesMap;
		this.outputArea = output;
		this.username=username;
		this.password=password;
		this.teamID = teamID;
		this.visibleBrowser = visibleBrowser;
		this.skipTLPlayers = skipTLPlayers;
		
		// initialize output lists
		this.playersWithMessagesNotSent = new ArrayList<String>();
		this.playersFromRetiredAndBot = new ArrayList<String>();
		this.playersWithMessagesSent = new ArrayList<String>();
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		// send messages
		if(this.playersURLs.size()==0) {
			this.outputArea.append("Players list is empty!" + System.getProperty("line.separator"));
			return false;
		}
		
		WebDriver driver = DriverInitializationHelper
				.initializeDriver(visibleBrowser);
		
		this.outputArea.setText("Trying to send messages for " + playersURLs.size() 
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
			if(!this.teamID.equals(overviewPage.getTeamID())) {
				outputArea.setForeground(Color.RED);
				outputArea.setText("ERROR: your team is not licenesed to use this tool!");
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
			
			for(int i=0; i<playersURLs.size(); i++) {
				
				setProgress(100 * (i) / playersURLs.size());
				
				try {
					String playerURL = playersURLs.get(i);
					String okMessageSuffix = "";
					
					String messageTemplate, subjectTemplate;
					outputArea.append((i+1) + "/" + playersURLs.size() + 
							": Sending message for '" + playerURL + "' ... ");
					driver.get(playerURL);
					Player playerPage = PageFactory.initElements(driver, Player.class);
					if(!playerPage.isLoggedIn(1)) {
						overviewPage = loginPage.login(username, password, Overview.class);
						driver.get(playerURL);
						playerPage = PageFactory.initElements(driver, Player.class);
						if(!this.teamID.equals(playerPage.getTeamID())) {
							outputArea.setForeground(Color.RED);
							outputArea.setText("ERROR: your team is not licenesed to use this tool!");
							return false;
						}
						
					}
					
					if(playerPage.isOwnerRetired()) {
						this.playersFromRetiredAndBot.add(playerURL);
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
						this.playersFromRetiredAndBot.add(playerURL);
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
					
					SendMessage sendMessagePage = teamPage.clickOnSendMessageIcon();
					
					sendMessagePage.setSubject(subjectTemplate.replaceAll(
							messagesAndSubjects.get(Messages.PLAYER_NAME_PLACEHOLDER_KEY), playerName));
					sendMessagePage.setMessage(messageTemplate.replaceAll(
							messagesAndSubjects.get(Messages.PLAYER_NAME_PLACEHOLDER_KEY), playerName));
					SentMessage sentMessagePage = sendMessagePage.clickOnSendButton();
					Thread.sleep(1000);
					
					boolean msgSent = sentMessagePage.isDisplayed();
					
					if(msgSent) {
						this.playersWithMessagesSent.add(playerURL);
						outputArea.append("OK" + okMessageSuffix + System.getProperty("line.separator"));
					} else {
						outputArea.append("FAIL" + System.getProperty("line.separator"));
					}
						
				} catch(Exception e) {
					outputArea.append("FAIL - " + e.getMessage() + System.getProperty("line.separator"));
				}
				
				setProgress(100 * (i+1) / playersURLs.size());
			}
		} finally {
			driver.close();
		}
		
		// update the lists
		for(String s : this.playersURLs) {
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
