package gov.nih.nci.nbia.textsupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
public class SolrAccess {

	public static List<SolrFoundDocumentMetaData> querySolr(String queryTerm)
	{
		  List <SolrFoundDocumentMetaData> returnValue = new ArrayList<SolrFoundDocumentMetaData> ();
		  try {			   
			   SolrServer server = NBIAEmbeddedSolrServer.getInstance().GetServer();
			   String term = "text:"+queryTerm;
			   SolrQuery query = new SolrQuery(term);
			   //query.setHighlight(true).setHighlightSnippets(1);
			   QueryResponse rsp = server.query( query );
			   SolrDocumentList docs = rsp.getResults();
			   for (SolrDocument doc : docs){
				   Integer documentId = Integer.valueOf(doc.get("id").toString());
				   SolrFoundDocumentMetaData hits = findIndexes(queryTerm, doc, documentId.intValue());
				   returnValue.add(hits);
			   }
			   
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return returnValue;

	}
	public static SolrFoundDocumentMetaData findIndexes(String term, SolrDocument solrDoc, int documentId )
	{
	    String fieldValue;
	    SolrFoundDocumentMetaData found=null;
		for (String field  : solrDoc.getFieldNames()){
			  if (solrDoc.getFieldValue(field)!=null)
			  {
				  fieldValue=solrDoc.getFieldValue(field).toString();
			      if (fieldValue.toLowerCase().indexOf(term.toLowerCase()) != -1)
			      {
			    	  found=new SolrFoundDocumentMetaData(term, field, fieldValue, 
			    			  fieldValue.toLowerCase().indexOf(term.toLowerCase()), documentId, (String)solrDoc.getFieldValue("patientId"));
			          return found;
			      }
			  }
		  }
          return found;
	}
	 
	public static void main(String[] args)
	{

		querySolr("male");
		querySolr("from");
	}
	
}
