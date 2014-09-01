package android.apps.movementanalyzer.city;

/**
 * This enumeration represents a set of Cities all around the world. For an
 * example Kandy, Colombo, Massachusetts, etc.
 * 
 * @author Ravindra
 * 
 */
public enum City {
	COLOMBO("Colombo"), MASSACHUSETTS("Massachusetts");

	private final String city;

	private City(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

}
