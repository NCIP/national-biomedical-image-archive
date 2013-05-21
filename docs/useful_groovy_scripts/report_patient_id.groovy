import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.dict.Tags;

def loadDicomFile(path) {
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
			  if (fileFormat == null) {
			     return null;
	      }
	 
			  Dataset dataset = oFact.newDataset();
	      parser.setDcmHandler(dataset.getDcmHandler());
	      parser.parseDcmFile(fileFormat, Tags.PixelData);
	      
	      return dataset;
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

def reportOnDicomFile(file) {
  
    def dataset = loadDicomFile(file.getAbsolutePath());
    if(dataset!=null) {
        def patientId = dataset.get(Tags.PatientID);       
        println "Patient ID:"+patientId
    }
    else println "Not a dicom file"
    //else not a dicom file, at least not one we recognize
}


///////////////////////////////////////BEGIN PROGRAM///////////////////////////////////

def targetDicom = args[0];

def result = reportOnDicomFile(new File(targetDicom));

println result;

