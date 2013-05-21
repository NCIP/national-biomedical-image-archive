import groovy.sql.Sql;


def outputFilePath = args[0]
def password = args[1]

                          

def sql = Sql.newInstance("jdbc:mysql://cbdb-p2001.nci.nih.gov:3638/nciaprod",
                          "xxxx", 
                          "xxxxxxxx", 
                          "com.mysql.jdbc.Driver");                           

writeCmdFile(sql, new File(outputFilePath))

println("File is done");


///////////////////////////////////////////////////////////////////////////////

def writeCmdFile(sql,
                 outputCmdFile, 
                 collectionName = 'LIDC-IDRI') {    
                 
    def printWriter = new PrintWriter(outputCmdFile);
        
    def query = "select distinct s.series_instance_uid "+
                "from general_image i, general_series s "+
                "where i.general_series_pk_id = s.general_series_pk_id and "+
                "      s.modality='CT' and "+
                "project = '"+collectionName+"'";
   
    sql.eachRow(query) {  
        def seriesInstanceUid = it.series_instance_uid
        
        println seriesInstanceUid
        
        def filePath = getAnnotationFilePath(sql, seriesInstanceUid);
        if (filePath == null) {
            println("!!!!!!!No annotation for series uid ="+seriesInstanceUid);
        }    
        else {
            printWriter.print("mkdir    ./anofan3_0/" + seriesInstanceUid+"\n");

            def pixelAndSliceCmd = getImagePixelSliceCmd(sql, seriesInstanceUid);        
            printWriter.print("./max.pl --fname " + 
                              filePath + 
                              "  --skip " + 
                              pixelAndSliceCmd +
                              " --dir-out=xmlout3_0\n");
            printWriter.print("mv ./xmlout3_0/* ./anofan3_0/"+seriesInstanceUid+"/\n");
        }
    }
    printWriter.close();           
}
    
    
def getAnnotationFilePath(sql, seriesInstanceUid) {
    def query = "select distinct file_path from annotation where series_instance_uid='"+seriesInstanceUid+"'";
    def firstRow = sql.firstRow(query)
    if(firstRow == null) {
        return null;
    }
    else {
        return firstRow.file_path;
    }
}
    
def parseImagePosition(image_position_patient) {
   def tups = image_position_patient.split("\\\\");
   return Double.parseDouble(tups[2]);
}

def getImagePixelSliceCmd(sql, seriesInstanceUid){
    def query = "select image_position_patient, pixel_spacing from general_image where series_instance_uid='"+seriesInstanceUid+"'";
    
    def slices = []
    def pixels = []
    sql.eachRow(query) {   
        /*
        let it fail.  need to know there are rows with bad data
        if(it.image_position_patient == null) {
            return;
        }*/
        
        slices.add(parseImagePosition(it.image_position_patient))
        pixels.add(it.pixel_spacing)    
    }

    Collections.sort(pixels);
    Collections.sort(slices);

    Double minSlice = getMinSlice(slices);
    def pixelAndSliceCmd = "--pixel=" + pixels[0] +" --slice=" + minSlice.toString();

    return pixelAndSliceCmd;
}
    
def getMinSlice(slices) {
    double min=0.0;
    boolean first = true;
    double pre=0.0;
    
    for (slice in slices) {
        if (first) {
            first = false;
            pre = slice.doubleValue();
        }
        else {
            if ((pre-slice.doubleValue()) <= min){
                min=(pre-slice.doubleValue())*(-1);
            }
            pre=slice.doubleValue();
        }
    }
    return Double.valueOf(min);
}
