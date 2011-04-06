/**
 *
 */
package gov.nih.nci.nbia;

import gov.nih.nci.nbia.download.SeriesData;
import gov.nih.nci.nbia.ui.DownloadManagerFrame;
import gov.nih.nci.nbia.util.JnlpArgumentsParser;
import gov.nih.nci.nbia.util.PropertyLoader;

import java.util.List;
import java.util.Properties;

/**
 * @author Thai Thi Le
 *
 */
public class Application {
	public static Integer getNumberOfMaxThreads(){
		return new Integer(NBIA_PROPERTIES.getProperty( "number_max_threads"));
	}
	public static String getOnlineHelpUrl(){
		return codebase +NBIA_PROPERTIES.getProperty("online_help_url");
	}

	public static void setFileLocation(String f){
		fileLocation = f;
	}

	public static String getFileLocation(){
		return fileLocation;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String userId = System.getProperty("userId");
		String password = System.getProperty("password");
		String codebase = System.getProperty("codebase");

		Application.codebase = codebase;
		String serverUrl = System.getProperty("downloadServerUrl");

		boolean includeAnnotation = Boolean.valueOf((System.getProperty("includeAnnotation")));


		if(args != null && ( args.length > 0)){

			List<SeriesData> seriesData = JnlpArgumentsParser.parse(args);
    		DownloadManagerFrame manager = new DownloadManagerFrame(userId,
    				                                                password,
    				                                                includeAnnotation,
    				                                                seriesData,
    				                                                serverUrl);
    		manager.setVisible(true);
    	}
		/*else{ // this is for testing only
		 String [] myargs = new String[6];

		    myargs[0]="LIDC|1.3.6.1.4.1.9328.50.3.0022|1.3.6.1.4.1.9328.50.3.68|1.3.6.1.4.1.9328.50.3.69|true|123|6469782|2416";
		    myargs[1]="LIDC|1.3.6.1.4.1.9328.50.3.0023|1.3.6.1.4.1.9328.50.3.194|1.3.6.1.4.1.9328.50.3.195|true|139|73113757|2887";
		    myargs[2]="LIDC|1.3.6.1.4.1.9328.50.3.0023|1.3.6.1.4.1.9328.50.3.194|1.3.6.1.4.1.9328.50.3.195|true|139|73113757|2887";
		    myargs[3]="LIDC|1.3.6.1.4.1.9328.50.3.0022|1.3.6.1.4.1.9328.50.3.68|1.3.6.1.4.1.9328.50.3.69|true|123|6469782|2416";
		    myargs[4]="LIDC|1.3.6.1.4.1.9328.50.3.0023|1.3.6.1.4.1.9328.50.3.194|1.3.6.1.4.1.9328.50.3.195|true|139|73113757|2887";
		    myargs[5]="LIDC|1.3.6.1.4.1.9328.50.3.0023|1.3.6.1.4.1.9328.50.3.194|1.3.6.1.4.1.9328.50.3.195|true|139|73113757|2887";
			serverUrl="https://imaging-dev.nci.nih.gov/ncia/download";
			includeAnnotation=true;
    		seriesData = JnlpArgumentsParser.parse(myargs);
    		DownloadManagerFrame manager = new DownloadManagerFrame("nbia_guest", "",includeAnnotation, seriesData, serverUrl);
    		manager.setVisible(true);
		}*/
	}


	 private static Properties NBIA_PROPERTIES = null;

	 private static String fileLocation="";
	 private static String codebase;

	 static{
	     try {
	    	 NBIA_PROPERTIES =  PropertyLoader.loadProperties( "config.properties");
	     }
	     catch(Exception e){
	    	 /*
	    	  * Create EMPTY properties to avoid null pointer exceptions
	    	  */
	    	 if(NBIA_PROPERTIES==null){
	    		 NBIA_PROPERTIES = new Properties();
	    	 }
	     }
     }
}
