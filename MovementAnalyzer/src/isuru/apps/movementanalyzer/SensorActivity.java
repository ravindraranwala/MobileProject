package isuru.apps.movementanalyzer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorActivity extends Activity implements SensorEventListener {
	private final SensorManager mSensorManager;
	private final Sensor mAccelerometer;

	public SensorActivity() {
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener((SensorEventListener) this,
				mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener((SensorEventListener) this,
				mAccelerometer);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		// Right in here is where you put code to read the current sensor values
		// and
		// update any views you might have that are displaying the sensor
		// information
		// You'd get accelerometer values like this:
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
			return;
//		float mSensorX, mSensorY;
		// //
//		mSensorX = event.values[0];
//		mSensorY = event.values[1];
		// //
		/*
		 * switch (mDisplay.getRotation()) { case Surface.ROTATION_0: mSensorX =
		 * event.values[0]; mSensorY = event.values[1]; break; case
		 * Surface.ROTATION_90: mSensorX = -event.values[1]; mSensorY =
		 * event.values[0]; break; case Surface.ROTATION_180: mSensorX =
		 * -event.values[0]; mSensorY = -event.values[1]; break; case
		 * Surface.ROTATION_270: mSensorX = event.values[1]; mSensorY =
		 * -event.values[0]; }
		 */
	}
}
