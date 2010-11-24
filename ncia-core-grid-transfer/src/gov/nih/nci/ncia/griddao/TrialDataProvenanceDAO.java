/**
 *
 */
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.DataAccess;
import gov.nih.nci.ncia.domain.TrialDataProvenance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * This object does hand-rolled data access for anything data provenance related in the
 * grid service.  It violates patterns a bit by potentially returning Hibernate
 * objects.... need to think about that a bit but this is a first cut...
 * 
 * <p>This came from a massive object called ImageFileProcessor which was split
 * into this object and several other "DAO" objects to isolate data access according
 * to similar pattern as rest of system.
 * 
 * @author lethai
 */
public class TrialDataProvenanceDAO {
	
	public TrialDataProvenance getTDPByPatientId(String patientId) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		TrialDataProvenance tdp = null;
		String sql = "";

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (patientId != null) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				sql = SELECT_TDP
						+ ", PATIENT P,GENERAL_SERIES GS WHERE P.TRIAL_DP_PK_ID = TDP.TRIAL_DP_PK_ID AND P.PATIENT_PK_ID=GS.PATIENT_PK_ID AND P.PATIENT_ID = '"
						+ patientId + AND_GS_VISIBILITY_EQ_1;

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				tdp = processTDP(rs);
				long end = System.currentTimeMillis();
				logger.info("Total time to get TDP for patientId "
						+ (end - start) + MS);
			} else {
				logger.info("patientId is empty");
			}
		} catch (Exception e) {
			logger.error("Could not get TDP for patientId: " + patientId
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting TrialDataProvenance info for patient: " + patientId);
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

		return tdp;
	}

	public TrialDataProvenance getTDPByStudyInstanceUID(String studyInstanceUID) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		TrialDataProvenance tdp = null;
		String sql = "";

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (studyInstanceUID != null) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				sql = SELECT_TDP
						+ PATIENT_P_STUDY_S_SERIES_GS_WHERE_TDP_MATCHES_AND
						+ "P.PATIENT_PK_ID = S.PATIENT_PK_ID AND P.PATIENT_PK_ID=GS.PATIENT_PK_ID AND S.STUDY_PK_ID=GS.STUDY_PK_ID " +
						"AND S.STUDY_INSTANCE_UID = '"
						+ studyInstanceUID + AND_GS_VISIBILITY_EQ_1;

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				tdp = processTDP(rs);
				long end = System.currentTimeMillis();
				logger
						.info("Total time to get TrialDataProvenance given studyInstanceUID: "
								+ (end - start) + MS);
			} else {
				logger.info("StudyInstanceUID is empty");
			}
		} catch (Exception e) {
			logger.error(
					"Could not get TrialDataProvenance for studyInstanceUID: "
							+ studyInstanceUID + SQL_STMT
							+ sql, e);
			e.printStackTrace();
			throw new Exception("Error getting TrialDataProvenance for studyInstanceUID: " + studyInstanceUID);
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

		return tdp;
	}

	public TrialDataProvenance getTDPBySeriesInstanceUID(
			String seriesInstanceUID) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		TrialDataProvenance tdp = null;
		String sql = "";

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (seriesInstanceUID != null) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				sql = SELECT_TDP
						+ PATIENT_P_STUDY_S_SERIES_GS_WHERE_TDP_MATCHES_AND
						+ "P.PATIENT_PK_ID = S.PATIENT_PK_ID AND S.STUDY_PK_ID = GS.STUDY_PK_ID AND GS.SERIES_INSTANCE_UID = '"
						+ seriesInstanceUID + AND_GS_VISIBILITY_EQ_1;


				logger.debug(sql);
				rs = stmt.executeQuery(sql);
				tdp = processTDP(rs);
				long end = System.currentTimeMillis();
				logger
						.info("Total time to get TrialDataProvenance given studyInstanceUID: "
								+ (end - start) + MS);
			} else {
				logger.info("StudyInstanceUID is empty");
			}
		} catch (Exception e) {
			logger.error(
					"Could not get TrialDataProvenance for seriesInstanceUID: "
							+ seriesInstanceUID + SQL_STMT
							+ sql, e);
			e.printStackTrace();
			throw new Exception("Error getting TrialDataProvenance for seriesInstanceUID: " + seriesInstanceUID);
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

		return tdp;
	}

	public TrialDataProvenance getTDPBySopInstanceUID(String sopInstanceUID) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		TrialDataProvenance tdp = null;
		String sql = "";

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (sopInstanceUID != null) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				sql = SELECT_TDP
						+ ", GENERAL_IMAGE GI, GENERAL_SERIES GS WHERE GI.TRIAL_DP_PK_ID = TDP.TRIAL_DP_PK_ID AND "
						+ "GS.GENERAL_SERIES_PK_ID = GI.GENERAL_SERIES_PK_ID AND "
						+ "GI.SOP_INSTANCE_UID = '" + sopInstanceUID + AND_GS_VISIBILITY_EQ_1;

				logger.info(sql);
				rs = stmt.executeQuery(sql);
				tdp = processTDP(rs);
				long end = System.currentTimeMillis();
				logger
						.info("Total time to get TrialDataProvenance given sopInstanceUID: "
								+ (end - start) + MS);
			} else {
				logger.info("StudyInstanceUID is empty");
			}
		} catch (Exception e) {
			logger.error(
					"Could not get TrialDataProvenance for sopInstanceUID: "
							+ sopInstanceUID + SQL_STMT
							+ sql, e);
			e.printStackTrace();
			throw new Exception("Error getting TrialDataProvenance for sopInstanceUID: " + sopInstanceUID);
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

		return tdp;
	}



	/**
	 * @param ids
	 * @param columnName
	 */
	public Map<String, TrialDataProvenance> getTDPByListIds(List<String> ids, String columnName) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Map<String,TrialDataProvenance> tdpList = new HashMap<String, TrialDataProvenance>();
		String sql = "";

		if(columnName.equalsIgnoreCase("PATIENT_ID")){
			String sb = buildWhereClause(ids, "P.PATIENT_ID").toString();
			sql= "SELECT DISTINCT TDP.PROJECT, TDP.DP_SITE_NAME, P.PATIENT_ID FROM TRIAL_DATA_PROVENANCE TDP "
			+ ", PATIENT P, GENERAL_SERIES GS WHERE P.TRIAL_DP_PK_ID = TDP.TRIAL_DP_PK_ID AND P.PATIENT_PK_ID=GS.PATIENT_PK_ID "
			+ " AND (P.PATIENT_ID = " + sb + ") AND GS.VISIBILITY = '1'";
		}else if(columnName.equalsIgnoreCase("STUDY_INSTANCE_UID")){
			String sb = buildWhereClause(ids, "S.STUDY_INSTANCE_UID").toString();
			sql = "SELECT DISTINCT TDP.PROJECT, TDP.DP_SITE_NAME, S.STUDY_INSTANCE_UID FROM TRIAL_DATA_PROVENANCE TDP"
			+ PATIENT_P_STUDY_S_SERIES_GS_WHERE_TDP_MATCHES_AND
			+ "P.PATIENT_PK_ID = S.PATIENT_PK_ID AND S.PATIENT_PK_ID=GS.PATIENT_PK_ID AND ( S.STUDY_INSTANCE_UID = "
			+ sb + " ) AND GS.VISIBILITY = '1'";

		}else if(columnName.equalsIgnoreCase("SERIES_INSTANCE_UID")){
			String sb = buildWhereClause(ids, "GS.SERIES_INSTANCE_UID").toString();
			sql ="SELECT TDP.PROJECT, TDP.DP_SITE_NAME, GS.SERIES_INSTANCE_UID FROM TRIAL_DATA_PROVENANCE TDP "
			+ PATIENT_P_STUDY_S_SERIES_GS_WHERE_TDP_MATCHES_AND
			+ " P.PATIENT_PK_ID = S.PATIENT_PK_ID AND S.STUDY_PK_ID = GS.STUDY_PK_ID AND (GS.SERIES_INSTANCE_UID = "
			+ sb + ") AND GS.VISIBILITY = '1'";

		}

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {

				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();


				logger.info(sql);
				rs = stmt.executeQuery(sql);
				tdpList = processTDP(rs, columnName);
				long end = System.currentTimeMillis();
				logger.info("Total time to get TDP for patientId "
						+ (end - start) + MS);

		} catch (Exception e) {
			logger.error("Could not get TDP for : " + sql
					+ SQL_STMT + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting TrialDataProvenance for list of UIDs");
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
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

		return tdpList;
	}

	///////////////////////////////////PRIVATE//////////////////////////////////////////////
	
	private static Logger logger = Logger.getLogger(TrialDataProvenanceDAO.class);

	private final static String SELECT_TDP = "SELECT DISTINCT TDP.PROJECT, TDP.DP_SITE_NAME "+
	                                         "FROM TRIAL_DATA_PROVENANCE TDP ";
	
	/**
	 * Fragment of SQL reused in a few places
	 */
	private final static String PATIENT_P_STUDY_S_SERIES_GS_WHERE_TDP_MATCHES_AND =
	    ", PATIENT P, STUDY S, GENERAL_SERIES GS WHERE P.TRIAL_DP_PK_ID = TDP.TRIAL_DP_PK_ID AND ";
	
	/**
	 * Fragment of SQL reused in a few places
	 */
	private final static String AND_GS_VISIBILITY_EQ_1 = "' AND GS.VISIBILITY = '1'";
	
	//used in logging
	private final static String MS = "ms";
	
	//used in logging
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

	//this method should return only one record
	private static  TrialDataProvenance processTDP(ResultSet rs) throws Exception{
		TrialDataProvenance tdp = null;
		int count = 0;
		try {

			while (rs.next()) {
				String project = rs.getString("PROJECT");
				String siteName = rs.getString("DP_SITE_NAME");
				logger.info("project: " + project + " site: " + siteName);
				tdp = new TrialDataProvenance();
				tdp.setProject(project);
				tdp.setSiteName(siteName);
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Error processing resultset to get TrialDataProvenance data", e);
		}
		if(count > 1){
			logger.error("Multiple TrialDataProvenance returned where one is expected");
			throw new Exception("Error: multiple TrialDataProvenance returned where one is expected");
		}
		return tdp;
	}
	
	private static Map<String,TrialDataProvenance> processTDP(ResultSet rs, String columnName) throws Exception{
		Map<String, TrialDataProvenance> tdpList = new HashMap<String, TrialDataProvenance>();
		TrialDataProvenance tdp=null;
		try {
			while (rs.next()) {
				String project = rs.getString("PROJECT");
				String siteName = rs.getString("DP_SITE_NAME");
				String id = rs.getString(columnName);
				logger.info("project: " + project + " site: " + siteName);
				tdp = new TrialDataProvenance();
				tdp.setProject(project);
				tdp.setSiteName(siteName);
				tdpList.put(id, tdp);
			}
		} catch (SQLException e) {
			logger.error("Error processing resultset", e);
			throw new Exception("Error processing resultSet", e);			
		}
		
		return tdpList;
	}

}
