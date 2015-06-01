/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.apache.log4j.Logger;


/*
 * Go to given directory recursively to parse the parameters out of dicom files 
 */
public class FileSpliter {

	final static Logger logger = Logger.getLogger(DicomParameterFinder.class);

	/*final static private String outputFileName = "c:\\result.xls";
	final static private String BASE_DATE = "1999-01-01";
	static private String infileName = "";
	static private String outfileName = "";*/
	
	public FileSpliter() {
	}

	
	/**
	 * Split a text file into number of files
	 * @param infileName
	 * @param outfileName
	 * @param numOutputFiles
	 */
	public void split(String infileName, String outfileName, int numOutputFiles){
		try { 
			BufferedReader in = new BufferedReader(new FileReader(infileName)); 		
			int loopCounter=0;
			//String s = in.readLine();
			String line = null;
			
			PrintWriter outWriters[]=new PrintWriter[numOutputFiles];
			for (int i=0;i<numOutputFiles;i++){
				outWriters[i] = new PrintWriter(new File(outfileName+i+1));
			}
		
			// write lines to each file in cycle
			while ((line = in.readLine()) != null) {
				if (loopCounter == numOutputFiles) {
					loopCounter=0;
				}
				//System.out.println(line);
				outWriters[loopCounter++].println(line);	
		
				
			}
			// close all files
			for (int i=0;i<numOutputFiles;i++){
				outWriters[i].close();
			}
		}
		catch (Exception ex) { ex.printStackTrace();
		}
	}


	/** ****************** test program ************************ */
	public static void main(String[] args) throws Exception {
		String infileName = args[0];
		String outfileName = args[1];
		int numOutputFiles = Integer.parseInt(args[2]);
		FileSpliter finder = new FileSpliter();
		logger.info("In process ...");
		finder.split(infileName, outfileName, numOutputFiles);
		logger.info("=== Process is completed ===");

	}
}

