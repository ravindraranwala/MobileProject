package android.apps.movementanalyzer.eventObjects;

import java.util.EventObject;

import android.graphics.Bitmap;

public class ImageSourceChangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private Bitmap imageSource;

	public ImageSourceChangeEvent(Object source) {
		super(source);
	}

	public Bitmap getImageSource() {
		return this.imageSource;
	}
	
	public void setImageSource(Bitmap imageSource) {
		this.imageSource = imageSource;
	}

}
