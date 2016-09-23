/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class DicomFileSizeCorrectorUtil {
    private static final int elementsPerExecute = 1000;
    private static Logger logger = Logger
            .getLogger(DicomFileSizeCorrectorUtil.class);
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
        String LOG_FILE = "dbUpdatorLog4j.properties";
        String PROP_FILE = "dbUpdator.properties";
        Properties logProp = new Properties();
        ClassLoader classLoader = DicomFileSizeCorrectorUtil.class
                .getClassLoader();
        try {
            logProp.load(classLoader.getResourceAsStream(LOG_FILE));
            PropertyConfigurator.configure(logProp);
            System.out.println("Logging enabled");
            logger.info("Logging enabled");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Logging not enabled");
        }
        Properties prop = new Properties();
        try {
            // load a properties file from class path, inside static method
            prop.load(classLoader.getResourceAsStream(PROP_FILE));
            // get the property value and print it out
            String mysqlDriverClass = prop
                    .getProperty("connection.driver_class");
            logger.info("mysqlDriverClass:-" + mysqlDriverClass);
            String databaseConnectionUrl = prop.getProperty("connection.url");
            logger.info("databaseConnectionUrl:-" + databaseConnectionUrl);
            String dbUserName = prop.getProperty("connection.username");
            logger.info("dbUserName:-" + dbUserName);
            String dbPassword = prop.getProperty("connection.password");
            DicomFileSizeCorrectorUtil sizeCorrector = new DicomFileSizeCorrectorUtil();
            sizeCorrector.setConnectionUrl(databaseConnectionUrl);
            sizeCorrector.setDriverClass(mysqlDriverClass);
            sizeCorrector.setUserName(dbUserName);
            sizeCorrector.setPassword(dbPassword);
            sizeCorrector.runFileSizeCorrector();
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage());
        }
    }

    private Connection getJDBCConnection() throws ClassNotFoundException,
            SQLException {
        // Load the JDBC-ODBC bridge
        Class.forName(driverClass);
        // specify the ODBC data source's URL
        return DriverManager.getConnection(connectionUrl, userName, password);
    }

    public void runFileSizeCorrector() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            Connection con = getJDBCConnection();
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt
                    .executeQuery("Select image_pk_id,dicom_file_uri,dicom_size from general_image order by image_pk_id asc");
            logger.info("looping through......");
            int batchSize = 0;
            // String sql =
            // "UPDATE general_image SET dicom_size =? , md5_digest=? where image_pk_id=?";
            String sql = "UPDATE general_image SET dicom_size =? where image_pk_id=?";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            while (rs.next()) {
                String imageId = rs.getString(1);
                String storedFileName = rs.getString(2);
                long storedFileSize = rs.getLong(3);
                File storedFile = new File(storedFileName);
                if (!storedFile.exists()) {
                    logger.info("Current file Name: " + storedFileName
                            + " was not found...");
                } else if (storedFileSize != storedFile.length()) {
                    logger.info("File size mismatch####### " + imageId + "|| "
                            + storedFileName + " || " + storedFileSize);
                    setCorrectFileSize(prepStmt, storedFile, imageId, md);
                    batchSize++;
                    if (batchSize == elementsPerExecute) {
                        excuteBatchStmt(prepStmt);
                        batchSize = 0;
                        con.commit();
                    }
                } else {
                    // Do nothing
                    // System.out.println("file size is same.....");
                }
            }
            // final update
            excuteBatchStmt(prepStmt);
            // close statement and connection
            con.commit();
            prepStmt.close();
            stmt.close();
            con.close();
        } catch (java.lang.Exception ex) {
            logger.info("Error occured:-" + ex.getMessage());
            return;
        }
        logger.info("DICOM File size correction has been completed....");
    }

    private void excuteBatchStmt(PreparedStatement prepStmt)
            throws SQLException {
        logger.info("doing batch update...");
        prepStmt.executeBatch();
        prepStmt.clearBatch();
    }

    private void setCorrectFileSize(PreparedStatement stmt, File file,
            String imageId, MessageDigest md) throws Exception {
        long actualFileSize = file.length();
        FileInputStream fIs = new FileInputStream(file);
        try {
            // String md5 = getDigest(fIs, md);
            // logger.info("updated filesize " + actualFileSize + "updated md5 "
            // + md5);
            logger.info("updated filesize " + actualFileSize);
            stmt.setBigDecimal(1, BigDecimal.valueOf(actualFileSize));
            // stmt.setString(2, md5);
            stmt.setString(2, imageId);
            stmt.addBatch();
        } catch (Exception ex) {
            logger.error("update failed for imageId " + imageId
                    + " exception is:- " + ex.getMessage());
        } finally {
            fIs.close();
        }
    }

    // private String getDigest(InputStream is, MessageDigest md)
    // throws NoSuchAlgorithmException, IOException {
    // int byteArraySize = 2048;
    // md.reset();
    // byte[] bytes = new byte[byteArraySize];
    // int numBytes;
    // while ((numBytes = is.read(bytes)) != -1) {
    // md.update(bytes, 0, numBytes);
    // }
    // byte[] digest = md.digest();
    // String result = new String(Hex.encodeHex(digest));
    // return result == null ? " " : result;
    // }
}
