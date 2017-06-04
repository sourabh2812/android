package com.idealyticapps.stoprape.activity;

import com.idealyticapps.stoprape.R;
import com.idealyticapps.stoprape.utility.CommonUtility;
import com.idealyticapps.stoprape.wsutil.WSHelper;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends ActionBarActivity {

	private EditText mail;

	private RequestParams params;

	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password);
		mail = (EditText) findViewById(R.id.resetUserMail);
	}

	public void getPassword(View view) {
		email = mail.getText().toString();

		params = new RequestParams();

		if (CommonUtility.isNotNull(email)) {

			// When Email entered is Valid
			if (CommonUtility.validate(email)) {

				params.put("email", email);

				// Invoke RESTful Web Service with Http parameters

				WSHelper.invokeUserResetService(params, getApplicationContext());

			} else {
				Toast.makeText(getApplicationContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
		}
	}

}
