/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.File;
import java.util.Calendar;

import gov.nih.nci.nbia.dbadapter.NCIADatabase;

import org.rsna.ctp.objects.DicomObject;
import org.rsna.ctp.pipeline.Status;


public class SubmitDICOM2DB {
	private String filePath = "C:\\CTP_data\\GroovyToCreateImage\\ImageResults\\gen";
	
	public SubmitDICOM2DB()
	{
   	    NCIADatabase nd = new NCIADatabase();

		try
		{
			long time0 = Calendar.getInstance().getTimeInMillis();
			for (int i = 43402; i < 43410; i = i+2)
			{
				long time1 = Calendar.getInstance().getTimeInMillis();
				File file = new File(filePath + i + ".dcm");
				DicomObject dicomObject = new DicomObject(file, false);
				Status st = nd.process(dicomObject, file, null);
				
				long time2 = Calendar.getInstance().getTimeInMillis();
				System.out.println(time2 - time1);
			}
			
			long completeTime = Calendar.getInstance().getTimeInMillis();
			
			System.out.println("Total Time: " + (completeTime - time0));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Could not parse Dicom File!!");
		}		
	}
	
	public static void main(String[] args)
	{
	  SubmitDICOM2DB sdicomDB = new SubmitDICOM2DB();
	}

}
