package com.gmail.onesmanteam.main.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlatFile {

	private String name;
	private String line;
	private List<String> content = new ArrayList<String>();

	public FlatFile(String name) {
		this.name = name;

		createFile();
		read();
	}

	public void createFile() {
		File f = new File(name);

		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void read() {
		try {
			FileReader fr = new FileReader(name);
			BufferedReader br = new BufferedReader(fr);
			
			while((line = br.readLine()) != null){
				content.add(line);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
			System.out.println("File not found '" + name + "'");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean contains(String value){
		for(int i = 0; i < content.size(); i++){
			if(content.get(i).equalsIgnoreCase(value)){
				return true;
			}
		}
		
		return false;
	}
	
	public void write(String value){
		try {
			FileWriter fw = new FileWriter(name, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			if(value != null){
				bw.write(value);
				bw.newLine();
				content.add(value);
			}
			
			bw.close();
		} catch (IOException e) {
			System.out.println("Error writing to file '" + name + "'");
		}
	}
	
	public List<String> getContent(){
		return content;
	}
	
}
