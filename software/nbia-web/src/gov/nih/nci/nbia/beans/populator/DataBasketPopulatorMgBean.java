/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.populator;

import gov.nih.nci.nbia.basket.ExternalDataBasket;
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.ncia.search.SeriesSearchResult;

import java.util.Arrays;
import java.util.List;


public class DataBasketPopulatorMgBean extends AbstractPopulatorMgBean {

	public String[] getPatients() {
		return patients;
	}

	public void setPatients(String[] patients) {
		this.patients = patients;
	}

	public String[] getStudies() {
		return studies;
	}

	public void setStudies(String[] studies) {
		this.studies = studies;
	}

	public String[] getSerieses() {
		return serieses;
	}

	public void setSerieses(String[] serieses) {
		this.serieses = serieses;
	}
	public String[] getShouldEmpty() {
		return shouldEmpty;
	}

	public void setShouldEmpty(String[] shouldEmpty) {
		this.shouldEmpty = shouldEmpty;
	}


	//preconditino: parameterMap is not null
	protected void populateImpl() throws Exception {
		List<String> patientList = null;
		List<String> studyList = null;
		List<String> seriesList = null;
		List<String> emptyList = null;
		if (patients != null && patients.length > 0)
		{
			patientList = Arrays.asList(patients);
		}
		if (studies != null && studies.length > 0)
		{
			studyList = Arrays.asList(studies);
		}
		if (serieses != null && serieses.length > 0)
		{
			seriesList = Arrays.asList(serieses);
		}
		if (shouldEmpty != null && shouldEmpty.length > 0)
		{
			emptyList = Arrays.asList(shouldEmpty);
		}

		ExternalDataBasket rsh = new ExternalDataBasket();
		List<SeriesSearchResult> sDto = rsh.getSeriesDTOList(patientList,
				                                             studyList,
				                                             seriesList,
				                                             BeanManager.getSecurityBean().getAuthorizationManager());
		BasketBean dataBasket = (BasketBean) BeanManager.getBasketBean();
		try {
		if (emptyList!=null)
		{
			if (!emptyList.isEmpty())
			{
		       String emptyString=(String)emptyList.get(0);
		       if (emptyString.equalsIgnoreCase("yes"))
		       {	
			       dataBasket.getBasket().removeAllSeries();
		       }
			}
		}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (sDto != null && sDto.size() > 0)
		{
			dataBasket.getBasket().addSeries(sDto);
		}
	}

	/////////////////////////////////////////PRIVATE////////////////////////////////

	private String[] patients;

	private String[] studies;

	private String[] serieses;
	
	private String[] shouldEmpty;

}

