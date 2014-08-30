package android.apps.movementanalyzer.model;

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
	 */
	public GeographicLocation(double latitude, double longitude, String city,
			byte[] image) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.city = city;
		this.image = image;
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

}
