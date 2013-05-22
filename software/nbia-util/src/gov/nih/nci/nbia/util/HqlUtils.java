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
* Revision 1.6  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.util;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Class containing various utils that are useful for building HQL.
 *
 *
 */
public class HqlUtils {
    /**
     * Builds an 'in' clause to include in a where clause
     *
     * @param textBeforeParen - everything (including the 'in') that needs to go
     *        prior to the parenthesis that opens the list of IDs
     * @param ids - list of IDs
     * @return the string
     */
    public static String buildInClause(String textBeforeParen,
        Collection<String> ids) {
        StringBuffer sb = new StringBuffer();
        sb.append(textBeforeParen);
        sb.append(" (");

        boolean first = true;

        if (ids != null) {
            // Loop through IDs to add to the in clause
            for (String id : ids) {
                // Only prefix a comma if it is not the first one
                if (!first) {
                    sb.append(", ");
                } else {
                    first = false;
                }

                sb.append('\'');
                sb.append(id);
                sb.append('\'');
            }
        }

        sb.append(')');

        return sb.toString();
    }

    /**
     * Builds an 'in' clause to include in a where clause
     *
     * @param textBeforeParen - everything (including the 'in') that needs to go
     *        prior to the parenthesis that opens the list of IDs
     * @param ids - list of IDs
     * @return the string
     */
    public static String buildInClauseUsingIntegers(String textBeforeParen,
        Collection<Integer> ids) {
        Collection<String> strings = new ArrayList<String>();

        if (ids != null) {
            for (Integer i : ids) {
                strings.add(String.valueOf(i));
            }
        }

        return buildInClause(textBeforeParen, strings);
    }
}
