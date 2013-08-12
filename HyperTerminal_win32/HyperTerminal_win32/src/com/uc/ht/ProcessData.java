package com.uc.ht;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProcessData {

	public static void getProcessed(String incomingData) 
	{
	System.out.println(incomingData);
		
		
	}
	
	public static void main(String[] args) throws IOException
	{
		 String strFile = "C:/Users/rgadipudi/Documents/Venture V2.75 7-23-13.TXT";

         //create BufferedReader to read csv file
         BufferedReader br = new BufferedReader( new FileReader(strFile));
         String strLine = "";
         String st = null;
         int lineNumber = 0, tokenNumber = 0;

         //read comma separated file line by line
         while( (strLine = br.readLine()) != null)
         {
                 lineNumber++;

                 //break comma separated line using ","
                 getProcessed(strLine);
         }
	}

}
