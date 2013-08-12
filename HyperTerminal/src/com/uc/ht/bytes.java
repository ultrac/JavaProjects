package com.uc.ht;

import java.io.IOException;

public class bytes 
{
	static int code =1;
	static int size = 1;
	public static int[] getByte()
	{
		int[] a = new int[size];
		if(code == 1)
		{
			size=4;
			a= new int[size];
			a = add(a, 0, 1337);
			a = add(a, 1, 1338);
			a = add(a, 2, 1339);
			a = add(a, 3, 1340);

			for(int i : a)
			    System.out.print(i + " ");
			}


		
		
		return a;
	}
	public static int[] add(int[] myArray, int pos, int n)
	{
	    for (int i = pos; i<myArray.length-1; i++){
	        myArray[i] = myArray[i+1];
	    }
	    myArray[pos] = n;
	    return myArray;
	}
	public static void main(String[] args) throws IOException
	{
		int c = 0;
		getByte();
		while ((c=System.in.read())>-1) 
		{
			System.out.println(c);
		}
		
	}
}
