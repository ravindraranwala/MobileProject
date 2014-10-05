package isuru.apps.movementanalyzer;

import android.apps.movementanalyzer.eventListners.LocationChangeListener;
import android.apps.movementanalyzer.eventObjects.LocationChangeEvent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class CoordinateManagerSectionFragment1 extends Fragment implements LocationListener {
	/**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    private LocationChangeListener locationChangeListener;
	private LocationManager locationManager;
	public Location lastKnownLocation;
	private LocationManagmentMethod1 locationManagementMethod = LocationManagmentMethod1.GPS;
	
	RelativeLayout coordManSectionLayout;
	private RadioGroup choiceSelectionGroup;
	private Button coordinateSendButton;
	private EditText textLatitude;
	private EditText textLongitude;
	
    
    public CoordinateManagerSectionFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Initialize location listener so that the current location will start getting updated
    	initializeLocationListner();
    	
    	// Populate the local variables
        View rootView = inflater.inflate(R.layout.coordinate_manager_section_layout, container, false);
        coordManSectionLayout = (RelativeLayout)rootView;
        
        textLatitude = (EditText)coordManSectionLayout.findViewById(R.id.editText1);
        textLongitude = (EditText)coordManSectionLayout.findViewById(R.id.editText3);
        
	    coordinateSendButton = (Button) coordManSectionLayout.findViewById(R.id.coordinateSendButton);
	    
	    // Trigger the LocationChange event and notifiy any others who are listening once the button is clicked
        coordinateSendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("aaa","Clicked");
				//mViewPager.setCurrentItem(1);
				triggerLocationChangeEvent();
			}

		});

    	choiceSelectionGroup = (RadioGroup) coordManSectionLayout.findViewById(R.id.radioLocationSelectionMethod);
    	// Location textFields are disabled if GPS is selected and enabled otherwise.
    	// Even though they are disabled they will show updated values also with gps location updates
    	choiceSelectionGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
				case R.id.radioButton1: // GPS
					locationManagementMethod = LocationManagmentMethod1.GPS;
					setTestBoxesEnabled(false);
					break;
				case R.id.radioButton2: // Manual
					locationManagementMethod = LocationManagmentMethod1.MANUAL_COORDINATES;
					setTestBoxesEnabled(true);
					break;
				default:
					break;						
				}
				updateLocation();
			}
		});
        return coordManSectionLayout;
    }
    
    public void setLocationChangeListener(LocationChangeListener listener){
    	this.locationChangeListener = listener;
    }
    
    public LocationChangeListener getLocationChangeListener(){
    	return this.locationChangeListener;
    }

	private void triggerLocationChangeEvent() {
		if(this.locationChangeListener != null){
			LocationChangeEvent lce = new LocationChangeEvent(this);
			try{
				Double latitude = Double.valueOf(textLatitude.getText().toString());
				Double longitude = Double.valueOf(textLongitude.getText().toString());
				lce.setLatitude(latitude);
				lce.setLongitude(longitude);
				if(this.locationChangeListener!=null){
					this.locationChangeListener.LocationChangeEventOccured(lce);
				}
			}catch(NumberFormatException ex){
				Log.e("NumberFormatError", ex.getMessage());
			}
		}
	}
	    
	protected void setTestBoxesEnabled(boolean enabled) {
		textLatitude.setEnabled(enabled);
		textLongitude.setEnabled(enabled);		
	}
	
	private void updateTextBoxes(){
		if(locationManagementMethod.equals(LocationManagmentMethod.GPS)){
			textLatitude.setText(Double.valueOf(lastKnownLocation.getLatitude()).toString());
			textLongitude.setText(Double.valueOf(lastKnownLocation.getLongitude()).toString());
		}
	}

	public void initializeLocationListner(){
		locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		updateLocation();
	}
	
	public Location getLocation(){
		updateLocation();
		return lastKnownLocation;
	}
	
	private void updateLocation(){
		switch(locationManagementMethod){
		case GPS:
			Criteria c = new Criteria();
	        if(locationManager == null){
	        	Log.e("GPS", "No locationManager found");
	        	return;
	        }
	        //it will check first satellite location than Internet and than Sim Network
	        String provider = locationManager.getBestProvider(c, false);
	        if(provider == null){
	        	Log.e("GPS", "No providers found");
	        	lastKnownLocation = new Location("Manual");
				double latitude = 2.345689;
				double longitude = 45.4567806;
				lastKnownLocation.setLatitude(latitude);
				lastKnownLocation.setLongitude(longitude);
	        	break;
	        }
	        Location location = locationManager.getLastKnownLocation(provider);
	        if(location != null){
	        	Log.i("GPS", location.toString());
	        	lastKnownLocation = location;
	        }
			break;
		case MANUAL_COORDINATES:
			lastKnownLocation = new Location("Manual");
			double latitude = 2.345689;
			double longitude = 45.4567806;
			lastKnownLocation.setLatitude(latitude);
			lastKnownLocation.setLongitude(longitude);
			break;
		default:
			break;
		
		}
		if(textLatitude!=null && textLongitude!=null){
			textLatitude.setText(String.valueOf(lastKnownLocation.getLatitude()));
			textLongitude.setText(String.valueOf(lastKnownLocation.getLongitude()));
		}
	}
    
	@Override
	public void onLocationChanged(Location location) {
		if(location != null){
        	Log.i("GPS", location.toString());
			lastKnownLocation = location;
			updateTextBoxes();
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}

enum LocationManagmentMethod1{
	GPS, MANUAL_COORDINATES;
}
