/**
 * 
 */
package com.squalala.ouedknissmessenger.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Preferences.java
 * Date : 22 sept. 2014
 * 
 */
public class Preferences {

	private SharedPreferences preferences;
	
	public Preferences(Context context) {
		preferences = context.getSharedPreferences("ouedkniss", MODE_PRIVATE);
	}
	
	public void setCookie(String Cookie) {
		preferences.edit().putString("cookie", Cookie).commit();
	}
	
	public void setUsername(String username) {
		preferences.edit().putString("username", username).commit();
	}
	
	public void setPassword(String password) {
		preferences.edit().putString("password", password).commit();
	}

	public void setInApp(boolean value) {
		preferences.edit().putBoolean("in_app_", value).commit();
	}

	public boolean isInApp() {
		return preferences.getBoolean("in_app_", false);
	}
	
	public String getCookie() {
		return preferences.getString("cookie", ""); 
	}
	
	public String getUsername() {
		return preferences.getString("username", ""); 
	}
	
	public String getPassword() {
		return preferences.getString("password", null); 
	}
	

}
