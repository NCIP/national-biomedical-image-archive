package gov.nih.nci.ncia.beans;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import gov.nih.nci.ncia.beans.security.SecurityBean;
import gov.nih.nci.ncia.deletion.DeletionDisplayObject;
import gov.nih.nci.ncia.deletion.ImageDeletionService;
import gov.nih.nci.ncia.deletion.ImageFileDeletionService;

public class SeriesDeletionBean {
	
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
		Map<String, List<String>> files = imageDeletionService.removeSeries(userName);
		//remove all annotation files and dicom files, this must be out of 
		//transaction
		imageFileDeletionService.removeImageFiles(files);
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
