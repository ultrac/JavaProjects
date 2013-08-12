package com.uc.gm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp 
{
	public String getDateTime() 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String sDate = dateFormat.format( date );
        return sDate;
    } 
	public String getDate() 
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String sDate = dateFormat.format( date );
        return sDate;
    } 
	public String getTime() 
	{
		DateFormat dateFormat = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String sDate = dateFormat.format( date );
        return sDate;
    } 
}
