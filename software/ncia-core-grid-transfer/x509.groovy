import groovy.sql.Sql;

def constructOrg(organizationName) {
    return "O="+organizationName;
}


def constructOU(organizationalUnitNames) {
    def result = "";
    
    for(def i=0;i<organizationalUnitNames.size();i++) {
        result += "OU="+organizationalUnitNames[i];
        if(i!=organizationalUnitNames.size()-1) {
            result += "/";
        }
    }
    return result;
}

def constructCommonName(commonName) {
    return "CN="+commonName;
}

/**
 * build up an x.509 identifier based upon constituent parts
 **/
def construct509Identifier(organizationName, organizationalUnitNames, commonName) {
    assert organizationalUnitNames.size() > 0;
    
    return "/"+
           constructOrg(organizationName)+
           "/"+
           constructOU(organizationalUnitNames)+
           "/"+
           constructCommonName(commonName);
}


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

//////////////////////////////////////        
def sqlInstance = Sql.newInstance("jdbc:mysql://cbiodb590.nci.nih.gov:3638/nciadev", 
                                  "xxxx", 
                                  "xxxx", 
                                  "com.mysql.jdbc.Driver");

        
def x509id = construct509Identifier("caBIG", 
                                    ["caGrid", "Stage LOA1", "NCI STAGE"], 
                                    "kascice-moo2") ;        
addCsmUser(sqlInstance,
           x509id,
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
           
associateCsmUserWithProtectionGroup(sqlInstance, x509id, "NCIA.PUBLIC", "NCIA.READ");         