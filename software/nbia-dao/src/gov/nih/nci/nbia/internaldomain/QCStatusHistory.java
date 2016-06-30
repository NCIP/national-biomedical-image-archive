/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.internaldomain;

import java.io.Serializable;
	/**
	* 	**/
public class QCStatusHistory  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


		/**
	* 	**/
	private String comment;
	/**
	* Retreives the value of comment attribute
	* @return comment
	**/

	public String getComment(){
		return comment;
	}

	/**
	* Sets the value of comment attribue
	**/

	public void setComment(String comment){
		this.comment = comment;
	}

		/**
	* 	**/
	private java.util.Date historyTimestamp;
	/**
	* Retreives the value of historyTimestamp attribute
	* @return historyTimestamp
	**/

	public java.util.Date getHistoryTimestamp(){
		return historyTimestamp;
	}

	/**
	* Sets the value of historyTimestamp attribue
	**/

	public void setHistoryTimestamp(java.util.Date historyTimestamp){
		this.historyTimestamp = historyTimestamp;
	}

		/**
	* 	**/
	private Integer id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public Integer getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(Integer id){
		this.id = id;
	}

		/**
	* 	**/
	private String newValue;
	/**
	* Retreives the value of newValue attribute
	* @return newValue
	**/

	public String getNewValue(){
		return newValue;
	}

	/**
	* Sets the value of newValue attribue
	**/

	public void setNewValue(String newValue){
		this.newValue = newValue;
	}

		/**
	* 	**/
	private String oldValue;
	/**
	* Retreives the value of oldValue attribute
	* @return oldValue
	**/

	public String getOldValue(){
		return oldValue;
	}

	/**
	* Sets the value of oldValue attribue
	**/

	public void setOldValue(String oldValue){
		this.oldValue = oldValue;
	}
	
	private String oldBatch;
	public String getOldBatch(){		
		return oldBatch;
	}

	public void setOldBatch(String oldBatch){
		this.oldBatch = oldBatch;
	}
	
    private String newBatch;
	public String getNewBatch(){		
		return newBatch;
	}

	public void setNewBatch(String newBatch){
		this.newBatch = newBatch;
	}

	private String oldSubmissionType;
	public String getOldSubmissionType(){		
		return oldSubmissionType;
	}

	public void setOldSubmissionType(String oldSubmissionType){
		this.oldSubmissionType = oldSubmissionType;
	}
	
	private String newSubmissionType;
	public String getNewSubmissionType(){		
		return newSubmissionType;
	}

	public void setNewSubmissionType(String newSubmissionType){
		this.newSubmissionType = newSubmissionType;
	}
	//----------------------------------------------------------------
	
	private String oldReleasedStatus;
	public String getOldReleasedStatus(){		
		return oldReleasedStatus;
	}

	public void setOldReleasedStatus(String oldReleasedStatus){
		this.oldReleasedStatus = oldReleasedStatus;
	}
	
	private String newReleasedStatus;
	public String getNewReleasedStatus(){		
		return newReleasedStatus;
	}

	public void setNewReleasedStatus(String newReleasedStatus){
		this.newReleasedStatus = newReleasedStatus;
	}
	
	//----------------------------------------------------------------

	/**
	* An associated gov.nih.nci.ncia.security.NCIAUser object
	**/

	private String userId;
	/**
	* Retreives the value of user attribue
	* @return user
	**/

	public String getUserId(){
		return userId;
	}
	/**
	* Sets the value of user attribue
	**/

	public void setUserId(String userId){
		this.userId = userId;
	}

	/**
	* An associated gov.nih.nci.ncia.domain.GeneralImage object
	**/

	private String seriesInstanceUid;
	/**
	* Retreives the value of series attribue
	* @return String
	**/

	public String getSeriesInstanceUid(){
		return seriesInstanceUid;
	}
	/**
	* Sets the value of image attribue
	**/

	public void setSeriesInstanceUid(String seriesInstanceUid){
		this.seriesInstanceUid = seriesInstanceUid;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof QCStatusHistory)
		{
			QCStatusHistory c =(QCStatusHistory)obj;
			if(getId() != null && getId().equals(c.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null) {
			return getId().hashCode();
		}
		return 0;
	}

}