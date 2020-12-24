package com.buzzerbeater.workers;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.buzzerbeater.pages.*;
import com.buzzerbeater.utils.BrowserType;
import com.buzzerbeater.utils.DriverInitializationHelper;
import com.buzzerbeater.utils.Files;

public class GetSkillsOfPlayersByInvitingThemToNTWorker extends SwingWorker<Boolean, Integer> {
	
	List<String> playersURLs;
	int minPotential;
	JTextArea outputArea;
	boolean visibleBrowser=false;
	File outputCSV;
	boolean autosccrollLogArea = true;
	boolean skipBotAndRetired;
	boolean inviteToNT;

	String username;
	String password;
	String teamID;
	private BrowserType browserType;
	
	public GetSkillsOfPlayersByInvitingThemToNTWorker(String username, String password, String teamID,
			List<String> players, int minPotential, boolean skipBotAndRetired, boolean inviteToNT,
			File outputCSV, JTextArea outputArea, boolean visibleBrowser, BrowserType browserType) {
		this.playersURLs=players;
		this.minPotential = minPotential;
		this.username=username;
		this.password=password;
		this.teamID = teamID;
		this.visibleBrowser = visibleBrowser;
		this.outputCSV = outputCSV;
		this.outputArea = outputArea;
		this.skipBotAndRetired = skipBotAndRetired;
		this.inviteToNT = inviteToNT;
		this.browserType =  browserType;
		
		if(autosccrollLogArea) {
			DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		
		int i=0;
		
		// list that will contain players skills
		ArrayList<String> playersInfoAndSkills = new ArrayList<String>();
		playersInfoAndSkills.add(com.buzzerbeater.model.Player.getCsvHeader());
		Files.saveLinesToFile(playersInfoAndSkills, outputCSV);
		
		// send messages
		if(this.playersURLs.size()==0) {
			this.outputArea.append("Players list is empty!" + System.getProperty("line.separator"));
			return false;
		}
		
		this.outputArea.setText("Trying to get skills for " + playersURLs.size() 
		+ " player(s)" + System.getProperty("line.separator"));
		
		// prepare the file for appending
		FileWriter fw = new FileWriter(outputCSV, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw);
	    out.println();

		while(i<playersURLs.size()) {
			WebDriver driver = DriverInitializationHelper
					.initializeDriver(visibleBrowser, true, browserType);
			
			try {
				driver.get(playersURLs.get(i));
				Overview overviewPage = PageFactory.initElements(driver, Overview.class);
				Login loginPage = PageFactory.initElements(driver, Login.class);
				if(!loginPage.isLoggedIn(1)) {
					overviewPage = loginPage.login(username, password, Overview.class);
					if(!loginPage.isLoggedIn(10)) {
						outputArea.append("Can't login to BB. Restarting..." + System.getProperty("line.separator"));
						break;
					}
				}
				
				// authenticate team
				if(!this.teamID.equals(overviewPage.getTeamIDFromMenu())) {
					outputArea.setForeground(Color.RED);
					outputArea.setText("ERROR: Your team ID does not match the ID from the Overview page");
					return false;
				}
				
				while(i<playersURLs.size()) { // inner loop without restarting browser
					
					setProgress(100 * (i) / playersURLs.size());
					
					try {
						
						String playerURL = playersURLs.get(i);
						String okMessageSuffix = "";
						
						outputArea.append((i+1) + "/" + playersURLs.size() + 
								": Getting info for '" + playerURL + "' ... ");
						driver.get(playerURL);
						Player playerPage = PageFactory.initElements(driver, Player.class);
						if(!playerPage.isLoggedIn(10)) {
							overviewPage = loginPage.login(username, password, Overview.class);
							driver.get(playerURL);
							playerPage = PageFactory.initElements(driver, Player.class);
							if(!this.teamID.equals(playerPage.getTeamIDFromMenu())) {
								outputArea.setForeground(Color.RED);
								outputArea.setText("ERROR: your team is not licenesed to use this tool!");
								return false;
							}
							
						}
						
						// if player is not of desired potential, skip it
						if(playerPage.getPotentialValue()< minPotential) {
							i++;
							outputArea.append("SKIP due to lower potential" + System.getProperty("line.separator"));
							setProgress(100 * i / playersURLs.size());
							continue;
						}
						
						String owner;
						boolean isOwnerRetired = playerPage.isOwnerRetired();
						owner = (isOwnerRetired ? "RETIRED" : "");
						
						if(skipBotAndRetired) {
							
							if(isOwnerRetired) {
								i++;
								outputArea.append("SKIP because owner is RETIRED" + System.getProperty("line.separator"));
								setProgress(100 * i / playersURLs.size());
								continue;
							}
						}
						
						// if skills are not visible and there is recruit button => recruit player to NT
						boolean playerInvitedToNT = false;
						
						if(!playerPage.arePlayerSkillsVisible() && inviteToNT) {
							if(playerPage.isRecruitToNTButtonDisplayed()) {
								playerPage.clickOnRecruitToNTButton();
								playerPage.clickOnRecruitToNTButtonConfirm();
								playerInvitedToNT = true;
								outputArea.append(" - Invited to NT");
							} else {
								outputArea.append(" - No 'Recruit' button");
							}
						}
						
						// get player skills
						com.buzzerbeater.model.Player p = playerPage.getPlayerInfo();
						
						outputArea.append(" - Info collected");
						
						if(playerInvitedToNT) { // remove from NT squad
							playerPage.dismissFromNT();
							playerInvitedToNT = false;
							outputArea.append(" - Dismissed");
						}
						
						out.println(p.getCsvPlayerInfo(Page.baseUrl));
						out.flush();
						i++;
						
						outputArea.append(" - OK" + okMessageSuffix + System.getProperty("line.separator"));
							
					} catch(Exception e) {
						outputArea.append(" - FAIL - General Error '" + e.getMessage() + "'. Restarting..." + System.getProperty("line.separator"));
						e.printStackTrace();
						try {
							driver.quit();
						} catch(Exception e1){}
						driver = null;
						break;
					}
					
					setProgress(100 * i / playersURLs.size());
				}
			} catch(Exception e) {
				try {
					driver.quit();
				} catch(Exception e1){}
				driver = null;
			} finally {
				driver.quit();
			}
		}
		
		out.close(); bw.close(); fw.close();
		return false;
	}
}
