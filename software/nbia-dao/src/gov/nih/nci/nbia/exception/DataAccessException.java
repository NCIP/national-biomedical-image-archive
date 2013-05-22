/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.exception;

public class DataAccessException extends RuntimeException {

  public DataAccessException() {
    super();
  }
  
  public DataAccessException(String message) {
    super(message);
  }
  
  public DataAccessException(String message, Throwable cause) {
    super(message, cause);
  }
}
