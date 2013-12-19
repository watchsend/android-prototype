This is an android prototype of Watchsend which includes
some of the core functionality.

The following functionality is implemented:

	- Asynchronously Captures the entire screen at a time interval
	- Packages the RAW Data (Timestamp, Size, and Data) into an arbitrary format
	- Uploads the data to an endpoint on the Watchsend Server
	- Untested Motion/TouchEvent capture

The implementation is summarized as follows:

	A Service is used which calls an AsyncTask that calls the method to
	take a screenshot. The AsyncTask (within the Service) code repeatedly calls the method for screenshot after asking the Thread to sleep for a given time.

Integration:

	At the onCreate(Bundle) method of each activity, one should set
	WatchsendService.current_activity = this; so the Service is aware 
	of which Activity it is currently taking the screenshot of. Leaving this null
	will result in blank pictures. 

	To start the Screenshot process initially, the developer must start the service, which can be done by

	    	ActivityName.this.startService(new Intent(ActivityName.this.getApplicationContext(), WatchsendService.class));

Expected Output:

	Currently, this service should output a video_file which has the JPEG data and also dump 50 (variable hardcoded as of now) JPEGs into a /screenshots/ directory in the root of the external storage.

Known Problems:
	
	Although not a priority, sometimes the screenshot coloring does not consider Theming since the views are manually drawn onto the canvas. May want to fix in the future.

	

