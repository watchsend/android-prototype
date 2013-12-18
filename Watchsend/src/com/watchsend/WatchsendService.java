package com.watchsend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;

public class WatchsendService extends Service{
	
	static Activity current_activity;
	
	
	/*
	 * Called when this service is instantiated 
	 * through a constructor call. As of now, 
	 * this doesn't many any effect on the service
	 * itself.
	 * 
	 */
	public WatchsendService(){}
	
	/*
	 * Not needed/implement as of yet. No concern 
	 * with binding the service.
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	public IBinder onBind(Intent intent){
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/*
	 * Called when the service is first created.
	 * This is probably not optimal for starting
	 * any screenshot code, since we don't have a guarantee
	 * that the service process has started.
	 * 
	 * @see android.app.Service#onCreate()
	 */
	public void onCreate(){

	}
	
	/*
	 * When the service starts, we should
	 * spawn an AsyncTask that takes care of the screenshot
	 * process on a separate thread, since the Service
	 * can still be on the UI thread.
	 * 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	public void onStart(Intent intent, int startId){
		new ScreenshotOperation().execute("");
	}
	
	/*
	 * When the service is Destroyed.
	 * 
	 * To be implemented. 
	 * @see android.app.Service#onDestroy()
	 */
	public void onDestroy(){
		
	}
	
	public static void change_activity(Activity new_act){
		current_activity = new_act;
	}
	
	
	/*
	 * Captures a screenshot of the device
	 * by drawing whatever is on the screen to the canvas
	 * and the canvas saves itself to a bitmap.
	 * 
	 * Note that Activity a will change when the user
	 * changes activities. This keeps the screenshots working
	 * well even after this change.
	 */
	public void captureScreenshot(int counter, Activity a) {

		/* Get device dimensions */
		Display display = a.getWindowManager().getDefaultDisplay();
		Point dimensions = new Point();
		display.getSize(dimensions);

		/* Create the bitmap to use to draw the screenshot */
		final Bitmap bitmap = Bitmap.createBitmap(dimensions.x, dimensions.y,
				Bitmap.Config.ARGB_8888);

		final Canvas canvas = new Canvas(bitmap);

		
		/* Draw the root view onto the canvas*/
		a.getWindow().getDecorView().findViewById(android.R.id.content).draw(canvas);
		
		/* Attempt to save the screenshot to the external storage. */
		saveScreenshot(bitmap, counter);
	}

	/*
	 * Saves the bitmap that came from the canvas to the external 
	 * storage. Always called after captureScreenshot() is called.
	 * 
	 * bit- the bitmap to be saved.
	 */
	public void saveScreenshot(Bitmap bit, int counter) {
		/* Save it in the /screenshots/ directory in the root of the filesystem */
		final String SCREENSHOTS_LOCATION = Environment
				.getExternalStorageDirectory().toString() + "/screenshots/";
		FileOutputStream fos = null;
		long tsLong = System.currentTimeMillis()/1000;	
		try {
			/* Where the screenshot will be saved */
			final File sddir = new File(SCREENSHOTS_LOCATION);
			if (!sddir.exists()) {
				sddir.mkdirs();
			}
			
			/* Outputs to a file on the SD card. */
			fos = new FileOutputStream(SCREENSHOTS_LOCATION
					+ counter + ".jpg");
			
			/* Outputs to a byte array and saves to a JPEG file.*/
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			if (fos != null && stream != null) {
				if (!bit.compress(Bitmap.CompressFormat.JPEG, 50, fos)) {
					Log.d("Watchsend", "Compress/Write failed");
				}
				if(!bit.compress(Bitmap.CompressFormat.JPEG, 50, stream)){
					Log.d("Watchsend", "Conversion to byte array failed.");
				}
				byte[] byteArray = stream.toByteArray();
				
				/* Append this picture's byte data to the file. */
				
				addData((int)tsLong, byteArray.length, byteArray);
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
	
	/*
	 * Simple function that converts the integer timestamp and 
	 * integer length of our byte data into byte arrays themselves
	 * (4 bytes each) and then appends all 3 things, including the JPEG
	 * data itself to the file.
	 * 
	 */
	public void addData(int timestamp, int length, byte[] jpeg_data){
		byte[] timestamp_byte = JPEGVideoFormat.timestamp(timestamp);
		byte[] length_byte = JPEGVideoFormat.length(length);
		JPEGVideoFormat.append(timestamp_byte, length_byte, jpeg_data);
	}
	
	/*
	 * The async task that the service calls when it is started.
	 * 
	 */
	private class ScreenshotOperation extends AsyncTask<String, Void, String> {

		/*
		 * In a background thread, so we cannot touch the UI thread.
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
        @Override
        protected String doInBackground(String... params) {

        	for(int i = 0; i < 50; i++){
        	   //
        	   if(current_activity == null){
        		   System.out.println("NULL");
        	   }
 	    	   if(current_activity != null){
 	    		   captureScreenshot(i, current_activity);
 	    	   }
 	            try { 
 	            	Thread.sleep(1000);
 	            } catch (InterruptedException e){ 
 	            	e.printStackTrace();
 	            }
 	    	  }
 	    	  JPEGVideoFormat.create_file();
        	return "Executed";
        }

        /*
         * This method is called after the AsyncTask is done,
         * and can touch the UI thread now.
         * 
         */
        @Override
        protected void onPostExecute(String result) {
        		
        }

        /*
         * Can touch the UI thread before it is executed.
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {}

   }

}
