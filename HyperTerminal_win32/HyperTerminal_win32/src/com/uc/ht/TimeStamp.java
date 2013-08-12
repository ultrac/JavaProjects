package com.uc.ht;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp 
{
	// returns timestamp in YYYYMMDDHHMMSS format
	public String getDateTime() 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    } 
	
	//returns date and time in yyyy-MM-dd HH:MM:SS format
	public String getTime() 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    } 
}
