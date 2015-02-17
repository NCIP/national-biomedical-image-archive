/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * @author Rona Zhou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package gov.nih.nci.ncia.tool.mirctest;

import gov.nih.nci.ncia.db.DataAccessProxy;
import gov.nih.nci.ncia.db.IDataAccess;
import gov.nih.nci.ncia.domain.CTImage;
import gov.nih.nci.ncia.domain.GeneralEquipment;
import gov.nih.nci.ncia.domain.GeneralImage;
import gov.nih.nci.ncia.domain.GeneralSeries;
import gov.nih.nci.ncia.domain.Patient;
import gov.nih.nci.ncia.domain.Study;
import gov.nih.nci.ncia.domain.TrialDataProvenance;
import gov.nih.nci.ncia.util.AdapterUtil;
import gov.nih.nci.ncia.util.DicomConstants;
import gov.nih.nci.ncia.util.IDataProcessor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;

/**
 * @author Rona Zhou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MIRCVerificationProcessor implements IDataProcessor {

	Logger log = Logger.getLogger(MIRCVerificationProcessor.class);
	final static String MIRCTEST_PROPERTIES = "mirctest.properties";
	final static String REFERENCE_DICOM_FILE = "reference_dicom_file";
	final static String TEST_DICOM_FILE_DESTINATION = "test_dicom_file_destination";
	final static String MIRCTEST_DATA = "mirctest_data";
	final static String MIRCTEST_REPORT = "mirctest_report";
	final static String NON_DOMAIN_FIELDS = "non_domain_fields";
	final static String MAX_DOMAIN_FIELDS = "max_domain_fields";
	final static int VERBOSE_INFO = 0;
	final static int VERBOSE_ERROR = 1;

	final static DcmParserFactory pFact = DcmParserFactory.getInstance();
	final static DcmObjectFactory oFact = DcmObjectFactory.getInstance();

	static String referenceDicomFile;
	static String testDicomFileDestination;
	static String mirctestData;
	static String mirctestReport;
	static HashSet nonDomainFields=new HashSet();
	static int maxDomainFields=0;
	long filesize;
	String url;

	private HashMap tagDescriptionMap = new HashMap();
	private HashMap tagElementMap = new HashMap();
	private Hashtable numbers = new Hashtable();
	private IDataAccess ida;
	private Properties dicomProp = new Properties();
	private Properties databaseProp = new Properties();
	private Properties mircTestProp = new Properties();
	private PrintWriter mirctestDataPw = null;
	private PrintWriter mirctestreportPw = null;
	
	public MIRCVerificationProcessor() throws Exception {
		
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(IDataProcessor.DATABASE_MAPPING);
		databaseProp.load(in);
		ida = (new DataAccessProxy()).getInstance(Integer.parseInt(databaseProp
				.getProperty("database_mapping")));

		// get properties from 
		InputStream inMircTestProp = Thread.currentThread().getContextClassLoader()
		.getResourceAsStream(MIRCTEST_PROPERTIES);
		mircTestProp.load(inMircTestProp);
		
		referenceDicomFile= mircTestProp.getProperty(REFERENCE_DICOM_FILE);
		testDicomFileDestination = mircTestProp.getProperty(TEST_DICOM_FILE_DESTINATION);
		mirctestData= mircTestProp.getProperty(MIRCTEST_DATA);
		mirctestReport= mircTestProp.getProperty(MIRCTEST_REPORT);
		System.out.println("\n");
		System.out.println("Your Setting==>");
		System.out.println("\n");
		System.out.println("database used: "
				+ databaseProp.getProperty("db_user")+"\n\n");
		System.out.println("Reference Dicom File path: "+referenceDicomFile);
		System.out.println("    Testt Dicom File path: "+testDicomFileDestination);
		System.out.println("    MIRC Test Data Output: "+mirctestData);
		System.out.println("         MIRC Test Report: "+mirctestReport);
		System.out.println("\n");
		
		maxDomainFields = Integer.parseInt(mircTestProp.getProperty(MAX_DOMAIN_FIELDS));
		
		String temp = mircTestProp.getProperty(NON_DOMAIN_FIELDS);
		StringTokenizer st = new StringTokenizer(temp, " ");

		if (st.countTokens()>0) {
			while (st.hasMoreTokens()) {
				nonDomainFields.add(st.nextToken());
			}
		}
		try {
			File dataFile = new File(mirctestData);
			mirctestDataPw = new PrintWriter(dataFile);
		} catch (Exception ex) {
			System.out.println("cannot open file " + mirctestData);
			System.out.println("Please close file "+mirctestData);			
			System.exit(-1);
		}
		try {
			File reportFile = new File(mirctestReport);
			mirctestreportPw = new PrintWriter(reportFile);
			mirctestreportPw.println("database used: "
					+ databaseProp.getProperty("db_user")+"\n\n");

		} catch (Exception ex) {
			System.out.println("cannot open file " + mirctestReport);
			System.out.println("Please close file "+mirctestReport);			
			System.exit(-1);			
		}
	}

	public void processDir()throws Exception {
		File dir = new File(testDicomFileDestination);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				if (files[i].getName().endsWith(".dcm")) {
					process(files[i], "undefined");
				}
			}
		}
	}
	
	public void process(Object file, String url) throws Exception {
		numbers.put("DICOM_FILE_URI", ((File) file).getAbsolutePath());
		numbers.put("DICOM_SIZE", new Long(((File) file).length()));
		numbers.put("MIRC_DOC_URI", url);
		this.process(file);
	}

	public void process(Object file) throws Exception {
		
		
		//==============================
		// process test data in file 
		//==============================
		
		Dataset set = parse((File) file, 0x5FFFffff);
		Hashtable numbers = parseDICOMPropertiesFile(set);
		
		printHeader();
		printDicomTag(numbers, "TestDataInFile");
		
		//=============================
		// process test data in DB
		//=============================
		// retrieve general image
		GeneralImageBuilder giBuilder = new GeneralImageBuilder(ida);
		GeneralImage gi = giBuilder.retrieveGeneralImageFromDB(numbers);
		Hashtable numbersInDb = new Hashtable();
		numbersInDb = giBuilder.addGeneralImage(numbersInDb, gi);
		
		// retrieve ct image
		CTImageBuilder ctBuilder = new CTImageBuilder(ida);
		CTImage ct = ctBuilder.retrieveCTImageFromDB((Integer) (numbersInDb
				.get("CTIMAGE_PK_ID")));
		numbersInDb = ctBuilder.addCTImage(numbersInDb, ct);
		
		// retrieve patient
		PatientBuilder pBuilder = new PatientBuilder(ida);
		Patient pt = pBuilder.retrievePaitentFromDB((Integer) (numbersInDb
				.get("PATIENT_PK_ID")));
		numbersInDb = pBuilder.addPatient(numbersInDb, pt);
		
		// retrieve trial Data Provenance
		TrialDataProvenanceBuilder tdpBuilder = new TrialDataProvenanceBuilder(ida);
		TrialDataProvenance tdp = tdpBuilder.retrieveTrialDataProvenanceFromDB((Integer) (numbersInDb
				.get("TRIAL_DP_PK_ID")));
		numbersInDb = tdpBuilder.addTrialDataProvenance(numbersInDb, tdp);

		// retrieve general series 
		GeneralSeriesBuilder gsBuilder = new GeneralSeriesBuilder(ida);
		GeneralSeries gs = gsBuilder.retrieveGeneralSeriesFromDB((Integer) (numbersInDb
				.get("GENERAL_SERIES_PK_ID")));
		numbersInDb = gsBuilder.addGeneralSeries(numbersInDb, gs);

		// retrieve study
		StudyBuilder studyBuilder = new StudyBuilder(ida);
		Study study = studyBuilder.retrieveStudyFromDB((Integer) (numbersInDb
				.get("STUDY_PK_ID")));
		numbersInDb = studyBuilder.addStudy(numbersInDb, study);
		
		// retrieve general equipment
		GeneralEquipmentBuilder geBuilder = new GeneralEquipmentBuilder(ida);
		GeneralEquipment ge = geBuilder.retrieveGeneralEquipmentFromDB((Integer) (numbersInDb
				.get("GENERAL_EQUIPMENT_PK_ID")));
		numbersInDb = geBuilder.addGeneralEquipment(numbersInDb, ge);

		//======================================================
		// 1. compare test data in file to test data in DB
		//======================================================
		this.mirctestreportPw.println("1.compare test data in file to test data in DB");
		CompareData(numbers,"Test DATA FROM FILE", numbersInDb, "Test DATA FROM DB");
		printDicomTag(numbersInDb,"TestDataInDB");
	

		//=======================================
		// process reference data in file 
		//=======================================
		File referenceFile = new File(referenceDicomFile);
		Dataset set1 = parse((File) referenceFile, 0x5FFFffff);
		Hashtable referenceNumbers = parseDICOMPropertiesFile(set1);
		//======================================================
		// compare test data in file to reference data in file
		//======================================================
		this.mirctestreportPw.println("2.compare test data in file to reference data in file");
		CompareData(numbers,"Test DATA FROM FILE", referenceNumbers, "Reference Dicom Data");
		printDicomTag(referenceNumbers,"ReferenceDataInFile");

		closeFile();
	}

	// private keys in numbers
	private void CompareData(Hashtable numbers1, String dataname1, Hashtable numbers2, String dataname2)
			throws Exception {

		Enumeration keys = numbers1.keys();
		int errorCount=0;
		while (keys.hasMoreElements()) {
			String propname = keys.nextElement().toString();
			Object fromFile1 = numbers1.get(propname);
			Object fromFile2 = numbers2.get(propname);
			if (fromFile1 != null && fromFile2 != null) {
				if (fromFile2 instanceof String) {
					if (!fromFile1.toString().equals(fromFile2.toString())){
						if (propname.equals(DicomConstants.PATIENT_AGE)){
							String ageGroup = AdapterUtil.convertToAgeGroup(fromFile1.toString());
							if (!ageGroup.equals(fromFile2.toString())){
								String msg="Values in the field are mismatched";
								this.printError(msg, VERBOSE_ERROR,propname,dataname1,fromFile1,dataname2,fromFile2);
								errorCount++;
							}
						}
						else { 
							String msg="Values in the field are mismatched";
							this.printError(msg, VERBOSE_ERROR,propname,dataname1,fromFile1,dataname2,fromFile2);
							errorCount++;
						}
					}
				} else if (fromFile2 instanceof Integer) {
					Integer valueFromFile = Integer.valueOf((String) fromFile1);
					if (!valueFromFile.equals((Integer) fromFile2)) {
						String msg="Values in the field are mismatched";
						this.printError(msg, VERBOSE_ERROR,propname,dataname1,fromFile1,dataname2,fromFile2);
						errorCount++;
					}
				} else if (fromFile2 instanceof Float) {
					Float valueFromFile = Float.valueOf((String) fromFile1);
					if (!valueFromFile.equals((Float) fromFile2)) {
						String msg="Values in the field are mismatched";
						this.printError(msg, VERBOSE_ERROR,propname,dataname1,fromFile1,dataname2,fromFile2);
						errorCount++;
					}
				} else if (fromFile2 instanceof Boolean) {
					Boolean valueFromFile = Boolean.valueOf((String) fromFile1);
					if (!valueFromFile.equals((Boolean) fromFile2)){
						String msg="Values in the field are mismatched";
						this.printError(msg, VERBOSE_ERROR,propname,dataname1,fromFile1,dataname2,fromFile2);
						errorCount++;
					}
				} else if (fromFile2 instanceof Date) {
					Date valueFromFile = stringToDate(((String) fromFile1)
							.trim());
					if (!valueFromFile.equals((Date) fromFile2)) {
						String msg="Values in the field are mismatched";
						this.printError(msg, VERBOSE_ERROR,propname,dataname1,valueFromFile,dataname2,fromFile2);
						errorCount++;
					}
				}
			} else {
				//DICOM_FILE_URI
				if (nonDomainFields.contains(propname)){
					String msg="The field doesn't have a domain field associated with. ignore";
					this.printError(msg, VERBOSE_INFO,propname,dataname1,fromFile1,dataname2,fromFile2);
				}
				else {
					String msg="The field has a null value.";
					this.printError(msg, VERBOSE_ERROR,propname,dataname1,fromFile1,dataname2,fromFile2);
					errorCount++;
				}
			}
		} // while
		if (errorCount>0) {
			mirctestreportPw.println(">>>>> ERROR: There are total "+errorCount+" errors.");
		}
		else { 
			mirctestreportPw.println("\n============ TEST IS DONE SUCCESSFULLY, NO ERROR.============\n");
		}
	}

	private Hashtable parseDICOMPropertiesFile(Dataset dicomSet)
			throws Exception {

		Hashtable numbers = new Hashtable();
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(IDataProcessor.DICOM_PROPERITIES);
		dicomProp.load(in);

		Enumeration enum1 = dicomProp.propertyNames();

		while (enum1.hasMoreElements()) {
			String propname = enum1.nextElement().toString(); // propname is tag element
			int pName = Integer.parseInt(propname, 16);
			String elementheader = null;

			// 00200037 90 Image Orientation (Patient)
			// 00200032 89 Image Position (Patient)
			if (propname.equals("00200037") || propname.equals("00200032")) {
				String[] temp = dicomSet.getStrings(pName);

				if (temp != null) {
					elementheader = temp[0];

					for (int i = 1; i < temp.length; i++) {
						elementheader += ("\\" + temp[i]);
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

				String[] temp = dicomProp.getProperty(propname).split("\t");
				// seqNumber, Value
				numbers.put(temp[0], elementheader);
			}
			
			// creat Tag-element map and tag-description map
			String value = (String)dicomProp.get(propname);
			String[] token= value.split("\t");
			tagDescriptionMap.put(token[0].trim(),token[1].trim());
			tagElementMap.put(token[0].trim(),propname.trim());	
		} // while
				
		return numbers;
	}

	private void printError(String msg, int level, String propname, String src1, Object data1, String src2, Object data2){
		String header = "["+tagElementMap.get(propname)+"]["+propname+"]["+tagDescriptionMap.get(propname)+"]";
		mirctestreportPw.println(header);
		if (level == VERBOSE_ERROR) {
			mirctestreportPw.append(">>>>> ERROR: ");//+msgValues in the field are mismatched.");
		}
		else if (level == VERBOSE_INFO) {
			mirctestreportPw.append(">>>>> INFO: ");//+msgValues in the field are mismatched.");
		}
		mirctestreportPw.append(msg);
		mirctestreportPw.println();
		mirctestreportPw.println("["+src1+"] value="+String.valueOf(data1));
		mirctestreportPw.println("["+src2+"] value="+String.valueOf(data2));
		mirctestreportPw.println();			
	}
	
	
	private String DateToString(Date myDate) {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		    FieldPosition pos = new FieldPosition(0);
		    StringBuffer empty = new StringBuffer();
		    StringBuffer date = sdf.format(myDate, empty, pos);
		    return date.toString();
		  }

	// private keys in numbers
	private void printDicomTag(Hashtable numbers, String dataname) throws Exception {
		StringBuffer values= new StringBuffer();
		values.append(dataname);
		int i=0;
		while (++i<=maxDomainFields ){
			if (nonDomainFields.contains(String.valueOf(i))) {
				continue;
			}
			else { 
				if (numbers.containsKey(String.valueOf(i))) {
					Object obj=numbers.get(String.valueOf(i));
					if (  obj instanceof Date){
						values.append("\t"+DateToString((Date)obj));
					}
					else {
						values.append("\t"+numbers.get(String.valueOf(i)));
					}
				}
				else {
					values.append("\tnull");
				}
			}
		} // while
		mirctestDataPw.println(values);
	}	
	private void printHeader() throws Exception {
		StringBuffer tags = new StringBuffer();
		StringBuffer element= new StringBuffer();
		StringBuffer description= new StringBuffer();
		tags.append("Tag Seq ");
		element.append("Dicom Element");
		description.append("Description");
		
		int i=0;
		while (++i<=maxDomainFields ){
			if (nonDomainFields.contains(String.valueOf(i))) {
				continue;
			}
			else { 
				tags.append("\t"+i);
				element.append("\t"+tagElementMap.get(String.valueOf(i)));
				description.append("\t"+tagDescriptionMap.get(String.valueOf(i)));
			}
		} // while
		mirctestDataPw.println(tags);
		mirctestDataPw.println(element);
		mirctestDataPw.println(description);
	}

//	private void printDicomTag(Hashtable numbers, String dataname) throws Exception {
//		Enumeration keys = numbers.keys();
//		StringBuffer tags = new StringBuffer();
//		StringBuffer values= new StringBuffer();
//		tags.append(dataname);
//		values.append(dataname);
//		while (keys.hasMoreElements()) {
//			String propname = keys.nextElement().toString();
//			tags.append("\t"+propname);
//			System.out.println(">>propname="+propname);
//			values.append("\t"+numbers.get(propname));
//		} // while
//		mirctestDataPw.println(tags);
//		mirctestDataPw.println(values);
//	}

	private Dataset parse(File f, int stopTag) throws IOException {
		FileFormat fileFormat = getFileFormat(f);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		DcmParser parser = pFact.newDcmParser(in);
		Dataset dataset = oFact.newDataset();
		parser.setDcmHandler(dataset.getDcmHandler());
		parser.parseDcmFile(fileFormat, stopTag);
		in.close();
		return dataset;
	}

	private FileFormat getFileFormat(File f) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));

		DcmParser parser = pFact.newDcmParser(in);
		FileFormat fileFormat = parser.detectFileFormat();

		if (fileFormat == null) {
			throw new IOException("Unrecognized file format: " + f);
		}
		in.close();
		return fileFormat;
	}

	protected Date stringToDate(String numbers) throws Exception {
		Date retval = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (numbers != null) {
			retval = sdf.parse(numbers);
		}
		return retval;
	}

	public void connect() throws Exception {
		ida.open();
	}

	public void disconnect() throws Exception {
		ida.close();
	}

	public void closeFile() {
		mirctestDataPw.close();
		mirctestreportPw.close();
	}
	/** ****************** test program ************************ */
	public static void main(String[] args) throws Exception {
		System.out.println("\nMIRC VERIFICATION PROCESSOR Starts...\n");
		MIRCVerificationProcessor processor = new MIRCVerificationProcessor();
		processor.connect();
		processor.processDir();
		processor.disconnect();
		System.out.println("\nMIRC VERIFICATION PROCESSOR IS COMPLETED.\n");
		
	}
}
