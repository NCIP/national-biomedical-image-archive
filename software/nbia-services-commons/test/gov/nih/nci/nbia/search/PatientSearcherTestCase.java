/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.dto.StudyNumberDTO;
import gov.nih.nci.nbia.factories.ApplicationFactory;
import gov.nih.nci.nbia.lookup.StudyNumberMap;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.search.PatientSearchResult;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"gov.nih.nci.nbia.search.LocalNode"})
@PrepareForTest({PatientSearcher.class,  
                 SpringApplicationContext.class,
                 ApplicationFactory.class,
                 LocalNode.class,
                 DICOMQueryHandler.class}) 
public class PatientSearcherTestCase {

	/**
	 * Just test reduction of triples to patients... do the
	 * heavy lifting of all criteria permutations in the DICOM Query Handler
	 * tests
	 */
	@Test
	public void testSearchForPatients() throws Exception{
        DICOMQuery dicomQuery = new DICOMQuery();

		ImageModalityCriteria imageModalityCriteria = new ImageModalityCriteria();
		imageModalityCriteria.setImageModalityValue("CT");
		dicomQuery.setCriteria(imageModalityCriteria);
		
		StudyNumberDTO studyNumberDTO0 = new StudyNumberDTO(1, "1", "p1", 2, 5);
		StudyNumberDTO studyNumberDTO1 = new StudyNumberDTO(6, "6", "p2", 1, 1);
		
		/////////////////////////////////
		
		mockStatic(SpringApplicationContext.class);
		mockStatic(ApplicationFactory.class);		
		ApplicationFactory applicationFactoryMock = createMock(ApplicationFactory.class);
		StudyNumberMap studyNumberMapMock = createMock(StudyNumberMap.class);
		DICOMQueryHandler dicomQueryHandlerMock = createMock(DICOMQueryHandler.class);

        expect(ApplicationFactory.getInstance()).
            andReturn(applicationFactoryMock).anyTimes();  
        expect(applicationFactoryMock.getStudyNumberMap()).
            andReturn(studyNumberMapMock).anyTimes();    
        expect(studyNumberMapMock.getStudiesForPatient(1)).
            andReturn(studyNumberDTO0);
        expect(studyNumberMapMock.getStudiesForPatient(6)).
            andReturn(studyNumberDTO1);        
        expect(SpringApplicationContext.getBean("dicomQueryHandler")).
            andReturn(dicomQueryHandlerMock);
        expect(dicomQueryHandlerMock.findTriples(dicomQuery)).
            andReturn(constructTriples());

        replay(SpringApplicationContext.class);
        replay(ApplicationFactory.class, applicationFactoryMock);        
        replay(DICOMQueryHandler.class, dicomQueryHandlerMock);
        replay(StudyNumberMap.class, studyNumberMapMock);   
        
        //////////////////            
          
        List<PatientSearchResult> results = patientSearcher.searchForPatients(dicomQuery);
        Assert.assertTrue(results.size()==2);
        Assert.assertTrue(results.get(0).getSubjectId().equals("1"));
        Assert.assertTrue(results.get(1).getSubjectId().equals("6"));
        
        verify(SpringApplicationContext.class);
        verify(ApplicationFactory.class, applicationFactoryMock);        
        verify(DICOMQueryHandler.class, dicomQueryHandlerMock);
        verify(StudyNumberMap.class, studyNumberMapMock);  
	}
	
	
    @Before
	public void setUp() throws Exception {
		patientSearcher = new PatientSearcher();
	}


    ////////////////////////////////////PRIVATE/////////////////////////////////

    private PatientSearcher patientSearcher;

    private List<PatientStudySeriesTriple> constructTriples() {
        List<PatientStudySeriesTriple> triples = new ArrayList<PatientStudySeriesTriple>();
        
        PatientStudySeriesTriple triple0 = new PatientStudySeriesTriple();
        triple0.setPatientPkId(1);
        triple0.setStudyPkId(1);
        triple0.setSeriesPkId(1);
        
        PatientStudySeriesTriple triple1 = new PatientStudySeriesTriple();
        triple1.setPatientPkId(1);
        triple1.setStudyPkId(1);
        triple1.setSeriesPkId(2);
        
        PatientStudySeriesTriple triple2 = new PatientStudySeriesTriple();
        triple2.setPatientPkId(1);
        triple2.setStudyPkId(1);
        triple2.setSeriesPkId(3);
        
        PatientStudySeriesTriple triple3 = new PatientStudySeriesTriple();
        triple3.setPatientPkId(1);
        triple3.setStudyPkId(4);
        triple3.setSeriesPkId(5);
        
        PatientStudySeriesTriple triple4= new PatientStudySeriesTriple();
        triple4.setPatientPkId(6);
        triple4.setStudyPkId(7);
        triple4.setSeriesPkId(8);         
        
        triples.add(triple0);
        triples.add(triple1);
        triples.add(triple2);
        triples.add(triple3);
        triples.add(triple4);
        
        return triples;                	
    }
}
