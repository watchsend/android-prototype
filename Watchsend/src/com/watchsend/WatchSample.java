package com.watchsend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class WatchSample extends Activity {
	
	String[] listContent = {
			 
            "Angela",
            "Katie",
            "Leyatt",
            "Cathie",
            "Isra",
            "Jodie",
            "Kirsten",
            "Serena",
            "Linda",
            "Nicole",
            "Natasha",
            "Emily",
            "Kate",
            "Taylor",
            "Michelle",
            "Rebecca",
            "Kavya",
            "Tracey",
            "Amy",
            "Carolyn",
            "Tanvi",
            "Payal"

    };
	
	ListView sample_list;

	/*
	 * Called when the app is first created in the lifecycle.
	 * Currently attempts to set up the layout and take a screenshot.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		WatchsendService.current_activity = this;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_sample);

		Button next_activity = (Button) findViewById(R.id.button1);
		sample_list = (ListView)findViewById(R.id.listView1);
		   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1,
		        listContent);
		 
		sample_list.setAdapter(adapter);
		   
		next_activity.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
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
	
	@Override
	public void onStart(){
		super.onStart();
		WatchsendService.current_activity = this;
	}
	
	 

}
