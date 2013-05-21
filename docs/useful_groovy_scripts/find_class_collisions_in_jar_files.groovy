import java.util.jar.JarFile;

def targetDir = new File("c:/fred/");
def outputFile = new File("c:/output.txt");

def jarFileFilter = [accept: { directory, file -> file ==~ /.*\.jar/ }];

def jarFiles = targetDir.listFiles(jarFileFilter as FilenameFilter);

def classNames = [];

def classNamesToJarsMap = [:];

def collisions = [];

for(jarFile in jarFiles) {
    def jarEntries = new JarFile(jarFile);
    
    for(entry in jarEntries.entries()) {
        if(entry.name ==~ /.*\.class/) {
            if(classNames.contains(entry.name)) {
                collisions.add(entry.name);
            }
            else {
                classNames.add(entry.name);                
            }    
            
            def existingJarFiles = classNamesToJarsMap.get(entry.name);
            if(existingJarFiles==null) {
               classNamesToJarsMap.put(entry.name, [jarFile.name]);
            }
            else {
               existingJarFiles.add(jarFile.name);
            }
        }
    }
}


outputFile.withWriter {
    out -> collisions.each() { 
               collision -> out.writeLine(collision  + "," + classNamesToJarsMap.get(collision));                           
           }
}

