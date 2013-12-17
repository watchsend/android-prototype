package com.watchsend;

import java.util.ArrayList;

/*
 * Very basic way of serializing
 * the JPEG raw data in the following
 * order:
 * 
 * TIMESTAMP + RAW_IMAGE_LENGTH + RAW_IMAGE
 * 
 */
public class JPEGVideoFormat {
	
	static String data_file;
	
	public static void append(String to_append){
		data_file += to_append;
	}
	
	public static void upload(){
		ServerAPI.sendVideo(data_file);
	}

}
