/**
 * 
 */
package com.squalala.ouedknissmessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.squalala.ouedknissmessenger.adapter.CustomListAdapter;
import com.squalala.ouedknissmessenger.common.BaseActivtiy;
import com.squalala.ouedknissmessenger.common.OuedKnissConstant;
import com.squalala.ouedknissmessenger.model.Conversation;
import com.squalala.ouedknissmessenger.utils.Utils;

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
 * Nom du fichier : ListConversationActivity.java
 * Date : 6 oct. 2014
 * 
 */
public class ListConversationActivity extends BaseActivtiy {
	
	private CustomListAdapter adapter;
    private List<Conversation> conversationList = new ArrayList<Conversation>();
    private Activity activity = this;
    private Handler handler;
    private Intent intent;
    
    @InjectView(R.id.list) ListView listView;
    @InjectView(R.id.txtEmpty) TextView txtEmpty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_conversation);
		
		ButterKnife.inject(this);
		
		adapter = new CustomListAdapter(this, conversationList);
		listView.setAdapter(adapter);
		handler = new Handler();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				Intent intent = new Intent(activity, ConversationActivity.class);
				intent.putExtra("id_sender", conversationList.get(position).getIdUser());
				intent.putExtra("pseudo", conversationList.get(position).getPseudo());
				startActivity(intent);

			}
		});

		intent = new Intent();
		intent.setAction(OuedKnissConstant.BOOT_COMPLETE);
		sendBroadcast(intent);
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
	      new ListConversation().execute();
	      handler.postDelayed(mStatusChecker, 10000);
	    } 
	  }; 

    private void startRepeatingTask() { 
	    mStatusChecker.run(); 
    } 
	 
    private void stopRepeatingTask() { 
	    handler.removeCallbacks(mStatusChecker);
    } 	
	
	private class ListConversation extends AsyncTask<String, String, Elements> {

		@Override
		protected Elements doInBackground(String... params) {
			
			Document doc = null;
		 
				try {
					
					Log.e("COOKIE",  ""+preferences.getCookie());
					
					doc = Jsoup.connect("http://www.ouedkniss.com/fr/membre/ajax/keep_alive.php?reload=1")
							.cookie(OuedKnissConstant.COOKIE_NAME, preferences.getCookie())
							.header("Referer", "http://www.ouedkniss.com/")
							.get();
					
					//System.out.println("RES f --> " + doc.body().toString());
					
					doc = Jsoup.connect(OuedKnissConstant.URL_LIST_CONVERSATION)
							.header("Referer", "http://www.ouedkniss.com/")
							.cookie(OuedKnissConstant.COOKIE_NAME, preferences.getCookie())
							.get();
					
				//	System.out.println("RES --> " + doc.body().toString());
					} catch (IOException e) {
					e.printStackTrace();
				}
			
			
			return doc.select("div.topbar_conversation");
		} 
		
		@Override
		protected void onPostExecute(Elements result) {
			super.onPostExecute(result);
			
			conversationList.clear();
			
			for (Element link : result) {
				
				Conversation conversation = new Conversation();

				conversation.setDate(link.select("span.topbar_conversation_time").text());
				conversation.setIdUser(Utils.removeNonDigits(link.select("a[href].topbar_conversation_supprimer").toString()));
				conversation.setPseudo(link.select("a.topbar_conversation_nom").text());
				conversation.setMessage(link.select("span.topbar_conversation_message").text());
				conversation.setUnReadConversation(link.toString().contains("topbar_conversation_new"));
	 
				conversationList.add(conversation);
			}
			
			adapter.notifyDataSetChanged();
			
			if (conversationList.size() == 0)
				txtEmpty.setVisibility(View.VISIBLE);
			else
				txtEmpty.setVisibility(View.GONE);
		}
		
	}

}
