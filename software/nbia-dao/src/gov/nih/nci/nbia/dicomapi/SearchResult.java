package gov.nih.nci.nbia.dicomapi;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Simple holder class to store one hit from search...
 * 
 * @author Marco
 * modified by: Carlos Ferreira
 * modified by: Pedro Bento
 */
public class SearchResult implements Serializable
{
    private String FileHash;
    private String Size;
    private Hashtable<String, String> extrafields;
    private String FileName;
    
    private String Origin;
    private String PluginName;

    public SearchResult(String FH, String name, String P, String S, Hashtable<String, String> EF, String PN)
    {
        FileHash = FH;
        Origin = P;
        Size = S;
        extrafields = EF;
        PluginName = PN;
        FileName = name;
    }

    /**
     * Get the name of the file
     * @return the name of the file
     */
    public String getFileName()
    {
        return FileName;
    }

    /**
     * Gets the hash of the file
     * @return the hash of the file
     */
    public String getFileHash()
    {
        return FileHash;
    }

    /**
     * Gets the path to the file
     * @return the path to the file
     */
    public String getOrigin()
    {
        return Origin;
    }

    /**
     * Gets the file size
     * @return the file size
     */
    public String getSize()
    {
        return Size;
    }

    /**
     * Gets the extra documents fields
     * @return the table containing the extra fields
     */
    public Hashtable<String, String> getExtrafields()
    {
        return extrafields;
    }

    public String getPluginName()
    {
        return PluginName;
    }


    @Override
    public String toString()
    {
        return this.Origin;
    }
}
