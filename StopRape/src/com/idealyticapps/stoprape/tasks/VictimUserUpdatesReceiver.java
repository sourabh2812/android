package com.idealyticapps.stoprape.tasks;

import com.idealyticapps.stoprape.gps.GPSTracker;
import com.idealyticapps.stoprape.wsutil.WSHelper;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class VictimUserUpdatesReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		GPSTracker gps = new GPSTracker(context);

		RequestParams params = new RequestParams();

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		params.put("email", preferences.getString("emailId", "user@user.com"));
		params.put("latitude", String.valueOf(gps.getLatitude()));
		params.put("longitude", String.valueOf(gps.getLongitude()));
		
		WSHelper.invokeVictimCheckService(params, context);
	}

}