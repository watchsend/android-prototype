package com.watchsend;

import java.io.File;
import java.io.FileNotFoundException;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ServerAPI {
	
	/*
	 * Makes a post call to a server endpoint that will
	 * essentially upload the video data that has been collected.
	 * 
	 * video_data- a String in the form of JPEGVideoFormat (see class).
	 * 
	 * Usage (static): ServerAPI.sendVideo(video_data);
	 */
	public static void sendVideo(File video_file){
		RequestParams params = new RequestParams();
		try {
			params.put("video_data", video_file);
		} catch (FileNotFoundException e) {}
		
		ServerConnectionClient.post("/upload_endpoint", params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(final String response){
				Log.d("Watchsend", "Upload was a success.");
			}
			
			@Override
			public void onFinish(){
				Log.d("Watchsend", "Finished the request to upload the video.");
			}
			
			@Override
			public void onFailure(Throwable error){
				Log.d("Watchsend", "Failure in upload request");
			}
			
		});
	}

}
