/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.exception;

public class NciaException extends Exception {
	

		  public NciaException() {
		    super();
		  }
		  
		  public NciaException(String message) {
		    super(message);
		  }
		  
		  public NciaException(String message, Throwable cause) {
		    super(message, cause);
		  }
	


}
