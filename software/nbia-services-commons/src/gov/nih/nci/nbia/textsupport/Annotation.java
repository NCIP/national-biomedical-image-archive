/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.textsupport;


import java.io.File;
import java.io.Serializable;
	/**
	* A non-image file associated with a series that provides additional information about the images in the series.   Examples of annotation files are image markup and radiologist reports.	**/
public class Annotation  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


		/**
	* The file extension of the annotation file.  For example, xml or zip.	**/
	private String annotationType;
	/**
	* Retreives the value of annotationType attribute
	* @return annotationType
	**/

	public String getAnnotationType(){
		return annotationType;
	}

	/**
	* Sets the value of annotationType attribue
	**/

	public void setAnnotationType(String annotationType){
		this.annotationType = annotationType;
	}

		/**
	* The location (full path and file name) of the annotation file on the file system.	**/
	private String filePath;
	/**
	* Retreives the value of filePath attribute
	* @return filePath
	**/

	public String getFilePath(){
		return filePath;
	}

	/**
	* Sets the value of filePath attribue
	**/

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}


	/**
     * Return only the file name
     */
    public String getFileName() {
        if ((filePath != null) && !filePath.equals("") ) {
            //if (filePath.lastIndexOf(File.separatorChar) > -1) {
        	File f = new File(filePath);
        	String fileName = f.getName();
            return fileName;
        }

        return null;
    }

		/**
	* The size (in bytes) of the annotation file	**/
	private Integer fileSize;
	/**
	* Retreives the value of fileSize attribute
	* @return fileSize
	**/

	public Integer getFileSize(){
		return fileSize;
	}

	/**
	* Sets the value of fileSize attribue
	**/

	public void setFileSize(Integer fileSize){
		this.fileSize = fileSize;
	}

		/**
	* Primary key of the series to which this annotation file is associated	**/
	private Integer generalSeriesPkId;
	/**
	* Retreives the value of generalSeriesPkId attribute
	* @return generalSeriesPkId
	**/

	public Integer getGeneralSeriesPkId(){
		return generalSeriesPkId;
	}

	/**
	* Sets the value of generalSeriesPkId attribue
	**/

	public void setGeneralSeriesPkId(Integer generalSeriesPkId){
		this.generalSeriesPkId = generalSeriesPkId;
	}

		/**
	* One or more characters used to identify, name, or characterize the nature, properties, or contents of a thing.	**/
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
	* Unique identifer of a study series.	**/
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
	* The date on which the annotation file was submitted.	**/
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
	* An associated gov.nih.nci.ncia.domain.GeneralSeries object
	**/

	private GeneralSeriesSubDoc generalSeries;
	/**
	* Retreives the value of generalSeries attribue
	* @return generalSeries
	**/

	public GeneralSeriesSubDoc getGeneralSeries(){
		return generalSeries;
	}
	/**
	* Sets the value of generalSeries attribue
	**/

	public void setGeneralSeries(GeneralSeriesSubDoc generalSeries){
		this.generalSeries = generalSeries;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof Annotation)
		{
			Annotation c =(Annotation)obj;
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