import groovy.sql.Sql;
import java.sql.*;

mySqlUser = "nciaadmin"
mySqlPassword = ""
mySqlUrl = "jdbc:mysql://cbiodb590.nci.nih.gov:3638/nciadev"

oracleUser = "nbia"
oraclePassword =""
oracleUrl = "jdbc:oracle:thin:@ncidb-nci-d.nci.nih.gov:1564:ncidev"

////////////////////////////////////////////////////////////////////////////////////////////

mySqlInstance = Sql.newInstance(mySqlUrl,
                                mySqlUser, 
                                mySqlPassword, 
                                "com.mysql.jdbc.Driver");                           

oracleInstance = Sql.newInstance(oracleUrl,
                                 oracleUser, 
                                 oraclePassword, 
                                 "oracle.jdbc.driver.OracleDriver"); 
                           

transferImageMarkup();

transferCollectionDescriptions();

/////////////////////////////////////////////////////////////////////////////////////////////

/**
 * this table should be small enough to use convenient groovy shorthand
 **/
def transferCollectionDescriptions() {
                           
    def query = "select collection_descriptions_pk_id,collection_name,user_name,description,collection_descriptions_timestamp from collection_descriptions";

    mySqlInstance.eachRow(query) { 
        oracleInstance.execute("insert into collection_descriptions "+
                               "(collection_descriptions_pk_id,collection_name,user_name,description,collection_descriptions_timest) "+
                               "values (?,?,?,?,?)", 
                               [it.collection_descriptions_pk_id, it.collection_name, it.user_name, it.description, it.collection_descriptions_timestamp]);
    }
}


/**
 * eachRow apparently loads the whole result set... or is at the mercy of MySQL JDBC driver which loads whole result set
 * not sure how to change fetch size so Groovy API picks up on it... so just use JDBC
 **/
def transferImageMarkup() {
    def query = "select image_markup_pk_id,series_instance_uid,general_series_pk_id,login_name,markup_content,submission_date from image_markup";
    def cnt = 0
    
    Class.forName("com.mysql.jdbc.Driver")
    def con = DriverManager.getConnection(mySqlUrl,mySqlUser,mySqlPassword);

    def statement = con.createStatement(); 
    statement.setFetchSize(Integer.MIN_VALUE)
    def resultSet = statement.executeQuery(query);

    while (resultSet.next()) {
        if((cnt % 50)==0) println "cnt:"+cnt
        
        Integer image_markup_pk_id   = resultSet.getInt("image_markup_pk_id");
        String series_instance_uid   = resultSet.getString("series_instance_uid");
        Integer general_series_pk_id = resultSet.getInt("general_series_pk_id");
        String login_name            = resultSet.getString("login_name");
        String markup_content        = resultSet.getString("markup_content");
        Date submission_date         = resultSet.getDate("submission_date");
    
    
        oracleInstance.execute("insert into image_markup "+
                               "(image_markup_pk_id,series_instance_uid,general_series_pk_id,login_name,markup_content,submission_date) "+
                               "values (?,?,?,?,?,?)", 
                               [image_markup_pk_id, series_instance_uid, general_series_pk_id, login_name, markup_content, submission_date]);    
        cnt+=1 
    }    
 
    resultSet.close();
    statement.close();
    con.close();    
}

