package gov.nih.nci.ncia.annotations;

import gov.nih.nci.ncia.internaldomain.AimImagingObservationCharacteristic;
import gov.nih.nci.ncia.internaldomain.GeneralSeries;

import java.io.File;
import java.util.Collection;
import java.util.List;

import maptest.TraceTargetConsole;

import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import com.mapforce.MappingMapToAIM_v2_rv15_XML;

public class AimAnnotationSubmissionProcessor extends TraditionalAnnotationSubmissionProcessor {
	@Transactional(propagation=Propagation.REQUIRED)
	public Status process(XmlObject file, File storedFile) {

	    Document document = file.getDocument();
	    String seriesInstanceUID = AimXmlUtil.getSeriesInstanceUID(document);
	    String studyInstanceUID = AimXmlUtil.getStudyInstanceUID(document);

	    convertToCedaraAIM(document);
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

	private void convertToCedaraAIM(Document document){
		System.out.println("Mapping Application");

		try { // Mapping
			TraceTargetConsole ttc = new TraceTargetConsole();
			MappingMapToAIM_v2_rv15_XML MappingMapToAIM_v2_rv15_XMLObject = new MappingMapToAIM_v2_rv15_XML();
			MappingMapToAIM_v2_rv15_XMLObject.registerTraceTarget(ttc);

			// run mapping
			{
				com.altova.io.Input AIM_v3_rv8_XML_beta_mod2Source = com.altova.io.DocumentInput(document);
				MappingMapToAIM_v2_rv15_XMLObject
						.run(AIM_v3_rv8_XML_beta_mod2Source);
			}

			System.out.println("Finished");
		} catch (com.altova.UserException ue) {
			System.out.print("USER EXCEPTION:");
			System.out.println(ue.getMessage());
		} catch (com.altova.AltovaException e) {
			System.out.print("ERROR: ");
			System.out.println(e.getMessage());
			if (e.getInnerException() != null) {
				System.out.print("Inner exception: ");
				System.out.println(e.getInnerException().getMessage());
				if (e.getInnerException().getCause() != null) {
					System.out.print("Cause: ");
					System.out.println(e.getInnerException().getCause()
							.getMessage());
				}
			}
			System.out.println("\nStack Trace: ");
			e.printStackTrace();
		}

		catch (Exception e) {
			System.out.print("ERROR: ");
			System.out.println(e.getMessage());
			System.out.println("\nStack Trace: ");
			e.printStackTrace();
		}
	}
}	
	class TraceTargetConsole implements com.altova.TraceTarget {
		public void writeTrace(String info) {
			System.out.println(info);
		}
}