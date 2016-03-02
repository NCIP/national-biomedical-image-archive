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
* Revision 1.1  2007/08/07 19:20:07  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.6  2007/02/09 10:48:53  bauerd
* *** empty log message ***
*
* Revision 1.5  2007/02/09 09:20:38  bauerd
* *** empty log message ***
*
* Revision 1.4  2007/01/11 22:44:28  dietrich
* Defect 174
*
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.servlet;



import java.util.Date;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import gov.nih.nci.nbia.dicomapi.QueryRetrieve;

public class DICOMServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DICOMServlet.class);
    /**
     * Run upon initialization of the servlet
     *
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() {

    	QueryRetrieve qr = new QueryRetrieve();
    	logger.info("starting query retrieve servlet");
    	qr.doStartService();
    }

}
