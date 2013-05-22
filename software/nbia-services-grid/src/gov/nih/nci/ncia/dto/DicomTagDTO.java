/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: DicomTagDTO.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2007/09/27 23:30:39  bauerd
* This is the checked in seperation of dependent classes betweeen the grid and the commons module...
*
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.3  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.dto;

/**
 * This class holds information so that the UI can display all of
 * the tag information for a dicom header.
 *
 * <P>BEWARE - this is serialized over the grid, so any changes
 * to this object can break remote search!!!!
 * 
 * @author NCIA Team
 */
public class DicomTagDTO {

    /**
     * Default constructor for grid.
     */
    public DicomTagDTO() {    	
    }
    
    public DicomTagDTO(String el, String name, String data) {
        this.element = el;
        this.name = name;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //////////////////////////////////////PRIVATE/////////////////////////
    private String element;
    private String name;
    private String data;    
}
