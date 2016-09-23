/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans;

import gov.nih.nci.nbia.beans.security.SecurityBean;
import gov.nih.nci.nbia.deletion.DeletionDisplayObject;
import gov.nih.nci.nbia.deletion.ImageDeletionService;
import gov.nih.nci.nbia.deletion.ImageFileDeletionService;
import gov.nih.nci.nbia.executors.AsynchonousServices;
import gov.nih.nci.nbia.executors.ImageDeletionMessage;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class SeriesDeletionBean {
	private static Logger logger = Logger.getLogger(SeriesDeletionBean.class);
	private List<DeletionDisplayObject> displayObject;
	private boolean showNoSeriesMessage;
	private boolean showNavigationBar;


	//private int totalSeriesAffectPerormance;
	@Autowired
	private ImageDeletionService imageDeletionService;

	@Autowired
	private ImageFileDeletionService imageFileDeletionService;

	public boolean isShowNavigationBar() {
		if (displayObject.size() > 10)
		{
			showNavigationBar = true;
		}
		else
		{
			showNavigationBar = false;
		}
		return showNavigationBar;
	}

	//This method is dedicate for CRON job
	public void removeSeriesFromCron() throws Exception
	{
		String userName = null;

		Map<String, List<String>> files = imageDeletionService.removeSeries(userName);
		//remove all annotation files and dicom files, this must be out of
		//transaction
		if (files == null)
		{
			return;
		}
		imageFileDeletionService.removeImageFiles(files);
	}

	public String removeSeries() throws Exception
	{
		SecurityBean secure = BeanManager.getSecurityBean();
		String userName = secure.getUsername();
		String email = secure.getEmail();

		ImageDeletionMessage izm = new ImageDeletionMessage();
		izm.setEmailAddress(email);
		izm.setUserName(userName);
       
		AsynchonousServices.performImageDeletion(izm);

		 return "confirmDeletion";
	}



	public boolean getShowDeletionLink()
	{
		SecurityBean secure = BeanManager.getSecurityBean();
		boolean deletionRole = secure.getHasDeletionRole();

		return deletionRole;
	}

	public String loadDeletion()
	{
		displayObject = imageDeletionService.getDeletionDisplayObject();
		if (displayObject.size() > 0)
		{
			showNoSeriesMessage = false;
		}
		else
		{
			showNoSeriesMessage = true;
		}
		return "onlineDeletion";
	}

	public List<DeletionDisplayObject> getDisplayObject() {
		return displayObject;
	}

	public void setDisplayObject(List<DeletionDisplayObject> seriesUID) {
		this.displayObject = seriesUID;
	}

	public boolean isShowNoSeriesMessage() {
		return showNoSeriesMessage;
	}

	public int getTotalSeriesAffectPerormance() {
		return displayObject.size();
	}
}
