package android.apps.movementanalyzer.model;

import android.apps.movementanalyzer.img.util.ImageUtil;
import android.graphics.Bitmap;

/**
 * This class represents a Geographic location. The location consists of
 * latitude and longitude coordinates, the city it belongs and an image which
 * represents that location. For an example it can be a Railway Station, Bus
 * stand, super market or Hospital etc.
 * 
 * @author Ravindra
 * 
 */
public class GeographicLocation {
	// private variables
	private int _id;
	private double latitude;
	private double longitude;
	private String city;
	private byte[] image;
	private String locationType;

	/**
	 * Constructs a Location instance with the given arguments in it.
	 * 
	 * @param latitude
	 *            <code>Latitude</code> value of the location
	 * @param longitude
	 *            <code>Longitude value of the location</code>
	 * @param city
	 *            THe city to which this particular location belongs
	 * @param image
	 *            The image which represents the location
	 * @param locationtype
	 *            The type of the location such as a railway station, bus stand,
	 *            supermarket etc.
	 */
	public GeographicLocation(double latitude, double longitude, String city,
			byte[] image, String locationType) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.city = city;
		this.image = image;
		this.locationType = locationType;
	}

	/**
	 * Creates a default Location instance
	 */
	public GeographicLocation() {
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int get_id() {
		return _id;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * Retrieves the image byte data as a {@link Bitmap}
	 * 
	 * @return A {@link Bitmap} which is backed by the image byte data.
	 */
	public Bitmap getBitmapImage() {
		return ImageUtil.getBitmapImage(image);
	}

}
