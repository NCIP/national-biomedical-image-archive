import org.dcm4che.data.Dataset;
import org.dcm4che.data.DcmObjectFactory;
import org.dcm4che.data.DcmParser;
import org.dcm4che.data.DcmParserFactory;
import org.dcm4che.data.FileFormat;
import org.dcm4che.dict.Tags;

def rootDir = new File(args[0]);

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

def iteratePerFile(file, patientIds) {
  
    def dataset = loadDicomFile(file.getAbsolutePath());
    if(dataset!=null) {
        def patientId = dataset.get(Tags.PatientID);       
        if(!patientIds.contains(patientId)) {
            patientIds.add(patientId);
        }
    }
    //else not a dicom file, at least not one we recognize
}

def printResult(caseDir, patientIds) {
    if(patientIds.size() > 1) {
        println "-------------------------"
        println caseDir.toString()+":"+patientIds.size()+" patient IDs"
        println "------------------------------"
        patientIds.each {
            println it
        }
    }
}

def reportOnMoreThanOnePatientId(caseDir) {
    def patientIds = []
    caseDir.eachFileRecurse() {
        file -> iteratePerFile(file, patientIds);
    };
    
    printResult(caseDir, patientIds);
}

def parseDirName(dir) {
    if(!dir.getName().endsWith("_CT") && !dir.getName().endsWith("_CXR")) {
        return null;
    }
    else {
        def dex = dir.getName().lastIndexOf('_');
        return dir.getName().substring(0, dex);
    }
}

def comparePatientId(dir1, dir2) {
   reportOnMoreThanOnePatientId(dir1)
   reportOnMoreThanOnePatientId(dir2)
}

def iterateThroughRootDirectory(rootDir) {
    def pairs = [:]
    rootDir.eachDir() {
       def patientName = parseDirName(it)
       if(patientName==null) {
           println "Skipping "+it.getName()       
           return;
       }
       
       def twoDirs = pairs.get(patientName)
       if(twoDirs==null) {
           pairs.put(patient, [it]);
       }
       else {
           twoDirs.add(it);
       }       
    };
    
    /////////////////////
    for(def pair in pairs) {
        if(pair.value.size()!=2) {
            println "Patient doesnt have two dirs:"+pair.key
            continue;
        }
        
        comparePatientId(pair.value[0], pair.value[1]);
    }
    
}


////////////////////////////////////////////////////////////////////

iterateThroughRootDirectory(rootDir);
