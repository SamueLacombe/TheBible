package net.bible.service.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import net.bible.android.BibleApplication;

import java.util.List;

/** Light Sensor interface 
 * 
 * @author Martin Denham [mjdenham at gmail dot com]
 * @see gnu.lgpl.License for license details.<br>
 *      The copyright to this program is held by it's author.
 */
public class LightSensor {

	public final static float NO_READING_YET = -1919;
	private float mReading = NO_READING_YET;
	
	private boolean mMonitoring = false;
	
	private static final String TAG = "LightSensor"; 
	
	/** return reading or null if no light sensor
	 */
	public int getReading() {
		if (!mMonitoring) {
			ensureMonitoringLightLevel();
		}
		Log.d(TAG, "Light Sensor:"+mReading);
		
		return Math.round(mReading);
	}
	
	private synchronized void ensureMonitoringLightLevel() {
		if (!mMonitoring) {
			if (isLightSensor()) {
				SensorManager sm = (SensorManager) BibleApplication.getApplication().getSystemService(Context.SENSOR_SERVICE);
				Sensor oSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
	
				sm.registerListener(myLightListener, oSensor, SensorManager.SENSOR_DELAY_UI);
				
				// wait for first event
				try {
					Thread.sleep(100);
				} catch (InterruptedException ie) {
					Log.e(TAG, "Interrupted getting light signal", ie);
				}
			}
			mMonitoring = true;
		}
	}
	
	final SensorEventListener myLightListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent sensorEvent) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
				mReading = sensorEvent.values[0];
			}
		}
	
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
    /**
     * Returns true if at least one Orientation sensor is available
     */
    public boolean isLightSensor() {
		Log.d(TAG, "check for a light sensor");

    	boolean isLightSensor = false;
   		SensorManager sm = (SensorManager) BibleApplication.getApplication().getSystemService(Context.SENSOR_SERVICE);
        if (sm != null) {
            List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_LIGHT);
            isLightSensor = (sensors.size() > 0);
        }
		Log.d(TAG, "Finished check for a light sensor");
		return isLightSensor;
    }
}
