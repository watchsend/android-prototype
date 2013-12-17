package com.watchsend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WatchSample extends Activity {
	LinearLayout linear_root;

	/*
	 * Called when the app is first created in the lifecycle.
	 * Currently attempts to set up the layout and take a screenshot.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_sample);
		linear_root = (LinearLayout) findViewById(R.id.linear_relative);
		Thread background_screenshot = new Thread(Screenshot_run);
		background_screenshot.start();
	}
	
	
	/*
	 * Define a Runnable that runs on the background
	 * and takes a screenshot a number of times.
	 * 
	 * Right now, it runs 25 times at the very beginning
	 * of the activity. 
	 * 
	 * To be refined, of course, based on action on the 
	 * user's end and whatnot.
	 */
	public Runnable Screenshot_run = new Runnable()
	{
	    @Override
	    public void run()
	    {
	    	  for(int i = 0; i < 25; i++){
	    	   captureScreenshot();
	            try { Thread.sleep(1000);} catch (InterruptedException e){ e.printStackTrace();}
	            
	    	  }
	    }
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.watch_sample, menu);
		return true;
	}

	/*
	 * Captures a screenshot of the device. 
	 * by drawing whatever is on the screen to the canvas
	 * and the canvas saves itself to a bitmap.
	 * 
	 * Arguments in the future may point to the root view
	 * since that will draw all its children.
	 */
	public void captureScreenshot() {

		/* Get device dimensions */
		Display display = this.getWindowManager().getDefaultDisplay();
		Point dimensions = new Point();
		display.getSize(dimensions);

		/* Create the bitmap to use to draw the screenshot */
		final Bitmap bitmap = Bitmap.createBitmap(dimensions.x, dimensions.y,
				Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(bitmap);

		/* 
		 * Get the theme of the activity; apparently otherwise there are conflicts
		 * with the background and theming since this screenshot is kind of hacky.
		 */
		final Activity activity = this;
		final Theme theme = activity.getTheme();
		final TypedArray ta = theme
				.obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
		final int res = ta.getResourceId(0, 0);
		final Drawable background = activity.getResources().getDrawable(res);

		/* Draw the background onto the canvas (which uses the bitmap to save)*/
		background.draw(canvas);

		/* Draw the view onto the canvas*/
		linear_root.draw(canvas);
		
		/* Attempt to save the screenshot to the external storage. */
		saveScreenshot(bitmap);
	}

	/*
	 * Saves the bitmap that came from the canvas to the external 
	 * storage. 
	 * 
	 * bit- the bitmap to be saved.
	 */
	public void saveScreenshot(Bitmap bit) {
		/* Save it in the /screenshots/ directory in the root of the filesystem */
		final String SCREENSHOTS_LOCATION = Environment
				.getExternalStorageDirectory().toString() + "/screenshots/";
		FileOutputStream fos = null;
		Long tsLong = System.currentTimeMillis()/1000;	
		try {
			/* Where the screenshot will be saved */
			final File sddir = new File(SCREENSHOTS_LOCATION);
			if (!sddir.exists()) {
				sddir.mkdirs();
			}
			/* Outputs to a file on the SD card. */
			fos = new FileOutputStream(SCREENSHOTS_LOCATION
					+ tsLong + ".jpg");
			/* Outputs to a byte array. */
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if (fos != null && stream != null) {
				if (!bit.compress(Bitmap.CompressFormat.JPEG, 50, fos)) {
					Log.d("Watchsend", "Compress/Write failed");
				}
				if(!bit.compress(Bitmap.CompressFormat.JPEG, 50, stream)){
					Log.d("Watchsend", "Conversion to byte array failed.");
				}
				byte[] byteArray = stream.toByteArray();
				addData(Long.toString(tsLong), Arrays.toString(byteArray));
				fos.flush();
				fos.close();
				stream.flush();
				stream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addData(String timestamp, String byteArray){
		JPEGVideoFormat.append(timestamp);
		JPEGVideoFormat.append(Integer.toString(byteArray.length()));
		JPEGVideoFormat.append(byteArray);
	}

}
