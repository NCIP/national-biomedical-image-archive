/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;

import java.util.*;

import org.hibernate.SessionFactory;

public interface PatientAccessDAO  {


  public SessionFactory getSessionFactory();
  public PatientDocument getPatientDocument(String patientId);

}
