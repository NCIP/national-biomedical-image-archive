/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.annotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import gov.nih.nci.nbia.internaldomain.AimImagingObservationCharacteristic;
import gov.nih.nci.nbia.internaldomain.AimQuantification;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AimXmlUtil {
	//take the first one
	//return "" if not found
	public static String getSeriesInstanceUID(Document document) {
        if (document == null) {
            return "";
        }

		NodeList imageSeriesNodes = document.getElementsByTagNameNS(AIM_NS, "ImageSeries");
		System.out.println("found nodes:"+imageSeriesNodes.getLength());
		if(imageSeriesNodes.getLength()>0) {
			return ((Element)imageSeriesNodes.item(0)).getAttribute("instanceUID");
		}
		return "";
	}

	//take the first one
	//return "" if not found
	public static String getStudyInstanceUID(Document document) {
        if (document == null) {
            return "";
        }

		NodeList imageStudyNodes = document.getElementsByTagNameNS(AIM_NS, "ImageStudy");
		if(imageStudyNodes.getLength()>0) {
			return ((Element)imageStudyNodes.item(0)).getAttribute("instanceUID");
		}
		return "";
	}
/*
	<ns1:ImageStudy cagridId="0" instanceUID="1.3.6.1.4.1.9328.50.20.79816808002210880355559090500572828035" startDate="0001-01-01T00:00:00.000Z" startTime="00:00:00" xsi:type="ns1:ImageStudy">
    <ns1:imageSeries>
     <ns1:ImageSeries cagridId="0" instanceUID="1.3.6.1.4.1.9328.50.20.180682294191502931053170028119443268942" xsi:type="ns1:ImageSeries">
*/

	public static Collection<AimImagingObservationCharacteristic>
	    parseImgObsCharacteristics(Document document, GeneralSeries generalSeries) {

		Collection<AimImagingObservationCharacteristic> results = new ArrayList<AimImagingObservationCharacteristic>();

		//ns1:ImagingObservationCharacteristic
		NodeList imagingObservationCharacteristics = document.getElementsByTagNameNS(AIM_NS, "ImagingObservationCharacteristic");

		for(int i=0;i<imagingObservationCharacteristics.getLength();i++) {
			Element imagingObservationCharacteristicElement = (Element)imagingObservationCharacteristics.item(i);

			String codeMeaning = imagingObservationCharacteristicElement.getAttribute("codeMeaning");
			String codeValue = imagingObservationCharacteristicElement.getAttribute("codeValue");
			String codingSchemeDesignator = imagingObservationCharacteristicElement.getAttribute("codingSchemeDesignator");

			AimImagingObservationCharacteristic imagingObservationCharacteristic =
				new AimImagingObservationCharacteristic();
			imagingObservationCharacteristic.setCodeMeaningName(codeMeaning);
			imagingObservationCharacteristic.setCodeSchemaDesignator(codingSchemeDesignator);
			imagingObservationCharacteristic.setCodeValue(codeValue);
			imagingObservationCharacteristic.setGeneralSeries(generalSeries);
			imagingObservationCharacteristic.setSeriesPKId(generalSeries.getId());

			Set<AimQuantification> quantifications = parseQuantifications(imagingObservationCharacteristicElement,
				                                                          imagingObservationCharacteristic);
			imagingObservationCharacteristic.setAimQuantificationCollection(quantifications);

			results.add(imagingObservationCharacteristic);
		}
		return results;
	}

    //private?
	public static Set<AimQuantification> parseQuantifications(Element rootElement,
			                                                  AimImagingObservationCharacteristic aimImagingObservationCharacteristic) {

		System.out.println("aimImagingObservationCharacteristic:"+aimImagingObservationCharacteristic);

		Set<AimQuantification> results = new HashSet<AimQuantification>();

		//ns1:ImagingObservationCharacteristic
		NodeList imagingQuantifications = rootElement.getElementsByTagNameNS(AIM_NS, "CharacteristicQuantification");

		for(int i=0;i<imagingQuantifications.getLength();i++) {
			Element imagingQuantification = (Element)imagingQuantifications.item(i);

			String name = imagingQuantification.getAttribute("name");
			String value = imagingQuantification.getAttribute("value");
			String type = imagingQuantification.getAttributeNS(XSI_NS, "type");

			AimQuantification aimQuantification = new AimQuantification();
			aimQuantification.setName(name);
			aimQuantification.setValue(value);
			aimQuantification.setType(filterNameSpace(type));
			aimQuantification.setAimImagingObservationCharacteristic(aimImagingObservationCharacteristic);

			results.add(aimQuantification);
		}
		return results;
	}

	private static String AIM_NS = "gme://caCORE.caCORE/3.2/edu.northwestern.radiology.AIM";

	private static String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

	private static String filterNameSpace(String nodeNameWithPossibleNameSpace) {
		int dex = nodeNameWithPossibleNameSpace.indexOf(':');
		if(dex!=-1) {
			if(dex<nodeNameWithPossibleNameSpace.length()-1) {
			    return nodeNameWithPossibleNameSpace.substring(dex+1);
	    	}
	    	else {
				return nodeNameWithPossibleNameSpace;
		    }
	    }
		else {
			return nodeNameWithPossibleNameSpace;
		}
	}
}
