package gov.nih.nci.nbia.dicomapi;

import java.io.Serializable;
import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.data.VR;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class TagValue implements Serializable {

    static final long serialVersionUID = 1L;
    
    /** tag value - (group, subgroup) in hex coding */ 
    private int tag = 0  ;
    /** Public name like Patient Name */
    private String name = null ;
    /** Alias to the name like PatientName */
    private String alias = null ;

    /**  ValueRepresentation */
    private String tgVR = null ;
    /** RetiredStatus - Deprecated mode */
    private int   ret  = 0 ;

    /** Value Multiciply */
    private int VM = 0 ;

    public TagValue(int tag, String alias)
    {
        this.tag = tag ;
        this.alias = alias ;
        
        VR tagVR = (new BasicDicomObject()).vrOf(tag);
        
        if(tagVR != null)
            tgVR = tagVR.toString();
    }
    /**
     * Get The Tag Number (group, subgroup)
     * @return int
     */
    public int getTagNumber()
    {
        return this.tag  ;
    }
    /**
     * A formal name
     * @return String
     */
    public String getName()
    {
        return this.name ;
    }
    /**
     * Alias
     * @return String
     */
    public String getAlias()
    {
        return this.alias ;
    }
    /**
     * Verify if it is deprecated
     * @return int
     */
    public int getRet()
    {
        return this.ret ;
    }



    public static String getGroup(int tag)
    {
        String result = Integer.toHexString(
                (tag & 0xFFFF0000 )>> 16) ;

        for (int i=0 ; i<6-result.length() ; i++)
        {
            if (result.length()==4)
                break ;
            result = "0" + result ;
        }

        return result ;

    }

    public static String getSubgroup(int tag)
    {
        String result =  Integer.toHexString(tag & 0x0000FFFF ) ;
        for (int i=0 ; i<5-result.length() ; i++)
        {
            if (result.length()==4)
                break ; 
            result = "0" + result ;
        }
        return result ; 
    }

    /**
     * @return the VR
     */
    public String getVR() {
        return tgVR;
    }

    /**
     * @param VR the VR to set
     */
    public void setVR(String VR) {
        this.tgVR = VR;
    }

    /**
     * @return the VM
     */
    public int getVM() {
        return VM;
    }

    /**
     * @param VM the VM to set
     */
    public void setVM(int VM) {
        this.VM = VM;
    }

}
