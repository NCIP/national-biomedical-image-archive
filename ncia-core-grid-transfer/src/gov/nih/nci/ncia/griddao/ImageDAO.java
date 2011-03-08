/**
 *
 */
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.gridzip.ZippingDTO;
import gov.nih.nci.ncia.internaldomain.GeneralImage;
import gov.nih.nci.ncia.util.HqlUtils;
import gov.nih.nci.ncia.util.NCIAConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.apache.log4j.Logger;
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
			StringBuffer sbSOPInstanceUIDList) throws Exception{
		StringBuffer hql = new StringBuffer();
		Map<String, String> retrievedFileNames = new HashMap<String, String>();

		if (sbSOPInstanceUIDList == null
					||  sbSOPInstanceUIDList.length() <= 0) {
			return null;
		}
	
		hql.append(IMAGE_STATEMENT);
		hql.append(" WHERE SOPInstanceUID = ");
		hql.append(sbSOPInstanceUIDList);
	
		List<GeneralImage> rs = this.getHibernateTemplate().find(hql.toString());
		retrievedFileNames = process(rs);
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
			String hql = IMAGE_STATEMENT + " WHERE patientId = '" + patientId + "'";
			List<GeneralImage> rs = getHibernateTemplate().find(hql);

			return process(rs);
	}

	/**
	 * Retrieve image file path by studyInstanceUID
	 * @param studyInstanceUID
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, String> getImagesFilesByStudyInstanceUID(
			String studyInstanceUID) throws Exception{
			String hql = "";
			if (studyInstanceUID == null) {
				return null;
			}
			hql = IMAGE_STATEMENT + " WHERE studyInstanceUID = '" + studyInstanceUID + "'";
			List<GeneralImage> result = getHibernateTemplate().find(hql);
			return process(result); 
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, String> getImagesFilesBySeriesInstanceUID(
			String seriesInstanceUID) throws Exception{
		
		if (seriesInstanceUID == null)
		{
			return null;
		}
		String hql = IMAGE_STATEMENT + " WHERE seriesInstanceUID = '"
						+ seriesInstanceUID + "'";

		List<GeneralImage> results = getHibernateTemplate().find(hql);
		
		return process(results);
	}


	public List<ZippingDTO> getImageFilesByPatientIds(List<String> patientIds)throws Exception{
			if (patientIds == null || patientIds.size() <= 0) {
				return null;
			}
			String patientList = HqlUtils.buildInClause("", patientIds);
			String hql = IMAGE_STATEMENT + ", GeneralSeries gs"
					+ " WHERE gi.patientPkId=gs.patientPkId AND gs.visibility = '1' AND gi.patientId in "
					+ patientList + ") ";
			List<GeneralImage> rs = getHibernateTemplate().find(hql);
		return processDTO(rs);
	}

	public List<ZippingDTO> getImageFilesByStudiesUids(List<String> studyInstanceUids) throws Exception {
			if (studyInstanceUids == null || studyInstanceUids.size() <=0) {
				return null;
			}
			String studyList = HqlUtils.buildInClause("", studyInstanceUids);
			String hql = IMAGE_STATEMENT +
						", GeneralSeries gs "
						+ " WHERE gs.id= gi.seriesPKId AND gs.visibility = '1' AND gi.studyInstanceUID in "
						+ studyList + ") ";
			//rs = stmt.executeQuery(sql);
			List<GeneralImage> rs = getHibernateTemplate().find(hql);
			return processDTO(rs);
	}

	public List<ZippingDTO> getImageFilesBySeriesUids(List<String> seriesInstanceUids) throws Exception{

		if (seriesInstanceUids == null || seriesInstanceUids.size() <=0) {
			return null;
		}
		String seriesList = HqlUtils.buildInClause("", seriesInstanceUids);
		String hql = IMAGE_STATEMENT
				     + ", GeneralSeries gs "
				 	 + " WHERE gi.seriesPKId=gs.id AND gs.visibility = '1' AND gi.seriesInstanceUID in "
					 + seriesList + ")";
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
			image.setCtPitchFactor(ima.getCtimage().getCTPitchFactor()==null? null:ima.getCtimage().getCTPitchFactor().intValue());
			image.setConvolutionKernel(ima.getCtimage().getConvolutionKernel());
			image.setDataCollectionDiameter(ima.getCtimage().getDataCollectionDiameter());
			image.setExposure(ima.getCtimage().getExposure());
			image.setExposureInMicroAs(ima.getCtimage().getExposureInMicroAs());
			image.setExposureTime(ima.getCtimage().getExposureTime());
			image.setKvp(ima.getCtimage().getKVP());
			image.setGantryDetectorTilt(ima.getCtimage().getGantryDetectorTilt());
			image.setReconstructionDiameter(ima.getCtimage().getReconstructionDiameter());
			image.setRevolutionTime(ima.getCtimage().getRevolutionTime() == null ? null : ima.getCtimage().getRevolutionTime().intValue());
			image.setScanOptions(ima.getCtimage().getScanOptions());
			image.setSingleCollimationWidth(ima.getCtimage().getSingleCollimationWidth()== null ? null : ima.getCtimage().getSingleCollimationWidth().intValue());
			image.setTableFeedPerRotation(ima.getCtimage().getTableFeedPerRotation() == null ? null : ima.getCtimage().getTableFeedPerRotation().intValue());
			image.setTableSpeed(ima.getCtimage().getTableSpeed() == null ? null : ima.getCtimage().getTableSpeed().intValue());
			image.setTotalCollimationWidth(ima.getCtimage().getTotalCollimationWidth() == null ? null : ima.getCtimage().getTotalCollimationWidth().intValue());
			image.setXrayTubeCurrent(ima.getCtimage().getXRayTubeCurrent());
		}

		return image;
	}


	//////////////////////////////////////PRIVATE////////////////////////////////////////////

	private static Logger logger = Logger.getLogger(ImageDAO.class);

	private final static String IMAGE_STATEMENT = "SELECT gi FROM GeneralImage gi";


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

	private static List<ZippingDTO> processDTO(List<GeneralImage> rs) {
		List<ZippingDTO> dtoList = new ArrayList<ZippingDTO>();
		int count = 0;
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
				count++;
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
    
//    public static void main(String[] args){
//    	try{
//        	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
//        	Date d = new Date();
//    		String today = df.format(d);
//    		System.out.println(today);
//    		Date d1 = df.parse(today);
//    		System.out.println(ImageDAO.toDateString(d1, true));
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
//    }
}
