package com.idealyticapps.mousepad.activity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.idealyticapps.mousepad.R;
import com.idealyticapps.mousepad.utility.CommonUtility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectionActivity extends Activity {

	private EditText ipAddress;

	private EditText portNbr;

	private String ip;

	private String port;

	// Shared preferences
	private SharedPreferences preferences;

	private boolean isConnected = false;

	protected TextView switchStatus;

	protected Switch slideSwitch;

	public static PrintWriter out;

	// Intent for redirection
	protected Intent intent;

	public static Socket socket;

	public boolean switchOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);

		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		ipAddress = (EditText) findViewById(R.id.ipAddress);

		// Retrive last entered IP
		ipAddress.setText(preferences.getString("IPAddress", ""));

		portNbr = (EditText) findViewById(R.id.portNbr);

		// Retrive last entered IP
		portNbr.setText(preferences.getString("port", ""));

		slideSwitch = (Switch) findViewById(R.id.slideSwitch);

		slideSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					switchOn = true;
					Log.d("DEBUG", "Switch Toggled");
				} else {
					switchOn = false;
					Log.d("DEBUG", "Switch UnToggled");
				}
			}
		});
	}

	public void getConnection(View view) {

		/*
		 * intent = new Intent(this, MousePad.class);
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * startActivity(intent);
		 */

		ip = ipAddress.getText().toString();

		port = portNbr.getText().toString();

		if (CommonUtility.isNotNull(ip) && CommonUtility.isNotNull(port)) {

			Log.d("DEBUG", "Entered IP address is-" + ip);

			Log.d("DEBUG", "Entered Port No. is-" + port);

			// Saving IP Address from user
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("IPAddress", ip);
			editor.putString("port", port);
			editor.apply();

			ConnectPhoneTask connectPhoneTask = new ConnectPhoneTask();
			// try to connect to server in another thread
			connectPhoneTask.execute(ip);
		} else {
			Toast.makeText(getApplicationContext(), "IP address and Port are required fields.", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public class ConnectPhoneTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = true;
			try {
				InetAddress serverAddr = InetAddress.getByName(params[0]);
				// Open socket on server IP and port
				socket = new Socket(serverAddr, Integer.parseInt(port));
			} catch (IOException e) {
				Log.e("remotedroid", "Error while connecting", e);
				result = false;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			isConnected = result;
			Toast.makeText(getApplicationContext(), isConnected ? "Connected to server!" : "Error while connecting",
					Toast.LENGTH_SHORT).show();
			try {
				if (isConnected) {
					Log.d("DEBUG", "Connected");
					// create output stream to send data to server
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
					// If Motion Sensor Switch is On.
					if (switchOn) {
						intent = new Intent(getApplicationContext(), MousePad.class);
					} else {
						intent = new Intent(getApplicationContext(), TouchPad.class);
					}
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			} catch (IOException e) {
				Log.e("remotedroid", "Error while creating OutWriter", e);
				Toast.makeText(getApplicationContext(), "Error while connecting", Toast.LENGTH_SHORT).show();
			}
		}
	}

}