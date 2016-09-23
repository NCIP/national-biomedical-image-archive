/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.5  2007/12/28 21:16:56  bauerd
* modifications required to handle a collection across the grid
*
* Revision 1.4  2007/10/01 12:24:57  bauerd
* *** empty log message ***
*
* Revision 1.3  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.2  2007/08/29 19:11:19  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.12  2006/11/15 15:40:06  shinohaa
* grid prototype
*
* Revision 1.11  2006/11/10 13:58:15  shinohaa
* grid prototype
*
* Revision 1.10  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


/**
 * Represents a Study for data transfer purposes
 *
 * @author dietrichj
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyDTO implements Comparable<StudyDTO>  {
    
    private String studyId;
    private Date date;
    private String description;
    private Integer id;

    // A filtered list of series that belong to this study
    private List<SeriesDTO> seriesList = new ArrayList<SeriesDTO>();

    public StudyDTO() {
    }

    public void setId(Integer i) {
        id = i;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SeriesDTO> getSeriesList() {        
        return seriesList;        
    }
    

    public void setSeriesList(List<SeriesDTO> seriesList) {
        this.seriesList = seriesList;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    /**
     * Sort the studies by date.
     */
    public int compareTo(StudyDTO o) {
        // Studies should be ordered according to the date
        if ((this.getDate() == null) || (o.getDate() == null)) {
            return 0;
        }

        return this.getDate().compareTo(o.getDate());
    }
}