/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 *
 */
package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.internaldomain.GeneralSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.hibernate.criterion.DetachedCriteria;

/**
 * @author Jim Zhou
 *
 */
public class AdapterUtil extends HibernateDaoSupport {
	private static Logger log = Logger.getLogger(AdapterUtil.class);
	
	public static Date stringToDate(String numbers) throws Exception {
        Date retval = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            if (numbers != null) {
                retval = sdf.parse(numbers);
            }
        } catch (ParseException pe) {
            log.error("ParseException in StringToDate: " + numbers);
        }

        return retval;
    }



	public static String convertToAgeGroup(String age) throws Exception {
        if (!age.endsWith("Y")) {
            return "";
        }

        int ageNumber = Integer.parseInt(age.substring(0, age.length() - 1));

        if (ageNumber < 5) {
            return "000Y";
        } 
        else {
	        for(int i=1;i<10;i++) {
	        	if(ageNumber < i*10+5) {
	        		return "0"+(i*10)+"Y";
	        	}
	        }
        }
        
        return "100Y";
    }

    private AdapterUtil() {
	}
    
    
    public boolean checkSeriesStatus(Map numbers)
    {
    	boolean status = false;
    	
    	DetachedCriteria criteria = DetachedCriteria.forClass(GeneralSeries.class);
    	criteria.setProjection(Projections.property("visibility"));
    	criteria.add(Restrictions.eq("seriesInstanceUID", numbers.get(DicomConstants.SERIES_INSTANCE_UID)));

    	 List rs =getHibernateTemplate().findByCriteria(criteria);

    	 if(rs == null || rs.size() <= 0)
    	 {
    		 status = true;
    	 }
    	 else
    	 {
	    	 String visibility = (String)rs.get(0);
	    	 if (visibility.equalsIgnoreCase("4"))
	    	 {
	    		 log.error("Series Instance UID - " + numbers.get(DicomConstants.SERIES_INSTANCE_UID)
	    				 + " marked as deleted. Cannot update this series");
	    		 status = false;
	    	 }
	    	 else
	    	 {
	    		 status = true;
	    	 }
    	 }

    	 return status;
    }
    
    private static ClassPathXmlApplicationContext ctx;
    
    public  static ClassPathXmlApplicationContext getApplicationContext() {
    	return ctx;
    }
    
    public static void setApplicationContext (ClassPathXmlApplicationContext context){
    	ctx = context;
    }
}
