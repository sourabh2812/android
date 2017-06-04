package com.idealyticapps.mousepad.activity;

import com.idealyticapps.mousepad.R;
import com.idealyticapps.mousepad.constants.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class TouchPad extends Activity implements View.OnClickListener {

	protected TextView mousePad;

	protected InputMethodManager ipManager;

	private float initX = 0;

	private float initY = 0;

	private float disX = 0;

	private float disY = 0;

	private boolean mouseMoved = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touchpad);

		// Get reference to the TextView acting as mousepad
		mousePad = (TextView) findViewById(R.id.mousePad);

		mousePad.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (ConnectionActivity.out != null) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// save X and Y positions when user touches the TextView
						initX = event.getX();
						initY = event.getY();
						mouseMoved = false;
						break;
					case MotionEvent.ACTION_MOVE:
						// Mouse movement in x
						disX = event.getX() - initX;
						// Mouse movement in y
						disY = event.getY() - initY;
						/*
						 * set init to new position so that continuous mouse
						 * movement is captured
						 */
						initX = event.getX();
						initY = event.getY();
						if (disX != 0 || disY != 0) {
							ConnectionActivity.out.println(disX + "," + disY);
						}
						mouseMoved = true;
						break;
					case MotionEvent.ACTION_UP:

						if (!mouseMoved) {
							ConnectionActivity.out.println(Constants.MOUSE_LEFT_CLICK);
						}
					}
				}
				return true;
			}
		});
	}

	public void rightClick(View v) {
		ConnectionActivity.out.println(Constants.MOUSE_RIGHT_CLICK);
	}

	public void leftClick(View v) {
		ConnectionActivity.out.println(Constants.MOUSE_LEFT_CLICK);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
