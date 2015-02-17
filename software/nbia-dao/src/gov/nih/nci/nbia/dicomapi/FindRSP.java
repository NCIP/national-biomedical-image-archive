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
 * 
 * The Goal of this class is based on DicomObject 
 * send FindRSP to destination entity 
 * 
 * It will search data at Index of Lucene 
 * 
 */
package gov.nih.nci.nbia.dicomapi;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DimseRSP;
import org.dcm4che2.net.Status;
//import pt.ua.dicoogle.DebugManager;
//import pt.ua.dicoogle.core.exceptions.CFindNotSupportedException;
//import pt.ua.dicoogle.core.index.IndexEngine;
//import pt.ua.dicoogle.server.SearchDicomResult;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class FindRSP implements DimseRSP 
{
    
    private DicomObject rsp;
    private DicomObject keys;
    static Logger log = Logger.getLogger(FindRSP.class);
    
    
    /** 
     * Each moment in timeline we're getting a item
     */
    private DicomObject mwl = null;

    SearchDicomResult search = null ; 
    
    
    public FindRSP(DicomObject keys, DicomObject rsp)
    {
        this.rsp = rsp ; 
        this.keys = keys ; 
        log.info("--> Creating FindRSP");
       // IndexEngine core = IndexEngine.getInstance();

        
        /** Debug - show keys, rsp, index */ 
        if (keys!=null)
        {
        	log.info("keys object: ");
        	log.info(keys.toString());
        }
        if (rsp!=null)
        {
        	log.info("Rsp object");
        	log.info(rsp.toString());
        }
        
        /** 
         * Get object to search
         */
        ArrayList<String> extrafields  = null ;
        extrafields = new ArrayList<String>();

        extrafields.add("PatientName");
        extrafields.add("PatientID");
        extrafields.add("Modality");
        extrafields.add("StudyDate");
        extrafields.add("StudyTime");
        extrafields.add("AccessionNumber");
        extrafields.add("StudyID");
        extrafields.add("StudyDescription");
        extrafields.add("SeriesNumber");
        extrafields.add("SeriesInstanceUID");
        extrafields.add("SeriesDescription");
        extrafields.add("ReferringPhysicianName");
        extrafields.add("PatientBirthDate");
        extrafields.add("ModalitiesInStudy");
        extrafields.add("StudyInstanceUID");
        
        DICOMParameters query = getQueryString(keys, rsp);
        
        if (!query.valid())
        {
        	log.error("query is empty");
            return;
        }
        /** 
         * Search results
         * TODO: 
         * - Get Search String Query?
         * - Is A Network Search?
         * - How works extrafields?
         */

        SearchDicomResult.QUERYLEVEL level = null ;
        /*
        if (CFindBuilder.isPatientRoot(rsp))
        {
            level = SearchDicomResult.QUERYLEVEL.PATIENT;
        }
        else if (CFindBuilder.isStudyRoot(rsp))
        {
            level = SearchDicomResult.QUERYLEVEL.STUDY;
        }*/
        DicomElement elem = keys.get(Integer.parseInt("00080052", 16));
        String levelStr = new String(elem.getBytes());

        if (levelStr.contains("PATIENT"))
        {
            level = SearchDicomResult.QUERYLEVEL.PATIENT;
            System.out.println("Query at patient level");
        }
        else if(levelStr.contains("STUDY"))
        {
            level = SearchDicomResult.QUERYLEVEL.STUDY;
            log.info("Query at study level");
        }
        else if (levelStr.contains("SERIES"))
        {
            level = SearchDicomResult.QUERYLEVEL.SERIE;
            System.out.println("Query at series level");
        }
        else if (levelStr.contains("IMAGE"))
        {
            level = SearchDicomResult.QUERYLEVEL.IMAGE;
            log.info("Query at image level");
        }

        search = new SearchDicomResult(query,
                 true, extrafields, level);



         if (search == null)
         {
        	 log.info(">> Search is null, something is wrong ");
         }
         
        // always return Specific Character Set
        if (!keys.contains(Tag.SpecificCharacterSet)) {
            this.keys.putNull(Tag.SpecificCharacterSet, VR.CS);
            keys.putNull(Tag.SpecificCharacterSet, VR.CS);
            this.rsp.putNull(Tag.SpecificCharacterSet, VR.CS);
        }

    }




    private DICOMParameters getQueryString(DicomObject keys, DicomObject rsp)
    {
    	DICOMParameters result = null;
        try
        {
            CFindBuilder c = new CFindBuilder(keys, rsp);
            result = c.getQueryString() ;
        } catch (Exception ex)
        {
        	ex.printStackTrace();
        }

        return result ;
    }





    /**
     * 
     * Verify if have a next DicomObject and set 
     * the pointer of DicomObject with correct paraments
     * It also apply the filter to verify if the DicomObject matches
     * with query and if it is not search for the next DicomObject.
     * 
     * @return true if there is a next DicomObject
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */

    @Override
    public boolean next() throws IOException, InterruptedException 
    {
        if (search!=null)
        {
            if (search.hasNext())
            {
                    //DebugManager.getInstance().debug("We have next, so get it");
                    mwl = search.next();
                    //if (mwl.matches(this.keys, true))
                    //{

                        // always return Specific Character Set
                        if (!this.mwl.contains(Tag.SpecificCharacterSet))
                            this.mwl.putNull(Tag.SpecificCharacterSet, VR.CS);
                        this.rsp.putInt(Tag.Status, VR.US, mwl.containsAll(keys) ? Status.Pending : Status.PendingWarning);
                        return true;
                    //}
            }

            /** Sucess */
            this.rsp.putInt(Tag.Status, VR.US, Status.Success);
            /** Clean pointers */
            this.mwl = null;
            this.search = null;
            return true ;
            
        }
        else
        {
            this.rsp.putInt(Tag.Status, VR.US, Status.Cancel);
        }
        return false;
    }
    
    /**
     * 
     * @return
     */
    @Override
    public DicomObject getCommand() 
    {
        return this.rsp;
    }
    
    
    /**
     * This method see the current DicomObject and return it
     * @return null or DicomObject
     */
    @Override
    public DicomObject getDataset() 
    {
        //DebugManager.getInstance().debug("Get Data Set");
        return  this.mwl != null ? this.mwl.subSet(this.keys) : null;
    }

    @Override
    public void cancel(Association arg0) throws IOException 
    {

        search = null ;
        try
        {
            arg0.release(true);
            
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

   @Override
    protected void finalize() throws Throwable
   {

        super.finalize();


    }

}
    
