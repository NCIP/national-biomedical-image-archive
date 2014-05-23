/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.workflowsupport;

import java.util.*;

import org.hibernate.SessionFactory;

public interface WorkflowSupportDAO  {


  public SessionFactory getSessionFactory();
  public List<WorkflowVisibilityUpdateDTO> getVisibilityUpdated(Date high, Date low);
  public List<WorkflowNewSeriesDTO> getNewSeries(Date high, Date low);

}
