import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.dict.Tags;

def targetDir = new File(args[0]);

sopMap = [:]

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

def incrementCount(sopMap, sopInstanceUid, file) {
    def fileArr = sopMap.get[sopInstanceUid]
    if(fileArr==null) {
        sopMap.put(sopInstanceUid, [file])
    }
    else {
        fileArr.add(file)
        sopMap.put(sopInstanceUid, fileArr)
    }
}

def iteratePerFile(file) {
  
    def dataset = loadDicomFile(file.getAbsolutePath());
    if(dataset!=null) {
        def sopInstanceUid = dataset.get(Tags.SopInstanceUID);
        incrementCount(sopMap, sopInstanceUid, file)      
    }
}

////////////////////////////////////////////////////////////////////

targetDir.eachFileRecurse {
   if(it.isFile()) {
       iteratePerFile(it)
   }
}

sopMap.each {
    key, value -> {
        if(value.size > 1) {
            println "SOP ${key} not unique: ${value}"
        }
    }
}