/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class TempFile {

	String filePath = "";
	
	public TempFile(String filePath)
	{
		this.filePath = filePath;
	}
	
	private void parseResult() throws Exception
	{
		BufferedReader br = null; 
		PrintWriter pw = null; 
		
		try{
			br = new BufferedReader(new FileReader(filePath + "BaselineOutput.txt"));
			String line = br.readLine();
			pw = new PrintWriter(new FileWriter(filePath+"BaselineCleanOutput.txt"));
			
			while(line != null){
				if (checkLine(line))
				{
					pw.println(line);
				}
				line = br.readLine();
			}
			System.out.println("Successful!");
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Failed!");
		}finally{
			br.close();
			pw.close();
		}
	}
	
	private boolean checkLine(String line)
	{
		if (line.indexOf("WARN") >= 0){
			return false;
		}

		return true;
	}
	
	public static void main(String[] args) {
		
		String currentResultFile = "C:\\CTP_data\\GroovyToCreateImage\\";
		
		TempFile tf = new TempFile(currentResultFile);
		try {
			tf.parseResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
