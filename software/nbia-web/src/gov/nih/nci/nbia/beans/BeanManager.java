/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans;

import gov.nih.nci.nbia.beans.basket.BasketBean;
import gov.nih.nci.nbia.beans.populator.ViewSeriesPopulatorQCBean;
import gov.nih.nci.nbia.beans.qctool.QcToolUpdateBean;
import gov.nih.nci.nbia.beans.savedquery.SavedQueryBean;
import gov.nih.nci.nbia.beans.searchform.SearchLookupBean;
import gov.nih.nci.nbia.beans.searchform.SearchWorkflowBean;
import gov.nih.nci.nbia.beans.searchresults.SearchResultBean;
import gov.nih.nci.nbia.beans.searchresults.SeriesSearchResultBean;
import gov.nih.nci.nbia.beans.security.AnonymousLoginBean;
import gov.nih.nci.nbia.beans.security.SecurityBean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;


/**
 * Encapsulates the retrieval of beans from the faces context
 *
 * @author dietrichj
 *
 */
public class BeanManager {
    /**
     *
     */
    public static BasketBean getBasketBean() {
        return (BasketBean) getBean("#{basketBean}");
    }
    
    /**
    *
    */
   public static QcToolUpdateBean getQcToolUpdateBean() {
       return (QcToolUpdateBean) getBean("#{qcToolUpdateBean}");
   }


    /**
     *
     */
    public static SecurityBean getSecurityBean() {
        return (SecurityBean) getBean("#{securityBean}");
    }

    /**
     * Look up SecurityBean via the HTTP session directly instead of
     * via the FacesContext
     *
     * @param request - The HttpRequest
     */
    public static SecurityBean getSecurityBean(HttpServletRequest request) {
        return (SecurityBean) request.getSession().getAttribute("securityBean");
    }

    /**
     *
     */
    public static SearchWorkflowBean getSearchWorkflowBean() {
        return (SearchWorkflowBean) getBean("#{searchWorkflowBean}");
    }

    /**
     *
     */
    public static SavedQueryBean getSavedQueryBean() {
        return (SavedQueryBean) getBean("#{savedQueryBean}");
    }

    /**
     *
     */
    public static SearchLookupBean getSearchLookupBean() {
        return (SearchLookupBean) getBean("#{searchLookupBean}");
    }

    /**
     *
     */
    public static SearchResultBean getSearchResultBean() {
        return (SearchResultBean) getBean("#{searchResultBean}");
    }

    public static SeriesSearchResultBean getSeriesSearchResultBean() {
        return (SeriesSearchResultBean) getBean("#{seriesSearchResultBean}");
    }

    public static ViewSeriesPopulatorQCBean getViewSeriesPopulatorQCBean() {
        return (ViewSeriesPopulatorQCBean) getBean("#{viewSeriesPopulatorQCBean}");
    }


    /**
     * 
     */
    public static AnonymousLoginBean getAnonymousLoginBean() {
        return (AnonymousLoginBean) getBean("#{anonymousLoginBean}");
    }

    /**
     *  Generically retrieves a bean from the context
     */
    private static Object getBean(String beanId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if(facesContext == null) {
            return null;
        }
        return facesContext.getApplication().createValueBinding(beanId)
                           .getValue(facesContext);
    }
}
