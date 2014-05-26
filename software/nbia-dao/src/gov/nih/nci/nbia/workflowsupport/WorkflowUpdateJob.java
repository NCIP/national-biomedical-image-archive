/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */


package gov.nih.nci.nbia.workflowsupport;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;


public class WorkflowUpdateJob implements Job {
	static Logger log = Logger.getLogger(WorkflowUpdateJob.class);
    /**
     * This method allows this class to be called as a Job using Quartz
     */
    public void execute(JobExecutionContext jec) {

        try {   
        	// 	Cache the latest date
        	log.warn("Workflow Executor job called by Quartz");
        	WorkflowExecutor updater=new WorkflowExecutor();
        	updater.runUpdates();
        	log.warn("Workflow Executor job complete");
        }
        catch(Exception ex) {
        	throw new RuntimeException(ex);
        }
    }
    

}
