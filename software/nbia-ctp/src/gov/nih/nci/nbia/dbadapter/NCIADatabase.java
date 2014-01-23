/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dbadapter;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.rsna.ctp.objects.DicomObject;
import org.rsna.ctp.objects.FileObject;
import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;
import org.rsna.ctp.stdstages.anonymizer.dicom.DAScript;
import org.rsna.ctp.stdstages.anonymizer.AnonymizerStatus;
import org.rsna.ctp.stdstages.anonymizer.IntegerTable;
import org.rsna.ctp.stdstages.anonymizer.LookupTable;
import org.rsna.ctp.stdstages.anonymizer.dicom.DICOMAnonymizer;
import org.rsna.ctp.stdstages.database.DatabaseAdapter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.rsna.ctp.stdstages.database.UIDResult;
import org.rsna.ctp.Configuration;
import org.rsna.ctp.stdstages.DicomAnonymizer;

public class NCIADatabase extends DatabaseAdapter{
    private Logger log = Logger.getLogger(NCIADatabase.class);
	public static ClassPathXmlApplicationContext ctx = null;
   	public static NCIADatabaseDelegator delegator = null;
   	static {
   		try{
   			ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
   			System.out.println("Stage 1 completed!");
   			delegator = (NCIADatabaseDelegator)ctx.getBean("nciaDelegator");
   			System.out.println("Stage 2 completed!");
   		}catch(Exception e){
   			System.out.println("Could not initialize Application Context");
   			e.printStackTrace();
   		}
   	}
   	
   	// Temporary fix until new CTP release provides a better solution
   	private File anonymizeFile(DicomObject infile) {
   		Configuration config = Configuration.getInstance();
   		// dicomAnonymizer -- id defined in config.xml in CTP server
		DicomAnonymizer da = (DicomAnonymizer)config.getRegisteredStage("dicomAnonymizer");
		File scriptFile = da.scriptFile;
		File lookupTableFile = da.lookupTableFile;
		IntegerTable intTable = da.intTable;
		DAScript dascript = DAScript.getInstance(scriptFile);
		Properties script = dascript.toProperties();
		Properties lookup = LookupTable.getProperties(lookupTableFile);
		File inDicomFile = infile.getFile();
		File outDicomFile = new File(inDicomFile.getAbsolutePath()+"tmp");
		AnonymizerStatus anonStatus =
				DICOMAnonymizer.anonymize(inDicomFile, outDicomFile, script, lookup, intTable, false, false);
		
		return outDicomFile;
   	}
   	
    public Status process(DicomObject file, File storedFile,String url) {
    	Status status = Status.OK;
    	File anonymizedFile = anonymizeFile(file);
    	try{
    		System.out.println("hashCode: " + this.hashCode());
    		// Temporary fix until new CTP release provides a better solution
    		delegator.setCorrectFileSize(anonymizedFile);
    		delegator.process(file, storedFile, url);
    		status = Status.OK;
    	}catch(RuntimeException rx){
    		log.error("Dicom Submission Failed!");
    		status = Status.FAIL;
    	}
    	anonymizedFile.delete();
    	return status;
    }
    
    public Status process(XmlObject file,File storedFile, String url) {
    	Status status = Status.OK;
    	
    	try{
    		status = delegator.process(file, storedFile, url);
    	}catch(RuntimeException rx){
    		log.error("XML Submission Failed!");
    		status = Status.FAIL;
    	}
    	return status;
    	
    }
    
    public Status process(ZipObject file, File storedFile, String url) {
    	Status status = Status.OK;
    	
    	try{
    		delegator.process(file, storedFile, url);
    		status = Status.OK;
    	}catch(RuntimeException rx){
    		log.error("ZIP Submission Failed!");
    		status = Status.FAIL;
    	}
    	return status;
    	
    }
    
    public Status process(FileObject file, File storedFile, String url) {
    	Status status = Status.OK;
    	
    	try{
    		delegator.process(file, storedFile, url);
    		status = Status.OK;
    	}catch(RuntimeException rx){
    		log.error("File Submission Failed!");
    		status = Status.FAIL;
    	}
    	return status;
    	
    }
    
    public Map<String, UIDResult> uidQuery(Set<String> uidSet)
    {
    	return delegator.uidQuery(uidSet);
    }
    
}
