/**
 * 
 */
package com.squalala.ouedknissmessenger.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.squalala.ouedknissmessenger.common.OuedKnissConstant;

import java.util.Map;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Utils.java
 * Date : 23 sept. 2014
 * 
 */
public class Utils {
	
	public static double d;

	/**
	 *  Pour récupérer le cookie
	 */
	@SuppressWarnings("rawtypes")
	public static String getCookie(Map<String, String> map) {
		
		String cookie = null;
		
		for (Map.Entry entry : map.entrySet()) { 
			if (entry.getKey().equals(OuedKnissConstant.COOKIE_NAME)) {
				cookie = String.valueOf(entry.getValue());
				System.out.println("UTILS , RESULT --> " + entry.getKey()  + " -- "+ entry.getValue());
				break;
			}
			
			
		} 
		
		return cookie;
	}
	
	public static String removeNonDigits(final String str) {
		   if (str == null || str.length() == 0) {
		       return ""; 
		   } 
		   return str.replaceAll("[^0-9]+", "");
	} 
	
	public static boolean isNumeric(String str)  
	{   
	  try   
	  {   
	    d = Double.parseDouble(str);  
	  }   
	  catch(NumberFormatException nfe)  
	  {   
	    return false;   
	  }   
	  return true;   
	} 
	
	public static boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try { 
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true; 
	    } catch (NameNotFoundException e) {
	        return false; 
	    } 
	} 

}
