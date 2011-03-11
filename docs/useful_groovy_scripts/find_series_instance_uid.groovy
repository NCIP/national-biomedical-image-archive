import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.dict.Tags;

targetDir = new File(args[0]);
seriesInstanceUidToFind = args[1];

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
	      return null;
	  }
	  finally {
	      if(inputStream!=null) {
	      	  inputStream.close();
	      }
	  }
}


def iteratePerFile(file) {
  
    def dataset = loadDicomFile(file.getAbsolutePath());
    if(dataset!=null) {
        def seriesInstanceUid = dataset.get(Tags.SeriesInstanceUID);

        set.add(seriesInstanceUid.getString(0, null))
        if(seriesInstanceUid.getString(0, null) == seriesInstanceUidToFind) {
            println file
        }
    }
}

////////////////////////////////////////////////////////////////////

targetDir.eachFileRecurse {
   if(it.isFile()) {
       iteratePerFile(it)
   }
}

