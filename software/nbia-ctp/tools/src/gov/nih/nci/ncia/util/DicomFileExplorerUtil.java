/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.FileFormat;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParserFactory;

public class DicomFileExplorerUtil {

	static final DcmParserFactory pFact = DcmParserFactory.getInstance();

	static final DcmObjectFactory oFact = DcmObjectFactory.getInstance();

	public void runTest() {
		try {

			// Load the JDBC-ODBC bridge
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// specify the ODBC data source's URL
			String url = "jdbc:oracle:thin:@cbiodb10.nci.nih.gov:1521:caprod";

			// connect
			Connection con = DriverManager.getConnection(url, "nciamaint", "dev1234");

			// create and execute a SELECT
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select distinct study_instance_uid from general_image where trial_dp_pk_id in (6, 84377600, 8, 69763072, 297402368, 323649536)");

			System.out.println("SeriesID				|  	0018,1164  |  		0028,0030");
			while(rs.next()){				
				String currentUID = rs.getString(1);
				File f = new File("Y:\\" + currentUID);

				if (!f.exists()) {
					System.out.println("Current file : " + currentUID
							+ " was not found...");
				} else {
					processStudy(f);
				}
			}
			// close statement and connection
			stmt.close();
			con.close();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}		
	}

	private void processStudy(File studyFile) {
		String seriesId = null;
		File[] imageFiles = studyFile.listFiles(new DcmFileFilter());
		for (int i = 0; i < imageFiles.length; i++) {
			try {
				PixDataDTO pixData = parse(imageFiles[i]);
				// just do this once per series...
				if (seriesId == null
						|| !seriesId.equals(pixData.getSeriesUID())) {
					seriesId = pixData.getSeriesUID();
					if (!pixData.isEmpty()) {
						System.out.println(pixData.getSeriesUID() + " "
								+ pixData.getPixTagOne() + " "
								+ pixData.getPixTagTwo());
					}
				}
			} catch (Exception e) {
				System.out.println("failed to process file : " + imageFiles[i]
						+ " error was " + e.getMessage());
			}
		}
	}

	private PixDataDTO parse(File f) throws IOException {
		FileFormat fileFormat = getFileFormat(f);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		DcmParser parser = pFact.newDcmParser(in);
		Dataset dataset = oFact.newDataset();
		parser.setDcmHandler(dataset.getDcmHandler());
		parser.parseDcmFile(fileFormat, 0x5FFFffff);
		in.close();
		int seriesIdNumber = Integer.parseInt("0020000E", 16);
		int pixOneTagNumber = Integer.parseInt("00181164", 16);
		int pixTwoTagNumber = Integer.parseInt("00280030", 16);

		PixDataDTO pixData = new PixDataDTO();
		pixData.setPixTagOne(dataset.getString(pixOneTagNumber));
		pixData.setPixTagTwo(dataset.getString(pixTwoTagNumber));
		pixData.setSeriesUID(dataset.getString(seriesIdNumber));

		return pixData;
	}

	private FileFormat getFileFormat(File f) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));

		// //System.out.println("input stream"+in);
		DcmParser parser = pFact.newDcmParser(in);

		// //System.out.println("parser"+parser);
		FileFormat fileFormat = parser.detectFileFormat();

		if (fileFormat == null) {
			throw new IOException("Unrecognized file format: " + f);
		}

		in.close();

		return fileFormat;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("beginning test...");
		DicomFileExplorerUtil dfeu = new DicomFileExplorerUtil();
		dfeu.runTest();
		System.out.println("ending test...");
	}
}

class PixDataDTO {

	private String seriesUID;

	private String pixTagOne;

	private String pixTagTwo;

	public String getPixTagOne() {
		return pixTagOne;
	}

	public void setPixTagOne(String pixTagOne) {
		this.pixTagOne = pixTagOne;
	}

	public String getPixTagTwo() {
		return pixTagTwo;
	}

	public void setPixTagTwo(String pixTagTwo) {
		this.pixTagTwo = pixTagTwo;
	}

	public String getSeriesUID() {
		return seriesUID;
	}

	public void setSeriesUID(String seriesUID) {
		this.seriesUID = seriesUID;
	}

	public boolean isEmpty() {
		if (pixTagOne == null && pixTagTwo == null) {
			return true;
		} else {
			return false;
		}
	}
}

class DcmFileFilter implements FilenameFilter {

	public static void main(String[] av) {
		DcmFileFilter ff = new DcmFileFilter();
		ff.process(".");
	}

	public void process(String dir) {
		String objects[] = (new File(dir)).list(this);

		for (int i = 0; i < objects.length; i++) {
			System.out.println(objects[i]);
		}
	}

	public boolean accept(File dir, String s) {
		if (s.endsWith(".dcm")) {
			return true;
		}
		// others?
		return false;
	}
}
