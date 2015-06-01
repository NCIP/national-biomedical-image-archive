/*  Copyright   2009 - IEETA
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



/** 
 * Class SearchResult is responsable to get results from Lucene indexer
 * But no text results are returned instead we use names of DICOM file and 
 * instance a new DicomObject, so therory this is a abtract to a list of 
 * DicomObject, neither list of *names* of DICOM file.
 */
package gov.nih.nci.nbia.dicomapi;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.wadosupport.WADOSupportDAO;
import gov.nih.nci.nbia.wadosupport.WADOSupportDAOImpl;
/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 * @since 17 Fev 2009
 */
public class SearchDicomResult implements Iterator<DicomObject>
{

    public enum QUERYLEVEL { PATIENT, STUDY, SERIE, IMAGE}

    private final QUERYLEVEL queryLevel; 

    static Logger log = Logger.getLogger(SearchDicomResult.class);
    /**
    * Get IndexCore
    */
   // IndexEngine core  = IndexEngine.getInstance();
    List<SearchResult> list = null ;
    List<Patient> patientList = new ArrayList<Patient>();
    List<Study> studyList = new ArrayList<Study>();
    List<Serie> seriesList = new ArrayList<Serie>();
    Iterator it = null ;
    
    String currentFile ;

    
    public SearchDicomResult(DICOMParameters searchQuery, boolean isNetwork, 
            ArrayList<String> extrafields, QUERYLEVEL level)
    {

        queryLevel = level ;

        /**
         * Get the array list of resulst match searchQuery
         */

        log.info("QUERY: " + searchQuery);
        log.info("QUERYLEVEL: " + queryLevel);
        log.info(searchQuery);
        list=new ArrayList<SearchResult>();
		WADOSupportDAO dDao = (WADOSupportDAO)SpringApplicationContext.getBean("WADOSupportDAO");
		List <DICOMSupportDTO> ddtos = dDao.getDICOMSupportDTO(searchQuery, extrafields);
        //list = core.searchSync(searchQuery, extrafields);
		for (DICOMSupportDTO ddto:ddtos){
			SearchResult item = new SearchResult(null, ddto.getFileName(),ddto.getFilePath(), ddto.getFileSize(), ddto.getFieldMap(), null);
		    list.add(item);
			
		}

        /*
         * Get iterator
         */
        
        if (list!=null)
        {
          it = list.iterator();
          log.info(">> We have a list of items found");
        }
        else
        {
          log.info(">> No list, no results, no iterator. ");
          it = null;
        }


        if (level == QUERYLEVEL.PATIENT||level == QUERYLEVEL.STUDY)
        {
            DIMGeneric dimModel = null;
            try
            {
            	log.info("Makeing new dimgeneric");
                dimModel = new DIMGeneric(list);
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            ArrayList<Patient> listPatients = dimModel.getPatients();
            
            for (Patient p : listPatients)
            {
                studyList.addAll(p.getStudies());
            }
            
            it = studyList.iterator();

        }
        else if (level == QUERYLEVEL.SERIE)
        {
            DIMGeneric dimModel = null;
            try
            {
                dimModel = new DIMGeneric(list);
            } catch (Exception ex)
            {
            }

            ArrayList<Patient> listPatients = dimModel.getPatients();
            for (Patient p : listPatients)
            {
                studyList.addAll(p.getStudies());
                for (Study s: p.getStudies())
                {
                    seriesList.addAll(s.getSeries());
                }
            }
            it = seriesList.iterator();

        }
   
    }

    @Override
    public boolean hasNext()
    {
      if (it!=null)
      {
      //  DebugManager.getInstance().debug("It has a iterator");
        if (it.hasNext())
        {
        //    DebugManager.getInstance().debug("and we have a next");
        }
         return it.hasNext();
      }
      else
      {
        return false;
      }
    }

    public String getCurrentFile()
    {
        return this.currentFile ; 
    }

    @Override
    public DicomObject next()
    {

        // TODO: this code need to be refactored
        // C-FIND RSP should be builded based on Search Result,
        // instead opening the file to build DicomObject.


        /** 
         * Get the fullpath of images 
         */
        ServerSettings s = ServerSettings.getInstance();
        String path = s.getPath(); 
        
       // DebugManager.getInstance().debug("Path of DICOM: "+path);




        if (it != null &&  it.hasNext())
        {
            Object next = it.next();
            if (queryLevel==QUERYLEVEL.IMAGE )
            {

                SearchResult sR = (SearchResult)next;
                path = sR.getOrigin();
                currentFile = path ;
                log.info("-> Next::: " + next.toString());
                    DicomInputStream din = null;
                try
                {
                    din = new DicomInputStream(new File(path));
                    log.info("Imagem: "+path+"..."+next);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                try
                {
                    /** This code is refactored in a experimental branch
                     * Building a BasicDicomObject based on Indexing
                     * It will increase the performace
                     */
                    BasicDicomObject result = new BasicDicomObject();
                    if (queryLevel==QUERYLEVEL.PATIENT)
                    {

                        // Experimental branch
                    }
                    // Fill fields of study now

                    return din.readDicomObject();
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            else if (queryLevel == QUERYLEVEL.STUDY||queryLevel == QUERYLEVEL.PATIENT)
            {

                Study studyTmp = (Study)next;
                BasicDicomObject result = new BasicDicomObject();
                result.putString(Tag.PatientName, VR.PN, studyTmp.getParent().getPatientName());
                result.putString(Tag.PatientID, VR.LO, studyTmp.getParent().getPatientID());
                result.putString(Tag.PatientBirthDate, VR.DA, studyTmp.getParent().getPatientBirthDate());
                result.putString(Tag.StudyDate, VR.DA, studyTmp.getStudyData());
                result.putString(Tag.StudyID, VR.SH, studyTmp.getStudyID());
                result.putString(Tag.StudyTime, VR.TM, studyTmp.getStudyTime());
                result.putString(Tag.AccessionNumber, VR.SH, studyTmp.getAccessionNumber());
                result.putString(Tag.StudyInstanceUID, VR.UI, studyTmp.getStudyInstanceUID());
                result.putString(Tag.StudyDescription, VR.LO, studyTmp.getStudyDescription());
                String modality = studyTmp.getSeries().get(0).getModality(); // Point of Failure, fix me
                result.putString(Tag.ModalitiesInStudy, VR.CS,modality);
                
                return result;
                
            }
            else if (queryLevel == QUERYLEVEL.SERIE)
            {
                // Serie

                
                Serie serieTmp = (Serie)next;
                BasicDicomObject result = new BasicDicomObject();
                log.info("Serie : "+ serieTmp);
                
                result.putString(Tag.StudyInstanceUID, VR.UI, serieTmp.getParent().getStudyInstanceUID());
                result.putString(Tag.SeriesInstanceUID, VR.UI, serieTmp.getSerieInstanceUID());
                result.putString(Tag.SeriesDescription, VR.LO, serieTmp.getSeriesDescription());
                result.putString(Tag.SeriesDate, VR.TM, "");
                result.putString(Tag.SeriesTime, VR.TM, "");
                result.putString(Tag.QueryRetrieveLevel, VR.LO, "SERIES");
                String modality = serieTmp.getModality(); // Point of Failure, fix me
                result.putString(Tag.Modality, VR.CS,modality);
                result.putString(Tag.SeriesNumber, VR.IS, "" + serieTmp.getSerieNumber());
                return result;

            }
            else
            {
                System.err.println("ERROR: WRONG QUERY LEVEL!");
            }
            

        }    
        return null ; 
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("Not supported. Nobody use it.");
    }
}
