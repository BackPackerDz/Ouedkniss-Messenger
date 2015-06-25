package com.squalala.ouedknissmessenger.common;

public class OuedKnissConstant {
	
	public static final String COOKIE_NAME = "PHPSESSID";
	public static final String ID_USER_NAME = "m_id";

	public static final int TIME_OUT = 60000;
	

	public static final String URL_LIST_CONVERSATION = "http://www.ouedkniss.com/fr/membre/connecte/ajax/conversations.php";
	public static final String URL_CONNEXION = "http://www.ouedkniss.com/fr/membre/connexion/";

	public static final String URL_HOME = "http://www.ouedkniss.com/";

	public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	
	public static final String URL_CONNEXION_CHAT = "http://www.ouedkniss.com/fr/membre/messages/message.php?id=";
	public static final String URL_CONVERSATION = "http://www.ouedkniss.com/fr/membre/connecte/chat/ajax/lire.php?sender=";
	
	public static final String URL_SEND_MESSAGE = "http://www.ouedkniss.com/fr/membre/connecte/chat/ajax/envoyer.php";
	
	public static final String TAG_USERNAME = "username_side";
	public static final String TAG_PASSWORD = "password_side";	
	public static final String TAG_REMEMBRE = "remember_me"; 
	public static final String TAG_LOGIN_SAME_PAGE = "login_same_page";
	
	public static final String TAG_MESSAGE = "texte";
	public static final String TAG_ID_MEMBRE = "membre";
	
	public static final int TIME_FETCH_MESSAGE = 1000 * 5;
	public final static int TIME_VIBRATE = 700;
	public final static String BOOT_COMPLETE = "ouedkniss.android.intent.action.BOOT_COMPLETED";

}
