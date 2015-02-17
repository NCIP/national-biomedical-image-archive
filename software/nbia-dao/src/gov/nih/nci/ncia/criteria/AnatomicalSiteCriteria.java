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
* Revision 1.2  2007/08/14 16:53:47  bauerd
* Removed the repopulate logic and cleaned up the class files
*
* Revision 1.1  2007/08/07 12:05:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.13  2006/12/13 14:04:14  dietrich
* Grid enhancement
*
* Revision 1.12  2006/10/10 18:48:16  shinohaa
* 2.1 enhancements
*
* Revision 1.11  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 *
 *
 */
package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * @author Prashant Shah - NCICB/SAIC
 *
 *
 *
 */
public class AnatomicalSiteCriteria extends MultipleValuePersistentCriteria
    implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> anatomicalSiteValueObjects;

    /**
     */
    public AnatomicalSiteCriteria() {
    }

    /**
     * @param anatomicalSiteValue
     */
    public AnatomicalSiteCriteria(List anatomicalSiteValue) {
        setAnatomicalSiteValueObjects(anatomicalSiteValue);
    }

    /**
     * @return Returns the anatomicalSiteValue.
     */
    public List<String> getAnatomicalSiteValueObjects() {
        return anatomicalSiteValueObjects;
    }

    public void setAnatomicalSiteValueObjects(Collection anatomicalObjects) {
        for (Iterator iter = anatomicalObjects.iterator(); iter.hasNext();) {
            Object thisObj = iter.next();

            if (thisObj instanceof String) {
                getCreateAnatomicalSiteObjects().add((String) thisObj);
            }
        }
    }
    
    public void setAnatomicalSiteValue(String anatomicalValue) {
        getCreateAnatomicalSiteObjects().add((String) anatomicalValue);
    }    

   private Collection<String> getCreateAnatomicalSiteObjects() {
        if (anatomicalSiteValueObjects == null) {
            anatomicalSiteValueObjects = new ArrayList<String>();
        }

        return anatomicalSiteValueObjects;
    }

    @Override
    protected Collection<String> getMultipleValues() {
        return anatomicalSiteValueObjects;
    }

    @Override
    public void addValueFromQueryAttribute(QueryAttributeWrapper attribute) {
        setAnatomicalSiteValue(attribute.getAttributeValue());
        
    }
}
