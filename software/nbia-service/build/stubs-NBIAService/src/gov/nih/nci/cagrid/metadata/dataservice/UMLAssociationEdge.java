/**
 * UMLAssociationEdge.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class UMLAssociationEdge  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference UMLClassReference;
    private int maxCardinality;  // attribute
    private int minCardinality;  // attribute
    private java.lang.String roleName;  // attribute

    public UMLAssociationEdge() {
    }

    public UMLAssociationEdge(
           gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference UMLClassReference,
           int maxCardinality,
           int minCardinality,
           java.lang.String roleName) {
           this.UMLClassReference = UMLClassReference;
           this.maxCardinality = maxCardinality;
           this.minCardinality = minCardinality;
           this.roleName = roleName;
    }


    /**
     * Gets the UMLClassReference value for this UMLAssociationEdge.
     * 
     * @return UMLClassReference
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference getUMLClassReference() {
        return UMLClassReference;
    }


    /**
     * Sets the UMLClassReference value for this UMLAssociationEdge.
     * 
     * @param UMLClassReference
     */
    public void setUMLClassReference(gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference UMLClassReference) {
        this.UMLClassReference = UMLClassReference;
    }


    /**
     * Gets the maxCardinality value for this UMLAssociationEdge.
     * 
     * @return maxCardinality
     */
    public int getMaxCardinality() {
        return maxCardinality;
    }


    /**
     * Sets the maxCardinality value for this UMLAssociationEdge.
     * 
     * @param maxCardinality
     */
    public void setMaxCardinality(int maxCardinality) {
        this.maxCardinality = maxCardinality;
    }


    /**
     * Gets the minCardinality value for this UMLAssociationEdge.
     * 
     * @return minCardinality
     */
    public int getMinCardinality() {
        return minCardinality;
    }


    /**
     * Sets the minCardinality value for this UMLAssociationEdge.
     * 
     * @param minCardinality
     */
    public void setMinCardinality(int minCardinality) {
        this.minCardinality = minCardinality;
    }


    /**
     * Gets the roleName value for this UMLAssociationEdge.
     * 
     * @return roleName
     */
    public java.lang.String getRoleName() {
        return roleName;
    }


    /**
     * Sets the roleName value for this UMLAssociationEdge.
     * 
     * @param roleName
     */
    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UMLAssociationEdge)) return false;
        UMLAssociationEdge other = (UMLAssociationEdge) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UMLClassReference==null && other.getUMLClassReference()==null) || 
             (this.UMLClassReference!=null &&
              this.UMLClassReference.equals(other.getUMLClassReference()))) &&
            this.maxCardinality == other.getMaxCardinality() &&
            this.minCardinality == other.getMinCardinality() &&
            ((this.roleName==null && other.getRoleName()==null) || 
             (this.roleName!=null &&
              this.roleName.equals(other.getRoleName())));
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
        if (getUMLClassReference() != null) {
            _hashCode += getUMLClassReference().hashCode();
        }
        _hashCode += getMaxCardinality();
        _hashCode += getMinCardinality();
        if (getRoleName() != null) {
            _hashCode += getRoleName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UMLAssociationEdge.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociationEdge"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("maxCardinality");
        attrField.setXmlName(new javax.xml.namespace.QName("", "maxCardinality"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("minCardinality");
        attrField.setXmlName(new javax.xml.namespace.QName("", "minCardinality"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("roleName");
        attrField.setXmlName(new javax.xml.namespace.QName("", "roleName"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UMLClassReference");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClassReference"));
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
