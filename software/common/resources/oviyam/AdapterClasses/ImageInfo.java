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

package in.raster.oviyam;

import de.iftm.dcm4che.services.CDimseService;
import de.iftm.dcm4che.services.ConfigProperties;
import de.iftm.dcm4che.services.NBIARequester;
import de.iftm.dcm4che.services.StorageService;
import in.raster.oviyam.model.InstanceModel;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.dcm4che.util.DcmURL;
import java.text.ParseException;
import org.dcm4che.data.Dataset;

/**
 *
 * @author asgar
 */
public class ImageInfo {

    //Initialize Logger
    private static Logger log = Logger.getLogger(ImageInfo.class);

    private ArrayList<InstanceModel> instancesList;

    //Constructor
    public ImageInfo() {
        instancesList = new ArrayList<InstanceModel>();
    }

    /**
     * Queries (cFIND) the Instance information from the machine (dcmProtocol://aeTitle!hostName:port).
     *
     * @param patientID
     * @param studyInstanceUID
     * @param seriesInstanceUID
     * @param SOPInstanceUID
     * @param dcmURL
     */
    public void callFindWithQuery(String patientID, String studyInstanceUID, String seriesInstanceUID, String SOPInstanceUID, String dcmURL,
    		 String oviyamId, String wadoUrl) {
    	instancesList=NBIARequester.getInstanceModels(patientID, studyInstanceUID,  seriesInstanceUID, SOPInstanceUID, dcmURL, oviyamId, wadoUrl);
    }

    /**
     * Getter for instances ArrayList<InstanceModel>,
     * @return the instance of ArrayList<InstanceModel>.
     */
    public ArrayList<InstanceModel> getInstancesList() {
        return instancesList;
    }

}