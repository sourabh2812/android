package com.idealyticapps.stoprape.activity;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.idealyticapps.stoprape.R;
import com.idealyticapps.stoprape.gps.GPSTracker;
import com.idealyticapps.stoprape.utility.CommonUtility;
import com.idealyticapps.stoprape.wsutil.WSHelper;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;

public class SignUp extends ActionBarActivity {

	private EditText emailId;

	private EditText pass;

	private EditText username;

	private EditText phone;

	private EditText grpCode;

	private Intent intent;

	private RequestParams params;

	private String email;

	private String password;

	private String name;

	private String phoneNbr;

	private String groupCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_screen);

		emailId = (EditText) findViewById(R.id.label_un);

		pass = (EditText) findViewById(R.id.label_pw);

		username = (EditText) findViewById(R.id.userName);

		phone = (EditText) findViewById(R.id.phoneNo);

		grpCode = (EditText) findViewById(R.id.inviteCode);
	}

	public void getUserSignUp(View view) {

		email = emailId.getText().toString();

		password = pass.getText().toString();

		name = username.getText().toString();

		phoneNbr = phone.getText().toString();

		groupCode = grpCode.getText().toString();

		params = new RequestParams();

		if (CommonUtility.isNotNull(email) && CommonUtility.isNotNull(password) && CommonUtility.isNotNull(name)
				&& CommonUtility.isNotNull(phoneNbr)) {

			// When Email entered is Valid
			if (CommonUtility.validate(email)) {

				GPSTracker gps = new GPSTracker(getApplicationContext());

				if (gps.isGPSEnabled()) {

					// Put Http parameter name
					params.put("name", name);

					// Put Http parameter email
					params.put("email", email);

					// Put Http parameter phoneNbr
					params.put("phoneNbr", phoneNbr);

					// Put Http parameter password
					params.put("password", password);

					// Put Http parameter latitude
					params.put("latitude", String.valueOf(gps.getLatitude()));

					// Put Http parameter longitude
					params.put("longitude", String.valueOf(gps.getLongitude()));

					// Put Http parameter groupCode
					params.put("groupcode", groupCode);

					// Invoke RESTful Web Service with Http parameters
					WSHelper.invokeSignUpService(params, getApplicationContext());

				} else {
					Toast.makeText(getApplicationContext(), "Please turn on Location Services.", Toast.LENGTH_SHORT)
							.show();
				}

			}
			// When Email is invalid
			else {
				Toast.makeText(getApplicationContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
		}
	}

	public void forgetPassword(View view) {

		intent = new Intent(getApplicationContext(), ResetPassword.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		getApplicationContext().startActivity(intent);
	}
}
