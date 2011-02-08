import org.dcm4che2.tool.xml2dcm.Xml2Dcm;
import groovy.xml.XmlUtil;
import groovy.util.Eval; 

/////////STUFF TO SET BEFORE EXECUTING SCRIPTS///////////////
dicomXmlTemplateFile = new File("c:/dicom_template.xml");
idsFile = new File("c:/ids.txt");
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


////////////////////////////////////
                     
idsFile.eachLine { 

    def ids = Eval.me(it)
  
    def generatedDicomXmlFile = generateDicomFileXml(dicomXmlTemplateFile, ids[0], ids[1], ids[2], ids[3]);

    def generatedDicomFile = convertXmlBackToDicom(generatedDicomXmlFile);

    generatedDicomXmlFile.delete();
}





