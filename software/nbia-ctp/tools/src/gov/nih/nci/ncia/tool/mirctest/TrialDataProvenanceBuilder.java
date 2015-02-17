/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * $Id: TrialDataProvenanceBuilder.java 6367 2008-09-16 19:16:36Z kascice $
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
import gov.nih.nci.ncia.domain.TrialDataProvenance;
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
public class TrialDataProvenanceBuilder extends DataBuilder {
	Logger log = Logger.getLogger(TrialDataProvenanceBuilder.class);

	public TrialDataProvenanceBuilder(IDataAccess ida) throws Exception {
		super(ida);
	}

	public TrialDataProvenance buildTrialDataProvenance(Hashtable numbers)
			throws Exception {
		// TODO Auto-generated method stub
		String temp;
		String hql = "from TrialDataProvenance as tdp where ";

		TrialDataProvenance tdp = new TrialDataProvenance();

		if ((temp = (String) numbers.get("106")) != null) {
			hql += ("lower(tdp.project) = '" + temp.trim().toLowerCase() + "' and ");
			tdp.setProject(temp.trim());
		} else {
			hql += "tdp.project is null and ";
		}

		if ((temp = (String) numbers.get("109")) != null) {
			hql += ("tdp.dpSiteId = '" + temp.trim() + "' and ");
			tdp.setDpSiteId(temp.trim());
		} else {
			hql += "tdp.dpSiteId is null and ";
		}

		if ((temp = (String) numbers.get("108")) != null) {
			hql += ("lower(tdp.dpSiteName) = '" + temp.trim().toLowerCase() + "' ");
			tdp.setDpSiteName(temp.trim());
		} else {
			hql += "tdp.dpSiteName is null ";
		}

		tdp = (TrialDataProvenance) this.update(hql, tdp);

		return tdp;
	}

	public TrialDataProvenance retrieveTrialDataProvenanceFromDB(Integer trialDpPkId)
			throws Exception {

		String hql = "from TrialDataProvenance as obj where ";

		if (trialDpPkId != null) {
			hql += (" obj.id = " + trialDpPkId + " ");
		}

		List retList = null;
		retList = (List) getIDataAccess().search(hql);

		TrialDataProvenance tdp = null;
		if ((retList != null) && (retList.size() != 0)) {
			tdp = (TrialDataProvenance) retList.get(0);
		} else {
			System.out.println("cannot find the trialDataProvenance");
		}
		return tdp;
	}

	public Hashtable addTrialDataProvenance(Hashtable numbersInDb,
			TrialDataProvenance tdp) {
		if (tdp == null) {
			System.out.println("TrialDataProvenance is null");
			return numbersInDb;
		}
		if (tdp.getProject() != null) {
			numbersInDb.put(DicomConstants.PROJECT_NAME, tdp.getProject());
		}
		if (tdp.getDpSiteId() != null) {
			numbersInDb.put(DicomConstants.SITE_ID, tdp.getDpSiteId());
		}
		if (tdp.getDpSiteName() != null) {
			numbersInDb.put(DicomConstants.SITE_NAME, tdp.getDpSiteName());
		}

		return numbersInDb;
	}

}
