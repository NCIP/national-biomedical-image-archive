/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.nih.nci.nbia.dicomapi;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class LogLine implements Serializable
{
    static final long serialVersionUID = 1L;

    private String type;
    private String date;
    private String ae;
    private String add;

    public LogLine(String type, String date, String ae, String add)
    {
        this.type = type ; 
        this.date = date ; 
        this.ae = ae ; 
        this.add = add ; 
    }

     public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }



    /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return the date
     */
    public String getDate()
    {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date)
    {
        this.date = date;
    }

    /**
     * @return the ae
     */
    public String getAe()
    {
        return ae;
    }

    /**
     * @param ae the ae to set
     */
    public void setAe(String ae)
    {
        this.ae = ae;
    }

    /**
     * @return the add
     */
    public String getAdd()
    {
        return add;
    }

    /**
     * @param add the add to set
     */
    public void setAdd(String add)
    {
        this.add = add;
    }


}
