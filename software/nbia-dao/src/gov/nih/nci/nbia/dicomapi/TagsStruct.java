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


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class TagsStruct
{
    /** Dim fields */
    private Hashtable<Integer, TagValue> dimFields = new Hashtable<Integer, TagValue>();
    /** Manual fields */
    private Hashtable<Integer, TagValue> manualFields = new Hashtable<Integer, TagValue>();
    /** */
    private ArrayList<String> modalities = new ArrayList<String>() ;
    
    
    private List<String> dictionaries = new ArrayList<String>();
    
    

    /** It keep a copy of all fields,
     *  then it will optimize the get fields
     */
    private ArrayList<String> fields  = new ArrayList<String>();

    private boolean Others = false  ;
    private boolean Modalities = false ;

    private boolean indexAllModalities = false;


    private static TagsStruct instance = null ;
    private static Semaphore sem = new Semaphore(1, true);

    public static synchronized TagsStruct getInstance()
    {
        try
        {
            sem.acquire();
            if (instance == null)
            {
                instance = new TagsStruct();
            }
            sem.release();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(TagsStruct.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instance;
    }


    private TagsStruct()
    {
        dimFields.put(Integer.parseInt("1021c0", 16), new TagValue(
                Integer.parseInt("1021c0", 16),"PregnancyStatus"));
        dimFields.put(Integer.parseInt("81050", 16), new TagValue(
                Integer.parseInt("81050", 16),"PerformingPhysicianName"));
        dimFields.put(Integer.parseInt("400243", 16), new TagValue(
                Integer.parseInt("400243", 16),"PerformedLocation"));
        dimFields.put(Integer.parseInt("100020", 16), new TagValue(
                Integer.parseInt("100020", 16),"PatientID"));
        dimFields.put(Integer.parseInt("80080", 16), new TagValue(
                Integer.parseInt("80080", 16),"InstitutionName"));
        dimFields.put(Integer.parseInt("80050", 16), new TagValue(
                Integer.parseInt("80050", 16),"AccessionNumber"));
        dimFields.put(Integer.parseInt("80020", 16), new TagValue(
                Integer.parseInt("80020", 16),"StudyDate"));
        dimFields.put(Integer.parseInt("102154", 16), new TagValue(
                Integer.parseInt("102154", 16),"PatientTelephoneNumbers"));
        dimFields.put(Integer.parseInt("101010", 16), new TagValue(
                Integer.parseInt("101010", 16),"PatientAge"));
        dimFields.put(Integer.parseInt("81070", 16), new TagValue(
                Integer.parseInt("81070", 16),"OperatorName"));
        dimFields.put(Integer.parseInt("200010", 16), new TagValue(
                Integer.parseInt("200010", 16),"StudyID"));
        dimFields.put(Integer.parseInt("81040", 16), new TagValue(
                Integer.parseInt("81040", 16),"InstitutionDepartmentName"));
        dimFields.put(Integer.parseInt("20000e", 16), new TagValue(
                Integer.parseInt("20000e", 16),"SeriesInstanceUID"));
        dimFields.put(Integer.parseInt("20000d", 16), new TagValue(
                Integer.parseInt("20000d", 16),"StudyInstanceUID"));
        dimFields.put(Integer.parseInt("100040", 16), new TagValue(
                Integer.parseInt("100040", 16),"PatientSex"));
        dimFields.put(Integer.parseInt("201208", 16), new TagValue(
                Integer.parseInt("201208", 16),"NumberOfStudyRelatedInstances"));
        dimFields.put(Integer.parseInt("100010", 16), new TagValue(
                Integer.parseInt("100010", 16),"PatientName"));
        dimFields.put(Integer.parseInt("80070", 16), new TagValue(
                Integer.parseInt("80070", 16),"Manufacturer"));
        dimFields.put(Integer.parseInt("81090", 16), new TagValue(
                Integer.parseInt("81090", 16),"ManufacturerModelName"));
        dimFields.put(Integer.parseInt("81030", 16), new TagValue(
                Integer.parseInt("81030", 16),"StudyDescription"));
        dimFields.put(Integer.parseInt("700", 16), new TagValue(
                Integer.parseInt("700", 16),"Priority"));
        dimFields.put(Integer.parseInt("80090", 16), new TagValue(
                Integer.parseInt("80090", 16),"ReferringPhysicianName"));
        dimFields.put(Integer.parseInt("80061", 16), new TagValue(
                Integer.parseInt("80061", 16),"ModalitiesInStudy"));
        dimFields.put(Integer.parseInt("80060", 16), new TagValue(
                Integer.parseInt("80060", 16),"Modality"));
        dimFields.put(Integer.parseInt("80030", 16), new TagValue(
                Integer.parseInt("80030", 16),"StudyTime"));
        dimFields.put(Integer.parseInt("80018", 16), new TagValue(
                Integer.parseInt("80018", 16),"SOPInstanceUID"));

        fields.add("SOPInstanceUID");
        fields.add("PregnancyStatus");
        fields.add("PerformingPhysicianName");
        fields.add("PerformedLocation");
        fields.add("PatientID");
        fields.add("InstitutionName");
        fields.add("AccessionNumber");
        fields.add("StudyDate");
        fields.add("PatientTelephoneNumbers");
        fields.add("PatientAge");
        fields.add("OperatorName");
        fields.add("StudyID");
        fields.add("InstitutionDepartmentName");
        fields.add("SeriesInstanceUID");
        fields.add("StudyInstanceUID");
        fields.add("PatientSex");
        fields.add("NumberOfStudyRelatedInstances");
        fields.add("PatientName");
        fields.add("Manufacturer");
        fields.add("ManufacturerModelName");
        fields.add("StudyDescription");
        fields.add("Priority");
        fields.add("ReferringPhysicianName");
        fields.add("ModalitiesInStudy");
        fields.add("Modality");
        fields.add("StudyTime");

        modalities.add("XA");
        modalities.add("CT");
        modalities.add("US");
        modalities.add("MG");
        modalities.add("MR");
        Modalities=true;
    }

    public ArrayList<String> getDIMTags()
    {
        ArrayList<String> _result = new ArrayList<String>();
        Set<Integer> keys  = this.dimFields.keySet();

        for ( int key : keys )
        {
            _result.add(this.dimFields.get(key).getAlias());
        }

        return _result ;

    }

    public void addModality(String mod)
    {
        this.modalities.add(mod);
    }

    public void removeModality(String mod)
    {
        this.modalities.remove(mod);
    }


    public void addDIM(TagValue tag )
    {
        // Insert it !

        this.getDimFields().put(tag.getTagNumber(), tag);

        // Insert into String list
        this.fields.add(tag.getAlias());

    }
    public void addOthers(TagValue tag)
    {
        // Verify if it exists in DIM
        // Otherwise it could be inserted on others

         if (!this.dimFields.containsKey(tag.getTagNumber()))
         {
            this.manualFields.put(tag.getTagNumber(), tag);
            this.fields.add(tag.getAlias());
         }
    }



    public ArrayList<String> getAllFields()
    {
        ArrayList<String> result = new ArrayList<String>();




        return result ;
    }



    public boolean isAllModalietiesEnable()
    {
        return getModalities() != null ;
    }
    public boolean isModalityEnable(String modality)
    {
        boolean result = false ;
        if (getModalities() == null )
            result =  false ;
        else
        {
            result = getModalities().contains(modality);
        }
        return result ;
    }

    public void setDefaults()
    {
        // TODO
    }




    /**
     * @return the dimFields
     */
    public Hashtable<Integer, TagValue> getDimFields()
    {
        return dimFields;
    }

    /**
     * @param dimFields the dimFields to set
     */
    public void setDimFields(Hashtable<Integer, TagValue> dimFields)
    {
        this.dimFields = dimFields;
    }

    /**
     * @return the manualFields
     */
    public Hashtable<Integer, TagValue> getManualFields()
    {
        return manualFields;
    }

    /**
     * @param manualFields the manualFields to set
     */
    public void setManualFields(Hashtable<Integer, TagValue> manualFields)
    {
        this.manualFields = manualFields;
    }

    /**
     * @return the modalities
     */
    public ArrayList<String> getModalities()
    {
        return modalities;
    }

    /**
     * @param modalities the modalities to set
     */
    public void setModalities(ArrayList<String> modalities)
    {
        this.modalities = modalities;
    }

    /**
     * @return the havaOthers
     */
    public boolean isOthers()
    {
        return Others;
    }

    /**
     * @param havaOthers the havaOthers to set
     */
    public void setOthers(boolean havaOthers)
    {
        this.Others = havaOthers;
    }

    /**
     * @return the haveModalities
     */
    public boolean isModalities()
    {
        return this.modalities!=null;
    }

    /**
     * @return the indexAllModalities
     */
    public boolean isIndexAllModalities() {
        return indexAllModalities;
    }

    /**
     * @param indexAllModalities the indexAllModalities to set
     */
    public void setIndexAllModalities(boolean indexAllModalities) {
        this.indexAllModalities = indexAllModalities;
    }

    /**
     * @return the dictionaries
     */
    public List<String> getDictionaries() {
        return dictionaries;
    }

    /**
     * @param dictionaries the dictionaries to set
     */
    public void setDictionaries(List<String> dictionaries) {
        this.dictionaries = dictionaries;
    }
    
    
    public void addDicionary(String dic)
    {
        this.dictionaries.add(dic);
    }
    public void removeDicionary(String dic)
    {
        this.dictionaries.remove(dic);
    }


}
