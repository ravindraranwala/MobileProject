package android.apps.movementanalyzer.eventListners;

import java.util.EventListener;

import android.apps.movementanalyzer.eventObjects.LocationChangeEvent;

public interface LocationChangeListener extends EventListener {
	
	public void LocationChangeEventOccured(LocationChangeEvent lce);

}
