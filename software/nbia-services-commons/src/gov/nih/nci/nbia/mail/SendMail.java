/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
* $Id$
*
* $Log: not supported by cvs2svn $
* Revision 1.2  2008/02/20 20:51:27  lethai
* Task 2267 - Change to use LDAP
*
* Revision 1.1  2007/08/07 12:05:12  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.15  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.mail;

import gov.nih.nci.nbia.util.NCIAConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;


public class SendMail {
    private static Logger logger = Logger.getLogger(SendMail.class);
    public static final String MAIL_PROPERTIES = "mail.properties";

    public SendMail() {
    }

    /**
     * Send a mail message where the FROM is set to what is is mail.properties.
     */
    public void sendMail(String mailTo, 
    		             String mailBody,
    		             String subject) {
        try {
            //get system properties
            Properties props = System.getProperties();

            //	This will get the values from the properties files
            Properties nciaProperties = new Properties();
            InputStream in = Thread.currentThread().getContextClassLoader()
                                   .getResourceAsStream(MAIL_PROPERTIES);

            try {
                nciaProperties.load(in);
            } catch (IOException ioe) {
                logger.error("Unable to load NCIA properties", ioe);
            }

            String host = NCIAConfig.getMailServerHostName();            
            String from = nciaProperties.getProperty("fromAddress");

            String to = mailTo;
            // Set up mail server
            props.put("mail.smtp.host", host);

            //Get session
            Session session = Session.getDefaultInstance(props, null);

            //Define Message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress(to));
            message.setSubject(subject);
            message.setText(mailBody);

            //Send Message
            Transport.send(message);
        } catch (Exception e) {
            logger.error("Send Mail error", e);
        } //catch
    } //send mail
    
    public void sendMail(String mailTo, 
    		             String mailFrom, 
    		             String mailBody,
    		             String subject) {
        try {
            //get system properties
            Properties props = System.getProperties();

            String host = NCIAConfig.getMailServerHostName();
           
            String to = mailTo;
            // Set up mail server
            props.put("mail.smtp.host", host);

            //Get session
            Session session = Session.getDefaultInstance(props, null);

            //Define Message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailFrom));
            message.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress(to));
            message.setSubject(subject);
            message.setText(mailBody);

            //Send Message
            Transport.send(message);
        } catch (Exception e) {
            logger.error("Send Mail error", e);
        } //catch
    } //send mail
} //Sendmail
