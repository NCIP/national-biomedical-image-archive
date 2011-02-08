import org.dcm4che2.tool.xml2dcm.Xml2Dcm;
import groovy.xml.XmlUtil;

/////////STUFF TO SET BEFORE EXECUTING SCRIPTS///////////////
def numPatients = 10
def numStudiesPerPatient = 10
def numSeriesPerStudy = 10
def numImagesPerSeries = 5
def dicomXmlTemplateFile = new File("dicom_template.xml");
/////////////////////////////////////////////////////////////

def updateTag(document, tagName, tagValue) {
    def tagNode = document.attr.find {
        it['@tag'] == tagName
    }
    
    tagNode['@len'] = tagValue.length();
    tagNode.setValue(tagValue);
}    


def generateDicomFileXml(dicomXmlTemplateFile, 
                         patientId, 
                         studyInstanceUid, 
                         seriesInstanceUid, 
                         sopInstanceUid) {
    def xmlParser = new XmlParser()
    def document = xmlParser.parse(dicomXmlTemplateFile)
                
    updateTag(document, '00100020', patientId.toString());
    updateTag(document, '0020000D', studyInstanceUid.toString());
    updateTag(document, '0020000E', seriesInstanceUid.toString());
    updateTag(document, '00080018', sopInstanceUid.toString());
    
    
    File generatedDicomFile = File.createTempFile('generated', '.xml')
    def fileOutputStream = new FileOutputStream(generatedDicomFile);
    try {
        XmlUtil.serialize(document, fileOutputStream);
    }
    finally {
        fileOutputStream.close()
    }
    return generatedDicomFile
}    


def convertXmlBackToDicom(xmlFile) {
    def generatedDicomFile = File.createTempFile('gen', '.dcm')
    String[] args = ["-x", xmlFile.getAbsolutePath(), "-o", generatedDicomFile.getAbsolutePath() ]		
    Xml2Dcm.main(args);
    return generatedDicomFile;
}

                     
for(def patient_i in 0..<numPatients) {
		for(def study_i in 0..<numStudiesPerPatient) {
				for(def series_i in 0..<numSeriesPerStudy) {
						for(def image_i in 0..<numImagesPerSeries) {

								def generatedDicomXmlFile = generateDicomFileXml(dicomXmlTemplateFile, patient_i, study_i, series_i, image_i);

								def generatedDicomFile = convertXmlBackToDicom(generatedDicomXmlFile);

								generatedDicomXmlFile.delete();
						}    
				}
		}
}





