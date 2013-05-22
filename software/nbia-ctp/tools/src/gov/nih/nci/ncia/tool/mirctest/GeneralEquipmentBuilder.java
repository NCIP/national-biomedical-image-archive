/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: GeneralEquipmentBuilder.java 6364 2008-09-16 19:15:55Z kascice $
*
* $Log: not supported by cvs2svn $
* Revision 1.17  2006/12/19 16:24:42  zhouro
* changed data type from Integer to Double
*
* Revision 1.16  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * Created on Jul 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.ncia.tool.mirctest;

import gov.nih.nci.ncia.db.IDataAccess;
import gov.nih.nci.ncia.domain.GeneralEquipment;
import gov.nih.nci.ncia.util.DicomConstants;

import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author Rona Zhou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GeneralEquipmentBuilder extends DataBuilder  {
    Logger log = Logger.getLogger(GeneralEquipmentBuilder.class);

    public GeneralEquipmentBuilder(IDataAccess ida) throws Exception{
    	super(ida);
    }

	public GeneralEquipment buildEquipment(Hashtable numbers) throws Exception {
		String temp;
		String hql = "from GeneralEquipment as equip where ";
		GeneralEquipment equip = new GeneralEquipment();

		if ((temp = (String) numbers.get("15")) != null) {
			equip.setManufacturer(temp.trim());
			hql += (" equip.manufacturer = '" + temp.trim() + "' ");
		} else {
			hql += " equip.manufacturer is null ";
		}

		if ((temp = (String) numbers.get("16")) != null) {
			equip.setInstitutionName(temp.trim());
			hql += (" and equip.institutionName = '" + temp.trim() + "' ");
		} else {
			hql += " and equip.institutionName is null ";
		}

		if ((temp = (String) numbers.get("23")) != null) {
			equip.setManufacturerModelName(temp.trim());
			hql += (" and equip.manufacturerModelName = '" + temp.trim() + "' ");
		} else {
			hql += " and equip.manufacturerModelName is null ";
		}

		if ((temp = (String) numbers.get("61")) != null) {
			equip.setSoftwareVersions(temp.trim());
			hql += (" and equip.softwareVersions = '" + temp.trim() + "' ");
		} else {
			hql += " and equip.softwareVersions is null ";
		}

		// System.out.println( "Equipment HQL: " + hql );
		equip = (GeneralEquipment) this.update(hql, equip);

		return equip;
	}


	public GeneralEquipment retrieveGeneralEquipmentFromDB(Integer generalEquipmentPkId) throws Exception {
        	
            String hql = "from GeneralEquipment as obj where ";

            if (generalEquipmentPkId != null) {
                hql += (" obj.id = " + generalEquipmentPkId + " ");
            }

            List retList = null;
            retList = (List) getIDataAccess().search(hql);

            GeneralEquipment generalEquipment=null;
            if ((retList != null) && (retList.size() != 0)) {
            	generalEquipment = (GeneralEquipment) retList.get(0);
            } else {
                System.out.println("cannot find the GeneralEquipmentImpl");
            }
            return generalEquipment;
    }
   
    public Hashtable addGeneralEquipment(Hashtable numbersInDb, GeneralEquipment ge) {
    	if (ge==null) {
    		System.out.println("Patient is null");
    		return numbersInDb;
    	}
    	if (ge.getManufacturer()!=null)	{
    		numbersInDb.put(DicomConstants.MANUFACTURER, ge.getManufacturer());
    	}
    	if (ge.getInstitutionName()!=null) {
    		numbersInDb.put(DicomConstants.INSTITUTION_NAME, ge.getInstitutionName());
    	}
    	if (ge.getManufacturerModelName()!=null) {
    		numbersInDb.put(DicomConstants.MANUFACTURER_MODEL_NAME, ge.getManufacturerModelName());
    	}
    	if (ge.getSoftwareVersions()!=null) {
    		numbersInDb.put(DicomConstants.SOFTWARE_VERSIONS, ge.getSoftwareVersions());
    	}

    	return numbersInDb;
    }
    
    
}
