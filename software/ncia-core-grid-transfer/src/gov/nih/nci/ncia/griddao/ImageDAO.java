/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.nbia.internaldomain.Annotation;
import gov.nih.nci.nbia.internaldomain.GeneralImage;
import gov.nih.nci.nbia.util.HqlUtils;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.Set;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ImageDAO extends HibernateDaoSupport implements ImageDAOInterface{

	@Transactional(propagation=Propagation.REQUIRED)
	public List<ZippingDTO> getImagesByNthStudyTimePointForPatient(String patientId,
			                                                       Date dateForTimepoint) throws Exception{
		List<ZippingDTO> dtoList = null;
		if(dateForTimepoint == null || patientId == null ){
			return new ArrayList<ZippingDTO>();
		}
		String hql = "select gi from GeneralImage gi, GeneralSeries gs, Study s " +
					"where gi.study.id = s.id and gi.patientPkId = s.patient.id and gi.generalSeries.id=gs.id and " +
					"gi.patientId='"+patientId + "' and s.studyDate="+ toDateString(dateForTimepoint, isOracle()) + " and gs.visibility='1' ";

		List<GeneralImage> rs = this.getHibernateTemplate().find(hql);
		dtoList = processDTO(rs);
		return dtoList;
	}


	/**
	 * This method queries the database with a list of SOPInstanceUIDs to get
	 * the file path of the Image.
	 *
	 * @param sbSOPInstanceUIDList
	 * @return Map
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, String> getImagesFiles(
			List<String> sbSOPInstanceUIDList) throws Exception{
		Map<String, String> retrievedFileNames = new HashMap<String, String>();

		if (sbSOPInstanceUIDList == null){
			return null;
		}
		if(sbSOPInstanceUIDList.size() > 0)
		{
			String sopInstanceList = HqlUtils.buildInClause("", sbSOPInstanceUIDList);
			
			String hql = IMAGE_STATEMENT +
			             " WHERE gi.SOPInstanceUID in " +
			             sopInstanceList;
			List<GeneralImage> rs = this.getHibernateTemplate().find(hql);
			retrievedFileNames = process(rs);
		}
		return retrievedFileNames;
	}

	/**
	 * This method queries the database with a PatientId to get
	 * the file path of the Image.
	 *
	 * @return HashMap
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, String> getImagesFilesByPatientId(String patientId) throws Exception{
			if (patientId == null) {
				return null;
			}
			String hql = IMAGE_SERIES_STATEMENT +
						" WHERE gs.id= gi.seriesPKId AND gs.visibility = '1' "+
						" and gi.patientId = '" + patientId + "'";
			List<GeneralImage> rs = getHibernateTemplate().find(hql);
			Map<String, String> filePathes = process(rs);
			if (rs != null && rs.size() > 0){
				Set<String> seriesIds = new HashSet<String>();
				for(GeneralImage image : rs){
					seriesIds.add(image.getSeriesInstanceUID());
				}
				filePathes = getImageAnnotationFileBySeriesInstanceUID(new ArrayList<String>(seriesIds), filePathes);
			}
			return filePathes;
	}

	/**
	 * Retrieve image file path by studyInstanceUID
	 * @param studyInstanceUID
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, String> getImagesFilesByStudyInstanceUID(
			String studyInstanceUID) throws Exception{
			if (studyInstanceUID == null) {
				return null;
			}
			String hql = IMAGE_SERIES_STATEMENT +
				  " WHERE gs.id= gi.seriesPKId AND gs.visibility = '1' " +
					" and gi.studyInstanceUID = '" + studyInstanceUID + "'";
			List<GeneralImage> result = getHibernateTemplate().find(hql);
			Map<String, String> filePathes = process(result);
			if (result != null && result.size() > 0){
				Set<String> seriesIds = new HashSet<String>();
				for(GeneralImage image : result){
					seriesIds.add(image.getSeriesInstanceUID());
				}
				filePathes = getImageAnnotationFileBySeriesInstanceUID(new ArrayList<String>(seriesIds), filePathes);
			}
			return filePathes;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, String> getImagesFilesBySeriesInstanceUID(
			String seriesInstanceUID) throws Exception{

		if (seriesInstanceUID == null)
		{
			return null;
		}
		String hql = IMAGE_SERIES_STATEMENT +
						" WHERE gs.id= gi.seriesPKId AND gs.visibility = '1' "+
						" and gi.seriesInstanceUID = '"
						+ seriesInstanceUID + "'";

		List<GeneralImage> results = getHibernateTemplate().find(hql);

		Map<String, String> downloadFiles = new HashMap<String, String>();
		downloadFiles = process(results);
		if (results != null && results.size() > 0){
			Set<String> seriesIds = new HashSet<String>();
			seriesIds.add(seriesInstanceUID);
			downloadFiles = getImageAnnotationFileBySeriesInstanceUID(new ArrayList<String>(seriesIds), downloadFiles);
		}
		return downloadFiles;
	}

	public List<ZippingDTO> getImageFilesByPatientIds(List<String> patientIds)throws Exception{
			if (patientIds == null || patientIds.size() <= 0) {
				return null;
			}
			String patientList = HqlUtils.buildInClause("", patientIds);
			String hql = IMAGE_SERIES_STATEMENT
					+ " WHERE gi.seriesPKId=gs.id AND gs.visibility = '1' AND gi.patientId in "
					+ patientList;
			List<GeneralImage> rs = getHibernateTemplate().find(hql);
		return processDTO(rs);
	}

	public List<ZippingDTO> getImageFilesByStudiesUids(List<String> studyInstanceUids) throws Exception {
			if (studyInstanceUids == null || studyInstanceUids.size() <=0) {
				return null;
			}
			String studyList = HqlUtils.buildInClause("", studyInstanceUids);
			String hql = IMAGE_SERIES_STATEMENT
						+ " WHERE gs.id= gi.seriesPKId AND gs.visibility = '1' AND gi.studyInstanceUID in "
						+ studyList;

			List<GeneralImage> rs = getHibernateTemplate().find(hql);
			return processDTO(rs);
	}

	public List<ZippingDTO> getImageFilesBySeriesUids(List<String> seriesInstanceUids) throws Exception{

		if (seriesInstanceUids == null || seriesInstanceUids.size() <=0) {
			return null;
		}
		String seriesList = HqlUtils.buildInClause("", seriesInstanceUids);
		String hql = IMAGE_SERIES_STATEMENT
				 	 + " WHERE gi.seriesPKId=gs.id AND gs.visibility = '1' AND gi.seriesInstanceUID in "
					 + seriesList;
		List<GeneralImage> rs = getHibernateTemplate().find(hql);

		return processDTO(rs);
	}


	public Image getRepresentativeImageBySeries(String seriesInstanceUID)throws Exception{

		String hql = "select gi from GeneralImage gi where gi.seriesInstanceUID = '" + seriesInstanceUID + "' order by gi.instanceNumber, gi.id";
		int imagePkId=0;
		List<Integer> imagePkIds = new ArrayList<Integer>();

		Image image = new Image();

		List<GeneralImage> rs = this.getHibernateTemplate().find(hql);
		for(GeneralImage im : rs) {
			imagePkIds.add(im.getId());
		}
		int totalImages = imagePkIds.size();
		if( totalImages > 0 ){
			int middleValue = totalImages/2;
			imagePkId = imagePkIds.get(middleValue);
		}

		hql = "select gi from GeneralImage gi, CTImage ct where gi.id= ct.generalImage.id and gi.id= " + imagePkId;
		List<GeneralImage> result = this.getHibernateTemplate().find(hql);
		for(GeneralImage ima : result){
			image.setAcquisitionDate(ima.getAcquisitionDate());
			image.setAcquisitionDatetime(ima.getAcquisitionDatetime());
			image.setAcquisitionMatrix(ima.getAcquisitionMatrix());
			image.setAcquisitionNumber(ima.getAcquisitionNumber());
			image.setAcquisitionTime(ima.getAcquisitionTime());
			image.setColumns(ima.getColumns());
			image.setContentDate(ima.getContentDate());
			image.setContentTime(ima.getContentTime());
			image.setContrastBolusAgent(ima.getContrastBolusAgent());
			image.setContrastBolusRoute(ima.getContrastBolusRoute());
			image.setFocalSpotSize(ima.getFocalSpotSize());
			image.setImageOrientationPatient(ima.getImageOrientationPatient());
			image.setImagePositionPatient(ima.getImagePositionPatient());
			image.setInstanceNumber(ima.getInstanceNumber());
			image.setImageType(ima.getImageType());
			image.setImageComments(ima.getImageComments());
			image.setImageLaterality(ima.getImageLaterality());
			image.setLossyImageCompression(ima.getLossyImageCompression());
			image.setPatientPosition(ima.getPatientPosition());
			image.setPixelSpacing(ima.getPixelSpacing());
			image.setRows(ima.getRows());
			image.setSliceThickness(ima.getSliceThickness());
			image.setSliceLocation(ima.getSliceLocation());
			image.setSopClassUID(ima.getSOPClassUID());
			image.setSopInstanceUID(ima.getSOPInstanceUID());
			image.setSourceToDetectorDistance(ima.getSourceToDetectorDistance());
			image.setSourceSubjectDistance(ima.getSourceSubjectDistance());
			image.setStorageMediaFileSetUID(ima.getStorageMediaFileSetUID());
			image.setAnatomicRegionSequence(ima.getCtimage().getAnatomicRegionSeq());
			image.setCtPitchFactor(nullSafeDouble(ima.getCtimage().getCTPitchFactor()));
			image.setConvolutionKernel(ima.getCtimage().getConvolutionKernel());
			image.setDataCollectionDiameter(ima.getCtimage().getDataCollectionDiameter());
			image.setExposure(ima.getCtimage().getExposure());
			image.setExposureInMicroAs(ima.getCtimage().getExposureInMicroAs());
			image.setExposureTime(ima.getCtimage().getExposureTime());
			image.setKvp(ima.getCtimage().getKVP());
			image.setGantryDetectorTilt(ima.getCtimage().getGantryDetectorTilt());
			image.setReconstructionDiameter(ima.getCtimage().getReconstructionDiameter());
			image.setRevolutionTime(nullSafeDouble(ima.getCtimage().getRevolutionTime()));
			image.setScanOptions(ima.getCtimage().getScanOptions());
			image.setSingleCollimationWidth(nullSafeDouble(ima.getCtimage().getSingleCollimationWidth()));
			image.setTableFeedPerRotation(nullSafeDouble(ima.getCtimage().getTableFeedPerRotation()));
			image.setTableSpeed(nullSafeDouble(ima.getCtimage().getTableSpeed()));
			image.setTotalCollimationWidth(nullSafeDouble(ima.getCtimage().getTotalCollimationWidth()));
			image.setXrayTubeCurrent(ima.getCtimage().getXRayTubeCurrent());
		}

		return image;
	}


	//////////////////////////////////////PRIVATE////////////////////////////////////////////

	private final static String IMAGE_STATEMENT = "SELECT gi FROM GeneralImage gi";
	private final static String IMAGE_SERIES_STATEMENT = "SELECT gi FROM GeneralImage gi, GeneralSeries gs ";

    private static Integer nullSafeDouble(Double d) {
        if(d==null) {
			return null;
	    }
	    else {
			return d.intValue();
		}

	}

	private static Map<String, String> process(List<GeneralImage> rs) throws Exception{
		Map<String, String> retrievedFileNames = new HashMap<String, String>();
		if (rs != null){
			for(GeneralImage image : rs)
			{
				String imagePath = image.getFilename();
				String sop = image.getSOPInstanceUID();
				retrievedFileNames.put(sop + ".dcm", imagePath);
			}
		}

		return retrievedFileNames;
	}

	private List<ZippingDTO> processDTO(List<GeneralImage> rs) {
		List<ZippingDTO> dtoList = new ArrayList<ZippingDTO>();
		Map<String, GeneralImage> tempHolder = new HashMap<String, GeneralImage>();
		for (GeneralImage image : rs) {
				String project = image.getProject();
				String patientId = image.getPatientId();
				String studyInstanceUid = image.getStudyInstanceUID();
				String seriesInstanceUid = image.getSeriesInstanceUID();
				String imagePath = image.getFilename();
				String sop = image.getSOPInstanceUID();

				ZippingDTO zdto = new ZippingDTO();
				zdto.setProject(project);
				zdto.setFilePath(imagePath);
				zdto.setPatientId(patientId);
				zdto.setSeriesInstanceUid(seriesInstanceUid);
				zdto.setStudyInstanceUid(studyInstanceUid);
				zdto.setSopInstanceUidAsFileName(sop + ".dcm");
				dtoList.add(zdto);
				//ignore duplicate images in a series because annoation can only exist in series level
				if (!tempHolder.containsKey(seriesInstanceUid)){
					tempHolder.put(seriesInstanceUid, image);
				}
		}
		if (tempHolder.size() > 0){
			processAnnoationFile(tempHolder, dtoList);
		}
		return dtoList;
	}

	private List<ZippingDTO> processAnnoationFile(Map<String, GeneralImage> temp, List<ZippingDTO> dtoList){
		Set<String> keys = temp.keySet();
		for (String seriesInstanceId : keys){
				GeneralImage im = (GeneralImage)temp.get(seriesInstanceId);
				String project = im.getProject();
				String patientId = im.getPatientId();
				String studyInstanceUid = im.getStudyInstanceUID();
				String seriesInstanceUid = im.getSeriesInstanceUID();
				List<Annotation> annotationList =  getImageAnnotationListBySeriesInstanceUID(seriesInstanceId);
				if (annotationList != null && annotationList.size() > 0){
					for(Annotation ann : annotationList){
						ZippingDTO z = new ZippingDTO();
						z.setProject(project);
						z.setFilePath(ann.getFilePath());
						z.setPatientId(patientId);
						z.setSeriesInstanceUid(seriesInstanceUid);
						z.setStudyInstanceUid(studyInstanceUid);
						File f = new File(ann.getFilePath());
						z.setSopInstanceUidAsFileName(f.getName());
						dtoList.add(z);
					}
				}
		}
		return dtoList;
	}

    private static boolean isOracle() {
    	String str = NCIAConfig.getDatabaseType();
    	if (str.equalsIgnoreCase("mysql") == true){
    		return false;
    	}else{
    		return true;
    	}

	}

    private static String toDateString(Date dateForTimepoint, boolean oracle) {

        if(oracle) {
          	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		String date = df.format(dateForTimepoint);
    		System.out.println("study date: " + date);
			return "to_date('"+date+"','YYYY-MM-DD')";
		}
		else {
			return "'"+dateForTimepoint.toString()+"'";
		}
	}

    private List<Annotation> getImageAnnotationListBySeriesInstanceUID(String seriesInstanceUID)
    {
    	String hql = "SELECT a FROM Annotation a where a.seriesInstanceUID = '" + seriesInstanceUID +"'";
    	return getHibernateTemplate().find(hql);
    }

    private Map<String, String> getImageAnnotationFileBySeriesInstanceUID(List<String> seriesInstanceIds, Map<String, String> filePath)
	{
		Map<String, String> files = filePath;
		String seriesList = HqlUtils.buildInClause("", seriesInstanceIds);
		String hql = "SELECT a FROM Annotation a where a.seriesInstanceUID in " + seriesList;
		List<Annotation> results = getHibernateTemplate().find(hql);

		for (Annotation ann : results){
			String key = getAnnotationName(ann.getFilePath());
			files.put(key, ann.getFilePath());
		}

		return files;
	}

	private String getAnnotationName(String fileName){
		File f = new File(fileName);
		if (f != null){
			return f.getName();
		}else{
			return "no_name.xml";
		}
	}
}
