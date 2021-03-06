package android.apps.movementanalyzer.dao;

import java.util.ArrayList;
import java.util.List;

import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Data Access Facade which performs CRUD operations against SQLite DB.
 * 
 * @author Ravindra
 * 
 */
public class DatabaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "locationManager";

	// Contacts table name
	private static final String TABLE_LOCATIONS = "locations";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_CITY = "city";
	// This type can be a railway station, bus stand, supermarket, hospital etc.
	private static final String KEY_LOCATION_TYPE = "type";

	private static final String KEY_LOCATION_DESCRIPTION = "description";

	private static final String KEY_IMAGE = "image";
	private static final String AND = " AND ";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		createLocationTable(db);

	}

	private void createLocationTable(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " REAL,"
				+ KEY_LONGITUDE + " REAL," + KEY_CITY + " TEXT,"
				+ KEY_LOCATION_TYPE + " TEXT," + KEY_LOCATION_DESCRIPTION
				+ " TEXT," + KEY_IMAGE + " BLOB" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropLocationTable(db);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Drops the Locations table from the DB.
	 */
	public void dropTable() {
		// Drop older table if existed
		SQLiteDatabase db = this.getWritableDatabase();
		dropLocationTable(db);
	}

	private void dropLocationTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
	}

	/**
	 * Creates a new location table in the DB.
	 */
	public void createTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		createLocationTable(db);
	}

	/**
	 * Adding a new location
	 * 
	 * @param location
	 *            A new location to be added.
	 */
	public void addLocation(final GeographicLocation location) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_LATITUDE, location.getLatitude());
		values.put(KEY_LONGITUDE, location.getLongitude());
		values.put(KEY_CITY, location.getCity());
		values.put(KEY_LOCATION_TYPE, location.getLocationType());
		values.put(KEY_LOCATION_DESCRIPTION, location.getDescription());
		values.put(KEY_IMAGE, location.getImage());

		// Inserting Row
		db.insert(TABLE_LOCATIONS, null, values);
		db.close();
	}

	/**
	 * Retrieves the locations associated with the given the city.
	 * 
	 * @param city
	 *            the current city
	 * @return A list of {@link GeographicLocation} instances associated with
	 *         the given city
	 */
	public List<GeographicLocation> getLocationByCity(String city) {
		List<GeographicLocation> locationsByCity = new ArrayList<GeographicLocation>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_LOCATIONS, new String[] { KEY_ID,
				KEY_LATITUDE, KEY_LONGITUDE, KEY_CITY, KEY_LOCATION_TYPE,
				KEY_LOCATION_DESCRIPTION, KEY_IMAGE }, KEY_CITY + " =?",
				new String[] { city }, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				GeographicLocation location = new GeographicLocation();
				location.setLatitude(cursor.getDouble(cursor
						.getColumnIndex(KEY_LATITUDE)));
				location.setLongitude(cursor.getDouble(cursor
						.getColumnIndex(KEY_LONGITUDE)));
				location.setCity(cursor.getString(cursor
						.getColumnIndex(KEY_CITY)));
				location.setLocationType(cursor.getString(cursor
						.getColumnIndex(KEY_LOCATION_TYPE)));
				location.setDescription(cursor.getString(cursor
						.getColumnIndex(KEY_LOCATION_DESCRIPTION)));
				location.setImage(cursor.getBlob(cursor
						.getColumnIndex(KEY_IMAGE)));

				locationsByCity.add(location);
			} while (cursor.moveToNext());
		}
		return locationsByCity;

	}

	/**
	 * Retrieves a {@link List} of {@link GeographicLocation} instances reside
	 * in the given coordinates.
	 * 
	 * @param latitude
	 *            The latitude value of the current place
	 * @param longitude
	 *            The longitude value of the current place.
	 * @return {@link List} of {@link GeographicLocation} instances reside
	 *         within the given coordinate.
	 */
	public List<GeographicLocation> getLocationByCoordinate(
			final double latitude, final double longitude) {
		List<GeographicLocation> locationByCoordinate = new ArrayList<GeographicLocation>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db
				.query(TABLE_LOCATIONS,
						new String[] { KEY_ID, KEY_LATITUDE, KEY_LONGITUDE,
								KEY_CITY, KEY_LOCATION_TYPE,
								KEY_LOCATION_DESCRIPTION, KEY_IMAGE },
						KEY_LATITUDE + " BETWEEN ? AND ?" + AND + KEY_LONGITUDE
								+ " BETWEEN ? AND ?",
						new String[] { String.valueOf(latitude - 0.1),
								String.valueOf(latitude + 0.1),
								String.valueOf(longitude - 0.1),
								String.valueOf(longitude + 0.1) }, null, null,
						null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				GeographicLocation location = new GeographicLocation();
				location.setLatitude(cursor.getDouble(cursor
						.getColumnIndex(KEY_LATITUDE)));
				location.setLongitude(cursor.getDouble(cursor
						.getColumnIndex(KEY_LONGITUDE)));
				location.setCity(cursor.getString(cursor
						.getColumnIndex(KEY_CITY)));
				location.setLocationType(cursor.getString(cursor
						.getColumnIndex(KEY_LOCATION_TYPE)));
				location.setDescription(cursor.getString(cursor
						.getColumnIndex(KEY_LOCATION_DESCRIPTION)));
				location.setImage(cursor.getBlob(cursor
						.getColumnIndex(KEY_IMAGE)));

				locationByCoordinate.add(location);
			} while (cursor.moveToNext());
		}
		return locationByCoordinate;
	}

	/**
	 * Retrieves the {@link GeographicLocation} instances of the given category
	 * which resides in the given city.
	 * 
	 * @param city
	 *            the city we are interested in
	 * @param category
	 *            location category to which the user interested in.
	 * @return A list of {@link GeographicLocation} instances which satisfies
	 *         the city and category criteria.
	 */
	public List<GeographicLocation> getLocationByCityAndCategory(
			final String city, final String category) {
		List<GeographicLocation> locationByCityAndcategory = new ArrayList<GeographicLocation>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_LOCATIONS, new String[] { KEY_ID,
				KEY_LATITUDE, KEY_LONGITUDE, KEY_CITY, KEY_LOCATION_TYPE,
				KEY_LOCATION_DESCRIPTION, KEY_IMAGE }, KEY_CITY + " =?" + AND
				+ KEY_LOCATION_TYPE + " =?", new String[] { city, category },
				null, null, null);

		if (cursor.moveToFirst()) {
			do {
				GeographicLocation location = new GeographicLocation();
				location.setLatitude(cursor.getDouble(cursor
						.getColumnIndex(KEY_LATITUDE)));
				location.setLongitude(cursor.getDouble(cursor
						.getColumnIndex(KEY_LONGITUDE)));
				location.setCity(cursor.getString(cursor
						.getColumnIndex(KEY_CITY)));
				location.setLocationType(cursor.getString(cursor
						.getColumnIndex(KEY_LOCATION_TYPE)));
				location.setDescription(cursor.getString(cursor
						.getColumnIndex(KEY_LOCATION_DESCRIPTION)));
				location.setImage(cursor.getBlob(cursor
						.getColumnIndex(KEY_IMAGE)));

				locationByCityAndcategory.add(location);
			} while (cursor.moveToNext());
		}
		return locationByCityAndcategory;
	}
}
