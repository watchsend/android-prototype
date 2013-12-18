package com.watchsend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class WatchSample extends Activity {
	LinearLayout linear_root;

	/*
	 * Called when the app is first created in the lifecycle.
	 * Currently attempts to set up the layout and take a screenshot.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		WatchsendService.current_activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_sample);
		linear_root = (LinearLayout) findViewById(R.id.linear_relative);
		
		Button next_activity = (Button) findViewById(R.id.button1);
		final Button invisible = (Button)findViewById(R.id.button2);
		next_activity.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//invisible.setVisibility(View.VISIBLE);
				Intent intent = new Intent(WatchSample.this, SecondActivity.class);
				startActivity(intent);
			}
			
		});
		
    	WatchSample.this.startService(new Intent(WatchSample.this.getApplicationContext(), WatchsendService.class));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.watch_sample, menu);
		return true;
	}
	
	 

}
