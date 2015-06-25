package com.squalala.ouedknissmessenger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.squalala.ouedknissmessenger.common.OuedKnissConstant;
import com.squalala.ouedknissmessenger.utils.Preferences;
import com.squalala.ouedknissmessenger.utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : LoginActivity.java
 * Date : 6 octobre. 2014
 * 
 */
public class LoginActivity extends Activity 
	implements OnClickListener {
	
	@InjectView(R.id.editPassword) BootstrapEditText editPassword;
	@InjectView(R.id.editUsername) BootstrapEditText editUsername;
	@InjectView(R.id.btn_login) BootstrapButton btn_login;

	private Preferences preferences;
	private Activity activity = this;
	private Map<String, String> map;
	private static final String REMEMBRE_ME = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ButterKnife.inject(this);
		
		btn_login.setOnClickListener(this);

		
		preferences = new Preferences(this);
		
		/*
		 * La personne a déjà mis les bons identifiants
		 *  donc on l'envoie direct sur la MainAcitivity
		 *  en se reconnectant pour avoir un cookie valide
		 */
		if (preferences.getPassword() != null) {
			editPassword.setText(preferences.getPassword());
			editUsername.setText(preferences.getUsername());
			new LoginTask().execute();
		}


	}


	public String getUsername() {
		return editUsername.getText().toString().trim();
	}
	
	public String getPassword() {
		return editPassword.getText().toString().trim();
	}
	
	class LoginTask extends AsyncTask<String, String, String> {
		
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage("Patientez...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			Connection.Response res = null , res1 = null;
			Document document = null;
			
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
						.data(OuedKnissConstant.TAG_USERNAME, getUsername())
						.data(OuedKnissConstant.TAG_PASSWORD, getPassword())
						.data(OuedKnissConstant.TAG_REMEMBRE, REMEMBRE_ME)
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
				
				map = res0.cookies();
				
				document = Jsoup.parse(res1.body().toString());
			
	//	 System.out.println("RESULT --> " + res.body().toString());
     //    System.out.println("RESULT 1 --> " + res1.body().toString());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				return document.title();
			}
			catch (NullPointerException e) {}
			
			return "error";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			pDialog.dismiss();
			
			/*
			 *  Si il n'y a pas la phrase "Pseudo ou Mot de passe incorrect" dans la page HTML 
			 *  C'est que la personne a rentré les bons identifiants
			 */
			if (!result.equals("Connexion à votre compte Ouedkniss")) {
				
				preferences.setPassword(getPassword());
				preferences.setUsername(getUsername());
				preferences.setCookie(Utils.getCookie(map));

				Intent intent = new Intent(activity, ListConversationActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
				activity.finish();
			}
			else if (result.equals("error")) {
				Toast.makeText(activity, "Veuillez vérifier votre connexion internet",
						Toast.LENGTH_LONG).show();
			}
			else { 
				Toast.makeText(getApplicationContext(),
						"Pseudo ou mot de passe incorrect",
						Toast.LENGTH_LONG).show();
			}
				
			
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {

		case R.id.btn_login:
			
			if (getPassword().length() > 0 && getUsername().length() > 0)
				new LoginTask().execute();
			else {
				Toast.makeText(activity, "Vérifiez les champs",
						Toast.LENGTH_LONG).show();
			}
			
			break;


		default:
			break;
		}
	}

}
