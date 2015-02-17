/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: PreProcessorFactory.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
* Revision 1.3  2006/09/28 19:29:00  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.ncia.util.preprocessor;

public class PreProcessorFactory {
    final static int LIDCXMLPREPROCESS = 0;
    final static int ACRINPREPROCESS = 1;

    public static IPreProcessor getInstance(int type) {
        switch (type) {
        case PreProcessorFactory.LIDCXMLPREPROCESS:
            return new LIDCPreProcessUtil();

        case PreProcessorFactory.ACRINPREPROCESS:
            return new ACRINPreProcessUtil();
        }

        return null;
    }
}
