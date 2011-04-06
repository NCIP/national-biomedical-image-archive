package gov.nih.nci.ncia.criteria;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;


public class ImagingObservationCharacteristicQuantificationCriteria extends MultipleValuePersistentCriteria
                                                                    implements Serializable {

    public ImagingObservationCharacteristicQuantificationCriteria() {
    }


    public Collection<String> getImagingObservationCharacteristicQuantifications() {
        return quantifications;
    }

    public void setImagingObservationCharacteristicQuantifications(Collection<String> quantifications) {
        for (String quantification : quantifications) {
            getCreateCollectionObjects().add(quantification);
        }
    }


    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
    	this.getCreateCollectionObjects().add(attr.getAttributeValue());
    }


    /////////////////////////////////PROTECTED//////////////////////////////////////

    protected Collection<String> getMultipleValues() {
        return getImagingObservationCharacteristicQuantifications();
    }

    /////////////////////////////PRIVATE//////////////////////////////////
    private Collection<String> quantifications;

    private Collection<String> getCreateCollectionObjects() {
        if (quantifications == null) {
        	quantifications = new ArrayList<String>();
        }

        return quantifications;
    }

}