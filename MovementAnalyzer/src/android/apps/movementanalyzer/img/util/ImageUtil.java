package android.apps.movementanalyzer.img.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * This utility class is used to manipulate images.
 * 
 * @author Ravindra
 * 
 */
public class ImageUtil {
	/**
	 * Retrieves byte data associated with the given {@link Drawable}
	 * 
	 * @param drawable
	 *            The {@link Drawable} instance
	 * @return byte data which represents the {@link Drawable}
	 */
	public static byte[] getImageByteData(final Drawable drawable) {
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	/**
	 * Converts the input byte data into the relevant {@link Bitmap} image.
	 * 
	 * @param imageData
	 *            byte array which backs the image.
	 * @return {@link Bitmap} data constructed using the given byte data.
	 */
	public static Bitmap getBitmapImage(final byte[] imageData) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// Convert byte array to bitmap
		return BitmapFactory.decodeByteArray(imageData, 0, imageData.length,
				options);
	}
}
