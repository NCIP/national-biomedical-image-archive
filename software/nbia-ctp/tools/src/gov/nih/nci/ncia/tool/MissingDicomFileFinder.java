/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

/*
 * Go to given directory recursively to parse the parameters out of dicom files 
 */
public class MissingDicomFileFinder {

	final static Logger logger = Logger.getLogger(DicomParameterFinder.class);

	final static private String BASE_DATE = "1999-01-01";
	private PrintWriter pw = null;
	private HashSet dicomFileUriSet = new HashSet();
	
	public MissingDicomFileFinder(String infile, String outfileName) {
		openDicomFileUriFile(infile);
		openOutFile(outfileName);
	}

	public void openOutFile(String outfileName){
		try {
			File outputFile = new File(outfileName);
			pw = new PrintWriter(outputFile);
			pw.println();
		} catch (Exception ex) {
			System.out.println("cannot open file " + outfileName);
		}
	}

	public void closeFile() {
		pw.close();
	}
	private void openDicomFileUriFile(String filename) {
        try {
        	String line=null;
          	File fin = new File(filename);
        	BufferedReader in = new BufferedReader(new FileReader(fin.getAbsoluteFile()));
        	while ((line = in.readLine()) != null) {
//        		StringTokenizer st = new StringTokenizer(line, "documents");
//        		String token = null;
//        		while (st.hasMoreTokens()) {
//        			token = st.nextToken();
//        		}
        		String segments[] = line.split("documents");
        		String s = segments[1].replace("/","\\");
        		//System.out.println("name="+s);
        		dicomFileUriSet.add(s);
        		//dicomFileUriMap.put(segments[1], line);
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
	
	public void process(String directoryName, String projectPattern, String dateStart) {
		if (dateStart == null) {
			dateStart = BASE_DATE;
		}
		
		File dir = new File(directoryName);
		File[] files = dir.listFiles();
		//System.out.println("file directory=" + directoryName);
		//System.out.println("file size=" + files.length);

		int count=0;
		HashSet projectFileSet = new HashSet();
		for (int i=0; i<files.length;i++) {
			if (files[i].getName().indexOf(projectPattern)!=-1 ) {
				File thisDir = new File(files[i].getAbsolutePath());
				File[] thisProjectFiles = thisDir.listFiles();
				for (int j=0;j<thisProjectFiles.length;j++){
					if (thisProjectFiles[j].getName().indexOf(".dcm")!=-1 ) {
//						if (projectFileSet.contains(thisProjectFiles[j].getName())) {
//							System.out.println((Set)projectFileSet);
//							System.out.println(thisProjectFiles[j].getAbsolutePath());
//						}	
//						else 
							//projectFileSet.add(thisProjectFiles[j].getName());
		        			String segments[] = thisProjectFiles[j].getAbsolutePath().split("documents");
		        			//System.out.println("projectFileSet: "+thisProjectFiles[j].getAbsolutePath());
		        			//String s = segments[1].replace("/\\",".");						
							projectFileSet.add(segments[1]);
							//System.out.println("projectFileSet: "+ segments[1]);
							count++;
					}
				}
			}			
		}
		//Set dicomFileUriSet =(Set) dicomFileUriMap.keySet();
		Iterator itr = dicomFileUriSet.iterator();
		System.out.println("total files="+dicomFileUriSet.size());
		System.out.println("count files="+count);
		pw.println("== > project: "+projectPattern);
		int total=0;
		while (itr.hasNext()) {
			String name = (String)itr.next();
			if (!projectFileSet.contains(name)){
				System.out.println("projectFileSet doesnot contains "+name);
				pw.println(name);
				total++;
			}
		}
		pw.println("total: "+ total);
		pw.close();
		/*
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(dateStart);
		} catch (java.text.ParseException ex) {
			System.out.println("can't parse date " + dateStart);
		}

		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				process(files[i].getAbsolutePath(), dateStart);
			} else if (files[i].isFile()) {

				if (files[i].getName().endsWith(".dcm")) {
					if (checkFile(files[i].getName())==true) {
						
					}
					
					Hashtable prop = processFile(files[i], date);
					String patientName = (String) (prop
							.get(DicomConstants.PATIENT_NAME));
					String patientId = (String) (prop
							.get(DicomConstants.PATIENT_ID));
//					String seriesUid = (String) (prop
//							.get(DicomConstants.SERIES_INSTANCE_UID));
//					String seriesId = (String) (prop
//							.get(DicomConstants.SERIES_ID));
//					String studyUID = (String) (prop
//							.get(DicomConstants.SERIES_INSTANCE_UID));

					// only need to run the first one
					logger.debug(files[i]);
					pw.print(files[i]);//appends the string to the file
					pw.print("\t");
					pw.print(patientName + "\t" + patientId);
					pw.println();
					return;

				}
			}
		}*/
	}
	

//	public Hashtable processFile(File file, Date date) {
//		Hashtable numbers = new Hashtable();
//
//		// check file date
//		if (date != null) {
//			// Get the last modified time
//			Date fileDate = new Date(file.lastModified());
//			if (fileDate.before(date)) {
//				return null;
//			}
//		}
//		
//		
//		try {
//			
//			DicomObject dcmfile = new DicomObject(file);
//			Dataset set = DcmUtil.parse(dcmfile.getFile(), 0x5FFFffff);
//			numbers = parseDICOMPropertiesFile(set, numbers);
//		} catch (Exception ex) {
//			logger.error("failed to process dicom file " + file.getName());
//		}
//		return numbers;
//
//	}

//	private Hashtable parseDICOMPropertiesFile(Dataset dicomSet,
//			Hashtable numbers) throws Exception {
//		InputStream in = Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream(IDataProcessor.DICOM_PROPERITIES);
//		Properties dicomProp = new Properties();
//		dicomProp.load(in);
//		Enumeration enum1 = dicomProp.propertyNames();
//		while (enum1.hasMoreElements()) {
//			String propname = enum1.nextElement().toString();
//			int pName = Integer.parseInt(propname, 16);
//			String elementheader = null;
//			if (propname.equals("00200037") || propname.equals("00200032")) {
//				String temp[] = dicomSet.getStrings(pName);
//				if (temp != null) {
//					elementheader = temp[0];
//					for (int i = 1; i < temp.length; i++) {
//						elementheader += "\\" + temp[i];
//					}
//				}
//			} else {
//				try {
//					elementheader = dicomSet.getString(pName);
//				} catch (UnsupportedOperationException uoe) {
//					elementheader = null;
//				}
//			}
//
//			if (elementheader != null) {
//				elementheader = elementheader.replaceAll("'", "");
//				String temp[] = dicomProp.getProperty(propname).split("\t");
//				numbers.put(temp[0], elementheader);
//			}
//		}//while
//		return numbers;
//	}

	/** ****************** test program ************************ */
	public static void main(String[] args) throws Exception {

		String infile = args[0];
		String outfile = args[1];
		String dirName = args[2];
		String projectPattern = args[3];
		
		MissingDicomFileFinder finder = new MissingDicomFileFinder(infile, outfile);
		logger.info("In process ...");
		if (args.length>4){
			String dateStart = args[4];
			finder.process(dirName, projectPattern, dateStart);
		}
		else {
			finder.process(dirName, projectPattern, null);
		}
		logger.info("=== Process is completed ===");

	}
}

