package isuru.apps.movementanalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.apps.movementanalyzer.adapters.LocationArrayAdapter;
import android.apps.movementanalyzer.data.provider.LocationDataProvider;
import android.apps.movementanalyzer.eventListners.ImageSourceChangeListner;
import android.apps.movementanalyzer.eventObjects.ImageSourceChangeEvent;
import android.apps.movementanalyzer.geoLocationFilters.CatogoryFilter;
import android.apps.movementanalyzer.geoLocationFilters.CityFilter;
import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class InformationSelectionFragment extends Fragment {

	private ImageSourceChangeListner imageSourceChangeListner;
	private LocationDataProvider dataProvider;

	RelativeLayout informationSelectionLayout;
	Context applicationContext;

	Spinner spinnerLocation;
	Spinner spinnerCatagory;
	ListView listView;

	List<GeographicLocation> geoLocationsList = new ArrayList<GeographicLocation>();
	String selectedCity = "All";
	String selectedCatogory = "All";

	public InformationSelectionFragment(LocationDataProvider dataProvider,
			Context applicationContext) {
		this.dataProvider = dataProvider;
		this.applicationContext = applicationContext;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.i("aaa", "Called");
		View rootView = inflater.inflate(R.layout.information_selection_layout,
				container, false);
		this.informationSelectionLayout = (RelativeLayout) rootView;
		this.applicationContext = inflater.getContext();

		spinnerLocation = (Spinner) informationSelectionLayout
				.findViewById(R.id.spinner1);
		spinnerCatagory = (Spinner) informationSelectionLayout
				.findViewById(R.id.spinner2);
		listView = (ListView) informationSelectionLayout
				.findViewById(R.id.listView1);

		return informationSelectionLayout;
	}

	private void triggerImageSourceChangeEvent(Bitmap imageSource) {
		if (this.imageSourceChangeListner != null) {
			ImageSourceChangeEvent isce = new ImageSourceChangeEvent(this);
			isce.setImageSource(imageSource);
			this.imageSourceChangeListner.imageSourceChangeOccured(isce);
		}
	}

	public void setImageSourceChangeListner(ImageSourceChangeListner listener) {
		this.imageSourceChangeListner = listener;
	}

	public ImageSourceChangeListner getImageSourceChangeListner() {
		return this.imageSourceChangeListner;
	}

	public void updateInformation(double latitude, double longitude) {
		// TODO Update the Spiner contents according the latitude and longitude
		// values recieved

		final List<GeographicLocation> geoLocationsList = this.dataProvider
				.getLocationByCoordinates(latitude, longitude);
		this.geoLocationsList = geoLocationsList;
		Set<String> locationSet = new HashSet<String>();
		Set<String> catogorySet = new HashSet<String>();
		// Populate locations and catogories
		locationSet.add("All");
		catogorySet.add("All");
		for (GeographicLocation geoLocation : geoLocationsList) {
			locationSet.add(geoLocation.getCity());
			catogorySet.add(geoLocation.getLocationType());
		}
		// Spinners
		// Location Spinner
		String[] locationList = locationSet.toArray(new String[locationSet
				.size()]);
		updateLocationSpinner(locationList);

		// Catogory Spinner
		String[] catogoryList = catogorySet.toArray(new String[catogorySet
				.size()]);
		updateCatogorySpinner(catogoryList);

		// Set 'All' to default selected values
		spinnerLocation.setSelection(((ArrayAdapter) spinnerLocation
				.getAdapter()).getPosition("All"));
		spinnerCatagory.setSelection(((ArrayAdapter) spinnerCatagory
				.getAdapter()).getPosition("All"));
		final List<GeographicLocation> filteredGeoLocationsList = filterLocations(
				geoLocationsList, "All", "All");

		// ListView1
		updateListView();

		// Add selection of City Change event listener
		spinnerLocation
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String city = spinnerLocation.getAdapter()
								.getItem(arg2).toString();
						Log.i("spinner", "Selected: " + city);
						List<GeographicLocation> cityFilteredList = CityFilter
								.filterLocationsByCity(geoLocationsList, city);

						// When City changed, update catogories accordingly
						Set<String> catogorySet = new HashSet<String>();
						catogorySet.add("All");
						if ("all".equalsIgnoreCase(city)) {
							catogorySet.addAll(CatogoryFilter
									.getAllCatogoriesUsed(geoLocationsList));
						} else {
							catogorySet.addAll(CatogoryFilter
									.getAllCatogoriesUsed(cityFilteredList));
						}
						updateCatogorySpinner(catogorySet
								.toArray(new String[catogorySet.size()]));
						// And reset the selected catogory to 'All'
						spinnerCatagory
								.setSelection(((ArrayAdapter) spinnerCatagory
										.getAdapter()).getPosition("All"));

						List<GeographicLocation> cityCatogoryFilteredList = filterLocations(
								geoLocationsList, city, "All");

						// Update the listView
						InformationSelectionFragment.this.selectedCity = city;
						InformationSelectionFragment.this.selectedCatogory = "All";
						updateListView();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}

				});

		spinnerCatagory
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String catogory = spinnerCatagory.getAdapter()
								.getItem(arg2).toString();
						Log.i("spinner", "Selected: " + catogory);
						InformationSelectionFragment.this.selectedCatogory = catogory;
						updateListView();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}

				});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(applicationContext,
						"Click ListItem Number " + position, Toast.LENGTH_LONG)
						.show();
				/*
				 * Render the image associated with the selected location here.
				 */
				triggerImageSourceChangeEvent(filteredGeoLocationsList.get(
						position).getBitmapImage());

			}
		});
	}

	private void updateLocationSpinner(String[] locationList) {

		ArrayAdapter<String> sp1Adaptor = new ArrayAdapter<String>(
				applicationContext,
				android.R.layout.simple_spinner_dropdown_item, locationList);
		spinnerLocation.setAdapter(sp1Adaptor);
	}

	private void updateCatogorySpinner(String[] catogoryList) {
		ArrayAdapter<String> sp2Adaptor = new ArrayAdapter<String>(
				applicationContext,
				android.R.layout.simple_spinner_dropdown_item, catogoryList);
		spinnerCatagory.setAdapter(sp2Adaptor);
	}

	private void updateListView() {
		if (this.geoLocationsList != null) {
			List<GeographicLocation> filteredGeoLocationsList = filterLocations(
					this.geoLocationsList, this.selectedCity,
					this.selectedCatogory);
			LocationArrayAdapter locationAdapter = new LocationArrayAdapter(
					applicationContext,
					android.R.layout.simple_dropdown_item_1line,
					filteredGeoLocationsList);
			listView.setAdapter(locationAdapter);
		}
	}

	private List<GeographicLocation> filterLocations(
			List<GeographicLocation> rowLocationsList, String city,
			String catogory) {
		List<GeographicLocation> filteredList = rowLocationsList;
		if (city != null && !city.equalsIgnoreCase("all")) {
			filteredList = CityFilter.filterLocationsByCity(filteredList, city);
		}

		if (catogory != null && !catogory.equalsIgnoreCase("all")) {
			filteredList = CatogoryFilter.filterLocationsByCatogory(
					filteredList, catogory);
		}

		return filteredList;
	}

}