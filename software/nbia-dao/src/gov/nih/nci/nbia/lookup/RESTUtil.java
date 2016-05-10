package gov.nih.nci.nbia.lookup;



import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;




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

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.api.client.Client; 
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.*;
import com.sun.jersey.api.client.filter.*;

import java.io.IOException;
import java.util.*;

import javax.ws.rs.core.MediaType;

import gov.nih.nci.nbia.dto.StudyDTO;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

public class RESTUtil {
	
    private static Map<String, String> userMap = new HashMap<String, String>();
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = Logger.getLogger(RESTUtil.class);
    private static String token = "2dc8965f-ae26-4b8f-8bdc-affb385da3fc";
	public static List<PatientSearchResult> getDynamicSearch(List<DynamicSearchCriteria> criteria,
                                                             String stateRelation, 
                                                             String userName)
	{
        // Use a form because there are an unknown number of values
	    MultivaluedMap form = new MultivaluedMapImpl(); 
	    // stateRelation is how the criteria are logically related "AND", "OR"
	    form.add("stateRelation", stateRelation); 
	    form.add("userName",userName); 
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
		client.addFilter(new LoggingFilter(System.out));
		WebResource resource = client.resource(APIURLHolder.getUrl()
				+"/nbia-api/services/getDynamicSearch"); 
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				                          .type(MediaType.APPLICATION_FORM_URLENCODED)
				                          .header("Authorization", token)
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
        	Object json = mapper.readValue(output, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            logger.info("Returned JSON\n"+indented);
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
	public static List<StudyDTO> getStudyDrillDown(List<Integer> criteria, String userName)
	{

		Form form = new Form(); 
	    form.add("userName",userName); 
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
                                                  header("Authorization", token).
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
        	Object json = mapper.readValue(output, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            logger.info("Returned JSON\n"+indented);
			myObjects = mapper.readValue(output, new TypeReference<List<StudyDTO>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

        return myObjects;
		
	}
	public static List<ImageDTO> getImageDrillDown(List<Integer> criteria, String userName)
	{

		Form form = new Form(); 
	    form.add("userName",userName); 
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
                                                  header("Authorization", token).
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
        	Object json = mapper.readValue(output, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            logger.info("Returned JSON\n"+indented);
			myObjects = mapper.readValue(output, new TypeReference<List<ImageDTO>>(){});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

        return myObjects;
		
	}
	

	public static List<PatientSearchResult> getJNLP(List<DynamicSearchCriteria> criteria,
                                                             String stateRelation, 
                                                             String userName,
                                                             List<BasketSeriesItemBean> seriesItems)
	{
        // Use a form because there are an unknown number of values
	    MultivaluedMap form = new MultivaluedMapImpl(); 
	    int i=0;
	    // Step through all data in series items for display in download manager
	    for (BasketSeriesItemBean item:seriesItems){
	    	form.add("collection"+i,item.getProject());
	    	form.add("patientId"+i,item.getPatientId());
	    	form.add("annotation"+i,item.getAnnotated());
	    	form.add("seriesInstanceUid"+i,item.getSeriesId());
	    	i++;
	    }
/***	    
	    
	                    String collection = seriesItem.getProject();
                String patientId = seriesItem.getPatientId();
                String studyInstanceUid = seriesItem.getStudyId();
                String seriesInstanceUid =seriesItem.getSeriesId();
                String annotation = seriesItem.getAnnotated();
                Integer numberImages = seriesItem.getTotalImagesInSeries();
                Long imagesSize = seriesItem.getTotalSizeForAllImagesInSeries();
                Long annoSize = seriesItem.getAnnotationsSize();
	    
	    
	    
	    
	    
	    
	    ***/
	    
	    
	    

		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(); 
		client.addFilter(new LoggingFilter(System.out));
		WebResource resource = client.resource(APIURLHolder.getUrl()
				+"/nbia-api/services/getDynamicSearch"); 
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				                          .type(MediaType.APPLICATION_FORM_URLENCODED)
				                          .header("Authorization", token)
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
        	Object json = mapper.readValue(output, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            logger.info("Returned JSON\n"+indented);
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
	
	
	
	
}
