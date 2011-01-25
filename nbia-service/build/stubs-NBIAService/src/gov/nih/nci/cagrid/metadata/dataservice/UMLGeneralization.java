/**
 * UMLGeneralization.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class UMLGeneralization  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference subClassReference;
    private gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference superClassReference;

    public UMLGeneralization() {
    }

    public UMLGeneralization(
           gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference subClassReference,
           gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference superClassReference) {
           this.subClassReference = subClassReference;
           this.superClassReference = superClassReference;
    }


    /**
     * Gets the subClassReference value for this UMLGeneralization.
     * 
     * @return subClassReference
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference getSubClassReference() {
        return subClassReference;
    }


    /**
     * Sets the subClassReference value for this UMLGeneralization.
     * 
     * @param subClassReference
     */
    public void setSubClassReference(gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference subClassReference) {
        this.subClassReference = subClassReference;
    }


    /**
     * Gets the superClassReference value for this UMLGeneralization.
     * 
     * @return superClassReference
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference getSuperClassReference() {
        return superClassReference;
    }


    /**
     * Sets the superClassReference value for this UMLGeneralization.
     * 
     * @param superClassReference
     */
    public void setSuperClassReference(gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference superClassReference) {
        this.superClassReference = superClassReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UMLGeneralization)) return false;
        UMLGeneralization other = (UMLGeneralization) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subClassReference==null && other.getSubClassReference()==null) || 
             (this.subClassReference!=null &&
              this.subClassReference.equals(other.getSubClassReference()))) &&
            ((this.superClassReference==null && other.getSuperClassReference()==null) || 
             (this.superClassReference!=null &&
              this.superClassReference.equals(other.getSuperClassReference())));
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
        if (getSubClassReference() != null) {
            _hashCode += getSubClassReference().hashCode();
        }
        if (getSuperClassReference() != null) {
            _hashCode += getSuperClassReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UMLGeneralization.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLGeneralization"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subClassReference");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "subClassReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClassReference"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("superClassReference");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "superClassReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClassReference"));
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
