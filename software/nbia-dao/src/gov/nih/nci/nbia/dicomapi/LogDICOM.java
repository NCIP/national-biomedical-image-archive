/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.nih.nci.nbia.dicomapi;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerConfigurationException;




/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class LogDICOM
{

    private static LogDICOM instance = null ;

    private static Semaphore sem = new Semaphore(1, true);


    private ArrayList<LogLine> ll = new ArrayList<LogLine>(); 


    private LogDICOM()
    {
        // Nothing to do.
    }


    public static synchronized LogDICOM getInstance()
    {
        try
        {
            sem.acquire();
            if (instance == null)
            {
                instance = new LogDICOM();
            }
            sem.release();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(LogDICOM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instance;
    }


    public void addLine(LogLine l)
    {
        this.getLl().add(l);
    }

    public void clearLog(){
        this.getLl().clear();
        
        try {
            LogXML log = new LogXML();
            log.printXML();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(LogDICOM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the ll
     */
    public ArrayList<LogLine> getLl()
    {
        return ll;
    }

    /**
     * @param ll the ll to set
     */
    public void setLl(ArrayList<LogLine> ll)
    {
        this.ll = ll;
    }



}
