package gov.nih.nci.nbia.textsupport;
import java.util.ArrayList;
import java.util.List;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
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
    private static Date lastRan;
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
    		} else
    		{
    		  stillRunning=true;
    		  updateSubmittedPatients();
    		  updateCollections();
    		  stillRunning = false;
    		}
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
    	  Date maxTimeStamp;
    	  SolrServerInterface serverAccess = (SolrServerInterface)SpringApplicationContext.getBean("solrServer");
    	  SolrServer server = serverAccess.GetServer();
    	  DateFormat df = new SimpleDateFormat("yyyyMMdd  HH:mm");
    	  String sdt = df.format(new Date(System.currentTimeMillis()));
		  if (lastRan==null)  // either new installation or server restarted we will look for it in Solr
		  {
  
				   String term = "id:NBIAsolrIndexingRun";
				   SolrQuery query = new SolrQuery(term);
				   QueryResponse rsp = server.query( query );
				   SolrDocumentList docs = rsp.getResults();
				   if (docs.size()<1)
				   {  // can't find it, we need to re-index to be sure
					   log.error("Can find last ran doc, we need to reindex");
					   lastRan = null;
				   } else // get the value
				   {
					   if (docs.get(0).get("lastRan") == null)
					   {
						   log.error("Can find last ran doc, we need to reindex");
						   System.out.println(docs.get(0));
						   lastRan = null;
					   } else 
					   {
					       lastRan = df.parse(docs.get(0).get("lastRan").toString());
					       log.error("The patient updater was last run - "+lastRan);
					   }
			       }
		  }
		  
		  maxTimeStamp = support.getMaxTimeStamp();
		  if (maxTimeStamp==null)
		  {
			  log.error("It appears the submission log is empty");
			  return; //nothing to do
		  }
		  List<Object>rs = support.getUpdatedPatients(maxTimeStamp, lastRan);
		   if (rs.size()==0) {
			   log.error("No new items in submission log");
			   return; //nothing to do
		   }
		   int i = 0;
		   int x = 0;
			for (Object result : rs)
			  {
				  PatientAccessDAO patientAccess = (PatientAccessDAO)SpringApplicationContext.getBean("patientAccessDAO");
				  String patientId = result.toString();
				  log.error("Updated patient-"+patientId+" Solr Update request made, this is the " + x++ + " patient updated");
			      PatientDocument doc = patientAccess.getPatientDocument(patientId);
			      if (doc!=null){
			    	 i++;
			         SolrStorage.addPatientDocument(doc);
			         //commit every 20 docs for performance
			         if (i==20)
			         {
			        	i=0;
			        	System.gc();
			            server.commit();
			         }
			      }
			  }
		   SolrInputDocument solrDoc = new SolrInputDocument();
		   solrDoc.addField( "id", "NBIAsolrIndexingRun");
		   solrDoc.addField( "lastRan", df.format(maxTimeStamp));
		   log.debug("Last ran = "+solrDoc.toString());
		   server.add(solrDoc);
		   server.commit();
	  

	  }
    @Transactional
    protected void updateCollections()  throws Exception
    {
    	TextSupportDAO support = (TextSupportDAO)SpringApplicationContext.getBean("textSupportDAO");
  	   // this.sessionFactory=support.getSessionFactory();
    	PatientAccessDAO patientAccess = (PatientAccessDAO)SpringApplicationContext.getBean("patientAccessDAO");
		List<String> localList = new ArrayList<String>();
		localList.addAll(collectionList);
		collectionList.clear();
		SolrServerInterface serverAccess = (SolrServerInterface)SpringApplicationContext.getBean("solrServer");
		SolrServer server = serverAccess.GetServer();
    	for (String collection:localList)
    	{
    		{
    			log.error("updating all patients in collection "+collection);
    			List<Object> rs = support.getPatientsForCollection(collection);
    			   if (rs.size()==0) return; //nothing to do
    			   int i = 0;
    				for (Object result : rs)
    				  {
    					  String patientId = result.toString();
    					  log.error("Calling to update patient from collection " + patientId);
    				      PatientDocument doc = patientAccess.getPatientDocument(patientId);
    				      SolrStorage.addPatientDocument(doc);
    				         //commit every 10 docs for performance
    				         if (i==10)
    				         {
    				        	i=0;
    				            server.commit();
    				         }
    				  }
    				server.commit();
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
