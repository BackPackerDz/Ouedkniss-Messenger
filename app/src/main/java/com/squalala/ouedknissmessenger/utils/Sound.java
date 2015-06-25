package com.squalala.ouedknissmessenger.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * Nom du fichier : Sound.java
 * Date : 18 sept. 2013 
 * Auteur: Kaddouri Fayçal
 * Description: 
 */
public class Sound extends MediaPlayer {
	
	//private MediaPlayer soundNewMessages;//, soundInnerNewMessages;
	private Context context;
	
	public Sound(Context context) {
		this.context = context;
	}
	
	
	public void startSoundNotification()
	{
		try { 
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    Ringtone r = RingtoneManager.getRingtone(context, notification);
		    r.play();
		} catch (Exception e) {
		    e.printStackTrace();
		} 
	}

}
