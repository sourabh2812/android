package com.idealyticapps.mousepad.activity;

import java.io.IOException;
import com.idealyticapps.mousepad.R;
import com.idealyticapps.mousepad.constants.Constants;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MousePad extends Activity implements SensorEventListener {

	// Sensor manager
	public SensorManager sensorManager;

	// Sensor
	public Sensor accelerometer;

	static final float NS2S = 1.0f / 1000000000.0f;

	float[] rotationMatrix = null;

	float[] orientationValues = null;

	float[] prevMatrix = null;

	// Mousemoved flag
	protected boolean mouseMoved = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mousepad);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

		sensorManager.registerListener((SensorEventListener) this, accelerometer,
				SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (ConnectionActivity.out != null) {

			SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
			SensorManager.getAngleChange(orientationValues, rotationMatrix, prevMatrix);

			final float SCALE = 10.f; // Please experiment with several scales:
										// 0.1f, 1.0f, 10.f, etc.

			float disX = orientationValues[0] * SCALE;
			float disY = orientationValues[1] * SCALE;

			prevMatrix = rotationMatrix;

			ConnectionActivity.out.println((int) disX + "," + (int) disY);

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (ConnectionActivity.out != null) {
			try {
				ConnectionActivity.out.println("exit"); // tell server to exit
				ConnectionActivity.socket.close(); // close socket
			} catch (IOException e) {
				Log.e("DEBUG", "Error in closing socket", e);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
	}

	public void rightClick(View v) {
		ConnectionActivity.out.println(Constants.MOUSE_RIGHT_CLICK);
	}

	public void leftClick(View v) {
		ConnectionActivity.out.println(Constants.MOUSE_LEFT_CLICK);
	}
}