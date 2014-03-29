package com.uiuc.adm.project;

import java.io.IOException;

public class Index {

	public static final String inputFile = "dataset.txt";
	public static final String outputFile="indexfile.txt";
	
	public static void main(String[] args) {
		try {
			IndexCreator ic = new IndexCreator(inputFile, outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
