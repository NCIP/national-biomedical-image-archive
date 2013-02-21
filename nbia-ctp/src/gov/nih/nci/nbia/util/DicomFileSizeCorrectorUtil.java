package gov.nih.nci.nbia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.rsna.ctp.objects.DicomObject;

public final class DicomFileSizeCorrectorUtil {
	private static Logger logger = Logger.getLogger(DicomFileSizeCorrectorUtil.class);
	private String connectionUrl;
	private String driverClass;
	private String userName;
	private String password;
	
	public String getConnectionUrl() {
		return connectionUrl;
	}
	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String mysqlDriverClass = "com.mysql.jdbc.Driver";
		String oracleDriverClass = "oracle.jdbc.driver.OracleDriver";
		
		String mysqlConnectionUrl = "jdbc:mysql://";
		String oracleConnectionUrl = "jdbc:oracle:thin:@";

		String uIDatabaseType = getUserInput("Enter Database Type (mysql Or oracle): ");
		String uIDatabaseServer = getUserInput("Enter Database Server Name (localhost): ");
		String uIDatabasePort = getUserInput("Enter Database Port (3306 Or 1521): ");
		String uIDatabaseName = getUserInput("Enter Database Name (nbiadb Or nbiadev): ");

		String databaseConnectionUrl = uIDatabaseServer +":"+uIDatabasePort;
		
		String userInputUserName = getUserInput("Enter Database User: ");
		String userInputPassword = getUserInput("Enter Database Password: ");
		String LOG_FILE = getUserInput("location of log4.properties "); 
	    Properties logProp = new Properties();      
	    try {      
	    	logProp.load(new FileInputStream (LOG_FILE));  
	        PropertyConfigurator.configure(logProp);      
	        logger.info("Logging enabled");    
	    } catch(IOException e) {       
	    	  System.out.println("Logging not enabled"); 
	    }
		DicomFileSizeCorrectorUtil sizeCorrector = new DicomFileSizeCorrectorUtil();
		
		if (uIDatabaseType.equalsIgnoreCase("mysql")) {
			sizeCorrector.setConnectionUrl(mysqlConnectionUrl + databaseConnectionUrl +"/"+ uIDatabaseName);
			sizeCorrector.setDriverClass(mysqlDriverClass);
		} else if(uIDatabaseType.equalsIgnoreCase("oracle")) {
			sizeCorrector.setConnectionUrl(oracleConnectionUrl + databaseConnectionUrl +":"+ uIDatabaseName);
			sizeCorrector.setDriverClass(oracleDriverClass);
		} else {
			System.out.println("Please enter valid database type. ");
			return;
		}
		sizeCorrector.setUserName(userInputUserName);
		sizeCorrector.setPassword(userInputPassword);
		sizeCorrector.runFileSizeCorrector();
	}
	public static String getUserInput(String msg) {
		//  prompt the user to enter 
	    System.out.print(msg);
	    // get their input
	    Scanner scanner = new Scanner(System.in);
	    String userInput = scanner.nextLine();
	    // test the String if empty then prompt again
	    if (userInput.trim().equals("")) {
		   return getUserInput(msg);
		} else {
			return userInput.trim();
		}
	}
	private Connection getJDBCConnection() throws ClassNotFoundException, SQLException {
		// Load the JDBC-ODBC bridge
		Class.forName(driverClass);
		// specify the ODBC data source's URL
		return DriverManager.getConnection(connectionUrl, userName, password);
	}
	public void runFileSizeCorrector() {
		try {
			Connection con = getJDBCConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("Select image_pk_id,dicom_file_uri,dicom_size from general_image order by image_pk_id asc");
			logger.info("looping through......");
			while(rs.next()){				
				 String imageId = rs.getString(1);
				 String storedFileName = rs.getString(2);
				 long storedFileSize = rs.getLong(3);
				 File storedFile = new File(storedFileName);
				 if (!storedFile.exists()) {
					 logger.info("Current file Name: " + storedFileName
							+ " was not found...");
				 } else if(storedFileSize != storedFile.length()) {
					 logger.info("File size mismatch####### "+imageId+ "|| "+ storedFileName + " || " + storedFileSize);
					setCorrectFileSize(con, storedFile,imageId );
				 } else {
					 //Do nothing
					 //System.out.println("file size is same.....");
				 }
			}
			// close statement and connection
			stmt.close();
			con.close();
		} catch (java.lang.Exception ex) {
			logger.info("Error occured" + ex.getMessage());
		}
		logger.info("DICOM File size correction has been completed....");
	}
	 private void setCorrectFileSize(Connection con, File file, String imageId) throws SQLException {
    	 // Temporary fix until new CTP release provides a better solution
    	long actualFileSize = file.length();
    	
    	try {
    		DicomObject tempFile = new DicomObject(file);
    		String md5 = tempFile.getDigest()== null? " " : tempFile.getDigest();
    		logger.info("updated filesize " + actualFileSize + "updated md5 " + md5);

    		String sql ="UPDATE general_image SET dicom_size =? , md5_digest=? where image_pk_id=?";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setBigDecimal(1, BigDecimal.valueOf(actualFileSize));
			stmt.setString(2, md5);
			stmt.setString(3, imageId);
    		stmt.execute() ;
			// close statement 
    		con.commit();
			stmt.close();
			
		} catch (Exception ex) {
			logger.info("update failed for imageId "+ imageId +" exception is:- " + ex.getMessage());
    		con.rollback();
    	}
    	
    }
}
