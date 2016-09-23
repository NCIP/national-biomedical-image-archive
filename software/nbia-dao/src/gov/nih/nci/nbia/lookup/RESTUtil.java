package gov.nih.nci.nbia.lookup;



import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;






import gov.nih.nci.nbia.dto.StudyDTO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.annotate.*;

import gov.nih.nci.nbia.searchresult.APIURLHolder;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.searchresult.PatientSearchResultImpl;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResult;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResultImpl;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.api.client.Client; 
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.*;
import com.sun.jersey.api.client.filter.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.core.MediaType;

import gov.nih.nci.nbia.dto.StudyDTO;
import gov.nih.nci.ncia.criteria.Criteria;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import gov.nih.nci.ncia.criteria.*;

public class RESTUtil {
	
    private static Map<String, String> userMap = new HashMap<String, String>();
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = Logger.getLogger(RESTUtil.class);
	public static List<PatientSearchResult> getDynamicSearch(List<DynamicSearchCriteria> criteria,
                                                             String stateRelation, 
                                                             String userToken)
	{
        // Use a form because there are an unknown number of values
	    MultivaluedMap form = new MultivaluedMapImpl(); 
	    // stateRelation is how the criteria are logically related "AND", "OR"
	    form.add("stateRelation", stateRelation); 
	    int i=0;
	    // Step through all criteria given, the form fields are appended with an integer
	    // to maintain grouping in REST call (dataGroup0, dataGroup1...)
	    for (DynamicSearchCriteria dcriteria:criteria){
	    	form.add("sourceName"+i,dcriteria.getDataGroup());
	    	form.add("itemName"+i,dcriteria.getField());
	    	form.add("operator"+i,dcriteria.getOperator().getValue());
	    	form.add("value"+i,dcriteria.getValue());
	    	i++;
	    }
	    

		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(); 
		WebResource resource = client.resource(APIURLHolder.getUrl()
				+"/nbia-api/services/getDynamicSearch"); 
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				                          .type(MediaType.APPLICATION_FORM_URLENCODED)
				                          .header("Authorization",  "Bearer "+userToken)
				                          .post(ClientResponse.class, form);
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        // display response
        String output = response.getEntity(String.class);
        List<PatientSearchResultImpl> myObjects;
        try {
        //	Object json = mapper.readValue(output, Object.class);
         //   String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
         //   logger.info("Returned JSON\n"+indented);
            myObjects = mapper.readValue(output, new TypeReference<List<PatientSearchResultImpl>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        List<PatientSearchResult> returnValue=new ArrayList<PatientSearchResult>();
        for (PatientSearchResultImpl result:myObjects)
        {
        	returnValue.add(result);
        }
        return returnValue;
		
	}
	public static void getSimpleSearch(List<Criteria> criteria,
            String userToken)
    {
      // Use a form because there are an unknown number of values
      MultivaluedMap form = new MultivaluedMapImpl(); 

       int i=0;
       // Step through all criteria given, the form fields are appended with an integer
       // to maintain grouping in REST call (dataGroup0, dataGroup1...)
       for (Criteria scriteria:criteria){
    	   if (scriteria instanceof CollectionCriteria) {
    		   CollectionCriteria cri = (CollectionCriteria)scriteria;
    		   for (String collection:((CollectionCriteria) scriteria).getCollectionObjects()){
    	         form.add("criteriaType"+i,"CollectionCriteria");
    	         form.add("value"+i, collection);
    		   }
    	   }
    	   if (scriteria instanceof ImageModalityCriteria) {
    		   ImageModalityCriteria cri = (ImageModalityCriteria)scriteria;
    		   for (String value:((ImageModalityCriteria) scriteria).getImageModalityObjects()){
    	         form.add("criteriaType"+i,"ImageModalityCriteria");
    	         form.add("value"+i, value);
    		   }
    	   }
    	   if (scriteria instanceof AnatomicalSiteCriteria) {
    		   AnatomicalSiteCriteria cri = (AnatomicalSiteCriteria)scriteria;
    		   for (String value:((AnatomicalSiteCriteria) scriteria).getAnatomicalSiteValueObjects()){
    	         form.add("criteriaType"+i,"AnatomicalSiteCriteria");
    	         form.add("value"+i, value);
    		   }
    	   }
    	   if (scriteria instanceof ManufacturerCriteria) {
    		   ManufacturerCriteria cri = (ManufacturerCriteria)scriteria;
    		   for (String value:((ManufacturerCriteria) scriteria).getManufacturerObjects()){
    	         form.add("criteriaType"+i,"ManufacturerCriteria");
    	         form.add("value"+i, value);
    		   }
    	   }
    	   if (scriteria instanceof DateRangeCriteria) {
    		   DateRangeCriteria cri = (DateRangeCriteria)scriteria;
    		   form.add("criteriaType"+i,"DateRangeCriteria");
    		   SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");
    	       form.add("fromDate"+i, formatter.format(cri.getFromDate()));
    	       form.add("toDate"+i, formatter.format(cri.getToDate()));
    	   }
    	   if (scriteria instanceof PatientCriteria) {
    		   PatientCriteria cri = (PatientCriteria)scriteria;
    		   for (String value:((PatientCriteria) scriteria).getPatientIdObjects()){
    	         form.add("criteriaType"+i,"PatientCriteria");
    	         form.add("value"+i, value);
    		   }
    	   }
    	   if (scriteria instanceof MinNumberOfStudiesCriteria) {
    		   MinNumberOfStudiesCriteria cri = (MinNumberOfStudiesCriteria)scriteria;
    	       form.add("criteriaType"+i,"MinNumberOfStudiesCriteria");
    	       form.add("value"+i, cri.getMinNumberOfStudiesValue().toString());
    	   }
       i++;
      }


       ClientConfig cc = new DefaultClientConfig();
       cc.getClasses().add(JacksonJsonProvider.class);
       Client client = Client.create(); 
       WebResource resource = client.resource(APIURLHolder.getUrl()
    	       +"/nbia-api/services/getSimpleSearch"); 
       ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
    	       .type(MediaType.APPLICATION_FORM_URLENCODED)
               .header("Authorization",  "Bearer "+userToken)
               .post(ClientResponse.class, form);
        // check response status code
       if (response.getStatus() != 200) {
           throw new RuntimeException("Failed : HTTP error code : "
        	       + response.getStatus());
           }

       // display response
       String output = response.getEntity(String.class);
       List<PatientSearchResultImpl> myObjects;
       try {
       //    Object json = mapper.readValue(output, Object.class);
        //   String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        //   logger.info("Returned JSON\n"+indented);
           myObjects = mapper.readValue(output, new TypeReference<List<PatientSearchResultImpl>>(){});
           } catch (Exception e) {
               e.printStackTrace();
               return;
           }
       List <PatientSearchResult> returnValue=new ArrayList<PatientSearchResult>();
       for (PatientSearchResultImpl result:myObjects)
        {
           returnValue.add(result);
        }
       return;
} 
	
	public static List<StudyDTO> getStudyDrillDown(List<Integer> criteria, String userToken)
	{

		Form form = new Form(); 
	    // Add all selected studies to the list
	    for (Integer dcriteria:criteria){
	    	form.add("list",dcriteria.toString());
	    }
	    
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(); 
		WebResource resource = client.resource(APIURLHolder.getUrl()
				+"/nbia-api/services/getStudyDrillDown"); 
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                                                  header("Authorization",  "Bearer "+userToken).
				                                  type(MediaType.APPLICATION_FORM_URLENCODED).
				                                  post(ClientResponse.class, form);
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        // display response
        String output = response.getEntity(String.class);
        List<StudyDTO> myObjects;
        try {
        //	Object json = mapper.readValue(output, Object.class);
        //    String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        //    logger.info("Returned JSON\n"+indented);
			myObjects = mapper.readValue(output, new TypeReference<List<StudyDTO>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

        return myObjects;
		
	}
	public static DefaultOAuth2AccessToken getToken(String userName, String password)
	{

		Form form = new Form(); 
	    form.add("username",userName); 
	    form.add("password",password); 
	    form.add("client_id","nbiaRestAPIClient"); 
	    form.add("client_secret","ItsBetweenUAndMe"); 
	    form.add("grant_type","password");
	    
    
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(); 
		WebResource resource = client.resource(APIURLHolder.getUrl()
				+"/nbia-api/oauth/token"); 
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                                                  type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        // display response
        String output = response.getEntity(String.class);
        output="["+output+"]";
        List<DefaultOAuth2AccessToken> myObjects;
        try {
			myObjects = mapper.readValue(output, new TypeReference<List<DefaultOAuth2AccessToken>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

        return myObjects.get(0);
		
	}
	
	public static List<ImageDTO> getImageDrillDown(List<Integer> criteria, String userToken)
	{

		Form form = new Form(); 
	    int i=0;
	    // List of selected series
	    for (Integer dcriteria:criteria){
	    	form.add("list",dcriteria.toString());
	    }
	    
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(); 
		WebResource resource = client.resource(APIURLHolder.getUrl()
				+"/nbia-api/services/getImageDrillDown"); 
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                                                  header("Authorization",  "Bearer "+userToken).
                                                  type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        // display response
        String output = response.getEntity(String.class);
        List<ImageDTO> myObjects;
        try {
        //	Object json = mapper.readValue(output, Object.class);
        //    String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        //    logger.info("Returned JSON\n"+indented);
			myObjects = mapper.readValue(output, new TypeReference<List<ImageDTO>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

        return myObjects;
		
	}
	public static String getJNLP(List<BasketSeriesItemBean> seriesItems,
                                                             String password, 
                                                             boolean annotation,
                                                             String userToken)
	{
        // Use a form because there are an unknown number of values
	    MultivaluedMap form = new MultivaluedMapImpl(); 
	    if ((seriesItems==null)||(seriesItems.size()==0))
	    {
	    	return "";
	    }
	    // Step through all data in series items for display in download manager
	    for (BasketSeriesItemBean item:seriesItems){
	    	form.add("list",item.getSeriesId());
	    }
	    form.add("password", password); 
	    String annotationString="false";
	    if (annotation){
	    	annotationString="true";
	    }
	    form.add("includeAnnotation", annotationString); 
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(); 
		WebResource resource = client.resource(APIURLHolder.getUrl()
				+"/nbia-api/services/getJNLPText"); 
		ClientResponse response = resource.accept(MediaType.TEXT_PLAIN)
				                          .type(MediaType.APPLICATION_FORM_URLENCODED)
				                          .header("Authorization",  "Bearer "+userToken)
				                          .post(ClientResponse.class, form);
        // check response status code
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        // display response
        String output = response.getEntity(String.class);
        return output;
		
	}
	
	public static List<PatientSearchResult> getTextSearch(String textValue,
            String userToken)
   {

		Form form = new Form(); 
    	form.add("textValue",textValue);


	     ClientConfig cc = new DefaultClientConfig();
	     cc.getClasses().add(JacksonJsonProvider.class);
	     Client client = Client.create(); 
	     WebResource resource = client.resource(APIURLHolder.getUrl()
	    	     +"/nbia-api/services/getTextSearch"); 
	     ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
	    	     .type(MediaType.APPLICATION_FORM_URLENCODED)
                 .header("Authorization",  "Bearer "+userToken)
                 .post(ClientResponse.class, form);
         // check response status code
        if (response.getStatus() != 200) {
             throw new RuntimeException("Failed : HTTP error code : "
             + response.getStatus());
        }

        // display response
        String output = response.getEntity(String.class);
        List<PatientTextSearchResult> myObjects;
        try {
            //Object json = mapper.readValue(output, Object.class);
          //  String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
           // logger.info("Returned JSON\n"+indented);
            myObjects = mapper.readValue(output, new TypeReference<List<PatientTextSearchResultImpl>>(){});
         } catch (Exception e) {
         e.printStackTrace();
         return null;
        }
        List<PatientSearchResult> returnValue=new ArrayList<PatientSearchResult>();
        for (PatientTextSearchResult result:myObjects)
        {
           returnValue.add(result);
        }
      return returnValue;
}
	
	
}
