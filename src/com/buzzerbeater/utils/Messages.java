package com.buzzerbeater.utils;

import java.util.HashMap;

public class Messages {


	public static String PLAYER_NAME_PLACEHOLDER_KEY = "playerNamePlaceholder";
	public static String PLAYER_ID_PLACEHOLDER_KEY = "playerIDPlaceholder";
	public static String SUBJECT_DOMESTIC_KEY = "subjectTemplateDomestic";
	public static String SUBJECT_ENGLISH_KEY = "subjectTemplateEN";
	public static String MESSAGE_DOMESTIC_KEY = "messageTemplateDomestic";
	public static String MESSAGE_ENGLISH_KEY = "messageTemplateEN";
	
	public static String playerNamePlaceholder = "_PLAYER_NAME_";
	public static String playerIDPlaceholder = "_PLAYER_ID_";

	public static String subjectTemplateDomestic = "NT Scout - igrac " + playerNamePlaceholder + " (" + playerIDPlaceholder + ")";
	public static String subjectTemplateEN = "NT Scout - player " + playerNamePlaceholder + " (" + playerIDPlaceholder + ")";


	public static String greetingMessageTemplateDomestic = "Cao,\n\nja sam jedan od skauta U21 reprezentacije Srbije i od selektora sam dobio zadatak da sa tobom radim na "
			+ "treniranju igraca " + playerNamePlaceholder + " (" + playerIDPlaceholder + ")" + ". Da bismo zajedno sto bolje napravili plan trening za igraca, potrebno mi je "
			+ "par sledecih informacija o igracu i o tvom timu:\n\nTrenutni skilovi igraca:\nLevel trenera kojeg imas u timu:\nPlan treniranja "
			+ "igaca ove sezone\n\nAko imas bilo kakvih pitanja oko kojih mogu da ti pomognem, stojim ti na raspolaganju.\n\nHvala puno i nadam se odlicnoj saradnji.";

	public static String skillsUpdatemessageTemplateDomestic = "Cao, "
			+ "\n\nda li je bilo danas na treningu povecanja skillova ovog tvog igraca? "
			+ "\n\nPozzdrav";

	public	static String greetingMessageTemplateEN = "Hello,\n\nI'm one of the Serbia U21 NT scouts and from Serbia U21 national manager I was assigneg to monitor improvement of your player "
			+ playerNamePlaceholder + " (" + playerIDPlaceholder + ")" + " beacuse he is interesting to Serbia U21 team. In order to help you train this player in the best way, I would be needing few info "
			+ "about the player and about your team:\n\nCurrent skills of the player:\nCoach Level in your team:\nTraining plans for this player\n\n"
			+ "If you have any questions that I might be able to answer/help you, please do not hesitate to ask me, I'll be in your disposal"
			+ "\n\nThanks a lot in advance and I hope we'll have a great collaboration this season.";
	public static String skillsUpdatemessageTemplateEN = "Hello,"
			+ "\n\nWas there any skill update after today for this player?"
			+ "\n\nThanks :)";
	
	public static HashMap<String, String> getGreetingMessageMap() {
		HashMap<String, String> map = getBaseMap();
		map.put(MESSAGE_DOMESTIC_KEY, greetingMessageTemplateDomestic);
		map.put(MESSAGE_ENGLISH_KEY, greetingMessageTemplateEN);
		return map;
	}
	
	public static HashMap<String, String> getSkillsUpdateMessageMap() {
		HashMap<String, String> map = getBaseMap();
		map.put(MESSAGE_DOMESTIC_KEY, skillsUpdatemessageTemplateDomestic);
		map.put(MESSAGE_ENGLISH_KEY, skillsUpdatemessageTemplateEN);
		return map;
	}
	
	public static HashMap<String, String> getCustomMap() {
		HashMap<String, String> map = getBaseMap();
		map.put(MESSAGE_DOMESTIC_KEY, "Type your localized message here. Use " + playerNamePlaceholder + " (" + playerIDPlaceholder + ")" + " to insert player name/ID in the realtime in subject or message field");
		map.put(MESSAGE_ENGLISH_KEY, "Type your message in English here. Use " + playerNamePlaceholder + " (" + playerIDPlaceholder + ")" + " to insert player name/ID in the realtime in subject or message field");
		map.put(SUBJECT_DOMESTIC_KEY, "Type your localized subject here");
		map.put(SUBJECT_ENGLISH_KEY, "Type subject in English here");
		return map;
	}
	
	public static HashMap<String, String> getBaseMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(PLAYER_ID_PLACEHOLDER_KEY, playerIDPlaceholder);
		map.put(PLAYER_NAME_PLACEHOLDER_KEY, playerNamePlaceholder);
		map.put(SUBJECT_DOMESTIC_KEY, subjectTemplateDomestic);
		map.put(SUBJECT_ENGLISH_KEY, subjectTemplateEN);
		return map;
	}
}
