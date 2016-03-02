/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.dynamicsearch.criteria;

import gov.nih.nci.nbia.util.SiteData;

import java.util.List;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

public class CriteriaForAuthorizedSiteData {

	public void setAuthorizedSiteData(String tableAlias, 
			                          DetachedCriteria criteria, 
			                          List<SiteData> sites)
	{
		Disjunction disjunction = Restrictions.disjunction();

		if(tableAlias != null)
		{
			for (SiteData sd : sites)
			{
				Conjunction con = new Conjunction();
				con.add(Restrictions.eq(tableAlias+".dpSiteName",sd.getSiteName()));
				con.add(Restrictions.eq(tableAlias+".project", sd.getCollection()));
				disjunction.add(con);
			}
			criteria.add(disjunction);
		}
		else
		{
			for (SiteData sd : sites)
			{
				Conjunction con = new Conjunction();
				con.add(Restrictions.eq("dpSiteName",sd.getSiteName()));
				con.add(Restrictions.eq("project", sd.getCollection()));
				disjunction.add(con);
			}
			criteria.add(disjunction);
		}
	}
	
	public void setSeriesSecurityGroups(String tableAlias, 
			                            DetachedCriteria criteria, 
			                            List<String> securityGroups)
	{
		Conjunction con = new Conjunction();
		if(tableAlias != null)
		{
			if (securityGroups != null && securityGroups.size() != 0)
			{	
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(Restrictions.isNull(tableAlias+".securityGroup"));
				disjunction.add(Restrictions.in(tableAlias+".securityGroup", securityGroups));
				con.add(disjunction);
				criteria.add(con);
			}
			else
			{
				criteria.add(Restrictions.isNull(tableAlias+".securityGroup"));
			}
			
		}
		else
		{
			if (securityGroups != null && securityGroups.size() != 0)
			{	
				Disjunction disjunction = Restrictions.disjunction();
				disjunction.add(Restrictions.isNull("securityGroup"));
				disjunction.add(Restrictions.in("securityGroup", securityGroups));
				con.add(disjunction);
				criteria.add(con);
			}
			else
			{
				criteria.add(Restrictions.isNull("securityGroup"));
			}
		}
	}
}
