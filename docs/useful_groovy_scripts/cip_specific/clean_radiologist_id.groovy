import groovy.xml.XmlUtil;
import groovy.xml.QName;

/////////STUFF TO SET BEFORE EXECUTING SCRIPTS///////////////

def lidcXmlDirectories = ['L:/NCICB/NCIA/nbia_LIDC/222',
                          'L:/NCICB/NCIA/nbia_LIDC/223',
                          'L:/NCICB/NCIA/nbia_LIDC/224',
                          'L:/NCICB/NCIA/nbia_LIDC/225',
                          'L:/NCICB/NCIA/nbia_LIDC/226'];


/////////////////////////////////////////////////////////////

def stripTagFromFile(xmlFile, 
                     tagNameToStrip) {
    def xmlParser = new XmlParser()
    def document = xmlParser.parse(xmlFile)
               
    def tagsToStrip = document.depthFirst().findAll {
         it.name().getLocalPart() == tagNameToStrip 
    }        
    if(tagsToStrip != null && tagsToStrip.size() > 0) {
        //println "Found tag in file:"+xmlFile.getAbsolutePath();
        for(def tagToStrip in tagsToStrip) {
            tagToStrip.value = 'anon'
        }    
    }
    else {
        //do nothing, could print out msg
    }
   
    
    
    File outputFile = new File(xmlFile.getAbsolutePath()+".out");
    def fileOutputStream = new FileOutputStream(outputFile);
    try {
        XmlUtil.serialize(document, fileOutputStream);
    }
    finally {
        fileOutputStream.close()
    }
    return outputFile
}    

////////////////////////////////////////////////////////////

for(def lidcXmlDirectory in lidcXmlDirectories) {
    println "Processing directory:"+lidcXmlDirectory
    
    new File(lidcXmlDirectory).eachFileMatch(~/.*\.xml/) {
        
        stripTagFromFile(it, 'servicingRadiologistID')     
    }   
}




