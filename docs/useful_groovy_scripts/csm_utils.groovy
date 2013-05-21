import groovy.sql.Sql;

///////////////////
// INPUT PARAMETERS
///////////////////
csmUserName = 'nciadevtest';
nbiaApplicationId = 2
///////////////////

def nullSafeSqlStringQuotes(str) {
    if(str==null) {
        return "null";    
    }
    else {
        return "'"+str+"'";
    }    
}


def constructOptionalUserAttributes(organization,  
                                    department, 
                                    title, 
                                    phone_number, 
                                    password, 
                                    email_id, 
                                    start_date, 
                                    end_date, 
                                    middle_name, 
                                    fax, 
                                    address, 
                                    city, 
                                    state, 
                                    country,
                                    postal_code) {
                                    
    return nullSafeSqlStringQuotes(organization)+","+
           nullSafeSqlStringQuotes(department)+","+
           nullSafeSqlStringQuotes(title)+","+
           nullSafeSqlStringQuotes(phone_number)+","+
           nullSafeSqlStringQuotes(password)+","+
           nullSafeSqlStringQuotes(email_id)+","+
           nullSafeSqlStringQuotes(start_date)+","+
           nullSafeSqlStringQuotes(end_date)+", "+
           "current_date(),"+
           nullSafeSqlStringQuotes(middle_name)+","+
           nullSafeSqlStringQuotes(fax)+","+
           nullSafeSqlStringQuotes(address)+","+
           nullSafeSqlStringQuotes(city)+","+
           nullSafeSqlStringQuotes(state)+","+
           nullSafeSqlStringQuotes(country)+","+
           nullSafeSqlStringQuotes(postal_code);
}

/**
 * add a row to the csm_user table with the specified attributes.
 * everything after last_name is optional
 **/
def addCsmUser(sqlInstance,
               login_name, 
               first_name, 
               last_name, 
               
               organization,  
               department, 
               title, 
               phone_number, 
               password, 
               email_id, 
               start_date, 
               end_date,  
               middle_name, 
               fax, 
               address, 
               city, 
               state, 
               country,
               postal_code) {
    assert sqlInstance != null;
    assert login_name != null;
    assert first_name != null;
    assert last_name != null;
    
    def insertCsmUserSql = 
        "INSERT INTO csm_user(login_name, "+
                             "first_name, "+
                             "last_name, "+
                             "organization, "+ 
                             "department, "+
                             "title, "+
                             "phone_number, "+
                             "password, "+
                             "email_id, "+
                             "start_date, "+
                             "end_date, "+
                             "update_date, "+
                             "middle_name, "+
                             "fax, "+
                             "address, "+
                             "city, "+
                             "state, "+
                             "country,"+
                             "postal_code)"+
        "VALUES ('${login_name}',"+
                "'${first_name}',"+
                "'${last_name}',"+
                constructOptionalUserAttributes(organization,  
                                                department, 
                                                title, 
                                                phone_number, 
                                                password, 
                                                email_id, 
                                                start_date, 
                                                end_date,  
                                                middle_name, 
                                                fax, 
                                                address, 
                                                city, 
                                                state, 
                                                country,
                                                postal_code)+")";
                
    sqlInstance.execute(insertCsmUserSql);
}        

def associateCsmUserWithProtectionGroup(sqlInstance,
                                        login_name,
                                        protection_group_name,
                                        role_name) {
    def user_id = sqlInstance.firstRow("SELECT user_id FROM csm_user WHERE login_name = ?",[login_name]).user_id;                                       
    assert user_id != null;
    println user_id
    
    def role_id = sqlInstance.firstRow("SELECT role_id FROM csm_role WHERE role_name = ?",[role_name]).role_id;
    assert role_id != null;
    println role_id
    
    def protection_group_id = sqlInstance.firstRow("SELECT protection_group_id "+
                                                   "FROM csm_protection_group "+
                                                   "WHERE protection_group_name = ?",[protection_group_name]).protection_group_id;                                                   
    assert protection_group_id != null;
    println protection_group_id
    
    def associationSql = "INSERT INTO csm_user_group_role_pg(user_id,"+
                                                            "group_id,"+
                                                            "role_id,"+
                                                            "protection_group_id,"+
                                                            "update_date) "+
                         "VALUES (?, null, ?, ?, current_date())";   

    println associationSql
    sqlInstance.execute(associationSql, [user_id, role_id, protection_group_id] );                         
}

def addProtectionElementForSite(sqlInstance,
                                collectionName,
                                siteName) {
                                
    def insertPeSql = "INSERT INTO csm_protection_element(protection_element_name,"+
                                                         "protection_element_description,"+
                                                         "object_id,"+
                                                         "attribute,"+
                                                         "protection_element_type,"+
                                                         "application_id,"+
                                                         "attribute_value,"+
                                                         "update_date) "+
                         "VALUES (?, ?, ?, ?, null, ?, null, current_date())";   

    println insertPeSql
    sqlInstance.execute(insertPeSql, ['NCIA.'+collectionName,
                                      'NCIA.TRIAL_DATA_PROVENANCE', 
                                      'NCIA.'+collectionName, 
                                      'NCIA.PROJECT', 
                                      nbiaApplicationId] );   
                                                                
    sqlInstance.execute(insertPeSql, ['NCIA.'+collectionName + "//"+siteName,
                                      'NCIA.TRIAL_DATA_PROVENANCE', 
                                      'NCIA.'+collectionName+ "//"+siteName, 
                                      'NCIA.PROJECT//DP_SITE_NAME', 
                                      nbiaApplicationId] );                                       
}

def addProtectionElementToPublicPG(sqlInstance, collectionName, siteName) {
    def collection_protection_element_id = sqlInstance.firstRow("SELECT protection_element_id FROM csm_protection_element WHERE protection_element_name = ?",['NCIA.'+collectionName]).protection_element_id;                                       
    assert collection_protection_element_id != null;
    println collection_protection_element_id 
    
    def site_protection_element_id = sqlInstance.firstRow("SELECT protection_element_id FROM csm_protection_element WHERE protection_element_name = ?",['NCIA.'+collectionName+"//"+siteName]).protection_element_id;                                       
    assert site_protection_element_id != null;
    println site_protection_element_id 
    
    def protection_group_id = sqlInstance.firstRow("SELECT protection_group_id FROM csm_protection_group WHERE protection_group_name = ?",['NCIA.PUBLIC']).protection_group_id;                                       
    assert protection_group_id != null;
    println protection_group_id    

    def associationSql = "INSERT INTO csm_pg_pe(protection_group_id,"+
                                               "protection_element_id,"+
                                               "update_date) "+
                         "VALUES (?, ?, current_date())";   

    println associationSql
    sqlInstance.execute(associationSql, [protection_group_id, collection_protection_element_id] );
    sqlInstance.execute(associationSql, [protection_group_id, site_protection_element_id] );
}
                                
//////////////////////////////////////        
def sqlInstance = Sql.newInstance("jdbc:mysql://localhost:3306/nciadb", 
                                  "root", 
                                  "Mysql1!.", 
                                  "com.mysql.jdbc.Driver");

        
/*    
addCsmUser(sqlInstance,
           csmUserName,
           "fake_first_name2",
           "fake_last_name2",
           
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null,
           null);
           
associateCsmUserWithProtectionGroup(sqlInstance, csmUserName, "NCIA.PUBLIC", "NCIA.READ");         
*/

addProtectionElementForSite(sqlInstance, 'Project', 'SiteName');

addProtectionElementToPublicPG(sqlInstance, 'Project', 'SiteName');