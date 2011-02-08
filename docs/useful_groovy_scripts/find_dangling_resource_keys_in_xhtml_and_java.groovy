#!/usr/bin/env groovy

def findInFiles(targetDir, filePattern, patternToFind) {
    def found = false;
    
    targetDir.eachDir() { subdirectory -> found = found || findInFiles(subdirectory, filePattern, patternToFind); };
    

    targetDir.eachFileMatch(filePattern) { matchedFile ->
        if(matchedFile.text =~ patternToFind) {
            found = true;            
        } 
        
    }
    return found;
}


def resourceBundlePath = this.args[0];
def resourceBundle = new Properties();
resourceBundle.load(new FileInputStream(new File(resourceBundlePath)));

def sourceTree = this.args[1];

def filePattern = ~/.*\.java|.*\.xhtml/;

for(pair in resourceBundle) {

    def patternToFind = pair.key;
    
    def found = findInFiles(new File(sourceTree), filePattern, patternToFind);    
    if(!found) {
        println("Value not found:"+pair.key);
    }
}
