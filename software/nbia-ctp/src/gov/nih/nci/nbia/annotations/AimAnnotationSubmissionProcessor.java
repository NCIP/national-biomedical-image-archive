/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.annotations;

import gov.nih.nci.nbia.internaldomain.AimImagingObservationCharacteristic;
import gov.nih.nci.nbia.internaldomain.GeneralSeries;
import gov.nih.nci.nbia.internaldomain.ImageMarkup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.List;

//import maptest.TraceTargetConsole;

import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import com.mapforce.MappingMapToAIM_v2_rv15_XML;

public class AimAnnotationSubmissionProcessor extends TraditionalAnnotationSubmissionProcessor {
	String outFilePath = System.getProperty("java.io.tmpdir")+ "/cedaraJava/";


	@Transactional(propagation=Propagation.REQUIRED)
	public Status process(XmlObject file, File storedFile) {

	    Document document = file.getDocument();
	    String seriesInstanceUID = AimXmlUtil.getSeriesInstanceUID(document);
	    String studyInstanceUID = AimXmlUtil.getStudyInstanceUID(document);

	    storeToMarkupTable(document,seriesInstanceUID);
	    storeAim(document,seriesInstanceUID);

	    //add a row to annotation table for downloads
	    return super.processImpl(studyInstanceUID,
                                 seriesInstanceUID,
                                 storedFile,
                                 file.getFile());
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Status process(ZipObject file, File storedFile) {
        return Status.FAIL;
    }




	/////////////////////////////////////////////PRIVATE///////////////////////////////////////////

	//AIM documents have uniqueIdentifier in ImageAnnotation element
	//probably use this for update purposes in a more realistic impl
	private void storeAim(Document document, String seriesInstanceUID) {

		GeneralSeries exampleSeries = new GeneralSeries();
		exampleSeries.setSeriesInstanceUID(seriesInstanceUID);

		List<GeneralSeries> seriesList = getHibernateTemplate().findByExample(exampleSeries);

		if(seriesList==null || seriesList.size()==0) {
			throw new RuntimeException("AIM annotation submitted for series that doesnt exist:"+seriesInstanceUID);
		}
		GeneralSeries series = seriesList.get(0);

		Collection<AimImagingObservationCharacteristic> characteristics = AimXmlUtil.parseImgObsCharacteristics(document, series);

		for(AimImagingObservationCharacteristic characteristic : characteristics) {
			getHibernateTemplate().save(characteristic);
		}

		getHibernateTemplate().flush();
	}

	private void storeToMarkupTable(Document document, String seriesInstanceUID) {
		try{
				File outFileDir = new File(outFilePath);
			    outFileDir.mkdirs();
			    convertToCedaraAIM(document);
				String xml = processFiles(outFilePath);
				insertToMarkupDatabase(seriesInstanceUID, xml);
				File[] files = outFileDir.listFiles();
				for (int i = 0; i < files.length; ++i) {
					files[i].delete();
				}
		}
		 catch (Exception e){// Catch exception if any
			      e.printStackTrace();
		}

	}
	private void convertToCedaraAIM(Document document){

		try { // Mapping
			TraceTargetConsole ttc = new TraceTargetConsole();
			MappingMapToAIM_v2_rv15_XML MappingMapToAIM_v2_rv15_XMLObject = new MappingMapToAIM_v2_rv15_XML();
			MappingMapToAIM_v2_rv15_XMLObject.registerTraceTarget(ttc);

			// run mapping
			{
				com.altova.io.Input AIM_v3_rv8_XML_beta_mod2Source = new com.altova.io.DocumentInput(document);
				MappingMapToAIM_v2_rv15_XMLObject
						.run(AIM_v3_rv8_XML_beta_mod2Source);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String processFiles(String outFilePath){
		File inPath = new File(outFilePath);
		StringBuffer sbr = new StringBuffer();

		if (inPath.isDirectory()) {
			File[] files = inPath.listFiles();
			try {
				//sbr.append("<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?>\n<Annotations>\n");
				for (int i = 0; i < files.length; ++i) {

					FileReader fr = new FileReader(files[i].getAbsoluteFile());
					BufferedReader br = new BufferedReader(fr);
					boolean first = true;
					String s;
					while ((s = br.readLine()) != null) {
						if (first) {
							first = false;
						} else {
							sbr.append(s);
							sbr.append('\n');
						}
					}
					fr.close();
				}
				sbr.append("</Annotations>");
				//System.out.println("combined file"+sbr.toString());
				return sbr.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void insertToMarkupDatabase(String uid, String xml){
		GeneralSeries exampleSeries = new GeneralSeries();
		exampleSeries.setSeriesInstanceUID(uid);

		List<GeneralSeries> seriesList = getHibernateTemplate().findByExample(exampleSeries);

		if(seriesList==null || seriesList.size()==0) {
			throw new RuntimeException("AIM annotation submitted for series that doesnt exist:"+uid);
			//System.out.println("AIM annotation submitted for series that doesnt exist:"+uid);
		}
         else {
        	 GeneralSeries series = seriesList.get(0);
        	 ImageMarkup im = new ImageMarkup();
        	 im.setSeries(series);
             im.setSeriesInstanceUID(uid);
             List<ImageMarkup> imList = getHibernateTemplate().findByExample(im);
             if ((imList != null) && (imList.size() != 0)){
            	 im = imList.get(0);
            	 String oldXml = im.getMarkupContent();
            	 oldXml = oldXml.replaceFirst("</Annotations>", "");
            	 xml = oldXml.concat(xml);
             }
             else {
            	 xml = "<?xml version=\"1.0\" encoding=\"UTF-16\" standalone=\"no\"?>\n<Annotations>\n".concat(xml);
             }
           	 im.setLoginName("LIDC");
           	 im.setMarkupContent(xml);


             im.setSubmissionDate(new java.util.Date());
        	 getHibernateTemplate().saveOrUpdate(im);
         }
        getHibernateTemplate().flush();
      }

}


class TraceTargetConsole implements com.altova.TraceTarget {
	public void writeTrace(String info) {
		System.out.println(info);
	}
}