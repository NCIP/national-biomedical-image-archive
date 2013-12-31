package gov.nih.nci.nbia.textsupport;
import org.hibernate.HibernateException;
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
public class PatientUpdaterTest {
    @Autowired
    private SessionFactory sessionFactory;
	PatientUpdater updater;
	@Test
	public void testUpdater()
    {
		try {
			System.setProperty("gov.nih.nci.ncia.solr.home", "c:/apps/nbia/solr-home/");
			updater = new PatientUpdater();
			sessionFactory.openSession();
			updater.setSessionFactory(sessionFactory);
			updater.updateSubmittedPatients();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
