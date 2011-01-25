/**
 * Predicate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package gov.nih.nci.cagrid.cqlquery;

public class Predicate implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected Predicate(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _EQUAL_TO = "EQUAL_TO";
    public static final java.lang.String _NOT_EQUAL_TO = "NOT_EQUAL_TO";
    public static final java.lang.String _LIKE = "LIKE";
    public static final java.lang.String _IS_NULL = "IS_NULL";
    public static final java.lang.String _IS_NOT_NULL = "IS_NOT_NULL";
    public static final java.lang.String _LESS_THAN = "LESS_THAN";
    public static final java.lang.String _LESS_THAN_EQUAL_TO = "LESS_THAN_EQUAL_TO";
    public static final java.lang.String _GREATER_THAN = "GREATER_THAN";
    public static final java.lang.String _GREATER_THAN_EQUAL_TO = "GREATER_THAN_EQUAL_TO";
    public static final Predicate EQUAL_TO = new Predicate(_EQUAL_TO);
    public static final Predicate NOT_EQUAL_TO = new Predicate(_NOT_EQUAL_TO);
    public static final Predicate LIKE = new Predicate(_LIKE);
    public static final Predicate IS_NULL = new Predicate(_IS_NULL);
    public static final Predicate IS_NOT_NULL = new Predicate(_IS_NOT_NULL);
    public static final Predicate LESS_THAN = new Predicate(_LESS_THAN);
    public static final Predicate LESS_THAN_EQUAL_TO = new Predicate(_LESS_THAN_EQUAL_TO);
    public static final Predicate GREATER_THAN = new Predicate(_GREATER_THAN);
    public static final Predicate GREATER_THAN_EQUAL_TO = new Predicate(_GREATER_THAN_EQUAL_TO);
    public java.lang.String getValue() { return _value_;}
    public static Predicate fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        Predicate enumeration = (Predicate)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static Predicate fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Predicate.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "Predicate"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
