/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.database.*;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.*;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.*;

/**
 * Methods that are mostly NCIA specific to assist in
 * DbUnit testing.
 */
public class DbUnitUtil {

	/**
	 * Edit this main to do an export with desired parms
	 */
    public static void main(String[] args) throws Exception{
       Class.forName("com.mysql.jdbc.Driver");
       Connection jdbcConnection = null;
       try {
    	   jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nciadev",
                                                        "nciaAdmin",
                                                        "nciA#112");
       }
       catch (SQLException se) {
    	   se.printStackTrace();
    	   System.exit(1);
  	   }
       IDatabaseConnection conn = new DatabaseConnection(jdbcConnection);

       FlatDtdDataSet.write(conn.createDataSet(), new FileOutputStream("test.dtd"));

       //DbUnitUtil.exportPatient(conn, 112918531, new File("test.xml"));
       //DbUnitUtil.exportCsmUser(conn, "kascice", new File("csm.xml"));       
       //DbUnitUtil.exportAll(conn, new File("c://temp//PublicTestData.xml"));
       //DbUnitUtil.exportCollectionDescription(conn, "lethai", new File("collection_desc.xml"));
       //DbUnitUtil.exportCustomSeriesList(conn, "lethai", new File("custom_series_list.xml"));
       conn.close();


    }

    public static void exportCsmUser(IDatabaseConnection conn,
                                     String loginName,
                                     File exportFile) throws SQLException,
                                                             IOException,
                                                             DataSetException {
        QueryDataSet dataset = new QueryDataSet(conn);
        dataset.addTable("csm_application");
        dataset.addTable("csm_protection_group");
        //watch out ATTRIBUTE in caps doesnt work on MySQL?  must change to lower case!??
        dataset.addTable("csm_protection_element");
        dataset.addTable("csm_pg_pe",
                         "select pg_pe_id, protection_group_id, protection_element_id from csm_pg_pe");
        dataset.addTable("csm_role");
        dataset.addTable("csm_privilege");
        dataset.addTable("csm_role_privilege");
        dataset.addTable("csm_group");

        dataset.addTable("csm_user",
                         "select * from csm_user where login_name = '"+loginName+"'");
        ITable csmUser = dataset.getTable("csm_user");
        Long userId = (Long)csmUser.getValue(0, "user_id");

        dataset.addTable("csm_user_group",
        		         "select * from csm_user_group where user_id = "+userId);
        ITable csmUserGroup = dataset.getTable("csm_user_group");

        dataset.addTable("csm_user_pe",
                         "select * from csm_user_pe where user_id = "+userId);

        dataset.addTable("csm_user_group_role_pg",
                         "select * from csm_user_group_role_pg where user_id = "+userId);

        for(int i=0;i<csmUserGroup.getRowCount();i++) {
        	Long groupId = (Long)csmUserGroup.getValue(i, "group_id");
	        dataset.addTable("csm_user_group_role_pg",
	                         "select * from csm_user_group_role_pg where group_id="+groupId);
        }

        FlatXmlDataSet.write(dataset,
                             new FileOutputStream(exportFile));
    }

    public static void exportPatient(IDatabaseConnection conn,
                                     Integer patientPkId,
                                     File exportFile) throws SQLException,
                                                             IOException,
                                                             DataSetException {

        QueryDataSet dataset = new QueryDataSet(conn);
        dataset.addTable("trial_data_provenance");
        dataset.addTable("trial_site");
        dataset.addTable("general_equipment");
        dataset.addTable("patient",
                         "select * from patient where patient_pk_id = "+patientPkId);
        dataset.addTable("study",
                         "select * from study where patient_pk_id = "+patientPkId);
        dataset.addTable("general_series",
                         "select * from general_series where patient_pk_id = "+patientPkId);
        ITable generalSeries = dataset.getTable("general_series");
        for(int i=0;i<generalSeries.getRowCount();i++) {
            Long generalSeriesPkId = (Long)generalSeries.getValue(i, "general_series_pk_id");
            dataset.addTable("annotation",
                             "select * from annotation where general_series_pk_id = "+generalSeriesPkId);

        }

        dataset.addTable("general_image",
                         "select * from general_image where patient_pk_id = "+patientPkId);

        ITable generalImage = dataset.getTable("general_image");
        for(int i=0;i<generalImage.getRowCount();i++) {
            Long imagePkId = (Long)generalImage.getValue(i, "image_pk_id");
            dataset.addTable("ct_image",
                             "select * from ct_image where image_pk_id = "+imagePkId);
        }

        dataset.addTable("curation_data",
                         "select * from curation_data where patient_pk_id = "+patientPkId);


        FlatXmlDataSet.write(dataset,
                             new FileOutputStream(exportFile));
    }

    public static void exportAll(IDatabaseConnection conn, File exportFile) throws SQLException,
                                                             IOException,
                                                             DataSetException
   {
	   QueryDataSet dataset = new QueryDataSet(conn);
	   dataset.addTable("annotation");
	   dataset.addTable("clinical_trial");
	   dataset.addTable("clinical_trial_protocol");
	   dataset.addTable("clinical_trial_sponsor");
	   dataset.addTable("clinical_trial_subject");
	   dataset.addTable("csm_application");
	   dataset.addTable("csm_filter_clause");
	   dataset.addTable("csm_group");
	   dataset.addTable("csm_pg_pe");
	   dataset.addTable("csm_privilege");
	   dataset.addTable("csm_protection_element");
	   dataset.addTable("csm_protection_group");
	   dataset.addTable("csm_role");
	   dataset.addTable("csm_role_privilege");
	   dataset.addTable("csm_user");
	   dataset.addTable("csm_user_group");
	   dataset.addTable("csm_user_group_role_pg");
	   dataset.addTable("csm_user_pe");
	   dataset.addTable("ct_image");
	   dataset.addTable("curation_data");
       dataset.addTable("curation_status");
       dataset.addTable("general_equipment");
       dataset.addTable("general_image");
       dataset.addTable("general_series");
       dataset.addTable("patient");
       dataset.addTable("study");
       dataset.addTable("trial_data_provenance");
       dataset.addTable("trial_site");

       FlatXmlDataSet.write(dataset,
                             new FileOutputStream(exportFile));
   }
    public static void exportCustomSeriesList(IDatabaseConnection conn,
            String loginName,
            File exportFile) throws SQLException, 
                                    IOException,
                                    DataSetException {
		QueryDataSet dataset = new QueryDataSet(conn);
		dataset.addTable("custom_series_list");
		dataset.addTable("custom_series_list_attribute");
		
		
		FlatXmlDataSet.write(dataset, 
		    new FileOutputStream(exportFile));                
	}
    
    
    public static void exportCollectionDescription(IDatabaseConnection conn,
            String loginName,
            File exportFile) throws SQLException, 
                                    IOException,
                                    DataSetException {
		QueryDataSet dataset = new QueryDataSet(conn);
		dataset.addTable("collection_descriptions");
		
		
		FlatXmlDataSet.write(dataset, 
		    new FileOutputStream(exportFile));                
	}

}
