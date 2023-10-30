package me.despawningbone.module.Utils;

import java.text.DecimalFormat;

public class NumberBeauty {
	
	static public DecimalFormat df_1 = new DecimalFormat("#.0");
	static public DecimalFormat df_2 = new DecimalFormat("#.00");
	static public DecimalFormat df_3 = new DecimalFormat("#.000");
	static public DecimalFormat df_4 = new DecimalFormat("#.0000");
	
	public static String formatNumber(double v)
	{
		String val = "";
		int sign = 1;
		if(v < 0.0) sign = -1;
		v = Math.abs(v);
		if(v >= 1.0) val = df_1.format(v);
		else if(v >= 0.1) val = df_1.format(v);
		else if(v >= 0.01) val = df_2.format(v);
		else if(v >= 0.001) val = df_3.format(v);
		if(v < 1) val = "0" + val;
		
		if(sign == -1)
			val = "-" + val;
		
		return val;
	}
	
	public static char[] c = new char[]{'k', 'M', 'B', 'T', 'Q'};
	
	public static String moneyFormat(double n)
	{
		return moneyFormat(n,0);
	}
	
	/**
	 * Recursive implementation, invokes itself for each factor of a thousand, increasing the class on each invokation.
	 * @param n the number to format
	 * @param iteration in fact this is the class from the array c
	 * @return a String representing the number n formatted in a cool looking way.
	 */
	private static String moneyFormat(double n, int iteration) {
	    double d = ((long) n / 100) / 10.0;
	    boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
	    return (d < 1000? //this determines the class, i.e. 'k', 'm' etc
	        ((d > 99.9 || isRound || (!isRound && d > 9.99)? //this decides whether to trim the decimals
	         (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
	         ) + "" + c[iteration]) 
	        : moneyFormat(d, iteration+1));
	}
}
