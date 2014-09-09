package isuru.apps.movementanalyzer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageViewSectionFragment  extends Fragment {
		
	private ImageView imageView;

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

		imageView = (ImageView) imageViewerSectionLayout
				.findViewById(R.id.test_image);
		
		return imageViewerSectionLayout;
	}

	public void updateImage(Bitmap newImage) {
		imageView.setImageBitmap(newImage);
	}
	
	
}