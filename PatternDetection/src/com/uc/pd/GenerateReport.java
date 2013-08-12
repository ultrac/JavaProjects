package com.uc.pd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenerateReport 
{
	SaveToCSV stc;
	List<String[]> lsBadge = new ArrayList<String[]>();
	int iCount=0;
	
	public GenerateReport(SaveToCSV stc) 
	{
		this.stc=stc;
	}
	
	GenerateReport()
	{
	}

	void report(String badgeId, String value, int iDate)
	{
		
		
		for(String[] sTemp : lsBadge)
		{
			if(sTemp[0].equalsIgnoreCase(badgeId))	
			{
				iCount++;
			}
		}
		if(iCount==1)
		{
			createEntry(badgeId,iDate,value);
		}
		else if(iCount==0)
		{
			createEntry(badgeId,iDate,value);
		}
		iCount=0;
	}

	void createEntry(String badgeId, int iDate, String value)
	{
		String[] date = new String[32];
		for (int i = 0; i <= 31; i++)
		{
			if(i==0)
			{
				date[i]=badgeId;
			}
			if(i==iDate)
			{
				date[i]=value;
			}
			else
			{
				date[i]="0";
			}
		}
		lsBadge.add(date);
		System.out.println(lsBadge.size());
	}
	void printReport() throws IOException
	{
		
		stc.totalReport("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31");
		System.out.println(lsBadge.size());
		for(String[] sTemp : lsBadge)
		{
			stc.totalReport(sTemp[0]+"|"+sTemp[2]+"|"+sTemp[11]+"|"+sTemp[12]);
			String sSavedData = "";
			for(String s_Temp : sTemp)
			{
				sSavedData += s_Temp + "|";
			}
			System.out.println(sSavedData);
			stc.totalReport(sSavedData);
		}
		
	}
}

