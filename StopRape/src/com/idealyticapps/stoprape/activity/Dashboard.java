package com.idealyticapps.stoprape.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.idealyticapps.stoprape.R;
import com.idealyticapps.stoprape.adapter.MapWrapperLayout;
import com.idealyticapps.stoprape.adapter.OnInfoWindowElemTouchListener;
import com.idealyticapps.stoprape.bean.User;
import com.idealyticapps.stoprape.bean.UserMarker;
import com.idealyticapps.stoprape.gps.GPSTracker;
import com.idealyticapps.stoprape.tasks.LocationUpdateReceiver;
import com.idealyticapps.stoprape.tasks.VictimUserUpdatesReceiver;
import com.idealyticapps.stoprape.wsutil.WSHelper;
import com.loopj.android.http.RequestParams;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;

@SuppressLint("InflateParams")
public class Dashboard extends ActionBarActivity {

	private MapFragment gmap;

	private PendingIntent pendingIntent;

	// Manager for scheduling update tasks
	private AlarmManager manager;

	// Coverage area
	private double radiusInMeters = 10000.0;

	// red outline
	private int strokeColor = 0xffff0000;

	// opaque red fill
	private int shadeColor = 0x44ff0000;

	// GPS Tracket utility obj
	private GPSTracker gps;

	// List of Markers
	private ArrayList<UserMarker> userMarkerArray;

	// Map to place users on map fragment
	private HashMap<Marker, UserMarker> markersHashMap;

	// Current loc of user
	private UserMarker currentLocation;

	// Request params for Web Service calls
	private RequestParams params;

	// Shared preferences
	private SharedPreferences preferences;

	// Receiver 1 component
	private ComponentName receiverOne;

	// Receiver 2 component
	private ComponentName receiverTwo;

	// Intent for schedule tasks
	private Intent alarmIntentTask;

	// Manager for getting app components
	private PackageManager packmanager;

	// Dashboard layout wrapper
	private MapWrapperLayout mapWrapperLayout;

	// Listner for Infowindow 'Click to Help' button
	private OnInfoWindowElemTouchListener infoButtonListener;

	// Options for Markers
	private MarkerOptions markerOption;

	// Views for Marker InfoWindow
	private ViewGroup infoWindow;

	private TextView infoName;

	private TextView infoPhoneNbr;

	private Button infoButton;

	/**
	 * Function called when activate is created
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		setContentView(R.layout.activity_dashboard);
		prepareDashboard();
	}

	/**
	 * Function called when app is resumed
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// Resetting alert sount if any
		if (WSHelper.mp != null) {
			WSHelper.mp.pause();
			WSHelper.stoppedByUser = true;
		}
	}

	/**
	 * Function called to prepare the dashboard view
	 */
	@SuppressWarnings("unchecked")
	public void prepareDashboard() {

		gps = new GPSTracker(getApplicationContext());

		markersHashMap = new HashMap<Marker, UserMarker>();

		userMarkerArray = new ArrayList<UserMarker>();

		// Users data to be populated
		for (User user : (ArrayList<User>) getIntent().getSerializableExtra("userlist")) {
			userMarkerArray.add(new UserMarker(user.getName(), user.getPhoneNo(), user.getLatitude(),
					user.getLongitude(), user.getDangerFlag(), user.isCurrentUser()));
		}

		if (gps.isLocationEnabled()) {

			// Start receiver one
			scheduleUpdateTask();

			// Start receiver two
			scheduleVictimCheckTask();

			// Activate receivers again
			activateScheduledServices();

			currentLocation = new UserMarker(preferences.getString("name", ""), preferences.getString("phoneNbr", ""),
					gps.getLatitude(), gps.getLongitude(), Integer.parseInt(preferences.getString("dangerFlag", "")),
					true);
		} else {

			// App user data is populated
			currentLocation = new UserMarker(preferences.getString("name", ""), preferences.getString("phoneNbr", ""),
					Double.parseDouble(preferences.getString("latitude", "")),
					Double.parseDouble(preferences.getString("longitude", "")),
					Integer.parseInt(preferences.getString("dangerFlag", "")), true);
		}

		setUpMap();

		userMarkerArray.add(currentLocation);

		CameraUpdate cameraUpdate = CameraUpdateFactory
				.newLatLngZoom(new LatLng(currentLocation.getmLatitude(), currentLocation.getmLongitude()), 12);
		gmap.getMap().animateCamera(cameraUpdate);

		if (currentLocation.getDangerFlag() == 1) {
			markerOption = new MarkerOptions()
					.position(new LatLng(currentLocation.getmLatitude(), currentLocation.getmLongitude()))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		} else {
			markerOption = new MarkerOptions()
					.position(new LatLng(currentLocation.getmLatitude(), currentLocation.getmLongitude()))
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		}

		Marker currentMarker = gmap.getMap().addMarker(markerOption);
		markersHashMap.put(currentMarker, currentLocation);

		CircleOptions circleOptions = new CircleOptions()
				.center(new LatLng(currentLocation.getmLatitude(), currentLocation.getmLongitude()))
				.radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(2);

		gmap.getMap().addCircle(circleOptions);

		plotMarkers(userMarkerArray);
	}

	/**
	 * Function used to set up Google Map fragment
	 */
	public void setUpMap() {
		// Do a null check to confirm that we have not already instantiated the
		if (gmap == null) {
			// Try to obtain the map from the SupportMapFragment.
			gmap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

			// Adding my location switch on the map.
			gmap.getMap().setMyLocationEnabled(true);

			// Adding a zoom switch on the map.
			gmap.getMap().getUiSettings().setZoomControlsEnabled(true);

			// Check if we were successful in obtaining the map.

			if (gmap != null) {
				gmap.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
					@Override
					public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
						marker.showInfoWindow();
						return true;
					}
				});
			} else
				Toast.makeText(getApplicationContext(), "Unable to load Maps", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Function called to plot markets on Google Map
	 */
	private void plotMarkers(ArrayList<UserMarker> markers) {

		if (markers.size() > 0) {
			for (UserMarker marker : markers) {
				if (marker.getDangerFlag() == 1 && !marker.getIsCurrentUser()) {
					markerOption = new MarkerOptions()
							.position(new LatLng(marker.getmLatitude(), marker.getmLongitude()))
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				} else if (marker.getDangerFlag() == 0 && !marker.getIsCurrentUser()) {
					markerOption = new MarkerOptions()
							.position(new LatLng(marker.getmLatitude(), marker.getmLongitude()))
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				}

				infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.infowindow_layout, null);
				infoName = (TextView) infoWindow.findViewById(R.id.marker_label);
				infoPhoneNbr = (TextView) infoWindow.findViewById(R.id.user_contact_label);
				infoButton = (Button) infoWindow.findViewById(R.id.navButton);

				Marker currentMarker = gmap.getMap().addMarker(markerOption);
				markersHashMap.put(currentMarker, marker);

				mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.dashboard_layout);

				// MapWrapperLayout initialization
				// 20 - offset between the default InfoWindow bottom edge
				mapWrapperLayout.init(gmap.getMap(), getPixelsFromDp(this, 10));

				infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
						getApplicationContext().getResources().getDrawable(R.drawable.button_infowindow),
						getApplicationContext().getResources().getDrawable(R.drawable.button_infowindow)) {
					@Override
					protected void onClickConfirmed(View v, Marker marker) {

						String baseUri = "http://maps.google.com/maps?daddr=%s,%s";
						String uri = String.format(baseUri, marker.getPosition().latitude,
								marker.getPosition().longitude);

						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				};

				infoButton.setOnTouchListener(infoButtonListener);

				gmap.getMap().setInfoWindowAdapter(new InfoWindowAdapter() {
					@Override
					public View getInfoWindow(Marker marker) {
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {

						UserMarker myMarker = markersHashMap.get(marker);
						// Setting up the infoWindow with current's marker info
						infoName.setText("Name:- " + myMarker.getName());
						infoPhoneNbr.setText("Phone:- " + myMarker.getPhoneNbr());

						// Display none incase dangerflag is zero
						if (myMarker.getDangerFlag() == 0 || myMarker.getIsCurrentUser()) {
							infoButton.setVisibility(View.GONE);
						} else {
							infoButton.setVisibility(View.VISIBLE);
						}
						infoButtonListener.setMarker(marker);
						mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
						return infoWindow;
					}
				});

			}

		}
	}

	/**
	 * Function used to get position offset for 'Press to Help' Button
	 */
	public int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * Function called to schedule location update task
	 */
	public void scheduleUpdateTask() {

		alarmIntentTask = new Intent(getApplicationContext(), LocationUpdateReceiver.class);

		pendingIntent = PendingIntent.getBroadcast(Dashboard.this, 0, alarmIntentTask,
				PendingIntent.FLAG_UPDATE_CURRENT);

		manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		int interval = 60000;
		manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	}

	/**
	 * Function called to schedule victim users data fetch job
	 */
	public void scheduleVictimCheckTask() {

		alarmIntentTask = new Intent(getApplicationContext(), VictimUserUpdatesReceiver.class);

		pendingIntent = PendingIntent.getBroadcast(Dashboard.this, 0, alarmIntentTask,
				PendingIntent.FLAG_UPDATE_CURRENT);

		manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		int interval = 30000;
		manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	}

	/**
	 * Function called when clicked on 'Help Me' Button
	 */
	public void helpMe(View view) {

		params = new RequestParams();
		params.put("email", preferences.getString("emailId", ""));
		params.put("dangerflag", String.valueOf(1));
		params.put("latitude", String.valueOf(gps.getLatitude()));
		params.put("longitude", String.valueOf(gps.getLongitude()));

		WSHelper.invokeSendAlertService(params, getApplicationContext());
	}

	/**
	 * Function called when clicked on 'Logout' Button
	 */
	public void logout(View view) {

		preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplicationContext().startActivity(intent);

		// Stopping Broadcast Services
		receiverOne = new ComponentName(getApplicationContext(), LocationUpdateReceiver.class);

		receiverTwo = new ComponentName(getApplicationContext(), VictimUserUpdatesReceiver.class);

		packmanager = getApplicationContext().getPackageManager();

		packmanager.setComponentEnabledSetting(receiverOne, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
		packmanager.setComponentEnabledSetting(receiverTwo, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);

		// Resetting alert sount if any
		if (WSHelper.mp != null) {
			WSHelper.mp.pause();
			WSHelper.stoppedByUser = true;
		}
	}

	/**
	 * Function called for re-anabling Schedules tasks.
	 */
	public void activateScheduledServices() {
		receiverOne = new ComponentName(getApplicationContext(), LocationUpdateReceiver.class);

		receiverTwo = new ComponentName(getApplicationContext(), VictimUserUpdatesReceiver.class);

		packmanager = getApplicationContext().getPackageManager();

		packmanager.setComponentEnabledSetting(receiverOne, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
		packmanager.setComponentEnabledSetting(receiverTwo, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}

	/**
	 * Function called when clicked on 'Refresh' button
	 */
	public void refresh(View view) {

		params = new RequestParams();

		params.put("email", preferences.getString("emailId", ""));

		// Put Http parameter password with value of Password Edit
		params.put("password", preferences.getString("password", ""));

		params.put("latitude", String.valueOf(gps.getLatitude()));

		params.put("longitude", String.valueOf(gps.getLongitude()));

		// Invoke RESTful Web Service with Http parameters
		WSHelper.invokeLoginService(params, getApplicationContext());
	}
}