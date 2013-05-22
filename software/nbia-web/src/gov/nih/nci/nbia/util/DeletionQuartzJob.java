/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.beans.SeriesDeletionBean;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DeletionQuartzJob extends QuartzJobBean {
	@Autowired
	private SeriesDeletionBean seriesDeletionBean;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try
		{
			seriesDeletionBean.removeSeriesFromCron();
			System.out.println("========Done with Cron Job=============");
		}catch(Exception e)
		{
			e.printStackTrace();
			throw new JobExecutionException(e.getMessage());
		}
	}

	public SeriesDeletionBean getSeriesDeletionBean() {
		return seriesDeletionBean;
	}

	public void setSeriesDeletionBean(SeriesDeletionBean seriesDeletionBean) {
		this.seriesDeletionBean = seriesDeletionBean;
	}

}
