import groovy.sql.Sql;
import java.sql.*;

oracleUser = "xxxx"
oraclePassword ="xxxxx"
oracleUrl = "jdbc:oracle:thin:@ncidb-nci-d.nci.nih.gov:1564:ncidev"

////////////////////////////////////////////////////////////////////////////////////////////

oracleInstance = Sql.newInstance(oracleUrl,
                                 oracleUser, 
                                 oraclePassword, 
                                 "oracle.jdbc.driver.OracleDriver"); 
          


/////////////////////////////////////////////////////////////////////////////////////////////



def query = "select i.dicom_file_uri, i.sop_instance_uid "+
            "from general_series s, general_image i "+
            "where s.modality='US' and s.general_series_pk_id = i.general_series_pk_id"
    

oracleInstance.eachRow(query) {   

    def dicomFile = new File(it.dicom_file_uri)
    if(!dicomFile.exists()) {
        println it.sop_instance_uid +" doesnt exist"
        return;
    }
    
    def found = false
    def name = dicomFile.getName()
    dicomFile.getParentFile().eachFileMatch(~"${name}.*jpeg") {
        found = true        
    }
    
    if(!found) {
        println it.sop_instance_uid +" has no thumbnail"
    }
}



