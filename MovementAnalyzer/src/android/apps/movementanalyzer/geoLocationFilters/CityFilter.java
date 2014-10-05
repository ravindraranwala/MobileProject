package android.apps.movementanalyzer.geoLocationFilters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.apps.movementanalyzer.model.GeographicLocation;

public class CityFilter {

	public static List<GeographicLocation> filterLocationsByCity(List<GeographicLocation> rowLocationsList, String city) {
		List<GeographicLocation> filteredListByCity = new ArrayList<GeographicLocation>();
		if(rowLocationsList!=null && city!=null){
			for(GeographicLocation geoLocation : rowLocationsList){
				if(city.equalsIgnoreCase(geoLocation.getCity())){
					filteredListByCity.add(geoLocation);
				}
			}
		}
		return filteredListByCity;
	}

	public static Set<String> getAllCitiesUsed(List<GeographicLocation> rowLocationsList){
		Set<String> citySet = new HashSet<String>();
		if(rowLocationsList!=null){
			for(GeographicLocation location : rowLocationsList){
				citySet.add(location.getCity());
			}
		}
		return citySet;
	}
	
}
