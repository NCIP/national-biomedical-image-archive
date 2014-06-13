package gov.nih.nci.nbia.workflowsupport;
import java.util.*;

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
import gov.nih.nci.nbia.textsupport.*;


public class WorkflowExecutor {
	
    static Logger log = Logger.getLogger(WorkflowExecutor.class);
    private static Date lastRan;
    private static boolean stillRunning=false;
    private static List<String> collectionList = new ArrayList<String>();

    public void runUpdates()
    {
    	try{
    		if (stillRunning) 
    		{
    			log.info("Previous update is still running");
    		} else
    		{
    		  stillRunning=true;
    		  Date maxTimeStamp;
    		  maxTimeStamp = new Date(System.currentTimeMillis());
    		  runWorkflows(maxTimeStamp);
    		  stillRunning = false;
    		}
    	} catch (Exception e)
    	{
    		stillRunning = false;  // I'm dead!
    		e.printStackTrace();
    	}
    	
    }

    protected void runWorkflows(Date maxTimeStamp) throws Exception
	{
    	  log.info("Workflow updated visibilities has been called");
    	  SolrServerInterface serverAccess = (SolrServerInterface)SpringApplicationContext.getBean("solrServer");
    	  SolrServer server = serverAccess.GetServer();
    	  DateFormat df = new SimpleDateFormat("yyyyMMdd  HH:mm");
    	  String sdt = df.format(new Date(System.currentTimeMillis()));
		  if (lastRan==null)  // either new installation or server restarted we will look for it in Solr
		  {
  
				   String term = "id:NBIAWorkflowRun";
				   SolrQuery query = new SolrQuery(term);
				   QueryResponse rsp = server.query( query );
				   SolrDocumentList docs = rsp.getResults();
				   if (docs.size()<1)
				   {  // can't find it, we need to run from now
					   log.error("Can't find last time workflow run, set the date to now");
					   lastRan = new Date(System.currentTimeMillis());
				   } else // get the value
				   {
					   if (docs.get(0).get("lastRan") == null)
					   {
						   log.error("Found workflow doc, last ran not there we need to set date to now");
						   log.info(docs.get(0));
						   lastRan = new Date(System.currentTimeMillis());
					   } else 
					   {
					       lastRan = df.parse(docs.get(0).get("lastRan").toString());
					       log.info("The workflow was last run - "+lastRan);
					   }
			       }
		  }
		  
		  
		   runNewVisibilityWorkflows(maxTimeStamp, lastRan);
		   runNewSeriesWorkflows(maxTimeStamp, lastRan);
		   lastRan=maxTimeStamp;
		   SolrInputDocument solrDoc = new SolrInputDocument();
		   solrDoc.addField( "id", "NBIAWorkflowRun");
		   solrDoc.addField( "lastRan", df.format(maxTimeStamp));
		   log.info("Workflow Last ran at finish= "+solrDoc.toString());
		   server.add(solrDoc);
		   server.commit();
	  

	  }

    private void runNewVisibilityWorkflows(Date high, Date low)
    {
    	WorkflowSupportDAO support = (WorkflowSupportDAO)SpringApplicationContext.getBean("workflowSupportDAO");
    	List<WorkflowVisibilityUpdateDTO> visibilityDTOs = support.getVisibilityUpdated(high, low);
    	for (WorkflowVisibilityUpdateDTO vDTO: visibilityDTOs)
    	{
    		try {
				RESTCaller.requestVisibilityUpdateWorkflow(vDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    private void runNewSeriesWorkflows(Date high, Date low)
    {
    	WorkflowSupportDAO support = (WorkflowSupportDAO)SpringApplicationContext.getBean("workflowSupportDAO");
    	List<WorkflowNewSeriesDTO> seriesDTOs = support.getNewSeries(high, low);
    	for (WorkflowNewSeriesDTO sDTO: seriesDTOs)
    	{
    		try {
				RESTCaller.requestNewSeriesWorkflow(sDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
