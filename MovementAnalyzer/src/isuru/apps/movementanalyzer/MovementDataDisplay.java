package isuru.apps.movementanalyzer;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.apps.movementanalyzer.city.City;
import android.apps.movementanalyzer.data.provider.LocationDataProvider;
import android.apps.movementanalyzer.eventListners.ImageSourceChangeListner;
import android.apps.movementanalyzer.eventListners.LocationChangeListener;
import android.apps.movementanalyzer.eventObjects.ImageSourceChangeEvent;
import android.apps.movementanalyzer.eventObjects.LocationChangeEvent;
import android.apps.movementanalyzer.img.util.ImageUtil;
import android.apps.movementanalyzer.location.type.LocationType;
import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
		
		// Instantiate the client data provider class here.
		dataProvider = new LocationDataProvider(this);
		// Drop the existing table before re-creating.
		dataProvider.dropLocationTable();
		// Then recreate the table.
		dataProvider.createLocationTable();
		// Then load some sample location data into the table.
		loadLocationData();
		
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

	}

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

		
		private CoordinateManagerSectionFragment coordinateManagerSectionFragment;
		private InformationSelectionFragment informationSelectionFragment;
		private ImageViewSectionFragment imageViewSectionFragment;
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			coordinateManagerSectionFragment = new CoordinateManagerSectionFragment();
			informationSelectionFragment = new InformationSelectionFragment(dataProvider, getApplicationContext());
			imageViewSectionFragment = new ImageViewSectionFragment();
			
			coordinateManagerSectionFragment.setLocationChangeListener(new LocationChangeListener() {				
				@Override
				public void LocationChangeEventOccured(LocationChangeEvent lce) {
					mViewPager.setCurrentItem(1);
					informationSelectionFragment.updateInformation(lce.getLatitude(), lce.getLongitude());
				}
			});
			
			informationSelectionFragment.setImageSourceChangeListner(new ImageSourceChangeListner() {
				@Override
				public void imageSourceChangeOccured(ImageSourceChangeEvent ise) {
					mViewPager.setCurrentItem(2);
					imageViewSectionFragment.updateImage(ise.getImageSource());
				}
			});
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.

			Fragment fragment = null;
			
			if (position == 0) {
				fragment = this.coordinateManagerSectionFragment;
			} else if (position == 1) {
				fragment = this.informationSelectionFragment;
			} else if (position == 2) {
				fragment = this.imageViewSectionFragment;
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


	
	// TODO Time to remove this from here and add a populate sample data set to a separate class
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
}
