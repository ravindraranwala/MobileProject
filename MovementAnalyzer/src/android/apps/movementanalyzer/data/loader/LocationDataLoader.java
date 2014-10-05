package android.apps.movementanalyzer.data.loader;

import isuru.apps.movementanalyzer.R;
import android.apps.movementanalyzer.city.City;
import android.apps.movementanalyzer.data.provider.LocationDataProvider;
import android.apps.movementanalyzer.img.util.ImageUtil;
import android.apps.movementanalyzer.location.type.LocationType;
import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.res.Resources;

public class LocationDataLoader {

	public static void loadData(LocationDataProvider dataProvider, Resources res) {
		byte[] fortRailwayStation = ImageUtil.getImageByteData(res
				.getDrawable(R.drawable.trainstation));

		byte[] hospital = ImageUtil.getImageByteData(res
				.getDrawable(R.drawable.hospital));

		byte[] uom = ImageUtil
				.getImageByteData(res.getDrawable(R.drawable.uom));
		byte[] kmart = ImageUtil.getImageByteData(res
				.getDrawable(R.drawable.supermarket));

		// Constructing the necessary sample data to persist in the DB.
		/*
		 * Adding some locations reside near Colombo area.
		 */
		dataProvider.addLocation(new GeographicLocation(6.92707860,
				79.86124300, City.COLOMBO.getCity(), fortRailwayStation,
				LocationType.RAILWAY_STATION.getLocationCategory(),
				"Fort Railway Station."));
		dataProvider.addLocation(new GeographicLocation(6.92707860,
				79.86124300, City.COLOMBO.getCity(), hospital,
				LocationType.HOSPITAL.getLocationCategory(),
				"National Hospital."));

		/**
		 * Adding some locations near Moratuwa area.
		 */
		dataProvider.addLocation(new GeographicLocation(6.77, 79.88,
				City.MORATUWA.getCity(), uom, LocationType.UNIVERSITY
						.getLocationCategory(), "University of Moratuwa"));

		dataProvider.addLocation(new GeographicLocation(6.77, 79.88,
				City.MORATUWA.getCity(), kmart, LocationType.SUPER_MARKET
						.getLocationCategory(), "KMART Supermarket"));

		dataProvider.addLocation(new GeographicLocation(42.40721070,
				-71.38243740, City.MASSACHUSETTS.getCity(), fortRailwayStation,
				LocationType.RAILWAY_STATION.getLocationCategory(),
				"Massachusetts Railway Station."));
	}
}
