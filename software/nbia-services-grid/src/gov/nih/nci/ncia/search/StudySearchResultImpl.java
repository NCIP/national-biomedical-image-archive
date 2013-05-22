/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.search;

import java.util.Date;

/**
 * Represents a study search result (drill down from a patient search result)
 * 
 * <P>Lines up with a StudyDTO, but with extra stuff not found in db.
 * 
 * <p>Note: this class has a natural ordering that is inconsistent with equals.
 */
public class StudySearchResultImpl implements StudySearchResult {
    
    public StudySearchResultImpl() {
    }

    /**
     * Set the (pk) id for this study.
     */
    public void setId(Integer i) {
        id = i;
    }

    /**
     * {@inheritDoc}
     */
    public Integer getId() {
        return id;
    }

    
    /**
     * {@inheritDoc}
     */    
    public Date getDate() {
        return date;
    }


    /**
     * Set the date for this study.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    
    /**
     * {@inheritDoc}
     */    
    public String getDescription() {
        return description;
    }

    
    /**
     * Set the study description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
    /**
     * {@inheritDoc}
     */    
    public SeriesSearchResult[] getSeriesList() {       
        return seriesList;        
    }
    
    
    /**
     * Set the series results that belong to this study result.
     */
    public void setSeriesList(SeriesSearchResult[] seriesList) {
        this.seriesList = seriesList;
    }
    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     */     
    public SeriesSearchResult getSeriesList(int i) {
        return this.seriesList[i];
    }

    
    /**
     * This is necessary for the web services serializer to recognize
     * this property is an indexed property.
     * 
     */     
    public void setSeriesList(int i, SeriesSearchResult _value) {
        this.seriesList[i] = _value;
    }
    
    
    /**
     * {@inheritDoc}
     */    
    public String getStudyInstanceUid() {
        return studyInstanceUid;
    }

    
    /**
     * Set the DICOM study instance uid for this result.
     */
    public void setStudyInstanceUid(String studyId) {
        this.studyInstanceUid = studyId;
    }

    /**
     * Sort the studies by study date in ascending order.
     */
    public int compareTo(StudySearchResult o) {
        if (this.getDate() == null) {
            return -1;
        }

        if (o.getDate() == null) {
            return 1;
        }        

        return this.getDate().compareTo(o.getDate());
    }

    /**
     * {@inheritDoc}
     */
	public String getOffSetDesc() {
		return offSetDesc;
	}


	public void setOffSetDesc(String offSetDesc) {
		this.offSetDesc = offSetDesc;
	}
	
    /**
     * {@inheritDoc}
     */
	public NBIANode associatedLocation() {
		return location;
	}

	
    /**
     * {@inheritDoc}
     */	
	public void associateLocation(NBIANode location) {
		this.location = location;
		
		if(seriesList!=null) {
			for(SeriesSearchResult result : seriesList) {
				result.associateLocation(location);
			}
		}
	}

	///////////////////////////////////////PRIVATE/////////////////////////////////////////
	
    private String studyInstanceUid;
    private Date date;
    private String description;
    private Integer id;
    private String offSetDesc=null;

    // A filtered list of series that belong to this study
    private SeriesSearchResult[] seriesList;	
    private NBIANode location;
	
}
