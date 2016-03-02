package gov.nih.nci.nbia.textsupport;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
@Transactional 
public class PatientDocumentTest {
    @Autowired
    private SessionFactory sessionFactory;
	PatientAccessDAO patientAccess;
	@Test
	public void testLoadPatient()
    {
		System.setProperty("gov.nih.nci.ncia.solr.home", "c:/apps/nbia/solr-home/");
		patientAccess = new PatientAccessDAO();
		sessionFactory.openSession();
		patientAccess.setSessionFactory(sessionFactory);
    	PatientDocument doc = patientAccess.getPatientDocument("Project-3210364490");
    	SolrStorage.addPatientDocument(doc);
    	SolrAccess.querySolr("Project");
    }
}
