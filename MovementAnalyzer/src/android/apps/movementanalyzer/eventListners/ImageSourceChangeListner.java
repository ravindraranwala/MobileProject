package android.apps.movementanalyzer.eventListners;


import java.util.EventListener;

import android.apps.movementanalyzer.eventObjects.ImageSourceChangeEvent;

public interface ImageSourceChangeListner extends EventListener{

	
	public void imageSourceChangeOccured(ImageSourceChangeEvent ise);
}
