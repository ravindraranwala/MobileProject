package android.apps.movementanalyzer.data.provider;

import java.util.List;

import android.apps.movementanalyzer.dao.DatabaseHandler;
import android.apps.movementanalyzer.model.GeographicLocation;
import android.location.Location;
import android.util.Log;

/**
 * This class acts as an interface between the backend database and the client
 * application which manipulates those data. All the backend Database CRUD
 * operations should pass through this layer.
 * 
 * @author Ravindra
 * 
 */
public class MovementAnalyserDataProvider {
	private final DatabaseHandler db;

	public MovementAnalyserDataProvider(DatabaseHandler db) {
		this.db = db;
	}

	/**
	 * Retrieves all the {@link Location} instances associated with the given
	 * city.
	 * 
	 * @param city
	 *            current city where the user resides.
	 * @return A List of {@link Location} instances associated with the given
	 *         city.
	 */
	public List<GeographicLocation> getLocationByCity(final String city) {
		Log.d("Reading: ", "Reading all locations in the city of " + city);
		// Then getting all the locations given a city.
		List<GeographicLocation> locationByCity = db.getLocationByCity(city);

		String log;
		for (GeographicLocation geographicLocation : locationByCity) {
			log = "ID: " + geographicLocation.get_id() + " Latitude: "
					+ geographicLocation.getLatitude() + " Longitude: "
					+ geographicLocation.getLongitude() + " City: "
					+ geographicLocation.getCity() + " Image: "
					+ geographicLocation.getImage();

			// Writing location to log.
			Log.d("Location", log);
		}

		return locationByCity;
	}

	/**
	 * Drops the Location table from the DB.
	 */
	public void dropLocationTable() {
		db.dropTable();
	}

	/**
	 * Creates the Location table in the DB.
	 */
	public void createLocationTable() {
		db.createTable();
	}

	public void AddLocation(final GeographicLocation location) {
		// CRUD operations. Inserting Locations.
		Log.d("Insert: ", "Inserting ..");
		db.addLocation(location);
	}

}
