

def uncompressTgzFile(tgzFile, dirToCopyTo) {
    def ant = new AntBuilder()   // create an antbuilder 
 

    ant.gunzip( src: tgzFile.getAbsolutePath(), 
                dest: dirToCopyTo.getAbsolutePath()+"/internal.tar") ;

    def tarFile = dirToCopyTo.listFiles()[0];
    
    //some of these zips just contain a tar.  this logic will break pretty easily....
    if(tarFile ==~ /.*\.tar/) {
        ant.untar(src: tarFile.getAbsolutePath(), 
                  dest: dirToCopyTo.getAbsolutePath(),
                  overwrite: "false");
                  
        tarFile.delete();  
    }
    else {
        println "Something is fishy.  gunzip didnt yield a tar:"+tgzFile.getAbsolutePath();
    }
}


def unzipZipFile(zipFile, dirToCopyTo) {
    def ant = new AntBuilder();   
    
    ant.unzip( src: zipFile.getAbsolutePath(), 
               dest: dirToCopyTo.getAbsolutePath(), 
               overwrite: "false");
}


def visitSeriesDir(dir, targetDir) {
    println "Visiting:"+dir;
        
    //make dir with same name in target dir    
    def dirToCopyTo = new File(targetDir, dir.getName());
    dirToCopyTo.mkdir();

    dir.eachFileMatch(~/.*\.zip/) { 
        unzipZipFile(it, dirToCopyTo);
    }
    

    dir.eachFileMatch(~/.*\.tgz|.*\.tar.gz/) { 
        uncompressTgzFile(it, dirToCopyTo);
    }
    
    def ant = new AntBuilder();
    dir.eachDir() {
		    ant.copydir( src:it, dest:dirToCopyTo);
    }
}

///////////////////////////////////////BEGIN PROGRAM///////////////////////////////////
//E:\LIDC_Data\Final
def srcDir = args[0];
//E:\lidc_submission
def targetDir = args[1];

new File(srcDir).eachDir() {
    subdirectory -> visitSeriesDir(subdirectory, targetDir)
};
