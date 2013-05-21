import org.dcm4che2.io.*;
import org.dcm4che2.data.*;
import org.dcm4che2.tool.dcm2xml.Dcm2Xml;

dicomFile = new File("3EAF6780")

def computeFileMetaInformationGroupLength(dicomFile) {
    def dicomInputStream = null
    try {
        dicomInputStream = new DicomInputStream(dicomFile)

        def dicomObject = dicomInputStream.readDicomObject();

        def endOfFileMetaInfoPosition =  dicomInputStream.getEndOfFileMetaInfoPosition();

        def START_OF_FILE_META_INFO_POSITION = 0x90

        def len = endOfFileMetaInfoPosition - START_OF_FILE_META_INFO_POSITION
        
        return len;
    }
    finally {
        dicomInputStream?.close()
    }    
}

def findGroupLength(dicomFile) {
    def randomAccessFile = new RandomAccessFile(dicomFile,"r")

    randomAccessFile.seek(0x8c)
    
    def len = randomAccessFile.read();
    
    return len;
}

def writeGroupLength(dicomFile, len) {

    def randomAccessFile = new RandomAccessFile(dicomFile,"rw")
    try {
        randomAccessFile.seek(0x8c)

        //the endian ness/transfer syntax would affect this
        randomAccessFile.write(len)

    }
    finally {
        randomAccessFile?.close()
    }    
}



new File('H:/CTP_working_area/WG12').eachFileRecurse() {
    file -> 
    
    if(file.isFile() && !file.getName().endsWith(".groovy") && !file.getName().equals("DICOMDIR") && !file.getName().endsWith(".xml")) {
        
        def xmlOutputFile = new File(file.getParentFile(), file.getName()+".xml")
        String[] args = ["-o", xmlOutputFile.getAbsolutePath(), file.getAbsolutePath() ]		
        println "**** "+file
        Dcm2Xml.main(args);        
    }        
};

/*
new File('H:/CTP_working_area/WG12').eachFileRecurse() {
    file -> 
    
    if(file.isFile() && !file.getName().endsWith(".groovy") && !file.getName().equals("DICOMDIR")) {
        try {
            len = computeFileMetaInformationGroupLength(file)
		        //println Long.toString(len, 16)
		 
		        set_len = findGroupLength(file)		  
            //println Integer.toString(findGroupLength(file), 16)
        
            if(len!=set_len) {
                println file
            }
        }
        catch(e) {
            //println "trouble with:"+file
        }
    }
};
*/


