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
* Revision 1.1  2007/08/05 21:52:15  bauerd
* *** empty log message ***
*
* Revision 1.1  2007/08/05 21:48:51  bauerd
* *** empty log message ***
*
* Revision 1.5  2006/09/27 20:46:28  panq
* Reformated with Sun Java Code Style and added a header for holding CVS history.
*
*/
package gov.nih.nci.nbia.jms;

import java.io.Serializable;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


public class JMSClient {
    private Logger log = Logger.getLogger(JMSClient.class);
    private Serializable message = null;
    private String queueName = null;
    private String mqURL = null;
    private QueueConnection conn;
    private QueueSession session;
    Queue que;

    public JMSClient(String qName, Serializable o, String url) {
        this.queueName = qName;
        this.message = o;
        this.mqURL = url;
    }

    public void setup() throws JMSException, NamingException {
        System.out.println("RequestSubmitter.setup");

        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(Context.INITIAL_CONTEXT_FACTORY,
            "org.jnp.interfaces.NamingContextFactory");
        props.put(Context.PROVIDER_URL, this.mqURL);
        props.put("java.naming.rmi.security.manager", "yes");
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming");

        //Get the initial context with given properties
        Context iniCtx = new InitialContext(props);
        Object tmp = iniCtx.lookup("ConnectionFactory");
        QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
        conn = qcf.createQueueConnection();
        que = (Queue) iniCtx.lookup(this.queueName);
        session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
        conn.start();
    }

    public void send() throws JMSException, NamingException {
        System.out.println("Begin sending ... ");

        QueueSender send = session.createSender(que);
        ObjectMessage om = session.createObjectMessage(message);
        send.send(om);
        send.close();

        System.out.println("End sending ... ");
    }

    public void close() throws JMSException {
        System.out.println("client close ... ");
        //		conn.stop();
        //		session.close();
        conn.close();
    }

    public void run() {
        try {
            setup();
            send();
            close();
        } catch (Exception e) {
            log.error("Unexpected exception in RequestSubmitter thread: " +
                e.getMessage());
            e.printStackTrace();
        }
    }
}
