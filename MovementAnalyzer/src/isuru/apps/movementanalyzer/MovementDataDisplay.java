package isuru.apps.movementanalyzer;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.apps.movementanalyzer.dao.DatabaseHandler;
import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import android.view.ViewGroup;
import android.widget.TextView;

public class MovementDataDisplay extends FragmentActivity implements
		ActionBar.TabListener {

	private static final String MASSACHUSETTS = "Massachusetts";

	private static final String COLOMBO = "Colombo";

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

	DatabaseHandler db;

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

		// Initializing the DB if it is NOT already done.
		db = new DatabaseHandler(this);
		// Drop the existing table before re-creating.
		db.dropTable();
		// Then recreate the table.
		db.createTable();
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
			}
			if (position == 1) {
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

	public static class ImageViewSectionFragment extends Fragment {
		public ImageViewSectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);

			return inflater.inflate(R.layout.image_viewer_section, container,
					false);

		}
	}

	private void loadLocationData() {
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.ic_launcher_web);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bitMapData = stream.toByteArray();

		// CRUD operations. Inserting Locations.
		Log.d("Insert: ", "Inserting ..");
		db.addLocation(new GeographicLocation(6.92707860, 79.86124300, COLOMBO,
				bitMapData));
		db.addLocation(new GeographicLocation(42.40721070, -71.38243740,
				MASSACHUSETTS, bitMapData));

		getLocationByCity(COLOMBO);
	}

	/**
	 * Retrieves all the {@link Location} instances associated with the given
	 * city.
	 * 
	 * @param city
	 *            current city where the user resides.
	 * @return A List of {@link Location} instances associated with the given
	 *         city.
	 */
	private List<GeographicLocation> getLocationByCity(final String city) {
		Log.d("Reading: ", "Reading all locations in the city of Colombo");
		// Then getting all the locations given a city.
		List<GeographicLocation> locationByCity = db.getLocationByCity(city);

		String log;
		for (GeographicLocation geographicLocation : locationByCity) {
			log = "ID: " + geographicLocation.get_id() + " Latitude: "
					+ geographicLocation.getLatitude() + " Longitude: "
					+ geographicLocation.getLongitude() + " City: "
					+ geographicLocation.getCity() + " Image: "
					+ geographicLocation.getImage();

			// Writing location to log.
			Log.d("Location", log);
		}

		return locationByCity;
	}

	private Bitmap getBitmapImage(final byte[] imageData) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// Convert byte array to bitmap
		return BitmapFactory.decodeByteArray(imageData, 0, imageData.length,
				options);
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
