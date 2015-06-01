/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ImageDirCheck {

	final static String mapBaseDir = "W:\\NCICBIMAGE\\documents\\";
	final static String srvrBasePath = "/usr/local/tomcat-5.5.17/webapps/NCICBIMAGE/documents/";

	/**
	 * REAL SIMPLE utility to test if the image directory exists or not.
	 * @author bollersj
	 */
	public static void main(String[] args) {
		try {
	    	BufferedReader in = new BufferedReader(new FileReader("C:\\NCIA\\DB\\9144-1646\\Study_Instance_UID_List.txt"));
	    	PrintWriter pw = new PrintWriter(new File("C:\\NCIA\\DB\\9144-1646\\Study_Instance_UID_List_Results.txt"));
	        String str;
	        pw.println("Server Path" + "\t" + "Mapped Directory" + "\t" + "Existent?");
	        while ((str = in.readLine()) != null) {
	            //process(str);
	        	String dir = mapBaseDir + str;
	        	String path = srvrBasePath + str;
	            boolean exists = (new File(dir)).exists();
	            String outStr = path + "\t" + dir + "\t";
	            if (exists) {
	                // File or directory exists
	            	outStr += "exists";
	            } else {
	                // File or directory does not exist
	            	outStr += "NONEXISTENT";
	            }
	            pw.println(outStr);
	        }
	        in.close();
	        pw.close();
	        System.out.println("finished!");
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}

}
