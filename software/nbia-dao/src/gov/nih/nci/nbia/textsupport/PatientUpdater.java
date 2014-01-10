package gov.nih.nci.nbia.textsupport;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import gov.nih.nci.nbia.util.SpringApplicationContext;

@Transactional 
public class PatientUpdater {
	@Autowired
	
    static Logger log = Logger.getLogger(PatientUpdater.class);
    private SessionFactory sessionFactory;
    private static String lastRan;
    private static boolean stillRunning=false;
    private static List<String> collectionList = new ArrayList<String>();
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public void runUpdates()
    {
    	try{
    		if (stillRunning) 
    		{
    			log.info("Previous update is still running");
    		}
    		updateSubmittedPatients();
    		updateCollections();
    	} catch (Exception e)
    	{
    		stillRunning = false;  // I'm dead!
    		e.printStackTrace();
    	}
    	
    }
    @Transactional(propagation=Propagation.REQUIRED)
    protected void updateSubmittedPatients() throws Exception
	  {
    	 
    	  TextSupportDAO support = (TextSupportDAO)SpringApplicationContext.getBean("textSupportDAO");
    	  //this.sessionFactory= support.getSessionFactory();

    	  log.error("Solr update submitted patients has been called");
    	  String maxTimeStamp;
    	  SolrServerInterface serverAccess = (SolrServerInterface)SpringApplicationContext.getBean("solrServer");
    	  SolrServer server = serverAccess.GetServer();
		  if (lastRan==null)  // either new installation or server restarted we will look for it in Solr
		  {
  
				   String term = "id:NBIAsolrIndexingRun";
				   SolrQuery query = new SolrQuery(term);
				   QueryResponse rsp = server.query( query );
				   SolrDocumentList docs = rsp.getResults();
				   if (docs.size()<1)
				   {  // can't find it, we need to re-index to be sure
					   log.error("Can find last ran doc, we need to reindex");
					   lastRan = "NOT_FOUND";
				   } else // get the value
				   {
					   if (docs.get(0).get("lastRan") == null)
					   {
						   log.error("Can find last ran doc, we need to reindex");
						   System.out.println(docs.get(0));
						   lastRan = "NOT_FOUND";
					   } else 
					   {
					       lastRan = docs.get(0).get("lastRan").toString();
					       log.error("The patient updater was last run - "+lastRan);
					   }
			       }
		  }
		  PatientAccessDAO patientAccess = new PatientAccessDAO();
		  maxTimeStamp = support.getMaxTimeStamp();
		  if (maxTimeStamp.length()<2)
		  {
			  log.error("It appears the submission log is empty");
			  return; //nothing to do
		  }
		  List<Object>rs = support.getUpdatedPatients(lastRan, maxTimeStamp);
		   if (rs.size()==0) {
			   log.error("No new items in submission log");
			   return; //nothing to do
		   }
			for (Object result : rs)
			  {
				  String patientId = result.toString();
				  log.error("Updated patient-"+patientId+" Solr Update request made");
			      PatientDocument doc = patientAccess.getPatientDocument(patientId);
			      SolrStorage.addPatientDocument(doc);
			  }
		   SolrInputDocument solrDoc = new SolrInputDocument();
		   solrDoc.addField( "id", "NBIAsolrIndexingRun");
		   solrDoc.addField( "lastRan", maxTimeStamp);
		   log.debug("Last ran = "+solrDoc.toString());
		   server.add(solrDoc);
		   server.commit();
	  

	  }
    @Transactional
    protected void updateCollections()  throws Exception
    {
    	TextSupportDAO support = (TextSupportDAO)SpringApplicationContext.getBean("textSupportDAO");
  	   // this.sessionFactory=support.getSessionFactory();
		PatientAccessDAO patientAccess = new PatientAccessDAO();
		patientAccess.setSessionFactory(sessionFactory);
		List<String> localList = new ArrayList<String>();
		localList.addAll(collectionList);
		collectionList.clear();
    	for (String collection:localList)
    	{
    		{
    			log.error("updating all patients in collection "+collection);
    			List<Object> rs = support.getPatientsForCollection(collection);
    			   if (rs.size()==0) return; //nothing to do
    				for (Object result : rs)
    				  {
    					  String patientId = result.toString();
    					  log.error("Calling to update patient from collection " + patientId);
    				      PatientDocument doc = patientAccess.getPatientDocument(patientId);
    				      SolrStorage.addPatientDocument(doc);
    				  }
    		}
    	}
    	
    	
    }
	public void addCollectionUpdate(String collection)
	{
		if (collection==null) return; // nothing to do;
		if (collectionList.contains(collection)) return; //already got it
		collectionList.add(collection);
	}
}
