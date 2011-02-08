def targetDir = new File(args[0]);

def ant = new AntBuilder()   // create an antbuilder 
ant.project.getBuildListeners().each { 
    it.setOutputPrintStream(new PrintStream('NUL')) 
}

targetDir.eachFileRecurse {
    if(it.getAbsolutePath() ==~ /.*\.jar/ && it.isFile()) {
        //println it.getAbsolutePath() +"," +it.getParent()
        ant.unjar(src: it.getAbsolutePath(), 
                  dest: it.getParent(),
                  overwrite: "false") {
                  
            patternset {
                include(name:'**/Manifest.mf')
                include(name:'**/MANIFEST.MF')
            }
                 
            mapper(type:"flatten")                  
        }        
        
        def manifestFile = new File(it.getParent(), "Manifest.mf");
        if(!manifestFile.exists()) {
            println "No manifest for "+it
        }
        else {
            def properties = new Properties();
            def manifestInputStream = new FileInputStream(manifestFile);
            try {
                properties.load(manifestInputStream);
                
                def mainClass = properties.get("Main-Class");
                if(mainClass==~/.*main\..*/) {
                    println mainClass + " " +it;
                }
            
            }
            catch(ex) {
                println "Failed on "+it;
                println ex;
            }
            finally {
                manifestInputStream.close();
                manifestFile.delete();
            }
        }
        
    }
    
   
}
