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
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.5  2007/01/11 22:44:28  dietrich
* Defect 174
*
* Revision 1.4  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.newresults;

import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.factories.ApplicationFactory;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;


public class LatestCurationDateJob implements Job {
    
    /**
     * This method allows this class to be called as a Job using Quartz
     */
    public void execute(JobExecutionContext jec) {

        try {   
        	// 	Cache the latest date
        	ApplicationFactory.getInstance().setLatestCurationDate(getLatestCurationDate());
        }
        catch(Exception ex) {
        	throw new RuntimeException(ex);
        }
    }
    
    /**
     * Retrieves the maximum curation status date
     *
     * @throws Exception
     */
    public Date getLatestCurationDate() throws Exception {       
        ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
        return imageDAO.findLastCurationDate();
    }
}
