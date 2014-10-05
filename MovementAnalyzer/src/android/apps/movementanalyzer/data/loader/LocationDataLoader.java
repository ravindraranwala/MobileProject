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
		byte[] bitMapData = ImageUtil.getImageByteData(res
				.getDrawable(R.drawable.ic_launcher_web));

		byte[] hospital = ImageUtil.getImageByteData(res
				.getDrawable(R.drawable.test));

		// Constructing the necessary sample data to persist in the DB.
		dataProvider.addLocation(new GeographicLocation(6.92707860,
				79.86124300, City.COLOMBO.getCity(), bitMapData,
				LocationType.RAILWAY_STATION.getLocationCategory(),
				"Fort Railway Station."));
		dataProvider.addLocation(new GeographicLocation(6.92707860,
				79.86124300, City.COLOMBO.getCity(), hospital,
				LocationType.HOSPITAL.getLocationCategory(),
				"National Hospital."));
		dataProvider.addLocation(new GeographicLocation(42.40721070,
				-71.38243740, City.MASSACHUSETTS.getCity(), bitMapData,
				LocationType.RAILWAY_STATION.getLocationCategory(),
				"Massachusetts Railway Station."));

		// TODO: This is just used to verify that the functionality is working
		// properly. Later on you may remove that when the system is put in the
		// production.
		dataProvider.getLocationByCity(City.COLOMBO.getCity());
	}

}
