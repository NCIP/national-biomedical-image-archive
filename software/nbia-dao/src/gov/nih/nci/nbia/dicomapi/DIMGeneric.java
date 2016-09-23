/*  Copyright   2010 - IEETA
 *
 *  This file is part of Dicoogle.
 *
 *  Author: Luís A. Bastião Silva <bastiao@ua.pt>
 *
 *  Dicoogle is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Dicoogle is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Dicoogle.  If not, see <http://www.gnu.org/licenses/>.
 */
package gov.nih.nci.nbia.dicomapi;


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
//import pt.ua.dicoogle.DebugManager;
//import pt.ua.dicoogle.sdk.Utils.SearchResult;


/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class DIMGeneric
{
    /**
     * There are double space to save the results but it decrease
     * the search time and it is important because a querySearch should be
     * little enought.
     */
    private ArrayList<Patient> patients = new ArrayList<Patient>();
    private Hashtable<String, Patient> patientsHash  = new Hashtable<String, Patient>();
    static Logger log = Logger.getLogger(SearchDicomResult.class);
    /**
     * it is allow to handle a ArrayList of Strings or SearchResults
     * @param arr
     */
    public DIMGeneric(List arr) throws Exception{
            Hashtable extra = null;
            //DebugManager.getInstance().debug("Looking search results: " + arr.size() );

            int size = arr.size();
            for (int i = 0 ; i<size ; i++ ){
                log.info("Adding new Image ");
            
                /**
                 * Looking for SeachResults and put it in right side :) 
                 */

                SearchResult r = (SearchResult) arr.get(i);
                extra = r.getExtrafields();

                /** Get data from Study */
                String studyUID = (String) extra.get("StudyInstanceUID");
                String studyID = (String) extra.get("StudyID");
                String studyDate = (String) extra.get("StudyDate");
                String studyTime = (String ) extra.get("StudyTime");
                String AccessionNumber = (String) extra.get("AccessionNumber");
                String StudyDescription = (String) extra.get("StudyDescription");
                /**
                 * Get data to Serie
                 */
                String serieUID = (String) extra.get("SeriesInstanceUID");
                log.info("serieUID"+serieUID);
                String serieNumber = (String) extra.get("SeriesNumber");
                String serieDescription = (String) extra.get("SeriesDescription");
                String modality = (String) extra.get("Modality");
                String patientID = (String) extra.get("PatientID");


                String patientSex = (String) extra.get("PatientSex");
                String patientBirthDate = (String) extra.get("PatientBirthDate");
                
                String patientName = (String) extra.get("PatientName");

                if (studyUID == null) studyUID = "" ;
                if (serieUID == null) serieUID = "" ;

                /**
                 * Get data to Image
                 */
                //TODO:Error checking here... but according to standard, all images
                //must have one of these...
                String sopInstUID = (String) extra.get("SOPInstanceUID");
                if(sopInstUID == null)
                    sopInstUID ="no uid";

                if (studyUID == null)
                         studyUID = "" ;




                /** Verify if Patient already exists */
                log.info("patientName:"+patientName);
                if (this.patientsHash.containsKey(patientName))
                {
                    /**
                     * Patient Already exists, let's check studys
                     */

                    Patient p = this.patientsHash.get(patientName);


                    /**
                     * Add Study
                     * It also verify if it exists
                     * In the last case Object will be discarded and the
                     * data will be added for the Study that already exists
                     */

                    Study s = new Study(p,studyUID,studyDate) ;
                    s.setAccessionNumber(AccessionNumber);
                    s.setStudyTime(studyTime);
                    s.setStudyID(studyID);
                    s.setStudyDescription(StudyDescription);
                    Serie serie = new Serie(s, serieUID, modality);
                    if (serieNumber!=null)
                        serie.setSerieNumber((int)Float.parseFloat(serieNumber));
                    serie.setSeriesDescription(serieDescription);
                    serie.addImage(r.getOrigin(),sopInstUID);
                    s.addSerie(serie) ;
                    p.addStudy(s);
                }
                else {
                    /**
                     * Patient does not exist
                     */
                     Patient p = new Patient(patientID, patientName);
                     p.setPatientSex(patientSex);
                     p.setPatientBirthDate(patientBirthDate);

                     /**
                      * Create Study
                      */
                     Study s = new Study(p, studyUID,studyDate) ;
                     s.setAccessionNumber(AccessionNumber);
                     s.setStudyTime(studyTime);
                     s.setStudyDescription(StudyDescription);
                     s.setStudyID(studyID);
                     p.addStudy(s);

                     /**
                     * Create Serie
                     */
                     Serie serie = new Serie(s,serieUID, modality);
                     if (serieNumber!=null)
                        serie.setSerieNumber((int)Float.parseFloat(serieNumber));
                     serie.setSeriesDescription(serieDescription);
                     serie.addImage(r.getOrigin(),sopInstUID);
                     s.addSerie(serie);
                     this.patients.add(p);
                     this.patientsHash.put(patientName, p);
                }
            }

    }

    public String getXML()
    {

        StringWriter writer = new StringWriter();


        StreamResult streamResult = new StreamResult(writer);
        SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory.newInstance();
        //      SAX2.0 ContentHandler.
        TransformerHandler hd = null;
        try {
            hd = tf.newTransformerHandler();
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        }
        Transformer serializer = hd.getTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.METHOD, "xml");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        hd.setResult(streamResult);
        try {
            hd.startDocument();

            AttributesImpl atts = new AttributesImpl();
            hd.startElement("", "", "DIM", atts);

            for (Patient p : this.patients)
            {
                atts.clear();
                atts.addAttribute("", "", "name", "",p.getPatientName().trim());
                atts.addAttribute("", "", "id", "",p.getPatientID().trim());
                hd.startElement("", "", "Patient", atts);


                for (Study s: p.getStudies())
                {
                    atts.clear();
                    atts.addAttribute("", "", "date", "",s.getStudyData().trim());
                    atts.addAttribute("", "", "id", "", s.getStudyInstanceUID().trim());

                    hd.startElement("", "", "Study", atts);

                    for (Serie serie : s.getSeries()){
                        atts.clear();
                        atts.addAttribute("", "", "modality", "", serie.getModality().trim());
                        atts.addAttribute("", "", "id", "", serie.getSerieInstanceUID().trim());

                        hd.startElement("", "", "Serie", atts);

                        ArrayList<String> img = serie.getImageList();
                        ArrayList<String> uid = serie.getSOPInstanceUIDList();
                        int size = img.size();
                        for (int i=0;i<size;i++){
                            atts.clear();
                            atts.addAttribute("", "", "path", "", img.get(i).trim());
                            atts.addAttribute("", "", "uid", "", uid.get(i).trim());

                            hd.startElement("", "", "Image", atts);
                            hd.endElement("", "", "Image");
                        }
                        hd.endElement("", "", "Serie");
                    }
                    hd.endElement("", "", "Study");
                }
                hd.endElement("", "", "Patient");
            }
            hd.endElement("", "", "DIM");

        } catch (SAXException ex) {
            ex.printStackTrace();
        }

        return writer.toString() ;
    }

    /**
     * @return the patients
     */
    public ArrayList<Patient> getPatients() {
        return patients;
    }
}
