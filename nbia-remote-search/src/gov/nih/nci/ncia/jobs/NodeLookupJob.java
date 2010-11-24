package gov.nih.nci.ncia.jobs;

import gov.nih.nci.ncia.remotesearch.RemoteNodes;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import gov.nih.nci.ncia.util.NCIAConfig;

/**
 * This job is responsible for doing a discovery of remote nodes from the
 * index server every so often (typically 4 hrs but is configurable).
 */
public class NodeLookupJob implements Job {
    
    /**
     * This method allows this class to be called as a Job using Quartz
     */
    public void execute(JobExecutionContext jec) throws JobExecutionException {
    	try {
	    	if(!NCIAConfig.getDiscoverRemoteNodes().equals("true")) {
	    		return;
	    	}
	    	
	    	RemoteNodes.getInstance().discoverNodes();
    	}
    	catch(Exception ex) {
    		throw new JobExecutionException(ex);
    	}
    }
}