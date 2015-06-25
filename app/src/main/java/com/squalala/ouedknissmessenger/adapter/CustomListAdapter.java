package com.squalala.ouedknissmessenger.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.ouedknissmessenger.R;
import com.squalala.ouedknissmessenger.model.Conversation;
import com.squareup.picasso.Picasso;

import java.util.List;

@SuppressLint("InflateParams")
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Conversation> listConversationItems;
  
    public CustomListAdapter(Activity activity, List<Conversation> listConvItems) {
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
  
    @Override 
    public View getView(int position, View convertView, ViewGroup parent) {
  
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);
  
        TextView pseudo = (TextView) convertView.findViewById(R.id.pseudo);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.last_message);
        ImageView avatar = (ImageView) convertView.findViewById(R.id.thumbnail);
        FontAwesomeText fontStateConversation = (FontAwesomeText)
        		convertView.findViewById(R.id.fontStateConversation);
        
        Picasso.with(activity)
        	.load(R.drawable.default_avatar)
        	.into(avatar);
  
        Conversation conversation = listConversationItems.get(position);
        
        pseudo.setText(conversation.getPseudo());
        date.setText(conversation.getDate());
        
        String message = conversation.getMessage();
        
        if (message.trim().equals(""))
        	message = "{simley}";
        
        lastMessage.setText(message);
        
        if (conversation.isUnReadConversation())
        	fontStateConversation.setVisibility(View.VISIBLE);
        else
        	fontStateConversation.setVisibility(View.GONE);
        
        return convertView;
    } 
  
} 