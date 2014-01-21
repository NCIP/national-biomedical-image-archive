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

public interface TextSupportDAO  {


  public SessionFactory getSessionFactory();
  public Date getMaxTimeStamp();
  public List<Object> getPatientsForCollection(String collection);
  public List<Object> getUpdatedPatients(Date high, Date low);
  public List<Object> getVisibilityUpdatedPatients(Date high, Date low);
  public List<Object> getDeletedPatients(Date high, Date low);
  public List<Object> getDeletedSeriesPatients(Date high, Date low);
  public List<Object> getDeletedStudyPatients(Date high, Date low);
  public List<Object> getPatients(String patientId);
  public List<Object> getCollectionDesc(String collection);
  public List<Object> getAnnotationFiles(Integer seriesPK);

}
