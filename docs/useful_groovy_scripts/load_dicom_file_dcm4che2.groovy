import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.data.DicomObject;

def dicomImageFile = new File("xxxxx");

def loadDicomFile2(dicomImageFile) {
    def dicomInputStream = null;
    try {
        dicomInputStream = new DicomInputStream(dicomImageFile);
        return din.readDicomObject();
    }
    catch (IOException e) {
        e.printStackTrace();
        return null;
    }
    finally {
        dicomInputStream?.close();
    }
}    


def dataset = loadDicomFile2(dicomImageFile);
println dataset
def iter = dataset.iterator()
while(iter.hasNext()) {
    println iter.next();
}