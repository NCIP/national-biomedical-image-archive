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
public class ImageMarkup  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	private GeneralSeries series;


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
	private String loginName;
	/**
	* Retreives the value of loginName attribute
	* @return loginName
	**/

	public String getLoginName(){
		return loginName;
	}

	/**
	* Sets the value of loginName attribue
	**/

	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

		/**
	* 	**/
	private String markupContent;
	/**
	* Retreives the value of markupContent attribute
	* @return markupContent
	**/

	public String getMarkupContent(){
		return markupContent;
	}

	/**
	* Sets the value of markupContent attribue
	**/

	public void setMarkupContent(String markupContent){
		this.markupContent = markupContent;
	}

		/**
	* 	**/
	private String seriesInstanceUID;
	/**
	* Retreives the value of seriesInstanceUID attribute
	* @return seriesInstanceUID
	**/

	public String getSeriesInstanceUID(){
		return seriesInstanceUID;
	}

	/**
	* Sets the value of seriesInstanceUID attribue
	**/

	public void setSeriesInstanceUID(String seriesInstanceUID){
		this.seriesInstanceUID = seriesInstanceUID;
	}

		/**
	* 	**/
	private java.util.Date submissionDate;
	/**
	* Retreives the value of submissionDate attribute
	* @return submissionDate
	**/

	public java.util.Date getSubmissionDate(){
		return submissionDate;
	}

	/**
	* Sets the value of submissionDate attribue
	**/

	public void setSubmissionDate(java.util.Date submissionDate){
		this.submissionDate = submissionDate;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof ImageMarkup)
		{
			ImageMarkup c =(ImageMarkup)obj;
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

	public GeneralSeries getSeries() {
		return series;
	}

	public void setSeries(GeneralSeries series) {
		this.series = series;
	}

}