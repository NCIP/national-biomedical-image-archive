package gov.nih.nci.ncia.util;

import java.text.SimpleDateFormat;


/**
 * Even though the system uses Hibernate to largely abstract the database,
 * there are a few little things that don't have an obvious way to be
 * made cross-database or cross-platform by databases (supporting Oracle
 * and MySQL).  Dump any little util here that has logic to
 * do the right, different thing, depending on Oracle or MySQL
 */
public class CrossDatabaseUtil {
	/**
	 * Add a suffix to discrimiante between oracle/mysql native sql queries
	 */
	public static String getNamedQuery(String queryName) {
        return queryName+"_"+NCIAConfig.getDatabaseType();
	}


	public static SimpleDateFormat getDatabaseSpecificDatePattern() {
        if(NCIAConfig.getDatabaseType().equals(MYSQL)) {
        	return new SimpleDateFormat("yyyy-MM-dd");
        }
        else
        if(NCIAConfig.getDatabaseType().equals(ORACLE)) {
        	return new SimpleDateFormat("dd-MMM-yyyy");
        }
        throw new RuntimeException("bad db type:getDatabaseSpecificDatePattern");
	}

	public static String submissionTimeStampRange(String startDateStr, String endDateStr) {
        if(NCIAConfig.getDatabaseType().equals("mysql")) {
			return "date(submission_timestamp)"+
			        " between '"+
			        startDateStr+
			        "' and '"+
			        endDateStr+
			        "'";
        }
        else
        if(NCIAConfig.getDatabaseType().equals(ORACLE)) {
			return "trunc(submission_timestamp, 'DDD') "+
			        " between "+
			        "to_date('"+startDateStr+"', 'YYYY-MM-DD') "+
			        " and "+
			        "to_date('"+endDateStr+"', 'YYYY-MM-DD') ";
        }
        throw new RuntimeException("bad db type:submissionTimeStampRange");

	}

	public static String curationTimeStampRange(String fromDateString,
			                                    String toDateString) {
        if(NCIAConfig.getDatabaseType().equals(MYSQL)) {
        	return curationTimeStampRangeMySQL(fromDateString, toDateString);
        }
        else
        if(NCIAConfig.getDatabaseType().equals(ORACLE)) {
        	return curationTimeStampRangeOracle(fromDateString, toDateString);
        }
        throw new RuntimeException("bad db type:curationTimeStampRange");
	}

	////////////////////////////////PRIVATE//////////////////////////////////////

	private static final String MYSQL = "mysql";
	private static final String ORACLE = "oracle";

	private static String curationTimeStampRangeMySQL(String fromDateString,
			                                          String toDateString) {
		if (((fromDateString != null) && (fromDateString.length() > 0)) &&
		    ((toDateString != null) && (toDateString.length() > 0))) {
         	return "and to_days(gi.curationTimestamp) >= to_days('" +
            	       fromDateString +
            	       "') and to_days(gi.curationTimestamp) <= to_days('" +
            	       toDateString + "')";
        }
        else {
			return "";
		}
	}

	private static String curationTimeStampRangeOracle(String fromDateString,
			                                           String toDateString) {
		if (((fromDateString != null) && (fromDateString.length() > 0)) &&
		    ((toDateString != null) && (toDateString.length() > 0))) {
         	return "and trunc(gi.curationTimestamp,'DDD') >= '" + fromDateString +
			       "' and trunc(gi.curationTimestamp, 'DDD') <= '" + toDateString + "'";

        }
        else {
			return "";
	    }
	}
}
