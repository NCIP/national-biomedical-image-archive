package gov.nih.nci.nbia.textsupport;

import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
public class SolrAccess {
	private static String getHitText(String hitContext, int index){
		int hitLength=60;
		int offsetFromStart = 20;
		String returnValue = hitContext;
		if (returnValue==null || returnValue.length()<=hitLength) return returnValue;
		if (index<=offsetFromStart) return returnValue.substring(0, 60);
		returnValue = returnValue.substring(index-offsetFromStart);
		if (returnValue.length()<61) return returnValue;
		return returnValue.substring(0, 60);
	}
	public static List<SolrAllDocumentMetaData> querySolr(String queryTerm)
	{
		  List <SolrAllDocumentMetaData> returnValue = new ArrayList<SolrAllDocumentMetaData> ();
		  try {			   
			  if (queryTerm==null || queryTerm.length()<2)
			  {
				  return returnValue;
			  }
			  SolrServerInterface serverAccess = (SolrServerInterface)SpringApplicationContext.getBean("solrServer");
			  SolrServer server = serverAccess.GetServer();
			   String term = "text:"+queryTerm;
			   SolrQuery query = new SolrQuery(term);
			   query.setHighlight(true).setHighlightSnippets(1);
			   query.addHighlightField("text");
			   query.setFields("id,patientId,docType");
			   query.setRows(1000);
			   query.setParam(GroupParams.GROUP, Boolean.TRUE);
			   query.setParam(GroupParams.GROUP_FIELD, "patientId"); 
			   query.setParam(GroupParams.GROUP_MAIN, true);
			   query.setParam(GroupParams.GROUP_FORMAT, "simple");
			   query.setParam(GroupParams.GROUP_LIMIT, "1");
			   QueryResponse rsp = server.query( query );
			   SolrDocumentList docs = rsp.getResults();
			   for (SolrDocument doc : docs){
				   String highlightedHit = "";
				   if (rsp.getHighlighting().get(doc.get("id").toString()) != null) {
				          List<String> highlightSnippets = rsp.getHighlighting().get(doc.get("id").toString()).get("text");
				          if (highlightSnippets!=null&&highlightSnippets.size()>0)
				          {
				        	  System.out.println("Found highlight"+(String)highlightSnippets.get(0));
				        	  highlightedHit=(String)highlightSnippets.get(0);
				          }
				   }
				   SolrAllDocumentMetaData hits = new SolrAllDocumentMetaData(queryTerm, highlightedHit, doc.get("id").toString(),
						   doc.getFieldValue("patientId").toString());
				   returnValue.add(hits);
			   }
			   
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return returnValue;

	}
	public static SolrFoundDocumentMetaData findIndexes(String term, SolrDocument solrDoc, int documentId,  String highlightedHit)
	{
	    String fieldValue;
	    SolrFoundDocumentMetaData found=null;
	    // need to remove wildcards to find in text
	    String localTerm=term.replaceAll("\\*", "");
        localTerm=localTerm.replaceAll("\\?", "");
		for (String field  : solrDoc.getFieldNames()){
			  if (solrDoc.getFieldValue(field)!=null)
			  {
				  fieldValue=solrDoc.getFieldValue(field).toString();
			      if (fieldValue.toLowerCase().indexOf(localTerm.toLowerCase()) != -1)
			      {
			    	  if (highlightedHit!=null&&highlightedHit.length()>0)
			          {
		    	          found=new SolrFoundDocumentMetaData(localTerm, field, 
		    	        		  highlightedHit,
				    			  fieldValue.toLowerCase().indexOf(localTerm.toLowerCase()), 
				    			  documentId, (String)solrDoc.getFieldValue("patientId"));
			          }
			    	  else {
			    	          found=new SolrFoundDocumentMetaData(localTerm, field, 
			    			  getHitText(fieldValue, fieldValue.toLowerCase().indexOf(term.toLowerCase())),
			    			  fieldValue.toLowerCase().indexOf(localTerm.toLowerCase()), 
			    			  documentId, (String)solrDoc.getFieldValue("patientId"));
			    	  }
			          return found;
			      }
			  }
		  }
		  // unable to locate where the hit is
          return new SolrFoundDocumentMetaData(term, "", 
        		  highlightedHit,
    			  -1, 
    			  documentId, (String)solrDoc.getFieldValue("patientId"));
	}
	 
	public static void main(String[] args)
	{

		querySolr("male");
		querySolr("from");
	}
	
}
