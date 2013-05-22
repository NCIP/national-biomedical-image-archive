/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.customserieslist;

import gov.nih.nci.nbia.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileParser {
	
	/**
	 * parse the file 
	 * @param file
	 * @return list of series instance uid in the file
	 */
	public List<String> parse(File file){
		List<String> seriesUids = new ArrayList<String>();
		BufferedReader bufferedReader = null;
		try{
			bufferedReader  = new BufferedReader(new FileReader(file));
			String line = null;
			//read each line of text file
			while((line = bufferedReader.readLine()) != null){
				//System.out.println("FileParser line: " + line);
				StringTokenizer st = new StringTokenizer(line,",");
				while (st.hasMoreTokens()){
					String seriesId = st.nextToken();
					if(!StringUtil.isEmptyTrim(seriesId)){
						seriesUids.add( seriesId);
					}
				}
			}
		}catch(Exception fnfe){
			fnfe.printStackTrace();
			throw new RuntimeException(fnfe);
		}finally{
		//close the file
			try{
				if(bufferedReader != null){
					bufferedReader.close();
				}
				//remove the file from server after parsing
				file.delete();
			}catch(Exception ioe){
				ioe.printStackTrace();
				throw new RuntimeException(ioe);
			}
		}		
		return seriesUids;
	}

	public boolean isCSVFile(File uploadedFile){
		boolean isCSVFile = false;
		String fileName = uploadedFile.getName();
			
		int dotPos = fileName.lastIndexOf('.');
		String fileExtension = fileName.substring(dotPos);
		System.out.println("file Extension: " +fileExtension);
		
		if(!fileExtension.equalsIgnoreCase(".csv")){
			isCSVFile = true;
		}
		return isCSVFile;
	}
}
