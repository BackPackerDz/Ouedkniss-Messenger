package com.squalala.ouedknissmessenger.service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squalala.ouedknissmessenger.ConversationActivity;
import com.squalala.ouedknissmessenger.R;
import com.squalala.ouedknissmessenger.common.OuedKnissConstant;
import com.squalala.ouedknissmessenger.model.Conversation;
import com.squalala.ouedknissmessenger.utils.Preferences;
import com.squalala.ouedknissmessenger.utils.Sound;
import com.squalala.ouedknissmessenger.utils.Utils;
import com.squalala.ouedknissmessenger.utils.Vibrating;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;


public class ServiceMessagesBackground extends Service {
  
  private Sound sound;
  private Vibrating vibrating;
  private Preferences preferences;
  

  @Override
  public int onStartCommand(final Intent intent, int flags, int startId) {

	  
	  preferences = new Preferences(getApplicationContext());
	  sound = new Sound(getApplicationContext());
	  vibrating = new Vibrating(getApplicationContext());

      if (!preferences.isInApp())
        new ServiceMessagesCheck().execute();

     return Service.START_NOT_STICKY;
  }
  
  private class ServiceMessagesCheck extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... params) {
		Document doc;

		Connection.Response res = null, res1 = null;
		try {

			Connection.Response res0 = Jsoup.connect(OuedKnissConstant.URL_HOME)
					.header("Referer", OuedKnissConstant.URL_HOME)
					.header("Origin", OuedKnissConstant.URL_HOME)
					.header("Content-Type", OuedKnissConstant.CONTENT_TYPE)
					.header("Host", "www.ouedkniss.com")
					.method(Method.GET)
					.timeout(OuedKnissConstant.TIME_OUT)
					.execute();


			res = Jsoup.connect(OuedKnissConstant.URL_CONNEXION)
					.header("Referer", OuedKnissConstant.URL_HOME)
					.header("Origin", OuedKnissConstant.URL_HOME)
					.header("Content-Type", OuedKnissConstant.CONTENT_TYPE)
					.header("Host", "www.ouedkniss.com")
					.data(OuedKnissConstant.TAG_USERNAME, preferences.getUsername())
					.data(OuedKnissConstant.TAG_PASSWORD, preferences.getPassword())
					.data(OuedKnissConstant.TAG_REMEMBRE, "1")
					.data(OuedKnissConstant.TAG_LOGIN_SAME_PAGE, "0")
					.cookies(res0.cookies())
					.method(Method.POST)
					.timeout(OuedKnissConstant.TIME_OUT)
                    .execute();

            res1 = Jsoup.connect("http://www.ouedkniss.com/fr/membre/")
					.header("Referer", OuedKnissConstant.URL_HOME)
                    .header("Origin", OuedKnissConstant.URL_HOME)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Host", "www.ouedkniss.com")
                    .cookies(res0.cookies())
                    .method(Method.GET)
                    .timeout(OuedKnissConstant.TIME_OUT)
					.followRedirects(true)
					.execute();

			preferences.setCookie(Utils.getCookie(res0.cookies()));

            System.out.println("LIST COOKIE BACKGROUND " +  preferences.getCookie());
			doc = Jsoup.connect(OuedKnissConstant.URL_LIST_CONVERSATION)
					.header("Referer", "http://www.ouedkniss.com/")
					.header("Origin", "http://www.ouedkniss.com")
					.header("Content-Type", "application/x-www-form-urlencoded")
					.header("Host", "www.ouedkniss.com")
					.cookies(res.cookies())
					.get();
			
			
			System.out.println("RES --> " + doc.body().toString());
			
			Elements links = doc.select("div.topbar_conversation");
			
			for (Element link : links) {
				
				Conversation conversation = new Conversation();
				
				conversation.setIdUser(Utils.removeNonDigits(link.select("a[href].topbar_conversation_supprimer").toString()));
				conversation.setPseudo(link.select("a.topbar_conversation_nom").text());
				conversation.setMessage(link.select("span.topbar_conversation_message").text());
				conversation.setUnReadConversation(link.toString().contains("topbar_conversation_new"));
				
			   if (conversation.isUnReadConversation()) {
				   String message = conversation.getPseudo() + ": " + conversation.getMessage();
				   
				   doc = Jsoup.connect(OuedKnissConstant.URL_CONNEXION_CHAT + conversation.getIdUser())
						    .header("Referer", "http://www.ouedkniss.com/")
						    .cookies(res.cookies())
							.get();
					
					doc = Jsoup.connect(OuedKnissConstant.URL_CONVERSATION + conversation.getIdUser())
							.header("Referer", "http://www.ouedkniss.com/")
							.cookies(res.cookies())
							.get();
				   
				   makeNotificationMessages(message, 
						   conversation.getIdUser(), conversation.getPseudo());
				   sound.startSoundNotification();
				   vibrating.vibrate();
			   }
	 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	  
  }
  
  

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }

  @SuppressLint("NewApi")
  private void makeNotificationMessages(String message, String idMembre, String pseudo)
  {
	  Intent notificationIntent = new Intent(getApplicationContext(), ConversationActivity.class);
	  notificationIntent.putExtra("id_sender", idMembre);
      notificationIntent.putExtra("pseudo", pseudo);
	  
	  int randomNumber = new Random().nextInt(1000);


      Intent intent = new Intent(this, ConversationActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.putExtra("id_sender", idMembre);
      intent.putExtra("pseudo", pseudo);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
              PendingIntent.FLAG_ONE_SHOT);

      Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
              .setSmallIcon(R.drawable.ic_launcher)
              .setContentTitle(getString(R.string.app_name))
              .setContentText(message)
              .setWhen(System.currentTimeMillis())
              .setAutoCancel(true)
              .setSound(defaultSoundUri)
              .setContentIntent(pendingIntent);

      NotificationManager notificationManager =
              (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      // On génère un nombre aléatoire pour pouvoir afficher plusieurs notifications
      notificationManager.notify(new Random().nextInt(9999), notificationBuilder.build());

	/*  if (Build.VERSION.SDK_INT < 15)
	  {

	  PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
	         randomNumber, notificationIntent,
	          PendingIntent.FLAG_CANCEL_CURRENT);

	  NotificationManager nm = (NotificationManager) getApplicationContext()
	          .getSystemService(Context.NOTIFICATION_SERVICE);

	  Resources res = getApplicationContext().getResources();
	  NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

	  builder.setContentIntent(contentIntent)
	              .setSmallIcon(R.drawable.ic_launcher)
	              .setWhen(System.currentTimeMillis())
	              .setAutoCancel(true)
	              .setContentTitle(res.getString(R.string.app_name))
	              .setContentText(message);
	  Notification n = builder.build();

	  nm.notify(randomNumber, n);

	  }
	  else
	  {

	  NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(getString(R.string.app_name))
		        .setAutoCancel(true)
		        .setContentText(message);
		// Creates an explicit intent for an Activity in your app

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(ConversationActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            randomNumber,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify( randomNumber, mBuilder.build());

	  }*/
  
  
		
  }


} 