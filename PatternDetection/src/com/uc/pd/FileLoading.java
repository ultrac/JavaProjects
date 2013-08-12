package com.uc.pd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class FileLoading 
{
	static int iPatternCount = 1;
	static int iTotal = 0;
	static int iCount = 1;
	static BufferedReader br;
	static FileInputStream fStream;
	static DataInputStream in;
	static List<String[]> laLogData = new ArrayList<String[]>();
	static String sFolderName = "C:/Users/rgadipudi/Downloads/july_2013/";
	public static void main(String args[]) throws IOException   
    {  
		
		String sLine = null;
		// Checks if there is argument passed or not  
		//if(args.length != 1) 
		//{
			// Throws error when no file is entered in command line
		//	System.err.println("Invalid command line, argument required");
		//	System.exit(1);
		//}
		
		// Reads all files from a folder.
		File folder = new File(sFolderName);
		File[] listOfFiles = folder.listFiles();
		SaveToCSV stc = new SaveToCSV(sFolderName,1);
		SaveToCSV stc_2 = new SaveToCSV(sFolderName);
		for (File file : listOfFiles) 
		{
		    if (file.isFile()) 
		    {
		    	System.out.println("File: "+ file.getName()+" is Processing............");
		    	List<String> laBadgeId = new ArrayList<String>();
		    	String sFileDate = file.getName();
		    	SaveToCSV stc_sub = new SaveToCSV(sFolderName,sFileDate.substring(0, 8));
		    	
		    	// Displays processing file name
		    	
		    	stc_sub.sortCSV("Processing File Name: "+file.getName());
		    	stc_sub.reportCSV("Processing File Name: "+file.getName());
		    	stc_sub.detailCSV("Processing File Name: "+file.getName());
		    	
		    	// Displays Date 
		    	
		    	stc_sub.sortCSV("Date : "+sFileDate.substring(0, 4)+"/"+sFileDate.substring(4, 6)+"/"+sFileDate.substring(6, 8));
		    	stc_sub.reportCSV("Date : "+sFileDate.substring(0, 4)+"/"+sFileDate.substring(4, 6)+"/"+sFileDate.substring(6, 8));
		    	stc_sub.detailCSV("Date : "+sFileDate.substring(0, 4)+"/"+sFileDate.substring(4, 6)+"/"+sFileDate.substring(6, 8));
		    	
		        try
		        {  
					// command line parameter passes file name args[0]  
			        fStream = new FileInputStream(sFolderName+sFileDate);  
			        // Get the object of DataInputStream  
			        in = new DataInputStream(fStream);  
			        br = new BufferedReader(new InputStreamReader(in));  

			    	stc_sub.detailCSV("Detecting Impossible Events States");

			    	//Read File Line By Line  
			        
			        while ((sLine = br.readLine()) != null)   
		        	{  
			        	//Checks the number of colons
			        	if(countOccurrences(sLine,"|")==12)
			        	{
			        		// invokes Parse method sending sLine by replacing "," to Space which returns the badge address.
			        		laBadgeId.add(parse(sLine.replace(",", ""), stc_sub));
			        	}
		        	}
		        }
		        catch(Exception e)
		        {
		        	e.printStackTrace();
		        }
		        finally
		        {
		        	//Returns the end count
		        	stc_sub.sortCSV("Total number of imposible events returned: "+iTotal);
		        }
		        
		        // saves the distinct values of laBadgeId array
		        HashSet<String> sUniqueSet = new HashSet<String>(laBadgeId);
		        
		        // Returns the total  no of badges active on that day
		        stc_sub.sortCSV("Total Number of Badges Active:" + sUniqueSet.size());
				
				for (String sSetData : sUniqueSet)
				{
					if(sSetData.length() != 0)
					{
						// invokes the method pattern detection phase
						patternDetectionPhase(sSetData, stc_sub, file.getName().substring(4, 6)); 
					}
				}
				stc_sub.sortCSV("Total Number of imposible event patterns occured: "+iPatternCount);
				stc_sub.detailCSV("Total Number of imposible event patterns occured: "+iPatternCount);
				stc_sub.reportCSV("Total Number of imposible event patterns occured: "+iPatternCount);
				System.out.println("File: "+ file.getName()+" is Done.");
				
		    }
		    laLogData.clear();
		    iPatternCount = 0;
		    System.gc();
		}
		new GenerateReport(stc_2).printReport();
    }
	
	// Counts the occurrences of a string
	public static int countOccurrences(String str, String sCheck)
	{
	    int iCount = 0;
	    char c;
	    for (int i=0; i < str.length(); i++)
	    {
	    	c = sCheck.charAt(0);
	        if (str.charAt(i) == c)
	        {
	             iCount++;
	        }
	    }
	    return iCount;
	}
	
	// Return the occurrences count is true or not
	public static boolean returnOccurrences(String str, String sCheck)
	{
		char c;
	    for (int i=0; i < str.length(); i++)
	    {
	    	c = sCheck.charAt(0);
	        if (str.charAt(i) == c)
	        {
	             return true;
	        }
	    }
	    return false;
	}
	
	
	private static String parse(String sLine, SaveToCSV stc_sub) throws IOException 
	{
		int i = 0;
		// Array For saving line data
		String[] sDataArray = new String[13];
		
		do
		{
			// gets the index of |
			int iA = sLine.indexOf("|");
			// Saves the sub string into array sDataArray
			sDataArray[i]=sLine.substring(0, iA);
			
			// Saves the remaining sub string in to string sLine
			sLine = sLine.substring(iA+1, sLine.length());
			i++;
			
			if(i==11)
			{
			for (String sTemp : sLine.split("|")) 
        		{
					if(i!=0)
					{
						sDataArray[12]=sTemp;
					}
					else
					{
						i++;
						
					}
        		}
			}
		}
		// Check the occurrences of |
		while(returnOccurrences(sLine,"|"));
		// Writes in to sort file
		// Invoke detection method
		detectionPhase(sDataArray[11]+sDataArray[12], sDataArray);
		
		// prints the line count 
		//System.out.println(iCount);
		//iCount++;
		return sDataArray[5];
	}
	
	// Detects the impossible compliance for consecutive entry's for a badge
	static void patternDetectionPhase(String sBadgeId, SaveToCSV stc_sub, String date) throws IOException 
	{
		BadgeData bd = new BadgeData();
		List<String[]> laSortData = new ArrayList<String[]>();
		int iDate = Integer.parseInt(date);
		for (String[] sSortData : laLogData)
		{
			if (sSortData[5].equalsIgnoreCase(sBadgeId))
			{
				laSortData.add(sSortData);
			}	
		}
		
		// This sorts the data set
		laSortData = sortTime(laSortData);
		
		if(bd.detectPattern(laSortData, stc_sub, sBadgeId)>0)
		{
			stc_sub.reportCSV("**********************************************");
			stc_sub.reportCSV("Impossible Events Patters Found for : " + sBadgeId);
			stc_sub.detailCSV("**********************************************");
			stc_sub.detailCSV("Impossible Events Patters Found for : " + sBadgeId);
			bd.printData(stc_sub,sBadgeId, iDate);
			iPatternCount++;
		}
		else
		{
			stc_sub.reportCSV("**********************************************");
			stc_sub.reportCSV("Impossible Events Patters Found for : " + sBadgeId);
			bd.printNonData(stc_sub,sBadgeId, iDate);
		}
		
	}

	private static List<String[]> sortTime(List<String[]> laSortData) 
	{ 
		// String array lists to Support Sorting
		List<String[]> laSortTimeData = new ArrayList<String[]>();
		List<String> laTime = new ArrayList<String>();
		
		// Saving All time stamps to list 
		for(String[] sTime : laSortData)
		{
			laTime.add(sTime[2]);
		}
		
		// By help of tree set which automatically sorts the list. i am sorting time stamp and also returns distinct numbers
		
		TreeSet<String> sTimeUniqueSet = new TreeSet<String>(laTime);
		
		// Comparing each time stamp with laSortData and saving back as sorted list in laSortTimeData 
		for (String sSetTimeData : sTimeUniqueSet)
		{
			if(sSetTimeData.length() != 0)
			{
				for (String[] sTimeSortData : laSortData)
				{
					if (sTimeSortData[2].equalsIgnoreCase(sSetTimeData))
					{
						laSortTimeData.add(sTimeSortData);
					}	
				}
				 
			}
		}
		return laSortTimeData;
	}

	// Detects the Impossible compliance for one entry
	static void detectionPhase(String sCompData, String[] s_DataArray) 
	{
		laLogData.add(s_DataArray);
		String sSavedData = "";
		for (String s_Temp : s_DataArray)
		{
			sSavedData += s_Temp + ",";
		}
		
		switch(sCompData)
		{
			case "02":
				System.out.println(sSavedData);	
				iTotal++;
				break;
			
			case "03":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "05":
				System.out.println(sSavedData);	
				iTotal++;
				break;
			
			case "14":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "21":
				System.out.println(sSavedData);	
				iTotal++;
				iTotal++;
				break;
			
			case "23":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "24":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "25":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "31":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "34":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "35":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "41":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "43":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "45":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "51":
				System.out.println(sSavedData);	
				iTotal++;
				break;
				
			case "54":
				System.out.println(sSavedData);	
				iTotal++;
				break;
		}
	}
}
