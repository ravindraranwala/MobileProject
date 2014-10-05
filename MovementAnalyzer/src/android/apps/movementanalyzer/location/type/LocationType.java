package android.apps.movementanalyzer.location.type;

/**
 * This enumeration defines a set of location types we are interested in for
 * this context aware application. These can be Railway Station, Bus Stand,
 * Hotels, Super Market, Hospital etc.
 * 
 * @author Ravindra
 * 
 */
public enum LocationType {
	RAILWAY_STATION("Railway Station"), BUS_STAND("Bus Stand"), SUPER_MARKET(
			"Super Market"), HOTEL("Hotels"), HOSPITAL("Hospital"), UNIVERSITY(
			"University");

	private final String locationCategory;

	private LocationType(String locationCategory) {
		this.locationCategory = locationCategory;
	}

	public String getLocationCategory() {
		return locationCategory;
	}

}
