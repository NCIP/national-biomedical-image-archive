/**
 * DomainModelExposedUMLAssociationCollection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class DomainModelExposedUMLAssociationCollection  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] UMLAssociation;

    public DomainModelExposedUMLAssociationCollection() {
    }

    public DomainModelExposedUMLAssociationCollection(
           gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] UMLAssociation) {
           this.UMLAssociation = UMLAssociation;
    }


    /**
     * Gets the UMLAssociation value for this DomainModelExposedUMLAssociationCollection.
     * 
     * @return UMLAssociation
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] getUMLAssociation() {
        return UMLAssociation;
    }


    /**
     * Sets the UMLAssociation value for this DomainModelExposedUMLAssociationCollection.
     * 
     * @param UMLAssociation
     */
    public void setUMLAssociation(gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] UMLAssociation) {
        this.UMLAssociation = UMLAssociation;
    }

    public gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation getUMLAssociation(int i) {
        return this.UMLAssociation[i];
    }

    public void setUMLAssociation(int i, gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation _value) {
        this.UMLAssociation[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DomainModelExposedUMLAssociationCollection)) return false;
        DomainModelExposedUMLAssociationCollection other = (DomainModelExposedUMLAssociationCollection) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UMLAssociation==null && other.getUMLAssociation()==null) || 
             (this.UMLAssociation!=null &&
              java.util.Arrays.equals(this.UMLAssociation, other.getUMLAssociation())));
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
        if (getUMLAssociation() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUMLAssociation());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUMLAssociation(), i);
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
        new org.apache.axis.description.TypeDesc(DomainModelExposedUMLAssociationCollection.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>exposedUMLAssociationCollection"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UMLAssociation");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociation"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociation"));
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
