package com.idealyticapps.stoprape.activity;

import android.support.v7.app.ActionBarActivity;
import com.idealyticapps.stoprape.R;
import com.idealyticapps.stoprape.gps.GPSTracker;
import com.idealyticapps.stoprape.utility.CommonUtility;
import com.idealyticapps.stoprape.wsutil.WSHelper;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private EditText emailId;

	private EditText password;

	// Intent for redirection
	private Intent intent;

	// SharedPrefs obj for session handling
	private SharedPreferences preferences;

	// Obj for GPS Services
	private GPSTracker gps;

	// Parameters for WS services
	private RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check for existing session
		checkForActiveSession();
	}

	public void checkForActiveSession() {
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if (!preferences.getString("emailId", "na").equalsIgnoreCase("na")) {
			params = new RequestParams();

			params.put("email", preferences.getString("emailId", ""));

			// Put Http parameter password with value of Password Edit
			params.put("password", preferences.getString("password", ""));

			params.put("latitude", preferences.getString("latitude", ""));

			params.put("longitude", preferences.getString("longitude", ""));

			// Invoke RESTful Web Service with Http parameters
			WSHelper.invokeLoginService(params, getApplicationContext());
		} else {
			setContentView(R.layout.activity_main);
			emailId = (EditText) findViewById(R.id.etUserName);
			password = (EditText) findViewById(R.id.etPass);
		}
	}

	public void getSignUp(View view) {
		intent = new Intent(this, SignUp.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void loginUser(View view) {

		String username = emailId.getText().toString();

		String pass = password.getText().toString();

		params = new RequestParams();

		gps = new GPSTracker(getApplicationContext());

		if (CommonUtility.isNotNull(username) && CommonUtility.isNotNull(pass)) {

			// When Email entered is Valid
			if (CommonUtility.validate(username)) {

				if (gps.isLocationEnabled()) {

					// Saving creadential in Session
					preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("emailId", username);

					editor.putString("password", pass);

					editor.apply();

					// Put Http parameter username with value of Email Edit View
					params.put("email", username);

					// Put Http parameter password with value of Password Edit
					// View
					params.put("password", pass);

					params.put("latitude", String.valueOf(gps.getLatitude()));

					params.put("longitude", String.valueOf(gps.getLongitude()));

					// Invoke RESTful Web Service with Http parameters
					WSHelper.invokeLoginService(params, getApplicationContext());
				} else {
					Toast.makeText(getApplicationContext(),
							"Please enable Location Services from Settings to continue login.", Toast.LENGTH_LONG)
							.show();
				}
			}
			// When Email is invalid
			else {
				Toast.makeText(getApplicationContext(), "Please enter a valid email.", Toast.LENGTH_LONG).show();
			}
		}

		else {
			Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_LONG).show();
		}
	}

	public void forgetPassword(View view) {

		intent = new Intent(getApplicationContext(), ResetPassword.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		getApplicationContext().startActivity(intent);
	}

	public void changePassword(View view) {

		intent = new Intent(getApplicationContext(), ChangePassword.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		getApplicationContext().startActivity(intent);
	}

}