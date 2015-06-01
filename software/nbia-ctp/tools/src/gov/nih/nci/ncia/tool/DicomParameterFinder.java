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

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.rsna.mircsite.util.DicomObject;

/*
 * Go to given directory recursively to parse the parameters out of dicom files 
 */
public class DicomParameterFinder {

	final static Logger logger = Logger.getLogger(DicomParameterFinder.class);

	final static private String outputFileName = "c:\\result.xls";

	final static private String BASE_DATE = "1999-01-01";

	private File outputFile = null;

	private PrintWriter pw = null;
	private HashSet sampleSet=null;

	public DicomParameterFinder() {


		try {
			
			outputFile = new File(outputFileName);
			pw = new PrintWriter(outputFile);
			//System.out.println("new pw");
			pw.print("Original Dicom File Name");
			pw.print("\tSOP Instance UID");
			pw.print("\tSeries Instance UID");
			pw.print("\tStudy Instance UID");
			pw.print("\tPatient name");
			//pw.print("\tMANUFACTURER_MODEL_NAME");			
			
			pw.println();
		} catch (Exception ex) {
			System.out.println("cannot open file " + outputFile);
            ex.printStackTrace();
		}
	}

	public void closeFile() {
		pw.close();
		System.out.println("close pw");
	}


	public void process(boolean recursive, String filename, String dateStart) {
		if (!recursive) {
			sampleSet = new HashSet();
			processData(filename, dateStart);
			return;
		}
		// recursive drill down to directory data and call processData
		File dir = new File(filename);
		if (dir.isFile()) {
			return;
		}
		
		File[] files = dir.listFiles();
	    System.out.println("dir.lenght="+dir.length() );
	    System.out.println("files="+files );
	    System.out.println("files.lenght="+files.length );
	    if (files==null) {
	    	return;
	    }
		for (int i = 0; i < files.length; i++) {
			System.out.println("current dir="+files[i].getAbsolutePath());
			if (files[i].getName().endsWith("data")) {
				System.out.println("process dir="+files[i].getAbsolutePath());
				process(false, files[i].getAbsolutePath(), dateStart);
			}
			else { 
				process(true, files[i].getAbsolutePath(), dateStart);
			}
		}
		
	}
	public void processData(String filename, String dateStart) {
		if (dateStart == null) {
			dateStart = BASE_DATE;
		}

		File dir = new File(filename);
		File[] files = dir.listFiles();
		logger.debug("filename=" + filename);
		//logger.debug("file size=" + files.length);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(dateStart);
		} catch (java.text.ParseException ex) {
			System.out.println("can't parse date " + dateStart);
		}

		//System.out.println(files.length );
		if (files==null) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			//System.out.println(files[i].getName());
			if (files[i].isDirectory()) {
				processData(files[i].getAbsolutePath(), dateStart);
			}
			else 
			if (files[i].isFile() && 
			    files[i].getName().endsWith(".dcm")) {
					
				Hashtable prop = processFile(files[i], date);
				String modelity = (String) (prop
						.get(DicomConstants.MODALITY));

				String manufactory = (String) (prop
						.get(DicomConstants.MANUFACTURER));
				
				String key = modelity+"_"+manufactory;
				System.out.println("key="+key);
				if (sampleSet.contains(key)) {
					System.out.println("return");
					return;
				}
				else {
					sampleSet.add(key);
				}
				if (pw==null) {
					System.out.println("pw is null");
				}
				pw.print(files[i]);//appends the string to the file

				String patientName = (String) (prop
						.get(DicomConstants.PATIENT_NAME));
				pw.print("\t" + patientName);
				String patientId = (String) (prop
						.get(DicomConstants.PATIENT_ID));
				pw.print("\t" + patientId);

				//String modelity = (String) (prop
				//		.get(DicomConstants.MODALITY));
				pw.print("\t" + modelity);

				//String manufactory = (String) (prop
				//		.get(DicomConstants.MANUFACTURER));
				pw.print("\t" + manufactory);

				String manufactory_model = (String) (prop
						.get(DicomConstants.MANUFACTURER_MODEL_NAME));					
				pw.print("\t" + manufactory_model);
				
//					String seriesUid = (String) (prop
//							.get(DicomConstants.SERIES_INSTANCE_UID));
//					String seriesId = (String) (prop
//							.get(DicomConstants.SERIES_ID));
//					String studyUID = (String) (prop
//							.get(DicomConstants.SERIES_INSTANCE_UID));

				// only need to run the first one
				pw.println();
				//return;
			}
		}
	}

	public Hashtable processFile(File file, Date date) {
		Hashtable numbers = new Hashtable();

		// check file date
		if (date != null) {
			// Get the last modified time
			Date fileDate = new Date(file.lastModified());
			if (fileDate.before(date)) {
				return null;
			}
		}
		try {
			DicomObject dcmfile = new DicomObject(file);
			Dataset set = DcmUtil.parse(dcmfile.getFile(), 0x5FFFffff);
			numbers = parseDICOMPropertiesFile(set, numbers);
		} catch (Exception ex) {
			logger.error("failed to process dicom file " + file.getName());
		}
		return numbers;

	}

	private Hashtable parseDICOMPropertiesFile(Dataset dicomSet,
			Hashtable numbers) throws Exception {
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(IDataProcessor.DICOM_PROPERITIES);
		Properties dicomProp = new Properties();
		dicomProp.load(in);
		Enumeration enum1 = dicomProp.propertyNames();
		while (enum1.hasMoreElements()) {
			String propname = enum1.nextElement().toString();
			int pName = Integer.parseInt(propname, 16);
			String elementheader = null;
			if (propname.equals("00200037") || propname.equals("00200032")) {
				String temp[] = dicomSet.getStrings(pName);
				if (temp != null) {
					elementheader = temp[0];
					for (int i = 1; i < temp.length; i++) {
						elementheader += "\\" + temp[i];
					}
				}
			} else {
				try {
					elementheader = dicomSet.getString(pName);
				} catch (UnsupportedOperationException uoe) {
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
    
    /**
     * This method goes through all the dicom files in the given directory and retrieve the seriesInstanceUID, studyInstanceUID, 
     * sopInstanceUID, patientID in the dicom. result.xls is saved in the C:\ directory
     * @param dirname
     * @throws Exception
     */
    
    public void process(String dirname) throws Exception {
        String studyInstanceUID, sopInstanceUID, patientID;
        String seriesInstanceUID;
        File tempFile = new File(dirname);

        
            File[] filelist = tempFile.listFiles();
            System.out.println(filelist.length);

            for (int i = 0; i < filelist.length; i++) {
                File a = filelist[i];
                if (a.getName().endsWith(".dcm")) {                   
                    
                    Dataset set = DcmUtil.parse(a, 0x5FFFffff);
                    int index = Integer.parseInt("0020000E", 16);
                    seriesInstanceUID = set.getString(index);            
                    index = Integer.parseInt("0020000D", 16);
                    studyInstanceUID = set.getString(index);
                    index = Integer.parseInt("00080018", 16);
                    sopInstanceUID = set.getString(index);
                    index = Integer.parseInt("00100010", 16);
                    patientID = set.getString(index);
                    System.out.println("file name: " + a.getName() + " sop instance uid: " +sopInstanceUID + " series instance uid: " + seriesInstanceUID + " study instance uid: " + studyInstanceUID + " patient id: "+ patientID);
                    pw.println( a.getName() + "\t" +sopInstanceUID + "\t" + seriesInstanceUID + "\t" + studyInstanceUID + "\t"+ patientID);
                }                 
                
                
            }
        
        
       
    }

	/** ****************** test program ************************ */
	public static void main(String[] args) throws Exception {

		DicomParameterFinder finder = new DicomParameterFinder();
        String dirname = "L:\\NCICB\\NCIA\\WRAMC\\Data\\data\\WRAMC-8\\WRAMC_VC-353M\\p";
		System.out.println("starting ...");
        finder.process(dirname);
        finder.closeFile();
        System.out.println("done");
        
        /*
		boolean recursive = false;
		try {
			recursive = Boolean.parseBoolean(args[0]);
		}
		catch (Exception ex) {
			throw new Exception();
		}
		if (args.length > 2)
			finder.process(recursive, args[1], args[2]);
		else
			finder.process(recursive, args[1],  null);

		finder.closeFile();
		logger.info("=== Process is completed, please read c:\result.xls ===");
        */
	}
}

