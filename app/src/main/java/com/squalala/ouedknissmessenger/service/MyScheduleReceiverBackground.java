package com.squalala.ouedknissmessenger.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squalala.ouedknissmessenger.common.OuedKnissConstant;

import java.util.Calendar;

public class MyScheduleReceiverBackground extends BroadcastReceiver {
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		

		AlarmManager service;

		service = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, MyStartServiceReceiverBackground.class);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND,  1);
		// InexactRepeating allows Android to optimize the energy consumption
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(),
				OuedKnissConstant.TIME_FETCH_MESSAGE,
				pending);

		System.out.println("SERVICE START BACKGROUND");

	}

}
