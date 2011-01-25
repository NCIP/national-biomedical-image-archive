/**
 * DomainModelExposedUMLClassCollection.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class DomainModelExposedUMLClassCollection  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] UMLClass;

    public DomainModelExposedUMLClassCollection() {
    }

    public DomainModelExposedUMLClassCollection(
           gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] UMLClass) {
           this.UMLClass = UMLClass;
    }


    /**
     * Gets the UMLClass value for this DomainModelExposedUMLClassCollection.
     * 
     * @return UMLClass
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] getUMLClass() {
        return UMLClass;
    }


    /**
     * Sets the UMLClass value for this DomainModelExposedUMLClassCollection.
     * 
     * @param UMLClass
     */
    public void setUMLClass(gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] UMLClass) {
        this.UMLClass = UMLClass;
    }

    public gov.nih.nci.cagrid.metadata.dataservice.UMLClass getUMLClass(int i) {
        return this.UMLClass[i];
    }

    public void setUMLClass(int i, gov.nih.nci.cagrid.metadata.dataservice.UMLClass _value) {
        this.UMLClass[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DomainModelExposedUMLClassCollection)) return false;
        DomainModelExposedUMLClassCollection other = (DomainModelExposedUMLClassCollection) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UMLClass==null && other.getUMLClass()==null) || 
             (this.UMLClass!=null &&
              java.util.Arrays.equals(this.UMLClass, other.getUMLClass())));
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
        if (getUMLClass() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUMLClass());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUMLClass(), i);
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
        new org.apache.axis.description.TypeDesc(DomainModelExposedUMLClassCollection.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">DomainModel>exposedUMLClassCollection"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UMLClass");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClass"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClass"));
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
