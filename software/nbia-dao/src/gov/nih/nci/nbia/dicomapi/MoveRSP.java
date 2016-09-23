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
 */
package gov.nih.nci.nbia.dicomapi;

import java.io.IOException;
import java.util.ArrayList;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.net.Association;
import org.dcm4che2.net.DimseRSP;
import org.dcm4che2.net.Status;
//import pt.ua.dicoogle.DebugManager;
//import pt.ua.dicoogle.core.index.IndexEngine;
//import pt.ua.dicoogle.server.SearchDicomResult;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class MoveRSP  implements DimseRSP 
{
    DicomObject rsp = null ;
    DicomObject keys = null ;

    DicomObject current = null ;
    SearchDicomResult search = null ; 


    public MoveRSP(DicomObject keys, DicomObject rsp)
    {
        /* Save args */
        this.rsp = rsp ;
        this.keys = keys ;
        this.current = rsp ; 
        

        // DebugManager.getInstance().debug("--> Creating MoveRSP");
        // IndexEngine core = IndexEngine.getInstance();


        /** Debug - show keys, rsp, index */
        if (keys!=null)
        {
            //DebugManager.getInstance().debug("keys object: ");
            //DebugManager.getInstance().debug(keys.toString());
        }
        if (rsp!=null)
        {
           // DebugManager.getInstance().debug("Rsp object");
            //DebugManager.getInstance().debug(rsp.toString());
        }

          this.rsp.putInt(Tag.Status, VR.US, Status.Success);
            
    }

    @Override
    public boolean next() throws IOException, InterruptedException
    {
  
            /** Sucess */
            this.rsp.putInt(Tag.Status, VR.US, Status.Success);
            /** Clean pointers */
            this.current = null;
            return true ;

    }

    @Override
    public DicomObject getCommand()
    {
        return this.rsp ; 
    }

    @Override
    public DicomObject getDataset()
    {
         return  this.current != null ? this.current.subSet(this.keys) : null;
    }

    @Override
    public void cancel(Association arg0) throws IOException
    {
        // TODO
    }

}
