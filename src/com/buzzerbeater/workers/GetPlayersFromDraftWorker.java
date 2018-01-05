package com.buzzerbeater.workers;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

import com.buzzerbeater.pages.*;
import com.buzzerbeater.utils.DriverInitializationHelper;
import com.buzzerbeater.utils.Files;

public class GetPlayersFromDraftWorker extends SwingWorker<Boolean, Integer> {
	
	List<String> leagueIDs;
	int season;
	JTextArea outputArea;
	boolean visibleBrowser=false;
	File outputFile;
	boolean autosccrollLogArea = true;

	String username;
	String password;
	String teamID;
	
	public GetPlayersFromDraftWorker(String username, String password, String teamID,
			List<String> leagues, int season, File outputFile, JTextArea outputArea, boolean visibleBrowser) {
		
		this.leagueIDs=leagues;
		this.username=username;
		this.password=password;
		this.teamID = teamID;
		this.visibleBrowser = visibleBrowser;
		this.outputFile = outputFile;
		this.outputArea = outputArea;
		
		if(autosccrollLogArea) {
			DefaultCaret caret = (DefaultCaret) outputArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		}
	}

	@Override
	protected Boolean doInBackground() throws Exception {
		
		int i=0;
		
		// 
		if(this.leagueIDs.size()==0) {
			this.outputArea.append("League list is empty!" + System.getProperty("line.separator"));
			return false;
		}
		
		// list that will contain players skills
		ArrayList<String> playersInfoAndSkills = new ArrayList<String>();
		playersInfoAndSkills.add("");
		Files.saveLinesToFile(playersInfoAndSkills, outputFile);
		
		
		this.outputArea.setText("Trying to get players URLs for " + leagueIDs.size() 
		+ " league(s)" + System.getProperty("line.separator"));
		
		// prepare the file for appending
		FileWriter fw = new FileWriter(outputFile, true);
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter out = new PrintWriter(bw);

		while(i<leagueIDs.size()) {
			WebDriver driver = DriverInitializationHelper
					.initializeDriver(visibleBrowser);
			
			try {
				
				while(i<leagueIDs.size()) { // inner loop without restarting browser
					
					setProgress(100 * (i) / leagueIDs.size());
					
					// form the draft summar page URL
					String url = "http://www.buzzerbeater.com/league/" + leagueIDs.get(i) + "/draftSummary.aspx";
					if(season>0) {
						url+="?season=" + season;
					}
					
					try {
						String okMessageSuffix = "";
						
						String messageTemplate, subjectTemplate;
						outputArea.append((i+1) + "/" + leagueIDs.size() + 
								": Getting list of players from '" + url + "' ... ");
						driver.get(url);
						DraftSummary draftSummary = PageFactory.initElements(driver, DraftSummary.class);
						
						// get players from the list
						List<String> players = draftSummary.getAllPlayersFromDraft();
						if(players.size() != 48) {
							outputArea.append("FAIL - List of players from the page is not correct. Restarting..." + System.getProperty("line.separator"));
							break;
						}
						
						// send player ulrs to output file
						for(String player : players){
							out.println(player);
						}
						out.flush();
						i++;
						outputArea.append("OK" + okMessageSuffix + System.getProperty("line.separator"));
							
					} catch(Exception e) {
						outputArea.append("FAIL - General Error. Restarting..." + System.getProperty("line.separator"));
						break;
					}
					
					setProgress(100 * i / leagueIDs.size());
				}
			} catch(Exception e) {
				try {
					driver.close();
				} catch(Exception e1){}
				driver = null;
			}
		}
		
		out.close(); bw.close(); fw.close();
		return false;
	}
}
