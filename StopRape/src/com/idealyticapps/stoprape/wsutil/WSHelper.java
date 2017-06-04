package com.idealyticapps.stoprape.wsutil;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.idealyticapps.stoprape.R;
import com.idealyticapps.stoprape.activity.Dashboard;
import com.idealyticapps.stoprape.activity.MainActivity;
import com.idealyticapps.stoprape.bean.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public abstract class WSHelper {

	/**
	 * URL for login service.
	 */
	private final static String LOGIN_USER_URL = "https://stoprapews-idealyticapps.rhcloud.com/StopRapeWS/loginUser";

	/**
	 * URL for password reset service.
	 */
	private final static String RESET_USER_PASS_URL = "https://stoprapews-idealyticapps.rhcloud.com/StopRapeWS/forgotPassword";

	/**
	 * URL for register new user service.
	 */
	private final static String ADD_USER_URL = "https://stoprapews-idealyticapps.rhcloud.com/StopRapeWS/addUser";

	/**
	 * URL for changing user password service.
	 */
	private final static String CHNAGE_USER_PASS_URL = "https://stoprapews-idealyticapps.rhcloud.com/StopRapeWS/changePassword";

	/**
	 * URL for updating users location service.
	 */
	private final static String UPDATE_USER_LOCATION_URL = "https://stoprapews-idealyticapps.rhcloud.com/StopRapeWS/updateUserLocation";

	/**
	 * URL for sending danger alert to in range users.
	 */
	private final static String SEND_ALERT_LOCATION_URL = "https://stoprapews-idealyticapps.rhcloud.com/StopRapeWS/sendAlert";

	/**
	 * URL for fetching nearby users who are in danger.
	 */
	private final static String FETCH_VICTIM_USERS_URL = "https://stoprapews-idealyticapps.rhcloud.com/StopRapeWS/fetchVictims";

	/**
	 * MediaPlayer obj for playing alert sound.
	 */
	public static MediaPlayer mp;

	/**
	 * MediaPlayer user stopped flag.
	 */
	public static Boolean stoppedByUser = false;

	/**
	 * Notification obj for sending lock screen notification.
	 */
	private static Notification notification;

	/**
	 * Method that performs login action using RESTful webservice
	 * 
	 * @param params
	 */
	public static void invokeLoginService(final RequestParams params, final Context context) {

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(LOGIN_USER_URL, params, new AsyncHttpResponseHandler() {

			// When the response returned by REST has Http response code '200'

			@Override
			public void onSuccess(String response) {

				ArrayList<User> userlist = new ArrayList<User>();

				try {
					JSONArray jarr = new JSONArray(response);

					for (int i = 0; i < jarr.length(); ++i) {
						JSONObject obj = jarr.getJSONObject(i);
						User user = new User();

						// Inserting the userId into the Application Session
						if (Boolean.parseBoolean(obj.getString("isCurrentUser"))) {
							SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
							SharedPreferences.Editor editor = preferences.edit();
							editor.putString("name", obj.getString("name"));
							editor.putString("phoneNbr", obj.getString("phoneNbr"));
							editor.putString("latitude", obj.getString("latitude"));
							editor.putString("longitude", obj.getString("longitude"));
							editor.putString("dangerFlag", obj.getString("dangerFlag"));
							editor.apply();
						}
						user.setName(obj.getString("name"));
						user.setEmailId(obj.getString("emailId"));
						user.setPhoneNo(obj.getString("phoneNbr"));
						user.setLatitude(Double.parseDouble(obj.getString("latitude")));
						user.setLongitude(Double.parseDouble(obj.getString("longitude")));
						user.setLastUpdated(obj.getString("lastUpdated"));
						user.setIsCurrentUser(Boolean.parseBoolean(obj.getString("isCurrentUser")));
						user.setDangerFlag(Integer.parseInt(obj.getString("dangerFlag")));
						userlist.add(user);
					}

					Intent intent = new Intent(context, Dashboard.class);

					intent.putExtra("userlist", userlist);

					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					context.startActivity(intent);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			// When the response returned by REST has Http code other than '200'

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {

				// Clearing session data
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

				SharedPreferences.Editor editor = preferences.edit();
				editor.clear();
				editor.commit();

				if (statusCode == 404) {
					Toast.makeText(context, "Incorrect username or password entered.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Remote server is down. Please try again later.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	/**
	 * Method that performs Sigup action using RESTful webservice
	 * 
	 * @param params
	 */
	public static void invokeSignUpService(final RequestParams params, final Context context) {

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(ADD_USER_URL, params, new AsyncHttpResponseHandler() {

			// When the response returned by REST has Http response code '200'

			@Override
			public void onSuccess(String response) {

				Toast.makeText(context, "User registered successfully.", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(context, MainActivity.class);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				context.startActivity(intent);
			}

			// When the response returned by REST has Http code other than '200'

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {

				if (statusCode == 404) {
					Toast.makeText(context, "Email ID is already registered with another user.", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(context, "Remote server is down. Please try again later.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	/**
	 * Method that performs Reset password action using RESTful webservice
	 * 
	 * @param params
	 */
	public static void invokeUserResetService(RequestParams params, final Context context) {

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(RESET_USER_PASS_URL, params, new AsyncHttpResponseHandler() {

			// When the response returned by REST has Http response code '200'

			@Override
			public void onSuccess(String response) {

				Toast.makeText(context, "Your password has been sent to your Email ID.", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, MainActivity.class);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

			}

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {

				if (statusCode == 404) {
					Toast.makeText(context, "You are not a registered user. Please go to Sign Up screen to register.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Remote server is down. Please try again later.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	/**
	 * Method that performs change password action using RESTful webservice
	 * 
	 * @param params
	 */
	public static void invokeChangePasswordService(RequestParams params, final Context context) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(CHNAGE_USER_PASS_URL, params, new AsyncHttpResponseHandler() {

			// When the response returned by REST has Http response code '200'

			@Override
			public void onSuccess(String response) {

				Toast.makeText(context, "Your password has been changed successfully. Please login.",
						Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(context, MainActivity.class);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				context.startActivity(intent);
			}

			// When the response returned by REST has Http code other than '200'

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {

				if (statusCode == 404) {
					Toast.makeText(context, "Entered email and password does not match our records.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Remote server is down. Please try again later.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

	}

	/**
	 * Method that performs location update action using RESTful webservice
	 * 
	 * @param params
	 */
	public static void invokeLocUpdateService(RequestParams params, final Context context) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(UPDATE_USER_LOCATION_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {

				Log.d("DEBUG", "Your Location has been updated.");
			}

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {

				Log.d("DEBUG", "Location data not updated with server.");
			}

		});

	}

	/**
	 * Method that performs alert update action using RESTful webservice
	 * 
	 * @param params
	 */
	public static void invokeSendAlertService(RequestParams params, final Context context) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(SEND_ALERT_LOCATION_URL, params, new AsyncHttpResponseHandler() {

			// When the response returned by REST has Http response code '200'

			@Override
			public void onSuccess(String response) {

				Toast.makeText(context, "Nearby users alerted.", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {

				Log.d("DEBUG", "Near by users not alerted.");
			}

		});

	}

	/**
	 * Method that performs checks for victim users using RESTful webservice
	 * 
	 * @param params
	 */
	public static void invokeVictimCheckService(RequestParams params, final Context context) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(FETCH_VICTIM_USERS_URL, params, new AsyncHttpResponseHandler() {

			// When the response returned by REST has Http response code '200'

			@Override
			public void onSuccess(String response) {

				if (!WSHelper.stoppedByUser) {
					sendLockScreenNotification(context);
					WSHelper.mp = MediaPlayer.create(context, R.raw.danger_alert);
					WSHelper.mp.start();
					WSHelper.mp.setLooping(true);
				}
			}

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {
				if (statusCode == 404) {
					// Flag re-initialized
					WSHelper.stoppedByUser = false;

					Log.d("DEBUG", "No users found in trouble.");
				}
			}

		});

	}

	/**
	 * Method to send Android lock screen notifications.
	 * 
	 * @param context
	 */
	public static void sendLockScreenNotification(Context context) {

		Intent notificationIntent = new Intent(context, MainActivity.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		notification = new NotificationCompat.Builder(context).setOnlyAlertOnce(true).setContentTitle("StopRape Alert!")
				.setContentText("Someone needs your help. Click to Help!.").setSmallIcon(R.drawable.notify_img)
				.setAutoCancel(true).setContentIntent(intent).build();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(1, notification);
	}

}