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

import in.raster.oviyam.ImageInfo;
import in.raster.oviyam.model.InstanceModel;
import in.raster.oviyam.util.InstanceComparator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.log4j.Logger;
import javax.servlet.jsp.PageContext;

/**
 * 
 * @author asgar
 */
public class ImageHandler extends SimpleTagSupport {

	// Initialize logger
	private static Logger log = Logger.getLogger(ImageHandler.class);

	// Attribute variables for the tag
	private String patientId;
	private String study;
	private String series;
	private String dcmURL;

	/**
	 * Setter for property patientId.
	 * 
	 * @param patientId
	 *            String object registers the patientId.
	 */
	public void setPatientId(String patientId) {
		if (patientId == null) {
			this.patientId = "";
		} else {
			this.patientId = patientId;
		}
	}

	/**
	 * Setter for property study.
	 * 
	 * @param study
	 *            String object that registers the study.
	 */
	public void setStudy(String study) {
		this.study = study;
	}

	/**
	 * Setter for property series.
	 * 
	 * @param series
	 *            String object registers the series
	 */
	public void setSeries(String series) {
		this.series = series;
	}

	/**
	 * Setter for property dcmURL.
	 * 
	 * @param dcmURL
	 *            String object registers the dcmURL
	 */
	public void setDcmURL(String dcmURL) {
		this.dcmURL = dcmURL;
	}

	/**
	 * Overridden Tag handler method. Default processing of the tag. This method
	 * will send the ImageInfo to generate a HTML page during its execution.
	 * 
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException {

		ImageInfo imageInfo = null;
		try {
			imageInfo = new ImageInfo();
			String oviyamId = (String)getJspContext().getAttribute("oviyamId",PageContext.SESSION_SCOPE); 
			String wadoUrl = (String)getJspContext().getAttribute("wadoUrl",PageContext.SESSION_SCOPE); 
			imageInfo.callFindWithQuery(patientId, study, series, null, dcmURL, oviyamId, wadoUrl);
		} catch (Exception e) {
			log.error(
					"Unable to create instance of ImageInfo and access the callFindWithQuery()",
					e);
			return;
		}

		try {
			ArrayList<InstanceModel> instances = imageInfo.getInstancesList();

			Collections.sort(instances, new InstanceComparator());

			for (int instanceCount = 0; instanceCount < instances.size(); instanceCount++) {
				InstanceModel instance = instances.get(instanceCount);

				/*
				 * Writes the instance informations such as imageId and
				 * numberOfImages to the response.
				 */
				getJspContext().setAttribute("imageId", instance.getSopIUID());
				// getJspContext().setAttribute("instanceNumber",
				// instance.getInstanceNumber());

				getJspContext().setAttribute("instanceNumber",
						instanceCount + 1);

				getJspContext().setAttribute("sopClassUID",
						instance.getSopClassUID());
				getJspContext().setAttribute("numberOfFrames",
						instance.getNumberOfFrames());

				if (instance.getNumberOfFrames() != null) {
					getJspContext().setAttribute("multiframe", "yes");
				} else {
					getJspContext().setAttribute("multiframe", "no");
				}
				
				getJspContext().setAttribute("rows", instance.getRows());				

				/*
				 * Process the body of the tag and print it to the response. The
				 * null argument means the output goes to the response rather
				 * than some other writer.
				 */
				getJspBody().invoke(null);
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

}