package com.uc.ht;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVSaveData 
{
	// File created with time in name
	static String fileName = "C:/Users/rgadipudi/TestData/GatewayHubdata "+new TimeStamp().getDateTime()+".csv";
	
	// Constructor creates the file
	/*CSVSaveData(int code) throws IOException
	{	
		if(i==0)
		{
			// creates a new file with based on device
			if(code==2)
			{
				fileName = "C:/Users/rgadipudi/TestData/Hubdata "+new TimeStamp().getDateTime()+".csv";
			}
			else if(code==3)
			{
				fileName = "C:/Users/rgadipudi/TestData/Gatewaydata "+new TimeStamp().getDateTime()+".csv";
			}
			else if(code==1)
			{
				fileName = "C:/Users/rgadipudi/TestData/GatewayHubdata "+new TimeStamp().getDateTime()+".csv";
			}
			i++;
			String newFileName = fileName;
			File newFile = new File(newFileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));
		}
	}*/
	
	CSVSaveData() throws IOException
	{	
			// creates a file with default name
			String newFileName =  fileName;
			File newFile = new File(newFileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(newFile, true));
	}
	
	// Save the incoming data in to CSV file
	public void sendtoCSV(String data) throws IOException
	{
		// Opens the file
		FileWriter writer = new FileWriter(fileName, true);
		// Send data to database
		// give radio messages count
	    // Appends the data with time stamp
		writer.append(new TimeStamp().getTime()+","+data+"\n");
		writer.flush();
	    writer.close();
	}
}
