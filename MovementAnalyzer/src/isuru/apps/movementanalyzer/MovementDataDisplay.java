package isuru.apps.movementanalyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.apps.movementanalyzer.city.City;
import android.apps.movementanalyzer.data.provider.LocationDataProvider;
import android.apps.movementanalyzer.img.util.ImageUtil;
import android.apps.movementanalyzer.location.type.LocationType;
import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MovementDataDisplay extends FragmentActivity implements
		ActionBar.TabListener {
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private LocationDataProvider dataProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movement_data_display);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		// Instantiate the client data provider class here.
		dataProvider = new LocationDataProvider(this);
		// Drop the existing table before re-creating.
		dataProvider.dropLocationTable();
		// Then recreate the table.
		dataProvider.createLocationTable();
		// Then load some sample location data into the table.
		loadLocationData();

		// Add action listners
		// addBottonClickListners();

	}

	/*
	 * private void addBottonClickListners() { RelativeLayout coordMan =
	 * (RelativeLayout) View.inflate(this,
	 * R.layout.coordinate_manager_section_layout, null); RadioGroup
	 * choiseSelectionGrp = (RadioGroup)
	 * coordMan.findViewById(R.id.radioLocationSelectionMethod); Button
	 * coordinateSendButton = (Button)
	 * coordMan.findViewById(R.id.coordinateSendButton);
	 * /*coordinateSendButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Log.i("aaa","Clicked");
	 * //mViewPager.setCurrentItem(1); } });
	 * 
	 * }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movement_data_display, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			Fragment fragment = null;
			if (position == 0) {
				fragment = new CoordinateManagerSectionFragment(mViewPager);
			} else if (position == 1) {
				fragment = new InformationSelectionFragment();
			} else if (position == 2) {
				fragment = new ImageViewSectionFragment();
			} else {
				fragment = new DummySectionFragment();
			}
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_movement_data_display_dummy, container,
					false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	@SuppressLint("ValidFragment")
	public class InformationSelectionFragment extends Fragment {
		public InformationSelectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			Log.i("aaa", "Called");

			View rootView = inflater.inflate(R.layout.information_selection_layout,
					container, false);
			RelativeLayout imageViewerSectionLayout = (RelativeLayout) rootView;

			final ImageView imageView = (ImageView) imageViewerSectionLayout
					.findViewById(R.id.test_image);
			Button buttonTestImage = (Button) imageViewerSectionLayout
					.findViewById(R.id.button1);

			final List<GeographicLocation> locationByCity = MovementDataDisplay.this.dataProvider
					.getLocationByCity(City.COLOMBO.getCity());
			buttonTestImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Log.i("aaa", "Clicked: ");
					// imageView.setImageResource(R.drawable.ic_launcher);

					GeographicLocation location = locationByCity.get(0);
					imageView.setImageBitmap(location.getBitmapImage());
					imageView.buildLayer();
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
					Toast.makeText(getApplicationContext(),
							"Click ListItem Number " + position,
							Toast.LENGTH_LONG).show();

					/*
					 * Render the image associated with the selected location
					 * here.
					 */
					imageView.setImageBitmap(locationByCity.get(position)
							.getBitmapImage());

				}
			});
			return imageViewerSectionLayout;
		}
	}
	
	@SuppressLint("ValidFragment")
	public class ImageViewSectionFragment extends Fragment {
		public ImageViewSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			Log.i("aaa", "Called");

			View rootView = inflater.inflate(R.layout.image_viewer_section,
					container, false);
			RelativeLayout imageViewerSectionLayout = (RelativeLayout) rootView;

			final ImageView imageView = (ImageView) imageViewerSectionLayout
					.findViewById(R.id.test_image);
			
			return imageViewerSectionLayout;
		}
	}

	public class LocationArrayAdapter extends ArrayAdapter<GeographicLocation> {
		HashMap<GeographicLocation, Integer> mIdMap = new HashMap<GeographicLocation, Integer>();

		public LocationArrayAdapter(Context context, int resource,
				List<GeographicLocation> objects) {
			super(context, resource, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			GeographicLocation location = getItem(position);
			return mIdMap.get(location);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}

	private void loadLocationData() {
		Resources res = getResources();
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

	/*
	 * public static class CoordinateManagerSectionFragment1 extends Fragment {
	 * // /** // * The fragment argument representing the section number for
	 * this // * fragment. // * public static final String ARG_SECTION_NUMBER =
	 * "section_number";
	 * 
	 * public CoordinateManagerSectionFragment1() { }
	 * 
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { View rootView =
	 * inflater.inflate(R.layout.coordinate_manager_section_layout, container,
	 * false); Button selectButton = (Button)
	 * rootView.findViewById(R.id.coordinateSendButton123);
	 * selectButton.setText("GO!!!"); selectButton.setOnClickListener(new
	 * OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Log.i("aaa","Clicked");
	 * mViewPager.setCurrentItem(1); } }); return rootView; } }
	 */

}
