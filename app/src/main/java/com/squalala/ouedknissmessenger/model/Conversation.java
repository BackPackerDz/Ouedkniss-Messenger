/**
 * 
 */
package com.squalala.ouedknissmessenger.model;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Conversation.java
 * Date : 6 oct. 2014	
 * 
 */
public class Conversation {
	
	private String pseudo, idUser, date, message;
	private boolean isUnReadConversation, isMe;
	
	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isUnReadConversation() {
		return isUnReadConversation;
	}

	public void setUnReadConversation(boolean isUnReadConversation) {
		this.isUnReadConversation = isUnReadConversation;
	}

	public boolean isMe() {
		return isMe;
	}

	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}

}
