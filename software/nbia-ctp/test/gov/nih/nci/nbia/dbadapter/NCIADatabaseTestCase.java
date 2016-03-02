/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dbadapter;

import java.io.InputStream;

import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.dict.Tags;

import junit.framework.TestCase;

public class NCIADatabaseTestCase extends TestCase {
	public static final String dcmFile = "dicom_with_anatomicRegionSeq.dcm";
	
	public void testhandleSQField(){
		Dataset dataset = getDataset();
		if (dataset == null){
			System.out.println("dataset is null");
			fail("dataset is null");
		}
		assertTrue(checkNCIADatabase(dataset));
	}
	
	private boolean checkNCIADatabase(Dataset dataset){
		boolean isOk = false;
		try{
			String elementHeader = NCIADatabaseDelegator.handleSQField(dataset, 533016);
		    boolean test = elementHeader.trim().equals(
					"(0008,0100)=T-D3000/(0008,0102)=SNM3/(0008,0104)=Chest");
		    if (test){
		    	isOk = true;
		    }
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("cannot read DICOM Tag");
			fail("cannot read DICOM Tag");
		}
		return isOk;
	}
	
	private Dataset getDataset(){
	    DcmObjectFactory dcmObjFact = DcmObjectFactory.getInstance();
	    DcmParserFactory dcmParserFact = DcmParserFactory.getInstance();
	    InputStream s = null;
	    Dataset dataset = null;
	    try
	    {
	    	s = this.getClass().getClassLoader().getResourceAsStream(dcmFile);
	    	
	    	DcmParser parser = dcmParserFact.newDcmParser(s);
	    	FileFormat fileFormat = null;
	        try {
	           fileFormat = parser.detectFileFormat();
	        }
	        catch(Throwable t) {
	        	throw new Exception("cannot detect File Format");
	        }
	        
	        dataset = dcmObjFact.newDataset();
	        parser.setDcmHandler(dataset.getDcmHandler());
	        parser.parseDcmFile(fileFormat, Tags.PixelData);
	    	
	    }catch(Exception e){
	    	System.out.println(e.getMessage());
	    	System.out.println("Cannot parse the dcm file -- " + dcmFile);
	    	fail("Cannot parse the dcm file -- " + dcmFile);
	    }finally{
	    	try{
	    		s.close();
	    	}catch(Exception ee){
	    		System.out.println("Cannot close FileInputStream in NCIADatabaseTestCase");
	    	}
	    }
	    return dataset;
	}

}
