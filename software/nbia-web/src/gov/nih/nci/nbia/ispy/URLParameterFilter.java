/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2007/10/01 12:20:17  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/15 12:21:07  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:11  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.4  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.ispy;

import gov.nih.nci.nbia.util.NameValueObj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class URLParameterFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) request;
        HttpSession session = hreq.getSession();

        //String checkforloginpage = hreq.getPathTranslated();
        String checkforloginpage = hreq.getServletPath();

        if (checkforloginpage.endsWith("referencedImages.jsf")) {
            Map paramMap = hreq.getParameterMap();
            String source = (String) hreq.getParameter("source");

            if (null != source) {
                UrlParams params = new UrlParams();
                params.setSource((String) hreq.getParameter("source"));
                params.setImage1Label((String) hreq.getParameter("image1Label"));
                params.setImage1TrialId((String) hreq.getParameter(
                        "image1TrialId"));
                params.setImage1PatientId((String) hreq.getParameter(
                        "image1PatientId"));
                params.setImage1StudyInstanceUid((String) hreq.getParameter(
                        "image1StudyInstanceUid"));
                params.setImage1SeriesInstanceUid((String) hreq.getParameter(
                        "image1SeriesInstanceUid"));
                params.setImage1ImageSopInstanceUid((String) hreq.getParameter(
                        "image1ImageSopInstanceUid"));
                params.setImage2Label((String) hreq.getParameter("image2Label"));
                params.setImage2TrialId((String) hreq.getParameter(
                        "image2TrialId"));
                params.setImage2PatientId((String) hreq.getParameter(
                        "image2PatientId"));
                params.setImage2StudyInstanceUid((String) hreq.getParameter(
                        "image2StudyInstanceUid"));
                params.setImage2SeriesInstanceUid((String) hreq.getParameter(
                        "image2SeriesInstanceUid"));
                params.setImage2ImageSopInstanceUid((String) hreq.getParameter(
                        "image2ImageSopInstanceUid"));

                String[] image1dataNames = (String[]) paramMap.get(
                        "image1dataName");
                String[] image1dataValues = (String[]) paramMap.get(
                        "image1dataValue");
                String[] image2dataNames = (String[]) paramMap.get(
                        "image2dataName");
                String[] image2dataValues = (String[]) paramMap.get(
                        "image2dataValue");

                if (image1dataNames != null) {
                    List<NameValueObj> image1dataNamesValueAry = toArrayList(image1dataNames,
                                                                             image1dataValues);
                    params.setImage1dataNameValues(image1dataNamesValueAry);
                }

                if (image2dataNames != null) {
                	List<NameValueObj> image2dataNamesValueAry = toArrayList(image2dataNames,
                                                                             image2dataValues);
                    params.setImage2dataNamesValues(image2dataNamesValueAry);
                }

                session.setAttribute("UrlParams", params);
            }
        }

        // deliver request to next filter
        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    public List<NameValueObj> toArrayList(String[] nameAry, String[] valueAry) {
    	ArrayList<NameValueObj> aryList = new ArrayList<NameValueObj>();

        for (int i = 0; i < nameAry.length; ++i) {
            NameValueObj nvj = new NameValueObj();
            nvj.setName(nameAry[i]);
            nvj.setValue(valueAry[i]);
            aryList.add(nvj);
        }

        aryList.trimToSize();

        return aryList;
    }
}
