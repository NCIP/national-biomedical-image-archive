package de.iftm.dcm4che.services;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import in.raster.oviyam.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class NBIARequester {
private static String lastSeries;
public static ArrayList<StudyModel> getStudyModels	(String patientID, String studyUID, String rstUrl, String oviyamId, String wadoUrl)
{
	ArrayList<StudyModel> studyList = new ArrayList<StudyModel>();
	try {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		System.out.println("Input:"+rstUrl);
		rstUrl=rstUrl.substring(0, rstUrl.indexOf("patientInfo"));
		String fullURL = rstUrl+"/getPatientStudy?format=json";
		if (patientID!=null&&patientID.length()>1){
			fullURL =fullURL+"&PatientID="+patientID;
		}
		if (studyUID!=null&&studyUID.length()>1){
			fullURL =fullURL+"&StudyInstanceUID="+studyUID;
		}
		fullURL =fullURL+"&oviyamId="+oviyamId+"&wadoUrl="+wadoUrl;
		System.out.println(fullURL);
		HttpGet httpGet = new HttpGet(fullURL);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		String bodyAsString = EntityUtils.toString(response1.getEntity());
		//System.out.println(bodyAsString);
		JSONParser parser = new JSONParser();
		Object jobj = parser.parse(bodyAsString);
		JSONArray jsonarray = (JSONArray)jobj;

		for (int i=0; i<jsonarray.size(); i++) {
			StudyModel model = new StudyModel();
		    JSONObject jsonObject= (JSONObject)jsonarray.get(i);
	        String studyInstanceUID = (String) jsonObject.get("StudyInstanceUID");
		    System.out.println(studyInstanceUID);
		    model.setStudyInstanceUID(studyInstanceUID);
		    model.setStudyDate((String) jsonObject.get("StudyDate"));
		    model.setStudyDescription((String) jsonObject.get("StudyDescription"));
		    model.setPatientID((String) jsonObject.get("PatientID"));
		    model.setPatientName((String) jsonObject.get("PatientName"));
		    model.setPatientGender((String) jsonObject.get("PatientSex"));
		    model.setPatientBirthDate("PatientBirthDate");
		    model.setAccessionNumber("");
		    model.setModalitiesInStudy("");
		    model.setStudyRelatedInstances("");
		    model.setStudyRelatedSeries("");
		    model.setStudyTime("");
		    studyList.add(model);		    
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return studyList;
}
public static ArrayList<SeriesModel> getSeriesModels	(String patientID, String studyUID, String rstUrl, String oviyamId, String wadoUrl, String seriesInstanceUIDIn)
{
	ArrayList<SeriesModel> seriesList = new ArrayList<SeriesModel>();
	try {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		rstUrl=rstUrl.substring(0, rstUrl.indexOf("patientInfo"));
		String fullURL = rstUrl+"/getSeries?format=json";
		if (patientID!=null&&patientID.length()>1){
			fullURL =fullURL+"&PatientID="+patientID;
		}
		if (studyUID!=null&&studyUID.length()>1){
			fullURL =fullURL+"&StudyInstanceUID="+studyUID;
		}
		// Oviyam sometime calls this method twice, the second time without the series uid
		if (seriesInstanceUIDIn!=null&&seriesInstanceUIDIn.length()>1){
			fullURL =fullURL+"&seriesUid="+seriesInstanceUIDIn;
			lastSeries=seriesInstanceUIDIn;
		} else {
			fullURL =fullURL+"&seriesUid="+lastSeries;
		}
		fullURL =fullURL+"&oviyamId="+oviyamId+"&wadoUrl="+wadoUrl;
		System.out.println("Series Call: "+fullURL);
		HttpGet httpGet = new HttpGet(fullURL);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		String bodyAsString = EntityUtils.toString(response1.getEntity());
		//System.out.println(bodyAsString);
		JSONParser parser = new JSONParser();
		Object jobj = parser.parse(bodyAsString);
		JSONArray jsonarray = (JSONArray)jobj;
		for (int i=0; i<jsonarray.size(); i++) {
			SeriesModel model = new SeriesModel();
		    JSONObject jsonObject= (JSONObject)jsonarray.get(i);
	        String seriesInstanceUID = (String) jsonObject.get("SeriesInstanceUID");
		    System.out.println(seriesInstanceUID);
		    model.setSeriesIUID(seriesInstanceUID);
		    model.setSeriesDescription((String) jsonObject.get("SeriesDescription"));
		    model.setModality((String) jsonObject.get("Modality"));
		    model.setBodyPartExamined((String) jsonObject.get("BodyPartExamined"));
		    model.setNumberOfInstances(jsonObject.get("ImageCount").toString());
		    model.setSeriesDate((String) jsonObject.get("SeriesDate"));
		    model.setSeriesNumber(jsonObject.get("SeriesNumber").toString());
		    model.setSeriesTime("");
		    seriesList.add(model);	
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return seriesList;
}
public static ArrayList<InstanceModel> getInstanceModels(String patientID, String studyInstanceUID, String seriesInstanceUID, String SOPInstanceUID, String rstUrl, String oviyamId, String wadoUrl)
{
	ArrayList<InstanceModel> instanceList = new ArrayList<InstanceModel>();
	try {
		System.out.println("Input Instance Request:"+rstUrl);
		rstUrl=rstUrl.substring(0, rstUrl.indexOf("patientInfo"));
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String fullURL = rstUrl+"/getInstance?format=json";
		if (patientID!=null&&patientID.length()>1){
			fullURL =fullURL+"&PatientID="+patientID;
		}
		if (studyInstanceUID!=null&&studyInstanceUID.length()>1){
			fullURL =fullURL+"&StudyInstanceUID="+studyInstanceUID;
		}
		if (seriesInstanceUID!=null&&seriesInstanceUID.length()>1){
			fullURL =fullURL+"&seriesUID="+seriesInstanceUID;
		}
		if (SOPInstanceUID!=null&&SOPInstanceUID.length()>1){
			fullURL =fullURL+"&SOPInstanceUID="+SOPInstanceUID;
		}
		fullURL =fullURL+"&oviyamId="+oviyamId+"&wadoUrl="+wadoUrl;
		//System.out.println(fullURL);
		HttpGet httpGet = new HttpGet(fullURL);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		String bodyAsString = EntityUtils.toString(response1.getEntity());
		//System.out.println(bodyAsString);
		JSONParser parser = new JSONParser();
		Object jobj = parser.parse(bodyAsString);
		JSONArray jsonarray = (JSONArray)jobj;
		for (int i=0; i<jsonarray.size(); i++) {
			InstanceModel model = new InstanceModel();
		    JSONObject jsonObject= (JSONObject)jsonarray.get(i);
		    model.setInstanceNumber(jsonObject.get("InstanceNumber").toString());
		    model.setNumberOfFrames(jsonObject.get("NumberOfFrames").toString());
		    model.setRows(jsonObject.get("Rows").toString());
		    model.setSopClassUID((String) jsonObject.get("SopClassUID"));
		    model.setSopIUID((String) jsonObject.get("SopIUID"));
		    instanceList.add(model);	
		}

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return instanceList;
}
public static void main(String[]args)
{
	String url="https://imaging.nci.nih.gov/nbia-api/services/v1/";
	String patientId="1.3.6.1.4.1.9328.50.1.0043";
    String studyId="1.3.6.1.4.1.9328.50.1.21854";
    getStudyModels	(patientId, studyId, url, "test", "test");
    getSeriesModels	(patientId, studyId, url, "test", "test", "test");
    url="http://localhost:45210/nbia-api/services/v1/";
    System.out.println(getInstanceModels(null, "88.8.212260453684133744634912263088626970434", null, null, url, "test", "test"));
}


}
