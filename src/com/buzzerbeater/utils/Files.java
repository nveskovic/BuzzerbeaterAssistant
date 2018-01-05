package com.buzzerbeater.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Files {
	public static List<String> readLinesFromFile(File f) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				if(!line.startsWith("#"))lines.add(line.trim());
			}
	
			return lines;
		} finally {
			br.close();
		}
	}

	public static void saveLinesToFile(ArrayList<String> lines, File fileToSave) 
			throws IOException {
		FileOutputStream fos = new FileOutputStream(fileToSave);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF8"));

		try {

			for (int i=0; i<lines.size(); i++) {
				bw.write(lines.get(i));
				if((i+1) != lines.size()) // don't write newLine at the end of file
					bw.newLine();
			}
		} finally {
			bw.close();
		}
	}
}
