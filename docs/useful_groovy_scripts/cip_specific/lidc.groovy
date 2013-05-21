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
			  	  fileFormat = null;
			  }
	      
	      return fileFormat;
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

/**
 * A directory must contain:
 *    exactly one XML file
 *    exactly one zip file OR one tgz file OR one directory
 *
 * and a directory CANNOT contain an XML file whose name contains "_processed"
 *
 * otherwise, the directory is ignored (false returned)
 **/
def validateSeriesDir(dir) {
    def numZipFilesCnt = 0;
    dir.eachFileMatch(~/.*\.zip/) { 
        numZipFilesCnt += 1;
    }
    
    def numTgzFilesCnt = 0;
    dir.eachFileMatch(~/.*\.tgz/) { 
        numTgzFilesCnt += 1;
    }
    
    def numXmlFilesCnt = 0    
    dir.eachFileMatch(~/.*\.xml/) { 
        numXmlFilesCnt += 1;
    }

    def numDirCnt = 0;
    dir.eachDir() {
        numDirCnt += 1;
    }
    
    def numProcessedXmlFilesCnt = 0    
    dir.eachFileMatch(~/.*processed\.xml/) { 
        numProcessedXmlFilesCnt += 1;
    }
    
    if(numProcessedXmlFilesCnt>0) {
        println "Skipping directory because already has _processed.xml:"+dir.getAbsolutePath();
        return false;    
    }
    
    //this logic isnt bullet proof but should be close enough
    if(numZipFilesCnt!=1 && numDirCnt!=1 && numTgzFilesCnt!=1) {
        println "Skipping directory because not exactly one zip, tgz or not exactly one dir:"+dir.getAbsolutePath();
        return false;
    }
    
    if(numXmlFilesCnt!=1) {
        println "Skipping directory because doesnt have exactly one xml file:"+dir.getAbsolutePath();
        return false;
    }
    
    return true;
}


/**
 * Create a temporary directory under the same directory as the specified TGZ file,
 * unzip the TGZ there, 
 * untar the TAR in the temp directory - flattening any directory strcucture,
 * find a DICOM file in that directory.  
 * 
 * If no DICOM file foudn, return null, else return the found dicom file
 **/
def uncompressTgzFile(tgzFile) {
    def ant = new AntBuilder()   // create an antbuilder 
 
    def tempDirFile = new File(tgzFile.getParent(), "tempDir");
    tempDirFile.mkdir();

    ant.gunzip( src: tgzFile.getAbsolutePath(), 
                dest: tempDirFile.getAbsolutePath()+"/internal.tar") ;

    def tarFile = tempDirFile.listFiles()[0];
    
    //some of these zips just contain a tar.  this logic will break pretty easily....
    if(tarFile ==~ /.*\.tar/) {
        ant.untar(src: tarFile.getAbsolutePath(), 
                  dest: tempDirFile.getAbsolutePath(),
                  overwrite: "false") {
            mapper(type:"flatten")                  
        }

                  
        tarFile.delete();  
        
        return confirmThereIsADicomFileInDirectory(tempDirFile)
    }
    else {
        println "Something is fishy.  gunzip didnt yield a tar:"+tgzFile.getAbsolutePath();
    }
    return null;
}

/**
 * Iterate over all files in a directory.... once the first DICOM file is found
 * return it.
 *
 * DICOM files may not have *.dcm name format...so have to parse them
 **/
def confirmThereIsADicomFileInDirectory(dir) {
    def filesArr = dir.listFiles();
    for(int i=0;i<filesArr.length;i++) {
        if(filesArr[i].isDirectory()) {
            continue;
        }
        def dataset = testDicomFile(filesArr[i]);
        if(dataset != null) {
            return filesArr[i];
        }
    }
    return null;
}

def unzipZipFile(zipFile) {
    def ant = new AntBuilder();   // create an antbuilder 
 
    def tempDirFile = new File(zipFile.getParent(), "tempDir");
    tempDirFile.mkdir();
    tempDirFile.deleteOnExit();
    
    ant.unzip( src: zipFile.getAbsolutePath(), 
               dest: tempDirFile.getAbsolutePath(), 
               overwrite: "false") {
        mapper(type:"flatten");
    }
    
    //the files aren't necessarily named .dcm
    def dcmFile = confirmThereIsADicomFileInDirectory(tempDirFile);
    


    return dcmFile;
}


def visitSeriesDir(dir) {
    
    if(!validateSeriesDir(dir)) {
        return;
    }
    
    //preconditions will make this one and only one
    def zipFile = null;
    dir.eachFileMatch(~/.*\.zip/) { 
        zipFile = it;
    }
    
    def tgzFile = null;
    dir.eachFileMatch(~/.*\.tgz/) { 
        tgzFile = it;
    }
    
    
    def xmlFile = null;    
    dir.eachFileMatch(~/.*\.xml/) { 
        xmlFile = it;
    }
    
    def subdir = null;
    dir.eachDir() {
		    subdir = it;
    }
    
    def dcmFile = null;
    if(zipFile!=null) {
        dcmFile = unzipZipFile(zipFile);
    }
    else
    if(tgzFile!=null) {
        dcmFile = uncompressTgzFile(tgzFile);
    }
    else {
        dcmFile = confirmThereIsADicomFileInDirectory(subdir);
    }
    
    if(dcmFile == null) {
        println "No dcm file for one reason or another in: "+dir +" so skippping";
        return;
    }
    
    def preprocessor = new LIDCPreProcessUtil();
    preprocessor.preprocess(dcmFile, xmlFile);


    def ant = new AntBuilder();
    ant.delete(dir: new File(dir, "tempDir").getAbsolutePath());   
}

///////////////////////////////////////BEGIN PROGRAM///////////////////////////////////

def targetDir = args[0];

new File(targetDir).eachDir() {
    subdirectory -> visitSeriesDir(subdirectory)
};
