package gov.nih.nci.nbia.restAPI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.sql.*;

//import gov.nih.nci.nbia.restAPI.dao.*;

// http://localhost:8080/nbia-service/api/v1/status

@Path("/v1/status")
public class V1_status {

	private static final String api_version = "00.01.00"; // version of the api

	/**
	 * This method sits at the root of the api. It will return the name of this
	 * api.
	 * 
	 * @return String - Title of the api
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle() {
		return "<p>NBIA Restful Web Service version " + api_version + " is running</p>";
	}

	/**
	 * This method will return the version number of the api Note: this is
	 * nested one down from the root. You will need to add version into the URL
	 * path.
	 * 
	 * Example: http://localhost:8080/rest.test/api/v1/status/version
	 * 
	 * @return String - version number of the api
	 */
	@Path("/version")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion() {
		return "<p>Version:" + api_version + "</p>";
	}

	/**
	 * This method will connect to the oracle database and return the date/time
	 * stamp. It will then return the date/time to the user in String format
	 * 
	 * This was explained in Part 3 of the Java Rest Tutorial Series on YouTube
	 * 
	 * @return String - returns the database date/time stamp
	 * @throws Exception
	 */
	@Path("/database")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnDatabaseStatus() throws Exception {

		//PreparedStatement query = null;
		String myString = null;
		String returnString = null;
		Connection conn = null;

		try {

//			conn = DbConnection.nbiaDSConn().getConnection(); 
//			// simple sql query to return the date/time
//			query = conn
//					.prepareStatement("select to_char(sysdate,'YYYY-MM-DD HH24:MI:SS') DATETIME "
//							+ "from sys.dual");
//			ResultSet rs = query.executeQuery();
//
//			// loops through the results and save it into myString
//			while (rs.next()) {
//				// /*Debug*/ System.out.println(rs.getString("DATETIME"));
//				myString = rs.getString("DATETIME");
//			}
//
//			query.close(); // close connection

			returnString = "<p>Database Status</p> "
					+ "<p>Database Date/Time return: " + myString + "</p>";

		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * The finally cause will always run. Even if the the method get a
		 * error. You want to make sure the connection to the database is
		 * closed.
		 */
		finally {
			if (conn != null)
				conn.close();
		}

		return returnString;
	}

}
