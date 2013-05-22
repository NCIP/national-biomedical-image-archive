/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.xmlobject;

import java.util.List;

public class Relation 
{
	private List<Element> element;
	
	public List<Element> getElement()
	{
		return element;
	}
	public void setElement(List<Element> elements)
	{
		element = elements;
	}
}
