import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;

/**
 * Parse a DICOM file and return the file format object
 * If null, this file isn't a DICOM file
 **/
def testDicomFile(path) {
    def oFact = DcmObjectFactory.getInstance();
		def pFact = DcmParserFactory.getInstance();
		
		def inputStream = null;
		try {
			  inputStream = new BufferedInputStream(new FileInputStream(path));
			
			  DcmParser parser = pFact.newDcmParser(inputStream);
			 
			  FileFormat fileFormat = null;
			  try {
			  	  fileFormat = parser.detectFileFormat();
			  }
			  catch(Throwable t) { 
			      t.printStackTrace();
			  	  fileFormat = null;
			  }
	      
	      return fileFormat;
	  }
	  catch(e) {
	      e.printStackTrace();
	      return null;
	  }
	  finally {
	      if(inputStream!=null) {
	      	  inputStream.close();
	      }
	  }
}


///////////////////////////////////////BEGIN PROGRAM///////////////////////////////////

def targetDicom = args[0];

def result = testDicomFile(new File(targetDicom));

println result;
