/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class MRUtil {
    private static Logger logger = Logger.getLogger(MRUtil.class);
    private ClassPathXmlApplicationContext ctx = null;
    MRUtil mrUtil = null;
    @Autowired
    private ProcessDicomObject mrObj;

    public static void main(String[] args) {
        String LOG_FILE = "dbUpdatorLog4j.properties";
        Properties logProp = new Properties();
        ClassLoader classLoader = MRUtil.class.getClassLoader();
        try {
            logProp.load(classLoader.getResourceAsStream(LOG_FILE));
            PropertyConfigurator.configure(logProp);
            System.out.println("Logging enabled");
            logger.info("Logging enabled");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Logging not enabled");
        }
        new MRUtil().reProcessMR();
        logger.info("completed the re processing of MR images");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void reProcessMR() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("loaded the application context");
        mrObj = (ProcessDicomObject) ctx.getBean("processDicomObject");
        SessionFactory sf1 = (SessionFactory) ctx.getBean("sessionFactory");
        Session s = sf1.openSession();
        try {
            // get the MR images
            String hql = "SELECT gi.id,"
                    + "gi.filename FROM GeneralImage as gi, GeneralSeries as gs  WHERE gi.seriesPKId = gs.id AND gs.modality = 'MR'"
                    + "and gi.id not in ( Select generalImage.id from MRImage)";
            Query q = s.createQuery(hql);
            q.setFirstResult(0);
            List<Object[]> searchResults = q.list();
            logger.info("got resultset of MR to reprocess");
            mrObj.process(searchResults);
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
