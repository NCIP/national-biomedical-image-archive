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
* Revision 1.4  2008/02/20 20:48:33  lethai
* Task 2267 - Change to use LDAP
*
* Revision 1.3  2007/12/13 17:41:24  lethai
* Externalize ftp_url for image download
*
* Revision 1.2  2007/08/29 19:11:18  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/07 12:05:11  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:44:38  bauerd
* Initial Check in of reorganized components
*
* Revision 1.39  2007/05/21 22:04:59  panq
* Improved the QA tool usability by adding a user notification email.
*
* Revision 1.38  2006/12/20 22:27:55  panq
* Modified for download images from the grid.
*
* Revision 1.37  2006/09/27 20:46:27  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.mail;

import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;


/**
 *
 *  Manages sending of emails
 *
 */
public class MailManager {
    private static Logger logger = Logger.getLogger(MailManager.class);

    // Name of the properties file for mail
    public static final String MAIL_PROPERTIES = "mail.properties";

    // Data read from the properties file
    private static final String deletionSubject;
    private static final String deletionUnformattedBody1;
    private static final String ftpSubject;
    private static final String ftpUnformattedBody1;
    private static final String ftpUnformattedBody2;
    private static final String ftpUnformattedBody3;
    private static final String ftpUnformattedBody4;
    private static final String qcStatusSubject;
    private static final String qcStatusUnFormattedBody;
    private static final String registerSubject;
    private static final String registerUnformattedBody;
    private static final String fileRetentionPeriodInDays;
    private static final String appSupportNumber;
    private static final String techSupportStartTime;
    private static final String techSupportEndTime;
    private static String ftpHostnameAndPort;
    private static String ldapToAddress;
    private static String ldapSubject;
    private static String ldapGreeting;
    private static String ldapBody1;
    private static String ldapBody2;
    private static String ldapBody3;
    private static String ldapBody4;
    private static String ldapBody5;
    private static String ldapBody6;
    private static String ldapBody7;
    private static final String sharedListDeletionSubject;
    private static final String sharedListDeletionUnFormattedBody;

    static {
        // Read data from the properties file
        Properties mailProps = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader()
                               .getResourceAsStream(MAIL_PROPERTIES);

        try {
            mailProps.load(in);
        } catch (IOException ioe) {
            logger.error("Unable to load NCIA properties", ioe);
        }

        ftpSubject = mailProps.getProperty("ftp.Subject");
        ftpUnformattedBody1 = mailProps.getProperty("ftp.Body1");
        ftpUnformattedBody2 = mailProps.getProperty("ftp.Body2");
        ftpUnformattedBody3 = mailProps.getProperty("ftp.Body3");
        ftpUnformattedBody4 = mailProps.getProperty("ftp.Body4");
        registerSubject = mailProps.getProperty("register.Subject");
        registerUnformattedBody = mailProps.getProperty("register.Body");

        qcStatusSubject = mailProps.getProperty("qcStatus.Subject");
        qcStatusUnFormattedBody = mailProps.getProperty("qcStatus.Body");

        deletionSubject = mailProps.getProperty("deletion.Subject");
        deletionUnformattedBody1 = mailProps.getProperty("deletion.Body1");
        
        /*fileRetentionPeriodInDays = mailProps.getProperty(
                "fileRetentionPeriodInDays");*/

        fileRetentionPeriodInDays = NCIAConfig.getFileRetentionPeriodInDays();
        appSupportNumber = mailProps.getProperty("techSupportNumber");
        techSupportStartTime = mailProps.getProperty("techSupportStartTime");
        techSupportEndTime = mailProps.getProperty("techSupportEndTime");
        //ftpHostnameAndPort = mailProps.getProperty("ftpHostnameAndPort");
        ftpHostnameAndPort = NCIAConfig.getFTPHostAndPort();
        logger.info("FTP host and port is " + ftpHostnameAndPort);
        //ldapToAddress = mailProps.getProperty("LDAP.toAddresss");
        ldapToAddress = NCIAConfig.getAdminEmailAddress();
        ldapSubject = NCIAConfig.getRegistrationMailSubject();
        ldapGreeting = mailProps.getProperty("ldap.greeting");
        ldapBody1 = mailProps.getProperty("ldap.Body1");
        ldapBody2 = mailProps.getProperty("ldap.Body2");
        ldapBody3 = mailProps.getProperty("ldap.Body3");
        ldapBody4 = mailProps.getProperty("ldap.Body4");
        ldapBody5 = mailProps.getProperty("ldap.Body5");
        ldapBody6 = mailProps.getProperty("ldap.Body6");
        ldapBody7 = mailProps.getProperty("ldap.Body7");
        sharedListDeletionSubject = mailProps.getProperty("deletion.sharedList.Subject");
        sharedListDeletionUnFormattedBody = mailProps.getProperty("deletion.sharedList.Body");
    }

    /**
     * Sends an email notifying user that their files are ready for download
     * via FTP
     *
     * @param mailTo - email address
     * @throws Exception
     */
    public static void sendFTPMail(String mailTo, List<String> fileNames)
        throws Exception {
        try {
            // Part 1 is always included
            String message = new MessageFormat(ftpUnformattedBody1).format(new String[] {
                        fileRetentionPeriodInDays
                    });

            // Part 2 is only included if there are multiple fies
            if (fileNames.size() > 1) {
                message += ftpUnformattedBody2;
            }

            // Part 3 appears once for each file
            for (String fileName : fileNames) {
                message += new MessageFormat(ftpUnformattedBody3).format(new String[] {
                        cleanFileName(fileName), ftpHostnameAndPort
                    });
            }

            // Part 4 always appears
            message += new MessageFormat(ftpUnformattedBody4).format(new String[] {
                    appSupportNumber, techSupportStartTime, techSupportEndTime
                });

            // Send the message
            new SendMail().sendMail(mailTo, message, ftpSubject);
        } catch (Exception e) {
            logger.error("Send FTP mail error", e);
        }
    }

    /**
     * Sends an email notifying user that their files are ready for download
     * via FTP
     *
     * @param mailTo - email address
     * @throws Exception
     */
    public static void sendFTPMail(String mailTo, String username, List<String> fileNames, String nodeName)
        throws Exception {
        try {
            // Part 1 is always included
            String message = new MessageFormat(ftpUnformattedBody1).format(new String[] {
                        nodeName, fileRetentionPeriodInDays
                    });

            // Part 2 is only included if there are multiple fies
            if (fileNames.size() > 1) {
                message += ftpUnformattedBody2;
            }

            // Part 3 appears once for each file
            for (String fileName : fileNames) {
                message += new MessageFormat(ftpUnformattedBody3).format(new String[] {
                        cleanFileName(fileName), ftpHostnameAndPort+ "/" + username
                    });
            }

            // Part 4 always appears
            message += new MessageFormat(ftpUnformattedBody4).format(new String[] {
                    appSupportNumber, techSupportStartTime, techSupportEndTime
                });


            // Send the message
            new SendMail().sendMail(mailTo, message, ftpSubject);
        } catch (Exception e) {
            logger.error("Send FTP mail error", e);
        }
    }

    public static void sendDeletionConfirmationMail(String mailTo, String username, List<String> allSeries, String initialzedDeletionTime)
    throws Exception {
    	String message = "Image deletion has been successfully completed. The process starts at " + initialzedDeletionTime + "\n\n";
    	message += "The following series has been removed from NBIA application database: \n";
    	message += stringForAllDeletedSeries(allSeries) + "\n\n";
    	message += new MessageFormat(deletionUnformattedBody1).format(new String[] {
                appSupportNumber, techSupportStartTime, techSupportEndTime
        });
 
    	new SendMail().sendMail(mailTo, message, deletionSubject);
    }
    
    private static String stringForAllDeletedSeries(List<String> allSeries){
    	StringBuilder all = new StringBuilder();
    	for (int i = 0; i < allSeries.size(); i++){
    		all.append(allSeries.get(i));
    		if ((i+1) % 8 == 0){
    			all.append("\n");
    		}else{
    			all.append(", ");
    		}
    	}
    	return all.toString();
    }
    /**
     * Sends an email notifying user that their registration has been accepted
     *
     * @param mailTo - email address
     * @param userId - user's login ID
     * @throws Exception
     */
    public static void sendConfirmationMail(String mailTo, String userId)
        throws Exception {
        try {
            String[] params = {
                    userId, appSupportNumber, techSupportStartTime,
                    techSupportEndTime
                };

            MessageFormat formatter = new MessageFormat(registerUnformattedBody);
            String formattedBody = formatter.format(params);

            new SendMail().sendMail(mailTo, formattedBody, registerSubject);
        } catch (Exception e) {
            logger.error("Send confirmation mail error", e);
        }
    }


    /**
     * Sends an email notifying user that the status change will have impact on certain shared list
     *
     * @param email - email address
     * @throws Exception
     */
    public static void sendQCShareListEmail(String email, String impactList, String userInfo)
    {
     System.out.println("reachsendQCShareListEmail="+impactList);
        try {
            String[] params = {impactList, userInfo};

            MessageFormat formatter = new MessageFormat(qcStatusUnFormattedBody);
            String qcStatusFormattedBody = formatter.format(params);
            System.out.println("sendQCShareListEmail: " + email + " " +qcStatusFormattedBody + "  "+qcStatusSubject);

            new SendMail().sendMail(email, qcStatusFormattedBody, qcStatusSubject);
        } catch (Exception e) {
            logger.error("Send confirmation mail error", e);
        }
    }


    /**
     * Send email to request LDAP account for user to use NCIA
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param phone
     * @param organization
     * @param title
     */
    public static void sendRegistrationEmail(String firstName, String lastName, String emailAddress,
    					String phone, String organization, String title, String fax, String reason){

    	//if title or fax are not empty, include them in email as well
        String message = new MessageFormat(ldapBody1).format(new String[] {ldapGreeting });
        message += new MessageFormat(ldapBody2).format(new String[] {firstName, lastName });
        message +=new MessageFormat(ldapBody3).format(new String[] {emailAddress, phone });
        message +=new MessageFormat(ldapBody4).format(new String[] {organization });

        if(!StringUtil.isEmptyTrim(title)){
        	message +=new MessageFormat(ldapBody5).format(new String[] {title });
        }
        if(!StringUtil.isEmptyTrim(fax)){
        	message +=new MessageFormat(ldapBody6).format(new String[] {fax });
        }
        message +=ldapBody7;

        message += "\n\nReason for requesting registration:\n"+ reason;

        // Send the message
        new SendMail().sendMail(ldapToAddress, emailAddress, message, ldapSubject);

    }

    public static void sendUsersListRegistration(String emailAddress, String name) {
    	/*
    	 * How do I subscribe to a LISTSERV list?
    	 * Send e-mail to LISTSERV@LIST.NIH.GOV with the following text in the message body:
    	 * subscribe listname your name
    	 * where listname is the name of the list you wish to subscribe to, and your name is your name.
    	 * (LISTSERV will get your e-mail address from the "From:" address of your e-mail message.)
    	 */

    	String message = "subscribe "+NCIAConfig.getUsersGroupListName()+" "+name;
        new SendMail().sendMail(NCIAConfig.getUsersGroupListEmailAddress(),
        		                emailAddress,
        		                message,
        		                "");

    }
    /**
     * The filename is sometimes sent in with a leading slash.  Clean it off.
     *
     * @param fileName
     */
    private static String cleanFileName(String fileName) {
        // Strip off path to just get the filename
        String nameWithoutPath = fileName.substring(fileName.lastIndexOf(
                    File.separator));

        return (nameWithoutPath.startsWith(File.separator))
        ? nameWithoutPath.substring(1) : nameWithoutPath;
    }
    /**
         * Sends an email notifying list creator that the shared list has been deleted by curator
         *
         * @param email - email address
         * @throws Exception
         */
        public static void sendDeletionOfShareListEmail(String email, String sharedListName, String impactList) {
            try {
                String[] params = {sharedListName,impactList, NCIAConfig.getAdminEmailAddress()};
    
                MessageFormat formatter = new MessageFormat(sharedListDeletionUnFormattedBody);
                String sharedListFormattedBody = formatter.format(params);
                System.out.println("sendQCShareListEmail: " + email + " " +sharedListFormattedBody + "  "+sharedListDeletionSubject);
    
                new SendMail().sendMail(email, sharedListFormattedBody, sharedListDeletionSubject);
            } catch (Exception e) {
                logger.error("Send sharedlist deletion mail error", e);
            }
        }
    
}
