package isuru.apps.movementanalyzer;

import java.util.List;

import android.apps.movementanalyzer.adapters.LocationArrayAdapter;
import android.apps.movementanalyzer.city.City;
import android.apps.movementanalyzer.data.provider.LocationDataProvider;
import android.apps.movementanalyzer.eventListners.ImageSourceChangeListner;
import android.apps.movementanalyzer.eventObjects.ImageSourceChangeEvent;
import android.apps.movementanalyzer.location.type.LocationType;
import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class InformationSelectionFragment  extends Fragment{
	
	private ImageSourceChangeListner imageSourceChangeListner;
	private LocationDataProvider dataProvider;
	
	// Adding this just to support temporary alert message. This property is
	// NOT required for final functionality
	Context applicationContext;

	public InformationSelectionFragment(LocationDataProvider dataProvider, Context applicationContext) {
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
		RelativeLayout imageViewerSectionLayout = (RelativeLayout) rootView;

		Button buttonTestImage = (Button) imageViewerSectionLayout
				.findViewById(R.id.button1);

		final List<GeographicLocation> locationByCity = this.dataProvider
				.getLocationByCity(City.COLOMBO.getCity());
		buttonTestImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.i("aaa", "Clicked: ");
				// imageView.setImageResource(R.drawable.ic_launcher);

				GeographicLocation location = locationByCity.get(0);
				triggerImageSourceChangeEvent(location.getBitmapImage());
			}
		});

		// Spinners
		// Location Spinner
		Spinner spinnerLocation = (Spinner) imageViewerSectionLayout
				.findViewById(R.id.spinner1);
		Spinner spinnerCatagory = (Spinner) imageViewerSectionLayout
				.findViewById(R.id.spinner2);

		String[] locationList = { City.COLOMBO.getCity(),
				City.MASSACHUSETTS.getCity() };
		ArrayAdapter<String> sp1Adaptor = new ArrayAdapter<String>(
				inflater.getContext(),
				android.R.layout.simple_spinner_dropdown_item, locationList);
		spinnerLocation.setAdapter(sp1Adaptor);

		// Catogory Spinner
		String[] catogoryList = {
				LocationType.RAILWAY_STATION.getLocationCategory(),
				LocationType.HOSPITAL.getLocationCategory() };
		ArrayAdapter<String> sp2Adaptor = new ArrayAdapter<String>(
				inflater.getContext(),
				android.R.layout.simple_spinner_dropdown_item, catogoryList);
		spinnerCatagory.setAdapter(sp2Adaptor);

		// ListView1
		ListView listView = (ListView) imageViewerSectionLayout
				.findViewById(R.id.listView1);
		LocationArrayAdapter locationAdapter = new LocationArrayAdapter(
				inflater.getContext(),
				android.R.layout.simple_dropdown_item_1line, locationByCity);

		listView.setAdapter(locationAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(applicationContext,
						"Click ListItem Number " + position,
						Toast.LENGTH_LONG).show();

				/*
				 * Render the image associated with the selected location
				 * here.
				 */
				triggerImageSourceChangeEvent(locationByCity.get(position)
						.getBitmapImage());

			}
		});
		return imageViewerSectionLayout;
	}
	
	private void triggerImageSourceChangeEvent(Bitmap imageSource){
		if(this.imageSourceChangeListner != null){
			ImageSourceChangeEvent isce = new ImageSourceChangeEvent(this);
			isce.setImageSource(imageSource);
			this.imageSourceChangeListner.imageSourceChangeOccured(isce);
		}
	}

	public void setImageSourceChangeListner(ImageSourceChangeListner listener){
		this.imageSourceChangeListner = listener;
	}
	
	public ImageSourceChangeListner getImageSourceChangeListner(){
		return this.imageSourceChangeListner;
	}

	public void updateInformation(double latitude, double longitude) {
		// TODO Update the Spiner contents according the latitude and longitude values recieved
		
	}
}