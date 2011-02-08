def parseAimXml(xmlFile) {
    def xmlParser = new XmlParser()
    def document = xmlParser.parse(xmlFile)
   
    def personElement = document.depthFirst().find {
         it.name().getLocalPart() == 'Person' 
    }     
    
    def imageStudyElement = document.depthFirst().find {
         it.name().getLocalPart() == 'ImageStudy' 
    } 
    
    def imageSeriesElement = document.depthFirst().find {
         it.name().getLocalPart() == 'ImageSeries' 
    } 
    
    def imageElement = document.depthFirst().find {
         it.name().getLocalPart() == 'Image' 
    }    
    
    println '[\''+personElement['@id']+'\',\''+imageStudyElement['@instanceUID']+'\',\''+imageSeriesElement['@instanceUID']+'\',\''+imageElement['@sopInstanceUID']+'\']'
}


def targetDir = new File("xxxxx")
targetDir.eachFileRecurse {
   if(it.isFile() && it.getName().endsWith("xml")) {
       parseAimXml(it)
   }
}