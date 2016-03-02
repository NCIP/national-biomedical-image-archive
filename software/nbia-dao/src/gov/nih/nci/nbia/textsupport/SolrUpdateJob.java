/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */


package gov.nih.nci.nbia.textsupport;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;


public class SolrUpdateJob implements Job {
	static Logger log = Logger.getLogger(SolrUpdateJob.class);
    /**
     * This method allows this class to be called as a Job using Quartz
     */
    public void execute(JobExecutionContext jec) {

        try {   
        	// 	Cache the latest date
        	log.warn("Patient updater job called by Quartz");
        	PatientUpdater updater=new PatientUpdater();
        	updater.runUpdates();
        	log.warn("Patient updater job complete");
        }
        catch(Exception ex) {
        	throw new RuntimeException(ex);
        }
    }
    

}
