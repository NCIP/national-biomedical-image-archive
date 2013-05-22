/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.1  2007/08/07 12:05:11  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.3  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
/*
 * HashCodeUtil.java
 *
 * Created on August 24, 2005, 11:39 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package gov.nih.nci.nbia.util;


/**
 *
 * @author piparom
 */
import java.lang.reflect.Array;


/**
* Collected methods which allow easy implementation of <code>hashCode</code>.
*
* Example use case:
* <pre>
*  public int hashCode(){
*    int result = HashCodeUtil.SEED;
*    //collect the contributions of various fields
*    result = HashCodeUtil.hash(result, fPrimitive);
*    result = HashCodeUtil.hash(result, fObject);
*    result = HashCodeUtil.hash(result, fArray);
*    return result;
*  }
* </pre>
*/
public final class HashCodeUtil {
    /**
    * An initial value for a <code>hashCode</code>, to which is added contributions
    * from fields. Using a non-zero value decreases collisons of <code>hashCode</code>
    * values.
    */
    public static final int SEED = 23;

    /// PRIVATE ///
    private static final int fODD_PRIME_NUMBER = 37;

    /**
    * booleans.
    */
    public static int hash(int aSeed, boolean aBoolean) {
        return firstTerm(aSeed) + (aBoolean ? 1 : 0);
    }

    /**
    * chars.
    */
    public static int hash(int aSeed, char aChar) {
        return firstTerm(aSeed) + (int) aChar;
    }

    /**
    * ints.
    */
    public static int hash(int aSeed, int aInt) {
        /*
        * Implementation Note
        * Note that byte and short are handled by this method, through
        * implicit conversion.
        */
        return firstTerm(aSeed) + aInt;
    }

    /**
    * longs.
    */
    public static int hash(int aSeed, long aLong) {
        return firstTerm(aSeed) + (int) (aLong ^ (aLong >>> 32));
    }

    /**
    * floats.
    */
    public static int hash(int aSeed, float aFloat) {
        return hash(aSeed, Float.floatToIntBits(aFloat));
    }

    /**
    * doubles.
    */
    public static int hash(int aSeed, double aDouble) {
        return hash(aSeed, Double.doubleToLongBits(aDouble));
    }

    /**
    * <code>aObject</code> is a possibly-null object field, and possibly an array.
    *
    * If <code>aObject</code> is an array, then each element may be a primitive
    * or a possibly-null object.
    */
    public static int hash(int aSeed, Object aObject) {
        int result = aSeed;

        if (aObject == null) {
            result = hash(result, 0);
        } else if (!isArray(aObject)) {
            result = hash(result, aObject.hashCode());
        } else {
            int length = Array.getLength(aObject);

            for (int idx = 0; idx < length; ++idx) {
                Object item = Array.get(aObject, idx);
                //recursive call!
                result = hash(result, item);
            }
        }

        return result;
    }

    private static int firstTerm(int aSeed) {
        return fODD_PRIME_NUMBER * aSeed;
    }

    private static boolean isArray(Object aObject) {
        return aObject.getClass().isArray();
    }
}
