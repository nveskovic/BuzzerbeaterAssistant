package com.buzzerbeater.utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class SwingUIHelper {

	public static List<String> getListOfLinesFromJTextArea(JTextArea area) {
		List<String> toReturn = new ArrayList<String>();
		String lineSeparator = "";
		if(area.getText().contains(System.getProperty("line.separator")))
			lineSeparator = System.getProperty("line.separator");
		else if(area.getText().contains("\n\r"))
			lineSeparator = "\n\r";
		else if(area.getText().contains("\n"))
			lineSeparator = "\n";
		else if(area.getText().contains("\r"))
			lineSeparator = "\r";
		
		// parse the list
		if(!lineSeparator.isEmpty()) {
			toReturn = Arrays.asList(area.getText()
					.split(lineSeparator));
		} else if(area.getLineCount() > 0) {
			for(int i=0; i<area.getLineCount(); i++) {
				try {
					if(area.getLineEndOffset(i) > 0)
						toReturn.add(area.getText().substring(
							area.getLineStartOffset(i), 
							area.getLineEndOffset(i))
							);
				} catch (BadLocationException e1) {}
			}
		} else {
			toReturn.add(area.getText());
		}
		
		return toReturn;
	}
	
	public static void loadLinesFromFileToTextArea(File f, JTextArea areaToLoadLinesTo, JTextArea logArea) {

		if(f == null) {
			logArea.append("Players file was not selected." + System.getProperty("line.separator"));
			return;
		}

		// load lines from file
		List<String> linesFromFile;
		try {
			linesFromFile = Files.readLinesFromFile(f);
		} catch (IOException e) {
			logArea.setForeground(Color.RED);
			logArea.append("ERROR while reading from file" + System.getProperty("line.separator"));
			return;
		}
		if(linesFromFile.isEmpty()) {
			logArea.append("No players to resend messages to." + System.getProperty("line.separator"));
			return;
		}

		areaToLoadLinesTo.setText(null);
		for(String player : linesFromFile) {
			areaToLoadLinesTo.append(player + System.getProperty("line.separator"));
		}
		
		logArea.append(linesFromFile.size() + " player(s) loaded from file" + System.getProperty("line.separator"));
	}

	public static void setListOfLinesToJTextArea(List<String> lines, JTextArea area, String linesSeparator) {
		area.setText(null);
		for(int i=0; i<lines.size();i++) {
			if(i != lines.size() - 1)
				area.append(lines.get(i) + linesSeparator);
			else
				area.append(lines.get(i));
		}
	}
	
	public static void setListOfLinesToJTextArea(List<String> lines, JTextArea area) {
		setListOfLinesToJTextArea(lines, area, System.getProperty("line.separator"));
	}
	
}
