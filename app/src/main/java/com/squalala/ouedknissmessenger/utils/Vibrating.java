package com.squalala.ouedknissmessenger.utils;


import android.content.Context;
import android.os.Vibrator;

import com.squalala.ouedknissmessenger.common.OuedKnissConstant;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Vibrating.java
 * Date : 14 août 2014
 * 
 */
public class Vibrating {
	
	private Context context;
	
	public Vibrating(Context context) {
		this.context = context;
	}
	
	public void vibrate() {
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(OuedKnissConstant.TIME_VIBRATE);
	}

}
