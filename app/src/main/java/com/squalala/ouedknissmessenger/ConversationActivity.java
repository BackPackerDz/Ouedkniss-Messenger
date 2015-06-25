/**
 * 
 */
package com.squalala.ouedknissmessenger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.squalala.ouedknissmessenger.adapter.CustomConversationListAdapter;
import com.squalala.ouedknissmessenger.common.BaseActivtiy;
import com.squalala.ouedknissmessenger.common.OuedKnissConstant;
import com.squalala.ouedknissmessenger.model.Conversation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ConversationActivity.java
 * Date : 6 oct. 2014
 * 
 */
public class ConversationActivity extends BaseActivtiy
	implements OnClickListener {
	
	private List<Conversation> conversationList = new ArrayList<Conversation>();
	private CustomConversationListAdapter cuAdapter;
	private Handler handler;
	private String idSender, message;

	@InjectView(R.id.txtEmpty) TextView txtEmpty;
	@InjectView(R.id.list) ListView listView;
	@InjectView(R.id.editMessage) BootstrapEditText editMessage;
	@InjectView(R.id.fontConversation) ImageView imgSendMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversation);
		
		ButterKnife.inject(this);
		cuAdapter = new CustomConversationListAdapter(this, conversationList);
		listView.setAdapter(cuAdapter);
		handler = new Handler();
		idSender = getIntent().getStringExtra("id_sender");
		
		setTitle(getIntent().getStringExtra("pseudo"));
		
		listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		
		imgSendMessage.setOnClickListener(this);
	}
	


	 @Override 
	  public void onResume() { 
	     startRepeatingTask();
	     super.onResume(); 
	  } 
	 
	  @Override 
	  public void onPause() { 
	     stopRepeatingTask();
	     super.onPause(); 
	  } 
	  
	  private Runnable mStatusChecker = new Runnable() {
		    @Override  
		    public void run() { 
		      new LoadConversation().execute();
		      handler.postDelayed(mStatusChecker, 10000);
		    } 
		  }; 
	
	    private void startRepeatingTask() { 
		    mStatusChecker.run(); 
	    } 
		 
	    private void stopRepeatingTask() { 
		    handler.removeCallbacks(mStatusChecker);
	    } 	
	    
	
	    public String getMessage() {
	    	return editMessage.getText().toString().trim();
	    }
	    

	private class LoadConversation extends AsyncTask<String, String, Elements> {

		@Override
		protected Elements doInBackground(String... params) {
			
			Document doc = null;
			 
				try {
					
					doc = Jsoup.connect(OuedKnissConstant.URL_CONNEXION_CHAT + idSender)
							.header("Referer", OuedKnissConstant.URL_HOME)
							.cookie(OuedKnissConstant.COOKIE_NAME, preferences.getCookie())
							.get();
					
					doc = Jsoup.connect(OuedKnissConstant.URL_CONVERSATION + idSender)
							.header("Referer", OuedKnissConstant.URL_HOME)
							.cookie(OuedKnissConstant.COOKIE_NAME, preferences.getCookie())
							.get();
					
				///	System.out.println("RESUTL -->" +doc.body().toString());
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
			return doc.select("div");
		}
		
		@Override
		protected void onPostExecute(Elements result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			conversationList.clear();
				
			for (Element link : result) {
				
				Conversation conversation = new Conversation();
				
				String date, message;
				
				message = link.select("span.message_texte").text();
				
				// Il y a la date et on doit la supprimer du message
				if (!TextUtils.isEmpty(link.select("span.message_time").text())) {
					date = link.select("span.message_time").text();
					message = message.replace(date, "");
				}
				else
					date = null;
				
				conversation.setDate(date);
				conversation.setMessage(message);
				conversation.setMe(!link.toString().contains(idSender));


				conversationList.add(conversation);
			}
			
			cuAdapter.notifyDataSetChanged();
			
			if (conversationList.size() == 0)
				txtEmpty.setVisibility(View.VISIBLE);
			else
				txtEmpty.setVisibility(View.GONE);
			
		}
		
	}
	
	private class SendMessage extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			Document doc;
			
			try {
				
			//	System.out.println("RESUTL SEND COOK-->" +preferences.getCookie());
				
				doc = Jsoup.connect(OuedKnissConstant.URL_CONNEXION_CHAT + idSender)
						.header("Referer", "http://www.ouedkniss.com/")
						.cookie(OuedKnissConstant.COOKIE_NAME, preferences.getCookie())
						.get();
				
				doc = Jsoup.connect(OuedKnissConstant.URL_SEND_MESSAGE)
						.cookie(OuedKnissConstant.COOKIE_NAME, preferences.getCookie())
						.header("Referer", "http://www.ouedkniss.com/")
						.data(OuedKnissConstant.TAG_MESSAGE, message)
						.data(OuedKnissConstant.TAG_ID_MEMBRE, idSender)
						.post();
				
				
				//System.out.println("RES " + doc.body().toString());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			new LoadConversation().execute();
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {

		case R.id.fontConversation:
			
			if (getMessage().length() > 0) {
				message = getMessage();
				editMessage.setText("");
				new SendMessage().execute();
			}
				
			
			break;

		default:
			break;
		}
		
	}
	

}
