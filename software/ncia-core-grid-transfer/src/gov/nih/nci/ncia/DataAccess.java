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
package gov.nih.nci.ncia;

import gov.nih.nci.cagrid.ncia.service.NCIACoreServiceConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 * @author lethai
 *
 */
public class DataAccess {
	private static Logger logger = Logger.getLogger(DataAccess.class);

	private Connection conn=null;

//	private String driver;
//	private String connectionUrl;
//	private String username;
//	private String password;

//	public DataAccess(String theDriver, String theConnectionUrl, String theUsername, String thePassword) {
//		this.driver = theDriver;
//		this.connectionUrl = theConnectionUrl;
//		this.username = theUsername;
//		this.password = thePassword;
//	}

	/**
	 * Rely upon grid configuration for initialization info
	 */
	public DataAccess() {
	}

	public Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup(NCIACoreServiceConfiguration.getConfiguration().getDatabaseDataSource());
		conn = ds.getConnection();

		return conn;

	}

	/**
	 * close database connection if it's not null
	 */
	public void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				logger.error("Error closing database connection: "
						+ e.getMessage());
				e.printStackTrace();
				conn = null;
			}
		}
	}

}
