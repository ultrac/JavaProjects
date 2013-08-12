package com.uc.gm;

import java.io.File;

public class MainActivityFile 
{
	static String sCreateFolder;
	static String sSystemName; 
	static String sCreateSubFolder;
	public static void main(String[] args)
	{
		File fDir;
		// Checks for file name to process
		if(args.length != 1) 
		{
			// Throws error if there is no command lines
			System.err.println("Invalid command line, argument required. Please Make Sure your file path dosent contain spaces");
			System.exit(1);
		}
		// Gives User name
		sSystemName = System.getProperty("user.name");
		sCreateFolder = "C:/Users/"+sSystemName+"/Test";
		fDir = new File(sCreateFolder);
		// if the directory does not exist, create it
		if (!fDir.exists()) 
		{
			fDir.mkdir();  
	  	}
		sCreateSubFolder = sCreateFolder+"/"+new TimeStamp().getDate();
		fDir = new File(sCreateSubFolder);
		// if the directory does not exist, create it
		if (!fDir.exists()) 
		{
			fDir.mkdir();  
	  	}
	}
}
