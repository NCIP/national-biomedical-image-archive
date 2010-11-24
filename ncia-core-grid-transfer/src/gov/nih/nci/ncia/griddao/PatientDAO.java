/**
 *
 */
package gov.nih.nci.ncia.griddao;

import gov.nih.nci.ncia.DataAccess;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This object does hand-rolled data access for anything patient related in the
 * grid service.  
 * 
 * <p>This came from a massive object called ImageFileProcessor which was split
 * into this object and several other "DAO" objects to isolate data access according
 * to similar pattern as rest of system.
 * 
 * @author lethai
 */
public class PatientDAO {

	/**
	 * This method return list of study instance uid for given patientId and studyNumber
	 * @param patientId
	 */
	public List<Date> getTimepointStudyForPatient(String patientId) throws Exception{

		String sql = "SELECT DISTINCT S.STUDY_DATE "+
		            " FROM STUDY S , PATIENT P, GENERAL_SERIES GS " +
					" WHERE P.PATIENT_PK_ID=S.PATIENT_PK_ID AND P.PATIENT_PK_ID=GS.PATIENT_PK_ID AND P.PATIENT_ID='" +
					patientId + "' AND S.STUDY_DATE IS NOT NULL AND GS.VISIBILITY='1' " +
					" ORDER BY STUDY_DATE";

		List<Date> studyDates = new ArrayList<Date>();
		Statement stmt = null;
		ResultSet rs = null;

		DataAccess da = new DataAccess();
		Connection conn = null;
		try {
			if (patientId != null ) {
				long start = System.currentTimeMillis();
				conn = da.getConnection();

				stmt = conn.createStatement();

				logger.info("sql: " + sql);
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					Date sd = rs.getDate("study_date");
					logger.info("study_date: " + sd);
					if(sd != null && !studyDates.contains(sd)){						
						studyDates.add(sd);						
					}
				}
				if(studyDates.isEmpty()){
					return null;
				}

				long end = System.currentTimeMillis();
				logger.info("Total time to get number of timepoint for patientId:  " + patientId +" : "
						+ (end - start) + " milli seconds.");
			} else {
				logger.info("patientId is empty");
			}
		} catch (Exception e) {
			logger.error("Could not get number of timepoint for patient: " + patientId
					+ " SQL statement: " + sql, e);
			e.printStackTrace();
			throw new Exception("Error getting number of timepoint for patient: " + patientId);
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
		return studyDates;
	}
	
	private static Logger logger = Logger.getLogger(PatientDAO.class);
	
}
