/* ***** BEGIN LICENSE BLOCK *****
* Version: MPL 1.1/GPL 2.0/LGPL 2.1
*
* The contents of this file are subject to the Mozilla Public License Version
* 1.1 (the "License"); you may not use this file except in compliance with
* the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
*
* Software distributed under the License is distributed on an "AS IS" basis,
* WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
* for the specific language governing rights and limitations under the
* License.
*
* The Original Code is part of Oviyam, an web viewer for DICOM(TM) images
* hosted at http://skshospital.net/pacs/webviewer/oviyam_0.6-src.zip
*
* The Initial Developer of the Original Code is
* Raster Images
* Portions created by the Initial Developer are Copyright (C) 2014
* the Initial Developer. All Rights Reserved.
*
* Contributor(s):
* Babu Hussain A
* Devishree V
* Meer Asgar Hussain B
* Prakash J
* Suresh V
*
* Alternatively, the contents of this file may be used under the terms of
* either the GNU General Public License Version 2 or later (the "GPL"), or
* the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
* in which case the provisions of the GPL or the LGPL are applicable instead
* of those above. If you wish to allow use of your version of this file only
* under the terms of either the GPL or the LGPL, and not to allow others to
* use your version of this file under the terms of the MPL, indicate your
* decision by deleting the provisions above and replace them with the notice
* and other provisions required by the GPL or the LGPL. If you do not delete
* the provisions above, a recipient may use your version of this file under
* the terms of any one of the MPL, the GPL or the LGPL.
*
* ***** END LICENSE BLOCK ***** */

package in.raster.oviyam.handler;

import in.raster.oviyam.SeriesInfo;
import in.raster.oviyam.model.SeriesModel;
import in.raster.oviyam.util.SeriesComparator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.log4j.Logger;

/**
 *
 * @author asgar
 */
public class SeriesDetailsHandler extends SimpleTagSupport {

    //Initialize logger
    private static Logger log = Logger.getLogger(SeriesDetailsHandler.class);

    //Attribute variables for this tag
    private String patientId;
    private String study;
    private String dcmURL;

    /**
     * Setter for property patientId
     * @param patientId String object registers the patientId.
     */
    public void setPatientId(String patientId) {
        if(patientId == null) {
            this.patientId = "";
        } else {
            this.patientId = patientId;
        }
    }

    /**
     * Setter for property study
     * @param study String object register the study.
     */
    public void setStudy(String study) {
        this.study = study;
    }

    /**
     * Setter for property dcmURL
     * @param dcmURL String object regist the study.
     */
    public void setDcmURL(String dcmURL) {
        this.dcmURL = dcmURL;
    }

    /**
     * Overridden Tag handler method. Default processing of the tag.
     * This method will send the Series information to generate a HTML page during its execution.
     *
     * @see javax.servlet.jsp.tasext.SimpleTagSupport#doTag()
     */
    @Override
    public void doTag() throws JspException, IOException {

        SeriesInfo seriesInfo = null;

        try {
            /**
             * in.raster.oviyam5.SeriesInfo used to query cFIND the Series information of given
             * study and patient.
             */
			String oviyamId = (String)getJspContext().getAttribute("oviyamId",PageContext.SESSION_SCOPE); 
			String wadoUrl = (String)getJspContext().getAttribute("wadoUrl",PageContext.SESSION_SCOPE); 
			Object seriesOUid = getJspContext().getAttribute("seriesUidIn",PageContext.SESSION_SCOPE);
			String seriesUid = null;
			if (seriesOUid!=null){
				seriesUid=seriesOUid.toString();
			}
            seriesInfo = new SeriesInfo();
            seriesInfo.callFindWithQuery(patientId, study, dcmURL, oviyamId, wadoUrl, seriesUid);
        } catch(Exception e) {
            log.error("Unable to create instance of SeriesInfo and access its callFindWithQuery()" , e);
            return;
        }

        try {
            ArrayList<SeriesModel> seriesList = seriesInfo.getSeriesList();

            Collections.sort(seriesList, new SeriesComparator());

            SimpleDateFormat sdf = null;
            Date serDateTmp = null;

            for(int i=0; i<seriesList.size(); i++) {
                SeriesModel sm = seriesList.get(i);

                /**
                 * Tag handler sets the attribute values such as seriesId, seriesDesc, modality etc.
                 * Example: User can access it from the JSP using the Expression Language of JSP such as
                 *    ${seriesID}
                 *    ${seriesDesc}
                 *    ${modality}
                 */
                getJspContext().setAttribute("seriesId", sm.getSeriesIUID());
                getJspContext().setAttribute("seriesNumber", sm.getSeriesNumber());
                getJspContext().setAttribute("seriesDesc", sm.getSeriesDescription());
                getJspContext().setAttribute("modality", sm.getModality());
                getJspContext().setAttribute("numberOfImages", sm.getNumberOfInstances());

                String serDate = sm.getSeriesDate();
                if(serDate != null && serDate.length() > 0) {
                    String serTime = sm.getSeriesTime();
                    //if(!serTime.equals("")) {
                    if(serTime != null && serTime.length() > 0) {
                        serDate = serDate + " " + serTime;
                        sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
                    } else {
                        sdf = new SimpleDateFormat("yyyyMMdd");
                    }

                    serDateTmp = sdf.parse(serDate);
                    sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                    getJspContext().setAttribute("seriesDate", sdf.format(serDateTmp));
                }

                /**
                 * Process the body of the tag and print it to the response. The numm argument
                 * means the output goes to the response rather than some other writer.
                 */
                getJspBody().invoke(null);
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}