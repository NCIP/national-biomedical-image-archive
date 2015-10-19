/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2007/08/10 20:09:54  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:06:53  bauerd
* Initial Check in of reorganized components
*
* Revision 1.11  2006/12/18 01:16:03  dietrich
* Grid enhancement
*
* Revision 1.10  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.9  2006/11/10 13:59:04  shinohaa
* grid prototype
*
* Revision 1.8  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.newresults;

import gov.nih.nci.nbia.dto.SavedQueryDTO;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.querystorage.QueryStorageManager;
import gov.nih.nci.nbia.search.PatientSearcher;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.util.SavedQueryReconstructor;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.criteria.Criteria;
import gov.nih.nci.ncia.criteria.CurationStatusDateCriteria;
import gov.nih.nci.ncia.criteria.PersistentCriteria;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;


public class NewResultsProcessor implements Job {

    public void execute(JobExecutionContext jec) {
        QueryStorageManager qsm = (QueryStorageManager)SpringApplicationContext.getBean("queryStorageManager");

        List<SavedQueryDTO> activeQueries = null;

        try {
            activeQueries = qsm.getListOfActiveNoNew();
        } catch (Exception e) {
        	System.out.println("Problem with new data results processor");
        	e.printStackTrace();
        }

        if (activeQueries != null) {

            for (SavedQueryDTO dto : activeQueries) {
                List<Criteria> kriteria = dto.getCriteriaList();
                DICOMQuery newQuery =  new DICOMQuery();

                if (!dto.getQueryName().equals("")) {
                    newQuery.setQueryName(dto.getQueryName());
                }

                newQuery.setSavedQueryId(dto.getId());

                // Only repopulates search fields for persistent criteria
                // because
                // other
                // criteria have dependent values.
                for (Criteria kritItem : kriteria) {
                    if (kritItem instanceof PersistentCriteria) {
                        PersistentCriteria persistCrit = (PersistentCriteria) kritItem;
                        SavedQueryReconstructor.repopulatePersistantCriteria(persistCrit, null, newQuery);
                    }
                }

                // Setup date criteria here for new results
                CurationStatusDateCriteria csd = new CurationStatusDateCriteria();
                csd.setCurationStatusDate(dto.getExecutionTime());
                newQuery.setCriteria(csd);

                try {
                    // Add authorization criteria here
                    AuthorizationManager am = new AuthorizationManager(dto.getUserId());
                    am.authorizeCollections(newQuery);
                    am.authorizeSitesAndSSGs(newQuery);
                    boolean newResults = doResultsExist(newQuery);

                    if (newResults) {
                        qsm.addNewResultsForQuery(newQuery.getSavedQueryId());
                    }
                }
                catch (Exception e) {
                	System.out.println("Problem re-executing query");
                	e.printStackTrace();
                }
            }
        }
    }


    private static boolean doResultsExist(DICOMQuery newQuery) {


       	PatientSearcher patientSearcher = new PatientSearcher();
    	try {
           List results = patientSearcher.searchForPatients(newQuery);

 		   if(results!=null && results.size()>0) {
					return true;
				}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
    }
}
