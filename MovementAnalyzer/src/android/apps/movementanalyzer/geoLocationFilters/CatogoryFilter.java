package android.apps.movementanalyzer.geoLocationFilters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.apps.movementanalyzer.model.GeographicLocation;

public class CatogoryFilter {

	public static List<GeographicLocation> filterLocationsByCatogory(List<GeographicLocation> rowLocationsList, String catogory) {
		List<GeographicLocation> filteredListByCatogory = new ArrayList<GeographicLocation>();
		if(rowLocationsList!=null && catogory!=null){
			for(GeographicLocation geoLocation : rowLocationsList){
				if(catogory.equalsIgnoreCase(geoLocation.getLocationType())){
					filteredListByCatogory.add(geoLocation);
				}
			}
		}
		return filteredListByCatogory;
	}
	
	public static Set<String> getAllCatogoriesUsed(List<GeographicLocation> rowLocationsList){
		Set<String> catogorySet = new HashSet<String>();
		if(rowLocationsList!=null){
			for(GeographicLocation location : rowLocationsList){
				catogorySet.add(location.getLocationType());
			}
		}
		return catogorySet;
	}
}
