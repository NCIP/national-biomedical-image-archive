package gov.nih.nci.nbia.textsupport;

import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
public class SolrAccess {
	static Logger log = Logger.getLogger(SolrAccess.class);
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
			  queryTerm=queryTerm.replaceAll(":", "");
			   if (queryTerm==null || queryTerm.length()<2)
			   {
			       return returnValue;
			   }
			   String term = "text:"+queryTerm;
			   SolrQuery query = new SolrQuery(term);
			   query.setHighlight(true).setHighlightSnippets(1);
			   query.addHighlightField("text");
			   query.setHighlightSimplePre("<strong>");
			   query.setHighlightSimplePost("</strong>");
			   query.setFields("id,patientId,f*");
			   // hold to 3000 values for performance
			   query.setRows(3000);
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
				        	  log.debug("Found highlight"+(String)highlightSnippets.get(0));
				        	  highlightedHit=(String)highlightSnippets.get(0);
				          }
				   }
				   SolrAllDocumentMetaData hits = findIndexes(queryTerm, doc, doc.get("id").toString(),
						   highlightedHit);
				   returnValue.add(hits);
			   }
			   
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return returnValue;

	}
	public static SolrAllDocumentMetaData findIndexes(String term, SolrDocument solrDoc, String documentId,  String highlightedHit)
	{
	    String fieldValue;
	    SolrAllDocumentMetaData found=null;
	    // need to remove wildcards to find in text
	    String localTerm=term.replaceAll("\\*", "");
        localTerm=localTerm.replaceAll("\\?", "");
        // remove the html so I can find the hit in the orginal field
        if (highlightedHit==null)
        {
        	highlightedHit="";
        }
        String localHighlightHit = highlightedHit.replaceAll("<strong>", "");
        localHighlightHit = localTerm.replaceAll("</strong>", "");
		for (String field  : solrDoc.getFieldNames()){
			  if (solrDoc.getFieldValue(field)!=null)
			  {
				  if (field=="id") continue;
				  fieldValue=solrDoc.getFieldValue(field).toString();
				  //System.out.println("field - "+field);
				  //System.out.println("field value is - "+fieldValue);
				  //System.out.println("localHighlightHit value is - "+localHighlightHit);
			      if (fieldValue.toLowerCase().indexOf(localHighlightHit.toLowerCase()) != -1)
			      {
			    	  String foundField = field;
			    	  //up carret is used to mark of the start of a dynamic field
			    	  if (foundField.indexOf("^")>1)
			    	  {
			    		  foundField=foundField.substring(foundField.indexOf("^")+1);
			    	  }
			    	//fields we want to check for text start with f-
			    	  if (foundField.startsWith("f-"))
			    	  {
			    		  foundField=foundField.substring(2);
			    	  }
			    	  if (localHighlightHit!=null&&localHighlightHit.length()>0)
			          {
		    	          found=new SolrAllDocumentMetaData(localTerm, 
		    	        		  "<em>"+foundField+"</em>"+": "+ highlightedHit,
				    			  documentId, (String)solrDoc.getFieldValue("patientId"));
			          }
			    	  else {
			    	          found=new SolrAllDocumentMetaData(localTerm, "<em>"+foundField+"</em>"+": "+
			    	          getHitText(fieldValue, fieldValue.toLowerCase().indexOf(localHighlightHit.toLowerCase())), 
			    			  documentId, (String)solrDoc.getFieldValue("patientId"));
			    	  }
			          return found;
			      }
			  }
		  }
		  // unable to locate where the hit is
          return new SolrAllDocumentMetaData(term, highlightedHit,
    			  documentId, (String)solrDoc.getFieldValue("patientId"));
	}
	 
	public static void main(String[] args)
	{

		querySolr("male");
		querySolr("from");
	}
	
}
