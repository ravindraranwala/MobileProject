package isuru.apps.movementanalyzer;

import java.text.NumberFormat;

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
import android.widget.Toast;

public class CoordinateManagerSectionFragment1 extends Fragment implements LocationListener {
	/**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    private LocationChangeListener locationChangeListener;
	private LocationManager locationManager;
	public Location lastKnownLocation;
	private LocationManagmentMethod1 locationManagementMethod = LocationManagmentMethod1.AUTO;
	
	RelativeLayout coordManSectionLayout;
	private RadioGroup choiceSelectionGroup;
	private Button coordinateSendButton;
	private EditText textLatitude;
	private EditText textLongitude;
	private NumberFormat nf = NumberFormat.getInstance();
	
	Context applicationContext;
    
    public CoordinateManagerSectionFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	nf.setMaximumFractionDigits(6);
        // Initialize location listener so that the current location will start getting updated
    	initializeLocationListner();
    	applicationContext = inflater.getContext();
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
					locationManagementMethod = LocationManagmentMethod1.AUTO;
					setTestBoxesEnabled(false);
					break;
				case R.id.radioButton2: // Manual
					locationManagementMethod = LocationManagmentMethod1.MANUAL;
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
		Log.i("TextBoxes", "Updating with data:" + locationManagementMethod.equals(LocationManagmentMethod1.AUTO) + lastKnownLocation.getLatitude() + ":" + lastKnownLocation.getLongitude());
		if(locationManagementMethod.equals(LocationManagmentMethod1.AUTO)){
			String latitude = nf.format(Double.valueOf(lastKnownLocation.getLatitude())).toString();
			String longitude = nf.format(Double.valueOf(lastKnownLocation.getLongitude())).toString();
			textLatitude.setText(latitude);
			textLongitude.setText(longitude);
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
		case AUTO:
			Criteria c = new Criteria();
			c.setAccuracy(Criteria.ACCURACY_MEDIUM);
	        if(locationManager == null){
	        	Log.e("GPS", "No locationManager found");
	        	Toast.makeText(applicationContext,
				"No locationManager found ", Toast.LENGTH_LONG)
				.show();
	        	return;
	        }
	        //it will check first satellite location than Internet and than Sim Network
	        
	        String provider = locationManager.getBestProvider(c, false);
	        if(provider == null){
	        	Log.e("GPS", "No providers found");
	        	lastKnownLocation = new Location("Manual");
				double latitude = 6.92707860;
				double longitude = 79.86124300;
				lastKnownLocation.setLatitude(latitude);
				lastKnownLocation.setLongitude(longitude);
	        	break;
	        }else{
	        	Log.i("GPS", "Provider: "+provider.toString());
	        }
	        try{
		        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	        }catch(IllegalArgumentException ex){}
	        try{
		        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	        }catch(IllegalArgumentException ex){}
	        Location location = locationManager.getLastKnownLocation(provider);
	        if(location != null){
	        	Log.i("GPS", location.toString());
	        	lastKnownLocation = location;
	        }else{
	        	Log.e("GPS", "Location value null");
	        }
	        
			break;
		case MANUAL:
			lastKnownLocation = new Location("Manual");
			double latitude = 6.82707860;
			double longitude = 79.86124300;
			lastKnownLocation.setLatitude(latitude);
			lastKnownLocation.setLongitude(longitude);
			break;
		default:
			break;
		
		}
		if(textLatitude!=null && textLongitude!=null && lastKnownLocation!=null){
			String latitude = String.valueOf(nf.format(lastKnownLocation.getLatitude()));
			String longitude = String.valueOf(nf.format(lastKnownLocation.getLongitude()));
			textLatitude.setText(latitude);
			textLongitude.setText(longitude);
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
	AUTO, MANUAL;
}
