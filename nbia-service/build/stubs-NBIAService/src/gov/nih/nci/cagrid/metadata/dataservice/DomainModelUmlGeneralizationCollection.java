/**
 * DomainModelUmlGeneralizationCollection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class DomainModelUmlGeneralizationCollection  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization[] UMLGeneralization;

    public DomainModelUmlGeneralizationCollection() {
    }

    public DomainModelUmlGeneralizationCollection(
           gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization[] UMLGeneralization) {
           this.UMLGeneralization = UMLGeneralization;
    }


    /**
     * Gets the UMLGeneralization value for this DomainModelUmlGeneralizationCollection.
     * 
     * @return UMLGeneralization
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization[] getUMLGeneralization() {
        return UMLGeneralization;
    }


    /**
     * Sets the UMLGeneralization value for this DomainModelUmlGeneralizationCollection.
     * 
     * @param UMLGeneralization
     */
    public void setUMLGeneralization(gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization[] UMLGeneralization) {
        this.UMLGeneralization = UMLGeneralization;
    }

    public gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization getUMLGeneralization(int i) {
        return this.UMLGeneralization[i];
    }

    public void setUMLGeneralization(int i, gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization _value) {
        this.UMLGeneralization[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DomainModelUmlGeneralizationCollection)) return false;
        DomainModelUmlGeneralizationCollection other = (DomainModelUmlGeneralizationCollection) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UMLGeneralization==null && other.getUMLGeneralization()==null) || 
             (this.UMLGeneralization!=null &&
              java.util.Arrays.equals(this.UMLGeneralization, other.getUMLGeneralization())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getUMLGeneralization() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUMLGeneralization());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUMLGeneralization(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DomainModelUmlGeneralizationCollection.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>umlGeneralizationCollection"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UMLGeneralization");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLGeneralization"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLGeneralization"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
