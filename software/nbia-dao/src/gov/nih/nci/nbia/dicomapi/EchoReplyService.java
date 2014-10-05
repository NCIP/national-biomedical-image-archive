/*
 * Thread for DICOM Echo Reply service implementation
 */

package gov.nih.nci.nbia.dicomapi;


/**
 *
 * @author Joaoffr  <joaoffr@ua.pt>
 * @author DavidP   <davidp@ua.pt>
 *
 * @version $Revision: 002 $ $Date: 2008-11-22 14:25:00  $
 * @since Nov 21, 2008
 *
 */
public class EchoReplyService extends Thread 
{

    private DicomEchoReply echoReply = null;


    public EchoReplyService() {
        this.echoReply = new DicomEchoReply();
    }



    public DicomEchoReply getEchoReply() {
        return echoReply;
    }



    @Override
    public void run() {

        try {
            System.out.println("Starting Verification Service...");
            if (this.echoReply.startListening()) {
                System.out.println("Verification Service start listening on port "+ this.echoReply.getLocalPort());

                System.out.println("Verification Service started!");
            } else {
            	System.out.println("Error Starting Verification Service on port " + this.echoReply.getLocalPort());
            }
        }
        catch (Exception ex) {
        	ex.printStackTrace();
        }
        

    }

    public void  stopService() {
        /* ///
        try {
            this.mw.add2ServerLogln("Stopping Verification Service...", MainWindow.LOG_MODES.WARNING);

            if (this.echoReply.stopListening()) {
                super.stop();

                this.mw.add2ServerLogln("Verification Service stoped.", MainWindow.LOG_MODES.WARNING);
            } else {
                this.mw.add2ServerLogln("Error stopping Verification Service on port " + this.echoReply.getLocalPort() + " ...",
                                        MainWindow.LOG_MODES.ERROR);
            }
        }
        catch (Exception ex) {
            this.mw.add2ServerLogln(ex.getMessage(), MainWindow.LOG_MODES.ERROR);
        }
         */
        
    }


}
