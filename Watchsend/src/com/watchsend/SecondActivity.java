package com.watchsend;

import android.app.Activity;
import android.os.Bundle;

/*
 * Creating a second activity for the purpose
 * of testing whether the screenshot will continue
 * to work in the background.
 * 
 */
public class SecondActivity extends Activity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
		WatchsendService.current_activity = SecondActivity.this;
	}
}
