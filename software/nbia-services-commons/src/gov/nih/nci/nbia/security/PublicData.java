/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.security;

import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dao.PatientDAO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.PatientDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.util.List;

public class PublicData {
	private AuthorizationManager authorizationManager;

	public boolean checkPublicPatient(Integer pid)
	{
		if(authorizationManager==null) {
			throw new RuntimeException("must call setAuthMgr before invoking this method checkPublicXXX");
		}
		boolean isPublic = false;
		PatientDAO pDao = (PatientDAO)SpringApplicationContext.getBean("patientDAO");

		PatientDTO patientDto = pDao.getPatientById(pid);
		if (patientDto == null)
		{
			throw new RuntimeException("Cannot find Patient in PublicData class");
		}
		List<SiteData> siteData = authorizationManager.getAuthorizedSites();
		for (SiteData site : siteData)
		{
			if (site.getCollection().equals(patientDto.getProject())
					&& site.getSiteName().equals(patientDto.getSiteName()))
			{
				isPublic = true;
				break;
			}
		}

		return isPublic;
	}

	public boolean checkPublicSeries(Integer seriesId)
	{
		if(authorizationManager==null) {
			throw new RuntimeException("must call setAuthMgr before invoking this method checkPublicXXX");
		}

		boolean isPublic = false;

		GeneralSeriesDAO gsDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
		SeriesDTO seriesDto = gsDao.getGeneralSeriesByPKid(seriesId);
		if (seriesDto == null)
		{
			throw new RuntimeException("Cannot find Series in PublicData class");
		}
		List<SiteData> siteData = authorizationManager.getAuthorizedSites();
		for (SiteData site : siteData)
		{
			if (site.getCollection().equals(seriesDto.getProject()) &&
					site.getSiteName().equals(seriesDto.getDataProvenanceSiteName()))
			{
				isPublic = true;
				break;
			}
		}

		return isPublic;
	}

	public boolean checkPublicImage(Integer imagePkId)
	{
		if(authorizationManager==null) {
			throw new RuntimeException("must call setAuthMgr before invoking this method checkPublicXXX");
		}

		boolean isPublic = false;

		ImageDAO imageDao =(ImageDAO)SpringApplicationContext.getBean("imageDAO");
		ImageDTO dto = imageDao.getGeneralImageByImagePkId(imagePkId);
		if(dto == null)
		{
			throw new RuntimeException("Cannot find image in PublicData class");
		}

		List<SiteData> siteData = authorizationManager.getAuthorizedSites();
		for (SiteData site : siteData)
		{
			if (site.getCollection().equals(dto.getProject()) &&
					site.getSiteName().equals(dto.getSiteName()))
			{
				isPublic = true;
				break;
			}
		}

		return isPublic;
	}

	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

}
