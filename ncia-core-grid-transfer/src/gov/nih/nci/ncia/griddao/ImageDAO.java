/**
 *
 */
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.DataAccess;
import gov.nih.nci.ncia.domain.Image;
import gov.nih.nci.ncia.gridzip.ZippingDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * This object does hand-rolled data access for anything image related in the
 * grid service.  It violates patterns a bit by potentially returning Hibernate
 * objects.... need to think about that a bit but this is a first cut...
 *
 * <p>This came from a massive object called ImageFileProcessor which was split
 * into this object and several other "DAO" objects to isolate data access according
 * to similar pattern as rest of system.
 *
 * @author lethai
 */
public class ImageDAO {




	public List<ZippingDTO> getImagesByNthStudyTimePointForPatient(String patientId,
			                                                       Date dateForTimepoint) throws Exception{
		DataAccess da = new DataAccess();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ZippingDTO> dtoList = null;
		String sql = "";
		String studyList="";

		try {
			//when timepoint is  out of range or not found, date is null
			if(dateForTimepoint == null){
				return new ArrayList<ZippingDTO>();
			}
			if (patientId != null ) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();
				sql = "SELECT GI.PROJECT, GI.PATIENT_ID, GI.STUDY_INSTANCE_UID, GI.SERIES_INSTANCE_UID, DICOM_FILE_URI, SOP_INSTANCE_UID " +
					" FROM GENERAL_IMAGE GI, GENERAL_SERIES GS, STUDY S " +
					" WHERE GI.STUDY_PK_ID= S.STUDY_PK_ID AND GI.PATIENT_PK_ID=S.PATIENT_PK_ID AND GI.GENERAL_SERIES_PK_ID=GS.GENERAL_SERIES_PK_ID AND" +
					" GI.PATIENT_ID='" + patientId + "' AND S.STUDY_DATE=" + toDateString(dateForTimepoint, isOracle(conn)) + " AND GS.VISIBILITY='1' ";

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				dtoList = processDTO(rs);
				long end = System.currentTimeMillis();
				logger.info("Total time to get image filepath for nthstudy timepoint for patientId  " + patientId + " and studyTimepoint" +  ": "
						+ (end - start) + MS);
			} else {
				logger.info("patientId is empty");
				return new ArrayList<ZippingDTO>();
			}
		} catch (Exception e) {
			logger.error("Could not get image by nth timepoint for patientId: " + studyList
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting image by nth timepoint for patient: " + patientId);
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				//da.closeConnection();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

		return dtoList;
	}


	/**
	 * This method queries the database with a list of SOPInstanceUIDs to get
	 * the file path of the Image.
	 *
	 * @param sbSOPInstanceUIDList
	 * @return Map
	 */
	public Map<String, String> getImagesFiles(
			StringBuffer sbSOPInstanceUIDList) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		Map<String, String> retrievedFileNames = new HashMap<String, String>();

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (sbSOPInstanceUIDList != null
					&& sbSOPInstanceUIDList.length() > 0) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				sql.append(SELECT_STATEMENT);
				sql.append(" WHERE SOP_INSTANCE_UID = ");
				sql.append(sbSOPInstanceUIDList);
				logger.info(sql.toString());
				rs = stmt.executeQuery(sql.toString());
				retrievedFileNames = process(rs);
				long end = System.currentTimeMillis();
				logger.info("Total time to get filepath (JDBC) and add to the Hashmap given SOPInstanceUIDList:  "
								+ (end - start) + MS);
			} else {
				logger.info("SOPInstanceUIDList is empty");
			}
		} catch (Exception e) {
			logger.error(
					"Could not get file paths for sop instance uid list. SQL statement was: "
							+ sql.toString(), e);
			e.printStackTrace();
			throw new Exception("Error getting file paths for sop instance uid list");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				da.closeConnection();
			} catch (SQLException sqle) {
				sqle.printStackTrace();

			}
		}
		return retrievedFileNames;
	}

	/**
	 * This method queries the database with a list of SOPInstanceUIDs to get
	 * the file path of the Image.
	 *
	 * @return HashMap
	 */
	public Map<String, String> getImagesFilesByPatientId(String patientId) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		Map<String, String> retrievedFileNames = null;

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (patientId != null) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				sql = SELECT_STATEMENT + " WHERE PATIENT_ID = '" + patientId + "'";

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				retrievedFileNames = process(rs);
				long end = System.currentTimeMillis();
				logger.info("Total time to get filepath (JDBC) and add to the Hashmap given patientId: "
								+ (end - start) + MS);
			} else {
				logger.info("SOPInstanceUIDList is empty");
			}
		} catch (Exception e) {
			logger.error("Could not get file paths for patientId: " + patientId
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting file path for patient: " +patientId);
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}

			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return retrievedFileNames;
	}

	/**
	 * Retrieve image file path by studyInstanceUID
	 * @param studyInstanceUID
	 */
	public Map<String, String> getImagesFilesByStudyInstanceUID(
			String studyInstanceUID) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		Map<String, String> retrievedFileNames = null;

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (studyInstanceUID != null) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();
				sql = SELECT_STATEMENT + " WHERE STUDY_INSTANCE_UID = '"
						+ studyInstanceUID + "'";

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				retrievedFileNames = process(rs); //this.processResultSet(rs);
				long end = System.currentTimeMillis();
				logger
						.info("Total time to get filepath (JDBC) and add to the Hashmap given studyInstanceUID: "
								+ (end - start) + MS);
			} else {
				logger.info("SOPInstanceUIDList is empty");
			}
		} catch (Exception e) {
			logger
					.error("Could not get file paths for studyInstanaceUID: "
							+ studyInstanceUID + SQL_STMT
							+ sql, e);
			e.printStackTrace();
			throw new Exception("Error getting file path for studyInstanceUID: " + studyInstanceUID);
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return retrievedFileNames;
	}

	public Map<String, String> getImagesFilesBySeriesInstanceUID(
			String seriesInstanceUID) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		Map<String, String> retrievedFileNames = null;

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (seriesInstanceUID != null) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				sql = SELECT_STATEMENT + " WHERE SERIES_INSTANCE_UID = '"
						+ seriesInstanceUID + "'";

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				retrievedFileNames = process(rs); //this.processResultSet(rs);
				long end = System.currentTimeMillis();
				logger
						.info("Total time to get filepath (JDBC) and add to the Hashmap given seriesInstanceUID: "
								+ (end - start) + MS);
			} else {
				logger.info("SeriesInstanceUID is empty");
			}
		} catch (Exception e) {
			logger.error("Could not get file paths for seriesInstanceUID: "
					+ seriesInstanceUID + SQL_STMT + sql,
					e);
			e.printStackTrace();
			throw new Exception("Error getting filepath for seriesInstanceUID: " + seriesInstanceUID);
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return retrievedFileNames;
	}


	public List<ZippingDTO> getImageFilesByPatientIds(List<String> patientIds)throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		List<ZippingDTO> dtoList = null;
		String sql = "";
		String patientList="";

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (patientIds != null && patientIds.size() >0) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();
				patientList = buildWhereClause(patientIds, "GI.PATIENT_ID").toString();
				sql = SELECT_FILE_STATEMENT + ", GENERAL_SERIES GS"
						+ " WHERE GI.PATIENT_PK_ID=GS.PATIENT_PK_ID AND GS.VISIBILITY = '1' AND (GI.PATIENT_ID = "
						+ patientList + ") ";

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				dtoList = processDTO(rs); //this.processResultSetDTO(rs);
				long end = System.currentTimeMillis();
				logger.info("Total time to get image filepath for patientId "
						+ (end - start) + MS);
			} else {
				logger.info("patientId is empty");
			}
		} catch (Exception e) {
			logger.error("Could not get image filepath for patientId: " + patientList
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting image filepath for list of patient ");
			//return null;
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				da.closeConnection();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

		return dtoList;
	}

	public List<ZippingDTO> getImageFilesByStudiesUids(List<String> studyInstanceUids) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		List<ZippingDTO> dtoList = null;
		String sql = "";
		String studyList="";

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (studyInstanceUids != null && studyInstanceUids.size() >0) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();
				studyList = buildWhereClause(studyInstanceUids, "GI.STUDY_INSTANCE_UID").toString();
				logger.debug("studyList: " + studyList);
				sql = SELECT_FILE_STATEMENT +
						", GENERAL_SERIES GS "
						+ " WHERE GS.GENERAL_SERIES_PK_ID= GI.GENERAL_SERIES_PK_ID AND GS.VISIBILITY = '1' AND (GI.STUDY_INSTANCE_UID = "
						+ studyList + ") ";

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				dtoList = processDTO(rs);
				long end = System.currentTimeMillis();
				logger.info("Total time to get image files for studyUIds "
						+ (end - start) + MS);
			} else {
				logger.info("studyUids is empty");
			}
		} catch (Exception e) {
			logger.error("Could not get image file path for list of studyuids: " + studyList
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting image filepath for lis tof studyUids");
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				da.closeConnection();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

		return dtoList;
	}

	public List<ZippingDTO> getImageFilesBySeriesUids(List<String> seriesInstanceUids) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		List<ZippingDTO> dtoList = null;
		String sql = "";
		String seriesList="";

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (seriesInstanceUids != null && seriesInstanceUids.size() >0) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();
				seriesList = buildWhereClause(seriesInstanceUids, "GI.SERIES_INSTANCE_UID").toString();
				logger.info("seriesList: " + seriesList);
				sql = SELECT_FILE_STATEMENT
				        + ", GENERAL_SERIES GS "
						+ " WHERE GI.GENERAL_SERIES_PK_ID=GS.GENERAL_SERIES_PK_ID AND GS.VISIBILITY = '1' AND (GI.SERIES_INSTANCE_UID = "
						+ seriesList + ")";

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				dtoList = processDTO(rs);
				long end = System.currentTimeMillis();
				logger.info("Total time to get image filepath for list of seriesUids "
						+ (end - start) + MS);
			} else {
				logger.info("seriesUids is empty");
			}
		} catch (Exception e) {
			logger.error("Could not get image filepath for seriesUids: " + seriesList
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting image filepath for list of series Uids");
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				da.closeConnection();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		return dtoList;
	}


	public Image getRepresentativeImageBySeries(String seriesInstanceUID)throws Exception{

		String sql = "select image_pk_id from general_image where series_instance_uid = '" + seriesInstanceUID + "' order by instance_number, image_pk_id";

		int imagePkId=0;
		List<Integer> imagePkIds = new ArrayList<Integer>();

		Image image = new Image();
		Statement stmt = null;
		ResultSet rs = null;

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {

				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					imagePkIds.add(rs.getInt("image_pk_id"));
				}
				int totalImages = imagePkIds.size();
				if( totalImages > 0 ){
					int middleValue = totalImages/2;
					imagePkId = imagePkIds.get(middleValue);
				}

				sql = "select gi.acquisition_date, gi.acquisition_datetime, gi.acquisition_matrix, " +
				"gi.acquisition_number,gi.acquisition_time, gi.i_columns, gi.content_date, gi.content_time, " +
				"gi.contrast_bolus_agent,  gi.contrast_bolus_route, gi.focal_spot_size, " +
				"gi.image_orientation_patient, gi.image_position_patient, gi.instance_number, gi.image_type, " +
				"gi.image_comments, gi.image_laterality, gi.lossy_image_compression, gi.patient_position, " +
				"gi.pixel_spacing, gi.i_rows, gi.slice_thickness, gi.slice_location,  gi.sop_class_uid, " +
				"gi.sop_instance_uid,  gi.source_to_detector_distance, gi.source_subject_distance, " +
				"gi.storage_media_file_set_uid, ct.anatomic_region_seq, ct.ct_pitch_factor, ct.convolution_kernel, " +
				"ct.data_collection_diameter,  ct.exposure, ct.exposure_in_microas, ct.exposure_time, ct.kvp, " +
				"ct.gantry_detector_tilt, ct.reconstruction_diameter, ct.revolution_time, ct.scan_options, " +
				"ct.single_collimation_width, ct.table_feed_per_rotation, ct.table_speed, ct.total_collimation_width, " +
				"ct.x_ray_tube_current " +
				"from general_image gi, ct_image ct where gi.image_pk_id= ct.image_pk_id and gi.image_pk_id= " + imagePkId;

				rs = stmt.executeQuery(sql);
				logger.info(sql);
				while(rs.next()){
					image.setAcquisitionDate(rs.getDate("acquisition_date"));
					image.setAcquisitionDatetime(rs.getString("acquisition_datetime"));
					image.setAcquisitionMatrix(rs.getDouble("acquisition_matrix"));
					image.setAcquisitionNumber(rs.getInt("acquisition_number"));
					image.setAcquisitionTime(rs.getString("acquisition_time"));
					image.setColumns(rs.getInt("i_columns"));
					image.setContentDate(rs.getDate("content_date"));
					image.setContentTime(rs.getString("content_time"));
					image.setContrastBolusAgent(rs.getString("contrast_bolus_agent"));
					image.setContrastBolusRoute(rs.getString("contrast_bolus_route"));
					image.setFocalSpotSize(rs.getDouble("focal_spot_size"));
					image.setImageOrientationPatient(rs.getString("image_orientation_patient"));
					image.setImagePositionPatient(rs.getString("image_position_patient"));
					image.setInstanceNumber(rs.getInt("instance_number"));
					image.setImageType(rs.getString("image_type"));
					image.setImageComments(rs.getString("image_comments"));
					image.setImageLaterality(rs.getString("image_laterality"));
					image.setLossyImageCompression(rs.getString("lossy_image_compression"));
					image.setPatientPosition(rs.getString("patient_position"));
					image.setPixelSpacing(rs.getDouble("pixel_spacing"));
					image.setRows(rs.getInt("i_rows"));
					image.setSliceThickness(rs.getDouble("slice_thickness"));
					image.setSliceLocation(rs.getDouble("slice_location"));
					image.setSopClassUID(rs.getString("sop_class_uid"));
					image.setSopInstanceUID(rs.getString("sop_instance_uid"));
					image.setSourceToDetectorDistance(rs.getDouble("source_to_detector_distance"));
					image.setSourceSubjectDistance(rs.getDouble("source_subject_distance"));
					image.setStorageMediaFileSetUID(rs.getString("storage_media_file_set_uid"));
					image.setAnatomicRegionSequence(rs.getString("anatomic_region_seq"));
					image.setCtPitchFactor(rs.getInt("ct_pitch_factor"));
					image.setConvolutionKernel(rs.getString("convolution_kernel"));
					image.setDataCollectionDiameter(rs.getDouble("data_collection_diameter"));
					image.setExposure(rs.getInt("exposure"));
					image.setExposureInMicroAs(rs.getInt("exposure_in_microas"));
					image.setExposureTime(rs.getInt("exposure_time"));
					image.setKvp(rs.getDouble("kvp"));
					image.setGantryDetectorTilt(rs.getDouble("gantry_detector_tilt"));
					image.setReconstructionDiameter(rs.getDouble("reconstruction_diameter"));
					image.setRevolutionTime(rs.getInt("revolution_time"));
					image.setScanOptions(rs.getString("scan_options"));
					image.setSingleCollimationWidth(rs.getInt("single_collimation_width"));
					image.setTableFeedPerRotation(rs.getInt("table_feed_per_rotation"));
					image.setTableSpeed(rs.getInt("table_speed"));
					image.setTotalCollimationWidth(rs.getInt("total_collimation_width"));
					image.setXrayTubeCurrent(rs.getInt("x_ray_tube_current"));
				}
				long end = System.currentTimeMillis();
				logger.info("Total time to get Image for  " + seriesInstanceUID +" : "
						+ (end - start) + MS);

		} catch (Exception e) {
			logger.error("Could not get Image for seriesInstanceUID " + seriesInstanceUID
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting Image for seriesInstanceUID " + seriesInstanceUID);
		} finally {
			da.closeConnection();
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				da.closeConnection();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
				throw new Exception("Error getting Image for seriesInstanceUID " + seriesInstanceUID, sqle);
			}
		}
		return image;
	}


	//////////////////////////////////////PRIVATE////////////////////////////////////////////

	private static Logger logger = Logger.getLogger(ImageDAO.class);

	private final static String SELECT_STATEMENT = "SELECT DICOM_FILE_URI, SOP_INSTANCE_UID FROM GENERAL_IMAGE GI";
	private final static String SELECT_FILE_STATEMENT = "SELECT GI.PROJECT, GI.PATIENT_ID, GI.STUDY_INSTANCE_UID, GI.SERIES_INSTANCE_UID, DICOM_FILE_URI, SOP_INSTANCE_UID FROM GENERAL_IMAGE GI";

	private final static String MS = "ms";

	private final static String SQL_STMT = " SQL statement: ";

	private static StringBuffer buildWhereClause(List<String> ids, String attribute){
		StringBuffer idList = new StringBuffer();
		idList.append('\'');

		Iterator<String> iter = ids.iterator();

		while (iter.hasNext()) {
			String id = iter.next();
			idList.append(id);
			if (iter.hasNext()) {
				idList.append("' OR " +  attribute + " = '");
			}
		}
		idList.append('\'');

		logger.info("build where clause: " + idList.toString());
		return idList;
	}

	/**
	 * This method processes the resultset and return a map that contains
	 * sopInstanceUid key and corresponding image file path
	 * @param rs
	 * @return Map
	 */
	private static Map<String, String> process(ResultSet rs) throws Exception{
		Map<String, String> retrievedFileNames = new HashMap<String, String>();
		try {
			while (rs.next()) {
				String imagePath = rs.getString("DICOM_FILE_URI");
				String sop = rs.getString("SOP_INSTANCE_UID");
				retrievedFileNames.put(sop + ".dcm", imagePath);
			}
		} catch (SQLException e) {
			logger.error("Error getting image paths: " + e);
			throw new Exception("Error getting image filepath", e);
		}
		return retrievedFileNames;
	}

	private static List<ZippingDTO> processDTO(ResultSet rs) throws Exception {
		List<ZippingDTO> dtoList = new ArrayList<ZippingDTO>();
		int count = 0;
		try {
			while (rs.next()) {
				String project = rs.getString("PROJECT");
				String patientId = rs.getString("PATIENT_ID");
				String studyInstanceUid = rs.getString("STUDY_INSTANCE_UID");
				String seriesInstanceUid = rs.getString("SERIES_INSTANCE_UID");
				String imagePath = rs.getString("DICOM_FILE_URI");
				String sop = rs.getString("SOP_INSTANCE_UID");
				logger.info("project: " + project + " PATIENT: " + patientId);
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
		} catch (SQLException e) {
			logger.error("Error processing resultset", e);
			throw new Exception("Error processing resultSet", e);
		}
		logger.debug("result count: " + count);
		return dtoList;
	}

    private static boolean isOracle(Connection conn) {
		try {
			return conn.getMetaData().getDriverName().toLowerCase().indexOf("oracle") != -1;
		}
		catch(Exception e) {
			return false;
		}
	}

    private static String toDateString(Date dateForTimepoint, boolean oracle) {
        if(oracle) {
			return "to_date('"+dateForTimepoint.toString()+"','YYYY-MM-DD')";
		}
		else {
			return "'"+dateForTimepoint.toString()+"'";
		}
	}
}
