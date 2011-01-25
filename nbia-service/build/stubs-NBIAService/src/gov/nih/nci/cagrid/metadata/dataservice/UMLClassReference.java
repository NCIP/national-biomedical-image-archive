/**
 * UMLClassReference.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.metadata.dataservice;


/**
 * Represents a "pointer/reference" to a UMLClass exposed by this
 * DomainModel.  The refid attribute must share the value of an UMLClass.id
 * on the exposedClassCollection of this model.  This exists solely as
 * an optimization for not duplicating the UMLClass (in XML) everywhere
 * it is associated (which is a significant savings).
 */
public class UMLClassReference  implements java.io.Serializable {
    private java.lang.String refid;  // attribute

    public UMLClassReference() {
    }

    public UMLClassReference(
           java.lang.String refid) {
           this.refid = refid;
    }


    /**
     * Gets the refid value for this UMLClassReference.
     * 
     * @return refid
     */
    public java.lang.String getRefid() {
        return refid;
    }


    /**
     * Sets the refid value for this UMLClassReference.
     * 
     * @param refid
     */
    public void setRefid(java.lang.String refid) {
        this.refid = refid;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UMLClassReference)) return false;
        UMLClassReference other = (UMLClassReference) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.refid==null && other.getRefid()==null) || 
             (this.refid!=null &&
              this.refid.equals(other.getRefid())));
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
        if (getRefid() != null) {
            _hashCode += getRefid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UMLClassReference.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "UMLClassReference"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("refid");
        attrField.setXmlName(new javax.xml.namespace.QName("", "refid"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
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
