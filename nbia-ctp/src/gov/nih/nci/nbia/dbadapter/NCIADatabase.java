package gov.nih.nci.nbia.dbadapter;

import java.io.File;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;
import org.rsna.ctp.objects.DicomObject;
import org.rsna.ctp.objects.FileObject;
import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;
import org.rsna.ctp.stdstages.database.DatabaseAdapter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.rsna.ctp.stdstages.database.UIDResult;


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
    public Status process(DicomObject file, File storedFile,String url) {
    	Status status = Status.OK;
    	try{
    		System.out.println("hashCode: " + this.hashCode());
    		delegator.process(file, storedFile, url);
    		status = Status.OK;
    	}catch(RuntimeException rx){
    		log.error("Dicom Submission Failed!");
    		status = Status.FAIL;
    	}
    	return status;
    }
    
    public Status process(XmlObject file,File storedFile, String url) {
    	Status status = Status.OK;
    	
    	try{
    		delegator.process(file, storedFile, url);
    		status = Status.OK;
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
