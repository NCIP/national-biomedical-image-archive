import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.*;
import org.dcm4che2.data.*;

def loadDicomFile2(dicomImageFile) {
    DicomObject dcmObj;
    DicomInputStream din = null;
    try {
        din = new DicomInputStream(dicomImageFile);
        dcmObj = din.readDicomObject();
    }
    catch (IOException e) {
        //e.printStackTrace();
        return null;
    }
    finally {
       din?.close();
    }
}    



////////////////////////////////////////////////////////////////////

totalCnt = 0
nullCnt = 0
zeroCnt = 0
new File("T:/NCIA/CTP related/WG12_test_data_modified").eachFileRecurse {
    try {
        if(it.isFile()) {
           def dataset = loadDicomFile2(it);
           if(dataset==null) return;
           def numFrames = dataset.get(Tag.NumberOfFrames)
           if(numFrames==0) {
               zeroCnt += 1
               //println "Zero num frames:"+it.getAbsolutePath()
           }
           if(numFrames==null) {
               nullCnt += 1
               //println "Null num frames:"+it.getAbsolutePath()
           }   
           totalCnt += 1
        }
    }
    catch(e) {
        //println "Couldn't load:"+it.getName()
        //e.printStackTrace()
    }

}

println "total:"+totalCnt
println "null:"+nullCnt
println "zero:"+zeroCnt