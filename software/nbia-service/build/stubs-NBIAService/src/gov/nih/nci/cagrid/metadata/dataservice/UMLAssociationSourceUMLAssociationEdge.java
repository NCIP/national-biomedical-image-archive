/**
 * UMLAssociationSourceUMLAssociationEdge.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;

public class UMLAssociationSourceUMLAssociationEdge  implements java.io.Serializable {
    private gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge UMLAssociationEdge;

    public UMLAssociationSourceUMLAssociationEdge() {
    }

    public UMLAssociationSourceUMLAssociationEdge(
           gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge UMLAssociationEdge) {
           this.UMLAssociationEdge = UMLAssociationEdge;
    }


    /**
     * Gets the UMLAssociationEdge value for this UMLAssociationSourceUMLAssociationEdge.
     * 
     * @return UMLAssociationEdge
     */
    public gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge getUMLAssociationEdge() {
        return UMLAssociationEdge;
    }


    /**
     * Sets the UMLAssociationEdge value for this UMLAssociationSourceUMLAssociationEdge.
     * 
     * @param UMLAssociationEdge
     */
    public void setUMLAssociationEdge(gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge UMLAssociationEdge) {
        this.UMLAssociationEdge = UMLAssociationEdge;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UMLAssociationSourceUMLAssociationEdge)) return false;
        UMLAssociationSourceUMLAssociationEdge other = (UMLAssociationSourceUMLAssociationEdge) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UMLAssociationEdge==null && other.getUMLAssociationEdge()==null) || 
             (this.UMLAssociationEdge!=null &&
              this.UMLAssociationEdge.equals(other.getUMLAssociationEdge())));
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
        if (getUMLAssociationEdge() != null) {
            _hashCode += getUMLAssociationEdge().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UMLAssociationSourceUMLAssociationEdge.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", ">UMLAssociation>sourceUMLAssociationEdge"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UMLAssociationEdge");
        elemField.setXmlName(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociationEdge"));
        elemField.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLAssociationEdge"));
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
