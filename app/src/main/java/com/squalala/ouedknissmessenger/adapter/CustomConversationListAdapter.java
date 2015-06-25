/**
 * 
 */
package com.squalala.ouedknissmessenger.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squalala.ouedknissmessenger.R;
import com.squalala.ouedknissmessenger.model.Conversation;

import java.util.List;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CustomConversationListAdapter.java
 * Date : 6 oct. 2014
 * 
 */
public class CustomConversationListAdapter extends BaseAdapter {
	
    private Activity activity;
    private LayoutInflater inflater;
    private List<Conversation> listConversationItems;
  
    public CustomConversationListAdapter(Activity activity, List<Conversation> listConvItems) {
        this.activity = activity;
        this.listConversationItems = listConvItems;
    } 
  
    @Override 
    public int getCount() { 
        return listConversationItems.size();
    } 
  
    @Override 
    public Object getItem(int location) {
        return listConversationItems.get(location);
    } 
  
    @Override 
    public long getItemId(int position) {
        return position;
    } 
  
    @SuppressLint({ "NewApi", "InflateParams" })
	@Override 
    public View getView(int position, View convertView, ViewGroup parent) {
  
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_conversation, null);
  
        TextView message = (TextView) convertView.findViewById(R.id.last_message);
        LinearLayout singleMessageContainer = (LinearLayout)
        		convertView.findViewById(R.id.singleMessageContainer);
     //   TextView date = (TextView) convertView.findViewById(R.id.date);
  
        Conversation conversation = listConversationItems.get(position);
        
     //   pseudo.setText(conversation.getPseudo());
        
        String messageStr = conversation.getMessage();
        
        
        if (messageStr.trim().equals(""))
        	messageStr = "{simley}";
        
        message.setText(messageStr);
        
        
        
      /*  if (conversation.getDate() != null) {
        	date.setText(conversation.getDate());
        	date.setVisibility(View.VISIBLE);
        }
        else {
        	date.setVisibility(View.GONE);
        }
      */

        Log.e("Adapter", ""+conversation.isMe());
        
        // C'est moi
        if (conversation.isMe()) {
        	message.setBackgroundResource(R.drawable.bubble_green);
        } // c'est lui
        else {
        	message.setBackgroundResource(R.drawable.bubble_yellow);
        }
        
        
		singleMessageContainer.setGravity(!conversation.isMe() ? Gravity.LEFT : Gravity.RIGHT);
        
        
        return convertView;
    } 
  
} 