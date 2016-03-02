/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.liquibase;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import liquibase.FileOpener;
import liquibase.change.custom.CustomSqlChange;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.sql.RawSqlStatement;
import liquibase.database.sql.SqlStatement;
import liquibase.exception.InvalidChangeDefinitionException;
import liquibase.exception.SetupException;
import liquibase.exception.UnsupportedChangeException;

public class AddDeletionProtectionElementChange implements CustomSqlChange {

	private final static String COMMON_PE = "NCIA.Common_Element";
	private final static String COMMON_PG = "NCIA.Common_PG";

    public SqlStatement[] generateStatements(Database database) throws UnsupportedChangeException {
    	List<SqlStatement> statementsToGenerate = new ArrayList<SqlStatement>();

		boolean hasElement = false;
		boolean hasGroup = false;

		DatabaseConnection con = database.getConnection();

		String findPESql = "select protection_element_id "+
		                   "from csm_protection_element "+
		                   "where protection_element_name = '"+ COMMON_PE +"'";
		String findPGSql = "select protection_group_id "+
		                   "from csm_protection_group "+
		                   "where protection_group_name = '"+ COMMON_PG +"'";
		Statement findPeStmt = null;
		ResultSet findPeResult = null;
		Statement findPgStmt = null;
		ResultSet findPgResult = null;
		try
		{
			findPeStmt = con.createStatement();
			findPeResult = findPeStmt.executeQuery(findPESql);
			if (checkResult(findPeResult, "protection_element_id"))
			{
				statementsToGenerate.add(createProtectionElement());
				hasElement = true;
			}
			else
			{
				throw new UnsupportedChangeException("The element has been already added!");
			}
			findPgStmt = con.createStatement();
			findPgResult = findPgStmt.executeQuery(findPGSql);
			if (checkResult(findPgResult, "protection_group_id"))
			{
				statementsToGenerate.add(createProtectGroup());
				hasGroup = true;
			}
			else
			{
				throw new UnsupportedChangeException("The element has been already added!");
			}

			if (hasElement == true && hasGroup == true)
			{
				statementsToGenerate.add(associateElementAndGroup());
			}

			System.out.println("Complete creating Protection Element and Group!");
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally {
			safeClose(findPeResult);
			safeClose(findPgResult);
			safeClose(findPgStmt);
			safeClose(findPeStmt);
		}
		return statementsToGenerate.toArray(new SqlStatement[]{});
    }


    public String getConfirmationMessage() {
        return "AddDeletionProtectionElementChange confirmed.";
    }

    public void setUp() throws SetupException {
    	//nothing to do
    }


    public void setFileOpener(FileOpener fileOpener) {
    	//nothing to do
    }

    public void validate(Database database) throws InvalidChangeDefinitionException {
    	//nothing to do
    }

    //////////////////////////////////////////////PRIVATE/////////////////////////////

    private static void safeClose(ResultSet rs) {
    	try {
    		if(rs!=null) {
    			rs.close();
    		}
    	}
    	catch(Exception ex) {
    		System.out.println("problem closing ResultSet");
    	}
    }

    private static void safeClose(Statement stmt) {
    	try {
    		if(stmt!=null) {
    			stmt.close();
    		}
    	}
    	catch(Exception ex) {
    		System.out.println("problem closing stamtent");
    	}
    }
	private boolean checkResult(ResultSet result,
			                    String columnName) throws Exception {

		if (!result.next()) {
			return true;
		}
		int elementID = result.getInt(columnName);
		return (elementID > 0 ? false : true);
	}

	private SqlStatement associateElementAndGroup() throws Exception	{
		return new RawSqlStatement("insert into csm_pg_pe " +
				                   "select max(pg_pe_id)+1, "+
				                   "(select protection_group_id " +
				                   " from csm_protection_group " +
				                   " where protection_group_name = '" + COMMON_PG + "'), "+
				                   "(select protection_element_id " +
				                   " from csm_protection_element " +
				                   " where protection_element_name = '" + COMMON_PE + "'), "+
				                   "'2009-11-11' " +
				                   "from csm_pg_pe");
	}

	private SqlStatement createProtectGroup() throws Exception {
		return new RawSqlStatement("insert into csm_protection_group "+
		 	                       "select max(protection_group_id)+1, " +
		 	                       "       '" + COMMON_PG +"'," +
		 	                       "       '', "+
		                           "(select application_id " +
		                           " from csm_application " +
		                           " where application_name = 'NCIA'), "+
		                           "       0," +
		                           "       '2009-11-11', " +
		                           "       NULL " +
		                           "from csm_protection_group");
	}


	private SqlStatement createProtectionElement() throws Exception	{
		return new RawSqlStatement("insert into csm_protection_element " +
				                   "select max(protection_element_id)+1, " +
				                   "       '" + COMMON_PE +"', " +
				                   "       '', " +
				                   "       '" + COMMON_PE +"', " +
				                   "       NULL, " +
				                   "       NULL, "+
				                   "(select application_id " +
				                   " from csm_application " +
				                   " where application_name = 'NCIA'), "+
				                   "       '2009-11-11', " +
				                   "       NULL " +
				                   "from csm_protection_element;");
	}
}

