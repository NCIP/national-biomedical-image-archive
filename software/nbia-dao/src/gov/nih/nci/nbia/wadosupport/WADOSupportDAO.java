/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.wadosupport;

import gov.nih.nci.nbia.dicomapi.DICOMParameters;
import gov.nih.nci.nbia.dicomapi.DICOMSupportDTO;

import java.util.*;

import org.hibernate.SessionFactory;

public interface WADOSupportDAO  {


  public SessionFactory getSessionFactory();
  public WADOSupportDTO getWADOSupportDTO(String study, String series, String image, String user);
  public WADOSupportDTO getWADOSupportDTO(String study, String series, String image);
  public WADOSupportDTO getWADOSupportDTO(WADOParameters params, String user);
  public WADOSupportDTO getOviyamWADOSupportDTO(String image, String contentType, String user);
  public List <DICOMSupportDTO> getDICOMSupportDTO(DICOMParameters params, List<String> extraFields);
}
