/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;

import java.util.List;

import org.hibernate.SessionFactory;

public interface TextSupportDAO  {


  public SessionFactory getSessionFactory();
  public String getMaxTimeStamp();
  public List<Object> getPatientsForCollection(String collection);
  public List<Object> getUpdatedPatients(String high, String low);
  public List<Object> getPatients(String patientId);
  public List<Object> getCollectionDesc(String collection);
  public List<Object> getAnnotationFiles(Integer seriesPK);

}
