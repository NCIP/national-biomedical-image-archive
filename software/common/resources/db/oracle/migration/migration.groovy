def sqlFile = new File(args[0])

//take a collection of strings and merge into string enclsoed in () and each token separated by comma
def constructValues(tokens) {
    def returnString = new StringBuffer();
    returnString.append('(');
    for(def i=0;i<tokens.size();i++) {
        returnString.append(tokens[i]);
        if(i!=tokens.size()-1) {
            returnString.append(",");
        }        
    }
    returnString.append(")");
    return returnString;
}

def parseStringLiteral(str) {
    def parseResult = []
    def buff = new StringBuffer()
    def terminatingQuoteSeen = false
    def munchedCnt = 0
    
    if(str[0]!="\'") {
        throw new RuntimeException("not a string literal???");
    }
    else {
       buff.append(str[0])
       munchedCnt += 1
    }
    
    def lastChar = null;
    for(def c in str.substring(1)) {
       munchedCnt+=1    
       if(c==',' && terminatingQuoteSeen) {
           break;
       }
       if(c=="\'" && lastChar != "\\") {
           terminatingQuoteSeen = true
           //println "saw terminator at"+munchedCnt+" vs "+str.size()
       }
       buff.append(c)
       lastChar = c
    }
    parseResult[0] = buff.toString()
    parseResult[1] = munchedCnt
    
    return parseResult
}


def parseLiteral(str) {
    def parseResult = []
    def buff = new StringBuffer()
    def munchedCnt = 0    
    for(def c in str) {
       munchedCnt+=1
       if(c==',') {
           break;
       }
       buff.append(c)
    }
    parseResult[0] = buff.toString()
    parseResult[1] = munchedCnt
    return parseResult    
}

//acts like tokenize(',') except that it looks for commas inside string literals
//wont handle functions just string literals, or other literals
def splitValues(values) {
    def splitArr = []
    
    def arrIndex = 0;
    def parsePtr = 0
    while(parsePtr<values.size()) {
        if(values[parsePtr]=="'") {
            def parseResult = parseStringLiteral(values.substring(parsePtr))
            splitArr[arrIndex++] = parseResult[0]
            parsePtr += parseResult[1];
        }
        else {
            def parseResult = parseLiteral(values.substring(parsePtr))
            splitArr[arrIndex++] = parseResult[0]    
            parsePtr += parseResult[1];
        }
    }
    
    //for(def elt in splitArr) {
        //println "elt:"+elt
    //}
    return splitArr;
    
}

//values looks like either ,(xxxxx) or (xxxx,yyy)
def setNthValue(values, indexTransformFunctionPairs) {

    def lParen = values.indexOf('(')    
    def rParen = values.lastIndexOf(')') 
    def noParens = values.substring(lParen+1,rParen)
    def tokens = splitValues(noParens)

    for(def pair in indexTransformFunctionPairs) {
        tokens[pair[0]] = pair[1](tokens[pair[0]])
    }

    def returnString = constructValues(tokens);
    return returnString;
}

transformMySQLTimeStampToOracleDate = {
    timestamp -> return "to_date("+timestamp+", 'YYYY-MM-DD HH24:MI:SS')"
}

transformMySQLDateToOracleDate = {
    mySqlDate -> return "to_date("+mySqlDate+", 'YYYY-MM-DD')"
}


def splitAtValues(line) {
    def dex = line.indexOf("values");
    if(dex==-1) {
        throw new RuntimeException("got -1 for values dex")
    }
    
    def pieces = [line.substring(0, dex+"values".size()),
                  line.substring(dex+"values".size()+1,line.size()-1)]  //strip semicolon
    return pieces;
}

processAnnotationLine = {
   values ->
     return setNthValue(values, [[6, transformMySQLTimeStampToOracleDate]])   
}

processSubmissionHistoryLine = {
   values ->
     return setNthValue(values, [[5, transformMySQLTimeStampToOracleDate]])
}


processDeletionAuditTrailLine = {
   values ->
     return setNthValue(values, [[5, transformMySQLTimeStampToOracleDate]])
}

processDownloadHistoryLine = {
   values ->
     return setNthValue(values, [[2, transformMySQLTimeStampToOracleDate]])
}

processGeneralImageLine = {
   values ->
     def returnVals = setNthValue(values, [[29, transformMySQLTimeStampToOracleDate],
                                           [30, transformMySQLTimeStampToOracleDate],
     
                                           [2, transformMySQLDateToOracleDate],
                                           [5, transformMySQLDateToOracleDate],
                                           [32, transformMySQLDateToOracleDate]]);
     return returnVals     
}

processGeneralSeriesLine = {
   values ->
     def returnVals = setNthValue(values, [[31, transformMySQLTimeStampToOracleDate],     
                                           [4, transformMySQLDateToOracleDate],
                                           [13, transformMySQLDateToOracleDate]]);

     return returnVals     
}

processStudyLine = {
   values ->
     return setNthValue(values, [[2, transformMySQLDateToOracleDate]])     
}


processPatientLine = {
   values ->
     return setNthValue(values, [[3, transformMySQLDateToOracleDate]])     
}

processImageMarkupLine = {
   values ->
     return setNthValue(values, [[5, transformMySQLTimeStampToOracleDate]])     
}

processLoginHistoryLine = {
   values ->
     return setNthValue(values, [[1, transformMySQLTimeStampToOracleDate]])     
}

processQcLine = {
   values ->
     return setNthValue(values, [[4, transformMySQLTimeStampToOracleDate]])   
}

processQueryHistory = {
   values ->
     return setNthValue(values, [[2, transformMySQLTimeStampToOracleDate]])   
}

processSavedQuery = {
   values ->
     return setNthValue(values, [[5, transformMySQLTimeStampToOracleDate]])   
}

processCsmUser = {
   values ->
     def returnVals = setNthValue(values, [[10, transformMySQLDateToOracleDate],
                                           [11, transformMySQLDateToOracleDate],
                                           [12, transformMySQLDateToOracleDate]]);

     return returnVals     
}



def processLine(line, lineProcessor) {
    def pieces = splitAtValues(line.trim());
        
    print pieces[0]
    
    def values = pieces[1]
    def transformed = lineProcessor(values)
    
    print transformed                
    println ";"    
}

def processSingleDateLine(line, offset) {
    def pieces = splitAtValues(line);
        
    print pieces[0]
    
    def values = pieces[1]
    def transformed = setNthValue(values, [[offset, transformMySQLDateToOracleDate]])
     
    print transformed
    println ";"               
}

def doNothing() {
}

System.err.println "Start:"+System.currentTimeMillis()
//println "Start:"+System.currentTimeMillis()  

cnt = 0
sqlFile.eachLine {

    line -> if(cnt%100000==0) System.err.println "processing line:"+cnt
            cnt+=1
            if(line =~ /^insert\s+into\s+annotation.*/) processLine(line, processAnnotationLine);
            else
            if(line =~ /^insert\s+into\s+submission_history.*/) processLine(line, processSubmissionHistoryLine);
            else
            if(line =~ /^insert\s+into\s+curation_data.*/) doNothing();
            else
            if(line =~ /^insert\s+into\s+curation_status.*/) doNothing();  
            else
            if(line =~ /^insert\s+into\s+databasechangelog.*/) doNothing(); 
            else
            if(line =~ /^insert\s+into\s+qa_status_history_old.*/) doNothing(); 
            else            
            if(line =~ /^insert\s+into\s+deletion_audit_trail.*/) processLine(line, processDeletionAuditTrailLine);   
            else            
            if(line =~ /^insert\s+into\s+download_history.*/) processLine(line, processDownloadHistoryLine);             
            else          
            if(line =~ /^insert\s+into\s+general_image.*/) processLine(line, processGeneralImageLine);            
            else            
            if(line =~ /^insert\s+into\s+general_series.*/) processLine(line, processGeneralSeriesLine); 
            else            
            if(line =~ /^insert\s+into\s+study.*/) processLine(line, processStudyLine); 
            else            
            if(line =~ /^insert\s+into\s+patient.*/) processLine(line, processPatientLine); 
            else            
            if(line =~ /^insert\s+into\s+image_markup.*/) processLine(line, processImageMarkupLine); 
            else            
            if(line =~ /^insert\s+into\s+login_history.*/) processLine(line, processLoginHistoryLine); 
            else            
            if(line =~ /^insert\s+into\s+qc_status_history.*/) processLine(line, processQcLine); 
            else            
            if(line =~ /^insert\s+into\s+query_history[\s\(].*/) processLine(line, processQueryHistory);             
            else            
            if(line =~ /^insert\s+into\s+saved_query[\s\(].*/) processLine(line, processSavedQuery);             
            else            
            if(line =~ /^insert\s+into\s+collection_description.*/) processSingleDateLine(line, 4);             
            else            
            if(line =~ /^insert\s+into\s+csm_application.*/) processSingleDateLine(line, 5);             
            else            
            if(line =~ /^insert\s+into\s+csm_filter_clause.*/) processSingleDateLine(line, 10);             
            else            
            if(line =~ /^insert\s+into\s+csm_group.*/) processSingleDateLine(line, 3);             
            else            
            if(line =~ /^insert\s+into\s+csm_pg_pe.*/) processSingleDateLine(line, 3);             
            else            
            if(line =~ /^insert\s+into\s+csm_privilege.*/) processSingleDateLine(line, 3); 
            else            
            if(line =~ /^insert\s+into\s+csm_protection_element.*/) processSingleDateLine(line, 7);             
            else            
            if(line =~ /^insert\s+into\s+csm_protection_group.*/) processSingleDateLine(line, 5);              
            else            
            if(line =~ /^insert\s+into\s+csm_role_privilege.*/) processSingleDateLine(line, 3); 
            else           
            if(line =~ /^insert\s+into\s+csm_role.*/) processSingleDateLine(line, 5);            //come after csm_role
            else
            if(line =~ /^insert\s+into\s+csm_user_group_role_pg.*/) processSingleDateLine(line, 5); 
            else            
            if(line =~ /^insert\s+into\s+csm_user_pe.*/) processSingleDateLine(line, 3); 
            else            
            if(line =~ /^insert\s+into\s+custom_series_list[\s\(].*/) processSingleDateLine(line, 4);
            else            
            if(line =~ /^insert\s+into\s+download_data_history.*/) processSingleDateLine(line, 3); 
            else            
            if(line =~ /^insert\s+into\s+csm_user\s*\(.*/) processLine(line, processCsmUser); 
            else {
                println line
            }    
}

System.err.println "End:"+System.currentTimeMillis()
//println "End:"+System.currentTimeMillis()