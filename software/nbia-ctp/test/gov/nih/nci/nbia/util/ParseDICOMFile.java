/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.dbadapter.NCIADatabase;

import java.io.File;
import java.util.Calendar;

import org.rsna.ctp.objects.DicomObject;

public class ParseDICOMFile {
	
	public ParseDICOMFile() 
	{
		String filePath = "C:\\CTP_data\\GroovyToCreateImage\\ImageResults\\gen";
		NCIADatabase nd = new NCIADatabase();
	
		try
		{
			long time0 = Calendar.getInstance().getTimeInMillis();
			//Thread.sleep(2*60*1000);
			nd.connect();
			//
			for (int i = 43402; i < 63401; i = i+2)
			{
			
				long time1 = Calendar.getInstance().getTimeInMillis();
				File file = new File(filePath + i + ".dcm");
				DicomObject dicomObject = new DicomObject(file, false);
				nd.process(dicomObject, file, null);

				long time2 = Calendar.getInstance().getTimeInMillis();
				System.out.println(time2 - time1);
			
			}
			
			long completeTime = Calendar.getInstance().getTimeInMillis();
			
			System.out.println("Total Time: " + (completeTime - time0));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Could not parse Dicom File!!");
		}finally{
			nd.disconnect();
		}
	}
	
	public static void main(String[] args) {
		
			ParseDICOMFile pd = new ParseDICOMFile();
	}

}
