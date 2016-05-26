package gov.nih.nci.nbia.restUtil;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.dto.StudyDTO;
import gov.nih.nci.nbia.dto.ImageDTO;
import java.io.IOException;
import java.util.List;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResultImpl;
import gov.nih.nci.nbia.textsupport.PatientTextSearchResult;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
public class JSONUtil {
	
	public static String getJSONforPatients(List<PatientSearchResult> patients){
		String jsonInString = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonInString = mapper.writeValueAsString(patients);
		} catch (Exception e) {

			e.printStackTrace();
			return("Unable to map to JSON");
		}
		return jsonInString;
	}
	public static String getJSONforTextPatients(List<PatientTextSearchResult> patients){
		String jsonInString = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonInString = mapper.writeValueAsString(patients);
		} catch (Exception e) {

			e.printStackTrace();
			return("Unable to map to JSON");
		}
		return jsonInString;
	}
	public static String getJSONforStudies(List<StudyDTO> studies){
		String jsonInString = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonInString = mapper.writeValueAsString(studies);
		} catch (Exception e) {

			e.printStackTrace();
			return("Unable to map to JSON");
		}
		return jsonInString;
	}
	public static String getJSONforImages(List<ImageDTO> images){
		String jsonInString = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonInString = mapper.writeValueAsString(images);
		} catch (Exception e) {

			e.printStackTrace();
			return("Unable to map to JSON");
		}
		return jsonInString;
	}
}
