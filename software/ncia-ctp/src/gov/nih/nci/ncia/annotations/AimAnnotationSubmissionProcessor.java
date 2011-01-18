package gov.nih.nci.ncia.annotations;

import gov.nih.nci.ncia.internaldomain.AimImagingObservationCharacteristic;
import gov.nih.nci.ncia.internaldomain.AimQuantification;
import gov.nih.nci.ncia.internaldomain.GeneralSeries;

import java.io.File;
import java.util.Collection;
import java.util.List;
import org.rsna.ctp.objects.XmlObject;
import org.rsna.ctp.objects.ZipObject;
import org.rsna.ctp.pipeline.Status;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;


public class AimAnnotationSubmissionProcessor extends TraditionalAnnotationSubmissionProcessor {
	@Transactional(propagation=Propagation.REQUIRED)
	public Status process(XmlObject file, File storedFile) {
		System.out.println("wtf:"+file);
	    Document document = file.getDocument();
	    String seriesInstanceUID = AimXmlUtil.getSeriesInstanceUID(document);
	    String studyInstanceUID = AimXmlUtil.getStudyInstanceUID(document);

	    System.out.println("seriesInstanceUID:"+seriesInstanceUID);
	    System.out.println("studyInstanceUID:"+studyInstanceUID);

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
		System.out.println("moo:"+series.getId());

		Collection<AimImagingObservationCharacteristic> characteristics = AimXmlUtil.parseImgObsCharacteristics(document, series);
		System.out.println("characteristics:"+characteristics.size());
		for(AimImagingObservationCharacteristic characteristic : characteristics) {
			getHibernateTemplate().save(characteristic);
		}

		getHibernateTemplate().flush();
	}


}