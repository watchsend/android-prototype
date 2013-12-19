package com.watchsend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.os.Environment;

/*
 * Very basic way of serializing
 * the JPEG raw data in the following
 * order:
 * 
 * TIMESTAMP + RAW_IMAGE_LENGTH + RAW_IMAGE
 * 
 */
public class JPEGVideoFormat {
	
	static FileOutputStream output;
	
	
	public JPEGVideoFormat(){
		try {
			output = new FileOutputStream(new File(Environment
					.getExternalStorageDirectory().toString() + "/screenshots/video_file"));
		} catch (FileNotFoundException e) {}

	}
	
	static String data_file;
	
	public static void append(byte[] timestamp, byte[]length, byte[]jpeg_data){
		if(output == null){
			try {
				output = new FileOutputStream(new File(Environment
						.getExternalStorageDirectory().toString() + "/screenshots/video_file"));
			} catch (FileNotFoundException e) {}

		}
		try {
			output.write(timestamp);
			output.write(length);
			output.write(jpeg_data);
		} catch (IOException e) {}
		
	}
	
	public static void upload(){
		File dir = Environment.getExternalStorageDirectory();
		File yourFile = new File(dir, "/screenshots/video_file");
		ServerAPI.sendVideo(yourFile);
	}
	
	public static byte[] timestamp(int timestamp){
		return ByteBuffer.allocate(4).putInt(timestamp).array();
	}
	
	public static byte [] length (int length){
		return ByteBuffer.allocate(4).putInt(length).array();
	}
	
	
	public static void create_file(){
		
		if(output == null){
			System.out.println("The output stream is null!");
			return;
		}
		
		try {
			output.flush();
			output.close();
		} catch (IOException e) {}
		
		//upload();
	}
	
	

}
