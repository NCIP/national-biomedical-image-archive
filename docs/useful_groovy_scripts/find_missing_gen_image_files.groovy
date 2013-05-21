import groovy.sql.Sql;

def targetDir = new File(args[0]);


def isDicomFile(path) {
    return path ==~ /.*\.dcm/;
}

def iteratePerFile(file, dicomFileUriSql) {
    if(isDicomFile(file.getAbsolutePath())) {
        
        def selectString = "SELECT COUNT(*) "+
                           "FROM general_image "+
                           "WHERE dicom_file_uri = \'${file.getAbsolutePath()}\'";   
                   
        def row = dicomFileUriSql.firstRow(selectString);        
        if(row[0]!=1) {
            println file
        }        
    }    
}


////////////////////////////////////////////////////////////////////

dicomFileUriSql = Sql.newInstance("jdbc:mysql://xxxxx:xxxx/xxxx", "xxxxx-user", "xxxxx-password", "com.mysql.jdbc.Driver");

targetDir.eachFileRecurse {
    if(it.isFile()) {
        iteratePerFile(it, dicomFileUriSql);
    }
}

