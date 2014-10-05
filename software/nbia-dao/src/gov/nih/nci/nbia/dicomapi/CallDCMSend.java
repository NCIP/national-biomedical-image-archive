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
package gov.nih.nci.nbia.dicomapi;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class CallDCMSend
{

    public CallDCMSend(ArrayList<File> files, int port, String hostname, String AETitle, String cmoveID) throws Exception
    {

             DcmSnd dcmsnd = new DcmSnd();
             
            System.out.println("hostname:"+hostname);
            dcmsnd.setRemoteHost(hostname);
            dcmsnd.setRemotePort(104);
            
            for (File fx : files){
            	    System.out.println("exists:"+fx.exists());
            	    System.out.println("can read:"+fx.canRead());
                    dcmsnd.addFile(fx);
            }
            dcmsnd.setCalledAET(AETitle);
     
        dcmsnd.configureTransferCapability();
//            try {
//                dcmsnd.initTLS();
//            } catch (Exception e) {
//                System.err.println("ERROR: Failed to initialize TLS context:"
//                        + e.getMessage());
//                System.exit(2);
//            }
        
         dcmsnd.setMoveOriginatorMessageID(cmoveID);
         dcmsnd.start();
         dcmsnd.open();
         dcmsnd.send();
         
      
        }



}
