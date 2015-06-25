package com.squalala.ouedknissmessenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyStartServiceReceiverBackground extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
	  Intent service = new Intent(context, ServiceMessagesBackground.class);
	    context.startService(service);
  }
} 