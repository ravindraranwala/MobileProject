package android.apps.movementanalyzer.adapters;

import java.util.HashMap;
import java.util.List;

import android.apps.movementanalyzer.model.GeographicLocation;
import android.content.Context;
import android.widget.ArrayAdapter;

public class LocationArrayAdapter  extends ArrayAdapter<GeographicLocation> {
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
