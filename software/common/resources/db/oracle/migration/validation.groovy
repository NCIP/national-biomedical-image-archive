import groovy.sql.Sql;
import java.sql.*;

mySqlUser = "nciaadmin"
mySqlPassword = ""
mySqlUrl = "jdbc:mysql://cbiodb580.nci.nih.gov:3638/nciaqa"

oracleUser = "nbia"
oraclePassword =""
oracleUrl = "jdbc:oracle:thin:@ncidb-nci-q.nci.nih.gov:1564:nciqa"

////////////////////////////////////////////////////////////////////////////////////////////

mySqlInstance = Sql.newInstance(mySqlUrl,
                                mySqlUser, 
                                mySqlPassword, 
                                "com.mysql.jdbc.Driver");                           

oracleInstance = Sql.newInstance(oracleUrl,
                                 oracleUser, 
                                 oraclePassword, 
                                 "oracle.jdbc.driver.OracleDriver"); 
          
tableNames = ['annotation',
              'collection_descriptions',
              'csm_application',
              'csm_group',
              'csm_pg_pe',
              'csm_privilege',
              'csm_protection_element',
              'csm_protection_group',
              'csm_role',
              'csm_role_privilege', 
              'csm_user',
              'csm_user_group',             
              'csm_user_group_role_pg',
              'csm_user_pe',               
              'ct_image',             
              'custom_series_list',
              'custom_series_list_attribute',
              'deletion_audit_trail',
              'download_data_history',
              'download_history',
              'general_equipment',
              'general_image',
              'general_series',
              'image_markup',
              'login_history',                    
              'patient',
              'qc_status_history',
              'query_history',
              'query_history_attribute',
              'saved_query',
              'saved_query_attribute',
              'study',
              'submission_history',
              'trial_data_provenance',
              'trial_site'];                     

//countValidation();

//findMissingRows('general_series', 'general_series_pk_id', 'general_series_pk_id');

//findMissingRows('csm_user', 'user_id', 'user_id');

//findMissingRows('csm_protection_group', 'protection_group_id', 'protection_group_id');

//findMissingRows('general_image', 'image_pk_id', 'image_pk_id');

findMissingRowsBackward('submission_history', 'submission_history_pk_id', 'submission_history_pk_id');

/////////////////////////////////////////////////////////////////////////////////////////////


          

               
def countValidation() {
   //for each table, ensure count is same
   for(def table in tableNames) {
       def mySqlRowCount = mySqlInstance.firstRow("select count(*) from "+table)[0]
       def oracleRowCount = oracleInstance.firstRow("select count(*) from "+table)[0]
       
       if(mySqlRowCount != oracleRowCount) {
           println "Table count doesn't agree for ${table}:"+mySqlRowCount+" for mysql vs. "+oracleRowCount+" for oracle"
       }
   }    

}

def findMissingRows(tableName, mySqlPkIdColumn, oraclePkIdColumn) {
    def query = "select "+mySqlPkIdColumn+" from "+tableName;
    
    Class.forName("com.mysql.jdbc.Driver")
    def con = DriverManager.getConnection(mySqlUrl,mySqlUser,mySqlPassword);

    def statement = con.createStatement(); 
    statement.setFetchSize(Integer.MIN_VALUE)
    def resultSet = statement.executeQuery(query);

    while (resultSet.next()) {
        
        Integer pkId   = resultSet.getInt(mySqlPkIdColumn);
    
        def oraclePkId = oracleInstance.firstRow("select "+oraclePkIdColumn+" from "+tableName+" where "+oraclePkIdColumn+"="+pkId)
        if(oraclePkId==null) {
            println "In "+tableName+" couldnt find "+oraclePkIdColumn+"="+pkId;
        }
    }    
 
    resultSet.close();
    statement.close();
    con.close();  
}

def findMissingRowsBackward(tableName, mySqlPkIdColumn, oraclePkIdColumn) {
    def query = "select "+oraclePkIdColumn+" from "+tableName;
    
    Class.forName("com.mysql.jdbc.Driver")
    def con = DriverManager.getConnection(oracleUrl,oracleUser,oraclePassword);

    def statement = con.createStatement(); 
    statement.setFetchSize(1000)
    def resultSet = statement.executeQuery(query);

    while (resultSet.next()) {
        
        Integer pkId   = resultSet.getInt(oraclePkIdColumn);
    
        def mySqlPkId = mySqlInstance.firstRow("select "+mySqlPkIdColumn+" from "+tableName+" where "+mySqlPkIdColumn+"="+pkId)
        if(mySqlPkId==null) {
            println "In "+tableName+" couldnt find "+mySqlPkIdColumn+"="+pkId;
        }
    }    
 
    resultSet.close();
    statement.close();
    con.close();  
}