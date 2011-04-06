package gov.nih.nci.ncia.criteria;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;


public class ImagingObservationCharacteristicCodeValuePairCriteria extends MultipleValuePersistentCriteria
                                                                   implements Serializable {

    public ImagingObservationCharacteristicCodeValuePairCriteria() {
    }


    public Collection<String> getImagingObservationCharacteristicCodeValuePairs() {
        return imagingObservationCharacteristicCodeValuePairs;
    }

    public void setImagingObservationCharacteristicCodeValuePairs(Collection<String> codeValuePairs) {
        for (String codeValuePair : codeValuePairs) {
            getCreateCollectionObjects().add(codeValuePair);
        }
    }


    public void addValueFromQueryAttribute(QueryAttributeWrapper attr) {
    	this.getCreateCollectionObjects().add(attr.getAttributeValue());
    }


    /////////////////////////////////PROTECTED//////////////////////////////////////

    protected Collection<String> getMultipleValues() {
        return getImagingObservationCharacteristicCodeValuePairs();
    }

    /////////////////////////////PRIVATE//////////////////////////////////
    private Collection<String> imagingObservationCharacteristicCodeValuePairs;

    private Collection<String> getCreateCollectionObjects() {
        if (imagingObservationCharacteristicCodeValuePairs == null) {
        	imagingObservationCharacteristicCodeValuePairs = new ArrayList<String>();
        }

        return imagingObservationCharacteristicCodeValuePairs;
    }

}