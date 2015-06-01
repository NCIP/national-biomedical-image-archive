/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.tool;

import gov.nih.nci.ncia.util.DcmUtil;
import gov.nih.nci.ncia.util.DicomConstants;
import gov.nih.nci.ncia.util.IDataProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.rsna.mircsite.util.DicomObject;

public class GetPathOfFile {

	final static Logger logger = Logger.getLogger(GetPathOfFile.class);
	//private ArrayList<File> quarantinedFileQueue = new ArrayList<File>();
    //private File outputFile = null;
    FileOutputStream fos = null;
    //private StringBuffer buf = null;
    private String two="9328.50.2";
    private String ten="9328.50.10";
    private String eleven="9328.50.11";
    private String others="others";
    private int twoCounter=0;
    private int tenCounter=0;
    private int elevenCounter=0;
    private int othersCounter=0;
    private java.util.HashMap rootMap = new HashMap();
    
    Properties props = new Properties();
    
    public GetPathOfFile () {

	    /*try {
	    	//outputFile = new File("C:\\result.txt");

	    	//fos = new FileOutputStream(outputFile);
	    	
	    }
	    catch (Exception ex) {
	    	System.out.println("cannot open file "+ outputFile);
	    }*/

    }

	// public String process(String dir) {
// // TODO Auto-generated method stub
// String logMessage = null;
//
// process(dir);
//			
// logMessage = "<p>QuarantineFinder completes all files under the directory
// <p>" + dir;

// if ( !quarantinedFileQueue.isEmpty() ) {
// logMessage = "<p>The following file(s) is failed to be processed (Please
// check the log for the detail):";
// Iterator<File> iter = unProcessedFilesQueue.iterator();
// while ( iter.hasNext() ) {
// logMessage += "<p>" + iter.next();
// }
// }

//
// return logMessage;
//
// }

 public void process(String filename, String dateStart) {
	File dir = new File(filename);
	File[] files = dir.listFiles();
	System.out.println("file size="+files.length);
    HashSet patientSet = new HashSet();
    String patientName=null;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    try {
    	df.parse(dateStart);
    }
    catch (java.text.ParseException ex) {
    	System.out.println("can't parse date "+dateStart);
    }
   // buf = new StringBuffer();
	for (int i = 0; i < files.length; i++) {
		if ( files[i].isFile()) {
		//	String filePath = processFile(files[i], date);
			store(props, new File("C:\result.txt"));
			if (patientName!=null){
				patientSet.add(patientName);
			}
		}
	}

	System.out.println(".2 has "+ twoCounter);
	System.out.println(".10 has "+ tenCounter);
	System.out.println(".11 has "+ elevenCounter);
	System.out.println("others has "+ othersCounter);
 }

	public String processFile(File file, Date date) {
        Hashtable numbers = new Hashtable();

        // check file date
        if (date != null) {
		    // Get the last modified time
		  //  long modifiedTime = file.lastModified();
		    Date fileDate = new Date(file.lastModified());
		    if (fileDate.before(date)) {
		    	return null;
		    }
        }
        
        String dicomFileName;
        File dicomFile=null;
		if ( file.getName().endsWith(".qe") ) {

	        try {
	        	
	        	BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
	        	while ((dicomFileName = in.readLine()) != null) {
	        		
	        		System.out.println(dicomFileName);
	        		
	        		props.setProperty(dicomFileName, " ");
	        	
	        			rootMap.put(two, new Integer(0));
	        			rootMap.put(ten, new Integer(0));
	        			rootMap.put(eleven, new Integer(0));
	        			rootMap.put(others, new Integer(0));
	        		if (dicomFileName.indexOf(two)!=-1) {
	        			twoCounter++;
	        		}
	        		else if (dicomFileName.indexOf(ten)!=-1) {
	        			tenCounter++;
	        		}
	        		else if (dicomFileName.indexOf(eleven)!=-1) {
	        			elevenCounter++;
	        		}
	        		else {
	        			othersCounter++;
	        		}
	        		return null;
		         }
	        	in.close();
	        } catch (FileNotFoundException e) {
		        System.err.println("FileStreamsTest: " + e);
		    } catch (IOException e) {
		        System.err.println("FileStreamsTest: " + e);
		    } catch (Exception e) {
		    	System.err.println("FileStreamsTest: " + e);
		    }
		}
		else {
			dicomFile = file;
		}

		try {
			DicomObject dcmfile = new DicomObject(dicomFile);
			Dataset set = DcmUtil.parse( dcmfile.getFile(), 0x5FFFffff);
			numbers = parseDICOMPropertiesFile(set, numbers);
		}
		catch (Exception ex){
			System.out.println("failed to process dicom file "+file.getName());
			ex.printStackTrace();
			System.exit(-1);
		}
            
			//System.out.println("patientName="+numbers.get(DicomConstants.PATIENT_NAME));
        return (String)(numbers.get(DicomConstants.PATIENT_NAME));

	}

	private Hashtable parseDICOMPropertiesFile( Dataset dicomSet, Hashtable numbers ) 
    throws Exception
    {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                IDataProcessor.DICOM_PROPERITIES);
        Properties dicomProp = new Properties();
        dicomProp.load( in );
        Enumeration enum1 = dicomProp.propertyNames();
        while (enum1.hasMoreElements()) {
            String propname = enum1.nextElement().toString();
            int pName = Integer.parseInt(propname, 16);
            String elementheader = null;
            if ( propname.equals("00200037") ||
                    propname.equals("00200032") ) {
                String temp[] = dicomSet.getStrings(pName);
                if (temp != null) {
                    elementheader = temp[0];
                    for (int i = 1; i < temp.length; i++) {
                        elementheader += "\\" + temp[i];
                    }
                }
            }else {
                try {   
                   elementheader = dicomSet.getString(pName);
                }
                catch(UnsupportedOperationException uoe) {
                    elementheader = null;
                }
            }

            if (elementheader != null) {
                elementheader = elementheader.replaceAll("'", "");
                String temp[] = dicomProp.getProperty(propname).split("\t");
                numbers.put(temp[0], elementheader);
            }
        }//while
        return numbers;
    }	
	
	 void store(Properties props, File file) {
	        try {
	            FileOutputStream fos = new FileOutputStream(file);

	            props.store(fos, "Test Properties");
	            //System.out.println("fos " + fos.toString());
	            fos.flush();
	            fos.close();
	        } catch (Exception e) {
	            e.getMessage();
	        }
	    }

	/** ****************** test program ************************ */
	
	// passing parameters 
	// "Q:\quarantine" "2006-11-08"
	public static void main(String[] args )throws Exception {
		
		//String qurantineDir ="C:\\qurantine-wramc\\";
		//String qurantineDir = "F:\\quarantine\\";
   //String qurantineDir ="W:\\NCICBIMAGE\\trial\\quarantine\\";
//   String qurantineDir = "E:\\IDRI Archive\\IDRI-batch6\\Cornell\\dicom\\CO0070-20060317-110737\\";
		//String qurantineDir ="T:\\Zhouro\\appshare\\dicom_store_backup\\";
		//String dateStart = "2006-10-18";
//   String dateStart = "2000-10-23";
		GetPathOfFile finder = new GetPathOfFile();
		
//		finder.process(qurantineDir, dateStart);
				
		logger.info("In process ...");
		if (args.length > 1) {
			finder.process(args[0], args[1]);
		}
		else {
			finder.process(args[0], null);
		}
		logger.info("=== Process is completed ===");
	}		
	
}

