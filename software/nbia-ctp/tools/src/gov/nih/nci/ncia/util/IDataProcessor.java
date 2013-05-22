/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id: IDataProcessor.java 4417 2008-04-18 20:43:12Z saksass $
*
* $Log: not supported by cvs2svn $
*/
/*
 * Created on Jul 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.nih.nci.ncia.util;


/**
 * @author Jin Chen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IDataProcessor {
    static final String DATABASE_MAPPING = "database.properties";
    static final String DICOM_PROPERITIES = "dicom.properties";
    static final int DICOM = 0;
    static final int LOGIN = 1;

    public void connect() throws Exception;

    public void disconnect() throws Exception;

    public void process(Object o) throws Exception;

    public void process(Object o, String url) throws Exception;
}
