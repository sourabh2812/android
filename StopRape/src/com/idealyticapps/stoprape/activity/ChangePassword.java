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

public class ChangePassword extends ActionBarActivity {

	private EditText mail;

	private EditText oldpass;

	private EditText newpass;

	private String email;

	private String oldpassword;

	private String newpassword;

	private RequestParams params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);

		mail = (EditText) findViewById(R.id.userMailId);

		oldpass = (EditText) findViewById(R.id.oldPass);

		newpass = (EditText) findViewById(R.id.newPass);

	}

	public void changePassword(View view) {

		email = mail.getText().toString();

		oldpassword = oldpass.getText().toString();

		newpassword = newpass.getText().toString();

		params = new RequestParams();

		if (CommonUtility.isNotNull(email) && CommonUtility.isNotNull(oldpassword)
				&& CommonUtility.isNotNull(newpassword)) {

			// When Email entered is Valid
			if (CommonUtility.validate(email)) {

				params.put("email", email);

				params.put("oldpassword", oldpassword);

				params.put("newpassword", newpassword);

				// Invoke RESTful Web Service with Http parameters
				WSHelper.invokeChangePasswordService(params, getApplicationContext());

			} else {
				Toast.makeText(getApplicationContext(), "Please enter a valid email.", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
		}
	}

}
