/*  Copyright   2008 João Pereira, Miguel Fonseca
 *              2007 Marco Pereira, Filipe Freitas
 *  This file is part of Dicoogle.
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

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dcm4che2.data.UID;

/**
 *
 * @author Marco Pereira
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 * @see XMLSupport
 * 
 */
public class ServerSettings
{
    private String AETitle;

    //Access List Settings
    private String [] CAETitle;
    private boolean permitAllAETitles;

    private String Path;
    private String ID;
    private int storagePort;

    private int rGUIPort;
    private String RGUIExternalIP;

    //Dicoogle Settings
    private String dicoogleDir;
    private boolean fullContentIndex;
    private boolean saveThumbnails;
    private String thumbnailsMatrix;
    //private boolean P2P;

    private boolean storage;
    private boolean queryRetrieve;
    private boolean encryptUsersFile;

    /** 
     * QueryRetrieve Server
     */
    
    private boolean wlsOn = false ; 
    
    /* DEFAULT Brief class description */
    private String deviceDescription ;
    /* DEFAULT Process Worklist Server AE Title */
    private String localAETName ;
    
    /* DEFAULT ("any"->null)Permited local interfaces to incomming connection
     * ('null'->any interface; 'ethx'->only this interface |->separator */
    private String permitedLocalInterfaces ;
    
    /* DEFAULT ("any"->null)Permited remote host name connections
     * ('null'->any can connect; 'www.x.com'->only this can connect |->separator */
    private String permitedRemoteHostnames ; 
    
    /* DEFAULT Dimse response timeout (in sec) */
    private int DIMSERspTimeout ;
    // Connection settings
    
    /* DEFAULT Listening TCP port */
    private int wlsPort ;
    /* DEFAULT Response delay (in miliseconds) */
    private int rspDelay ;
    /* DEFAULT Idle timeout (in sec) */
    private int idleTimeout ;
    /* DEFAULT Accept timeout (in sec) */
    private int acceptTimeout ;
    /* DEFAULT Connection timeout (in sec) */
    private int connectionTimeout ;
    
    
    private int maxMessages = 2000;
    private String SOPClass  ; 
    private String transfCAP ; 
    
     /* DEFAULT Max Client Associations */
    private int MAX_CLIENT_ASSOCS  ; 
   
    private int MAX_PDU_LENGTH_RECEIVE ;
    private int MAX_PDU_LENGTH_SEND ;


    HashMap<String, String> modalityFind = new HashMap<String, String>();

    ArrayList<MoveDestination> dest = new ArrayList<MoveDestination>();

    private boolean indexAnonymous = false;

    private boolean indexZIPFiles = true;
    
    private boolean monitorWatcher = false;
    


    /**
     * P2P
     */

    private String p2pLibrary = "JGroups";
    private String nodeName = "Dicoogle";
    private boolean nodeNameDefined = false ;

    private String networkInterfaceName ="";

    /** Indexer */
    private String indexer = "lucene2.2";
    private int indexerEffort = 0 ;
    private HashSet<String> extensionsAllowed = new HashSet();



    /**
     * @return the web
     */
    public Web getWeb()
    {
        return web;
    }

    /**
     * @param web the web to set
     */
    public void setWeb(Web web)
    {
        this.web = web;
    }

    /**
     * @return the p2pLibrary
     */
    public String getP2pLibrary() {
        return p2pLibrary;
    }

    /**
     * @param p2pLibrary the p2pLibrary to set
     */
    public void setP2pLibrary(String p2pLibrary) {
        this.p2pLibrary = p2pLibrary;
    }

    /**
     * @return the indexer
     */
    public String getIndexer() {
        return indexer;
    }

    /**
     * @param indexer the indexer to set
     */
    public void setIndexer(String indexer) {
        this.indexer = indexer;
    }

    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * @param nodeName the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return the nodeNameDefined
     */
    public boolean isNodeNameDefined() {
        return nodeNameDefined;
    }

    /**
     * @param nodeNameDefined the nodeNameDefined to set
     */
    public void setNodeNameDefined(boolean nodeNameDefined) {
        this.nodeNameDefined = nodeNameDefined;
    }

    public String getNetworkInterfaceName()
    {
        return networkInterfaceName;
    }

    public void setNetworkInterfaceName(String interfaceName)
    {
        this.networkInterfaceName = interfaceName;
    }


    /**
     * @return the indexerEffort
     */
    public int getIndexerEffort() {
        return indexerEffort;
    }

    /**
     * @param indexerEffort the indexerEffort to set
     */
    public void setIndexerEffort(int indexerEffort) {
        this.indexerEffort = indexerEffort;
    }

    /**
     * @return the encryptUsersFile
     */
    public boolean isEncryptUsersFile() {
        return encryptUsersFile;
    }

    /**
     * @param encryptUsersFile the encryptUsersFile to set
     */
    public void setEncryptUsersFile(boolean encryptUsersFile) {
        this.encryptUsersFile = encryptUsersFile;
    }

    /**
     * @return the indexZIPFiles
     */
    public boolean isIndexZIPFiles() {
        return indexZIPFiles;
    }

    /**
     * @param indexZIPFiles the indexZIPFiles to set
     */
    public void setIndexZIPFiles(boolean indexZIPFiles) {
        this.indexZIPFiles = indexZIPFiles;
    }

    /**
     * @return the RGUIExternalIP
     */
    public String getRGUIExternalIP() {
        return RGUIExternalIP;
    }

    /**
     * @param RGUIExternalIP the RGUIExternalIP to set
     */
    public void setRGUIExternalIP(String RGUIExternalIP) {
        this.RGUIExternalIP = RGUIExternalIP;
    }

    /**
     * @return the monitorWatcher
     */
    public boolean isMonitorWatcher() {
        return monitorWatcher;
    }

    /**
     * @param monitorWatcher the monitorWatcher to set
     */
    public void setMonitorWatcher(boolean monitorWatcher) {
        this.monitorWatcher = monitorWatcher;
    }

    /**
     * @return the indexAnonymous
     */
    public boolean isIndexAnonymous() {
        return indexAnonymous;
    }

    /**
     * @param indexAnonymous the indexAnonymous to set
     */
    public void setIndexAnonymous(boolean indexAnonymous) {
        this.indexAnonymous = indexAnonymous;
    }

    /**
     * Web (including web server, webservices, etc)
     */
    public class Web
    {

        private boolean webServer = false;
        private boolean webServices = false;

        private int serverPort = 8080 ;
        private int servicePort = 6060;

        public Web()
        {
        }

        /**
         * @return the webServer
         */
        public boolean isWebServer() {
            return webServer;
        }

        /**
         * @param webServer the webServer to set
         */
        public void setWebServer(boolean webServer) {
            this.webServer = webServer;
        }

        /**
         * @return the webServices
         */
        public boolean isWebServices() {
            return webServices;
        }

        /**
         * @param webServices the webServices to set
         */
        public void setWebServices(boolean webServices) {
            this.webServices = webServices;
        }

        /**
         * @return the serverPort
         */
        public int getServerPort() {
            return serverPort;
        }

        /**
         * @param serverPort the serverPort to set
         */
        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        /**
         * @return the servicePort
         */
        public int getServicePort() {
            return servicePort;
        }

        /**
         * @param servicePort the servicePort to set
         */
        public void setServicePort(int servicePort) {
            this.servicePort = servicePort;
        }

    }

    private Web web = new Web() ;



    
    private static ServerSettings instance = null;
    

    public static synchronized ServerSettings getInstance()
    {
        
        
        if (instance == null)
        {
            instance = new ServerSettings();
        }


        return instance;
    }

    private ServerSettings()
    {
        rGUIPort = 9014;
        storagePort = 104;
        AETitle = "NBIA";
        CAETitle = new String[0];
        permitAllAETitles = true;
        Path = "";
        dicoogleDir = "";
        fullContentIndex = false;
        saveThumbnails = false;
        thumbnailsMatrix = "64";

        encryptUsersFile = false;

        /**
         * Set default values of QueryRetrieve Server
         */

        this.deviceDescription = "NBIA - Server SCP" ;
        this.localAETName  = "NBIA Service";
        this.permitedLocalInterfaces = "any";
        this.permitedRemoteHostnames = "any";
        this.wlsPort = 1040 ;  // default: 104
        this.idleTimeout = 60 ;
        this.acceptTimeout = 60 ;
        this.rspDelay = 0 ;        
        this.DIMSERspTimeout = 60 ;
        this.connectionTimeout = 60 ;
        
        this.transfCAP = UID.ImplicitVRLittleEndian + "|" + UID.ExplicitVRBigEndian + "|" + UID.ExplicitVRLittleEndian;     

        this.SOPClass = UID.StudyRootQueryRetrieveInformationModelFIND 
        + "|" + UID.PatientRootQueryRetrieveInformationModelFIND;
               
        fillModalityFindDefault();
        this.MAX_CLIENT_ASSOCS = 20 ; 
        this.MAX_PDU_LENGTH_RECEIVE = 16364 ; 
        this.MAX_PDU_LENGTH_SEND = 16364 ;
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    // Nasty bug fix; no thumbnails references here = null pointers
    public void setDefaultSettings()
    {
        rGUIPort = 9014;
        storagePort = 6666;
        AETitle = "NBIA-STORAGE";
        Path = System.getProperty("java.io.tmpdir");
        CAETitle = new String[0];
        permitAllAETitles = true;
        dicoogleDir = System.getProperty("java.io.tmpdir");
        fullContentIndex = false;
        saveThumbnails = false;
        thumbnailsMatrix = "64";

        setEncryptUsersFile(false);
    }

    public void setAE(String AE)
    {
        AETitle = AE;
    }

    public String getAE()
    {
        return AETitle;
    }

    public void setID(String I)
    {
        ID = I;
    }

    public String getID()
    {
        return ID;
    }

    public void setCAET(String[] CAET)
    {
        CAETitle = CAET;            
    }

    public String[] getCAET()
    {
        return CAETitle;
    }

    public void setPermitAllAETitles(boolean value){
        permitAllAETitles = value;
    }

    public boolean getPermitAllAETitles(){
        return permitAllAETitles;
    }

    public void setStoragePort(int p)
    {
        storagePort = p;
    }

    public void setPath(String p)
    {
        Path = p;
    }

    public String getPath()
    {
        return Path;
    }

    public int getStoragePort()
    {
        return storagePort;
    }

    public void setRemoteGUIPort(int port){
        rGUIPort = port;
    }

    public int getRemoteGUIPort(){
        return rGUIPort;
    }

    public String getDicoogleDir() {
        return dicoogleDir;
    }

    public void setDicoogleDir(String dicoogleDir) {
        this.dicoogleDir = dicoogleDir;
    }


    public boolean getFullContentIndex() {
        return fullContentIndex;
    }

    public void setFullContentIndex(boolean fullContentIndex) {
        this.fullContentIndex = fullContentIndex;
    }

    public boolean getSaveThumbnails() {
        return saveThumbnails;
    }

    // Yet another bug...
    public void setSaveThumbnails(boolean saveThumbnails) {
        this.saveThumbnails = saveThumbnails;
    }
    
    public String getThumbnailsMatrix() {
        return thumbnailsMatrix;
    }

    public void setThumbnailsMatrix(String thumbnailsMatrix) {
        this.thumbnailsMatrix = thumbnailsMatrix;
    }

    /**
     * Query Retrieve Server
     */

    public void setWlsPort(int port)
    {
        this.wlsPort = port ;
    }

    public int getWlsPort()
    {
        return this.wlsPort ;
    }

    public void setIdleTimeout(int timeout)
    {
        this.idleTimeout = timeout ;
    }

    public int getIdleTimeout()
    {
        return this.idleTimeout ;
    }

    public void setRspDelay(int delay)
    {
        this.rspDelay = delay ;
    }

    public int getRspDelay()
    {
        return this.rspDelay  ;
    }

    public void setAcceptTimeout(int timeout)
    {
        this.acceptTimeout = timeout ;
    }
    public int getAcceptTimeout()
    {
        return this.acceptTimeout;
    }

    public void setConnectionTimeout(int timeout)
    {
        this.connectionTimeout = timeout; 
    }
    public int getConnectionTimeout()
    {
        return this.connectionTimeout ;
    }
    
    public void setSOPClass(String SOPClass)
    {
        this.SOPClass = SOPClass ;
    }
    
    public String[] getSOPClasses()
    {
        String []tmp = {
            UID.StudyRootQueryRetrieveInformationModelFIND ,
            UID.PatientRootQueryRetrieveInformationModelFIND
        };
        return tmp ; 
    }

    public String getSOPClass()
    {
        return this.SOPClass ; 
    }
    public void setDIMSERspTimeout(int timeout)
    {
        this.DIMSERspTimeout = timeout ; 
    }
    public int getDIMSERspTimeout()
    {
        return this.DIMSERspTimeout ; 
    }
    public void setDeviceDescription(String desc)
    {
        this.deviceDescription = desc ; 
    }
    
    public String getDeviceDescription()
    {
        return this.deviceDescription;
    }
    
    public void setTransfCap(String transfCap)
    {
        this.transfCAP = transfCap;
    }
        
    public String getTransfCap()
    {
        return this.transfCAP ; 
    }
    
    public void setMaxClientAssoc(int maxClients)
    {
        this.MAX_CLIENT_ASSOCS = maxClients ; 
    }
    
    public int getMaxClientAssoc()
    {
        return this.MAX_CLIENT_ASSOCS ; 
    }
    
    public void setMaxPDULengthReceive(int len)
    {
        this.MAX_PDU_LENGTH_RECEIVE = len ;
    }
    
    public int getMaxPDULengthReceive()
    {
        return this.MAX_PDU_LENGTH_RECEIVE ; 
    }
    public void setMaxPDULengthSend(int len)
    {
        this.MAX_PDU_LENGTH_SEND = len ;
    }
    public int getMaxPDULenghtSend()
    {
        return this.MAX_PDU_LENGTH_SEND; 
    }
    
    public void setLocalAETName(String name)
    {
        this.localAETName = name ; 
    }
    public String getLocalAETName()
    {
        return this.localAETName ; 
    }
    
    public void setPermitedLocalInterfaces(String localInterfaces)
    {
        this.permitedLocalInterfaces  = localInterfaces ; 
    }
    
    public String getPermitedLocalInterfaces()
    {
        return this.permitedLocalInterfaces ; 
    }
    
    public void setPermitedRemoteHostnames(String remoteHostnames)
    {
        this.permitedRemoteHostnames = remoteHostnames ; 
    }
    public String getPermitedRemoteHostnames()
    {
        return this.permitedRemoteHostnames;
    }

    /**
     * @return the P2P
     */
   /* public boolean isP2P() {
        return P2P;
    }*/
    public boolean isStorage() {
        return storage;
    }
    public boolean isQueryRetrive() {
        return queryRetrieve;
    }



    public void add(MoveDestination m)
    {
        this.dest.add(m);
    }
    public boolean remove(MoveDestination m)
    {
        return this.dest.remove(m);
    }
    public boolean contains(MoveDestination m){
        return this.dest.contains(m);
    }
    public ArrayList<MoveDestination> getMoves()
    {
        return this.dest ;
    }
    public void setMoves(ArrayList<MoveDestination> moves)
    {
        if(moves != null)
            this.dest = moves;
    }



    private void fillModalityFindDefault()
    {
         addModalityFind("1.2.840.10008.5.1.4.1.2.2.1",
                 "Study Root Query/Retrieve Information Model");

         addModalityFind("1.2.840.10008.5.1.4.1.2.1.1",
                    "Patient Root Query/Retrieve Information Model"
                 );

    }

    /**
     * Set default values
     */
    public void setDefaultsValues()
    {
        this.fillModalityFindDefault();
    }

    /**
     * Add a modality
     * @param sop Number like 1.2.3.5.6.7.32.1
     * @param description Description like "Modality Worklist Model"
     */
    public void addModalityFind(String sop, String description)
    {
        this.modalityFind.put(sop, description);
    }

    /**
     *
     * @return HashMap with Modalitys FIND
     */
    public HashMap getModalityFind()
    {
        return this.modalityFind;
    }



    /** P2P Mode */

    /**
     * @param P2P the P2P to set
     */
  /*  public void setP2P(boolean P2P)
    {
        this.P2P = P2P;
    }*/

    public void setStorage(boolean storage)
    {
        this.storage = storage;
    }

    public void setQueryRetrive(boolean queryRetrieve)
    {
        this.queryRetrieve = queryRetrieve;
    }

    public ArrayList<String> getNetworkInterfacesNames()
    {
        ArrayList<String> interfaces = new ArrayList<String>();
        Enumeration<NetworkInterface> nets = null;
        try
        {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex)
        {
            ex.printStackTrace();
        }

        
        for (NetworkInterface netint : Collections.list(nets))
        {
            try
            {
                if (!netint.isLoopback())
                {
                    Enumeration<InetAddress> addresses = netint.getInetAddresses();
                    while (addresses.hasMoreElements())
                    {
                        if (Inet4Address.class.isInstance(addresses.nextElement()))
                        {
                            interfaces.add(netint.getDisplayName());
                        }
                    }
                }
            } catch (SocketException ex)
            {
                Logger.getLogger(ServerSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return interfaces;
    }

    public String getNetworkInterfaceAddress()
    {
        Enumeration<NetworkInterface> nets = null;
        try
        {
            nets = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ex)
        {
            ex.printStackTrace();
        }

        for(NetworkInterface netint : Collections.list(nets))
        {
            if(netint.getDisplayName().compareTo(this.networkInterfaceName) == 0)
            {
                Enumeration<InetAddress> addresses = netint.getInetAddresses();
                while(addresses.hasMoreElements())
                {
                    InetAddress address = addresses.nextElement();
                    if(Inet4Address.class.isInstance(address))
                    {
                        return address.getHostAddress();
                    }
                }
                return null;
            }
        }
        return null;
    }
    /**
     * Add an extension to list of allowed indexing extensions.
     * <p>
     * All extensions should be added (ie, dicom, etc).
     *
     * @param ext          It is the extensions of files that should be indexed.
     * <b>empty</b> string means that documents without extension will be indexed.
     * 
     * @see   IndexEngine
     */
    public void addExtension(String ext)
    {
        this.extensionsAllowed.add(ext);
    }



    public HashSet<String> getExtensionsAllowed()
    {
        return extensionsAllowed;
    }

    /**
     * @return the maxMessages
     */
    public int getMaxMessages() {
        return maxMessages;
    }

    /**
     * @param maxMessages the maxMessages to set
     */
    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
    }

}
