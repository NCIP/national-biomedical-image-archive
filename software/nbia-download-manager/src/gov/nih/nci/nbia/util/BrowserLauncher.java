/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.IOException;
import java.lang.reflect.Method;
import gov.nih.nci.nbia.Application;

/**
 * @author lethai
 *
 */
public class BrowserLauncher {
    private static boolean isMac, isWin;
    private static String osname;
    static {
        osname = System.getProperty("os.name");
        isWin = osname.startsWith("Windows");
        isMac = !isWin && osname.startsWith("Mac");
    }

    /**
     * open a web browser from a given url
     */
    public static void openUrl(){
        String url = Application.getOnlineHelpUrl();
        if (isMac){
            openUrlForMac(url);
        }else if(isWin){
            openUrlForWindows(url);
        }else{
            openUrlForOthers(url);
        }
    }

    private static void openUrlForMac(String url){
        try{
            /* the fowllowing reflection codes to open browser on Mac is from 
             * http://www.centerkey.com/java/browser/ 
             * */
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL",new Class[] {String.class});
            openURL.invoke(null, new Object[] {url});
        }catch(Exception e){
            System.out.println("Exception while launching browser " + e.getMessage());
        }
    }
    private static void openUrlForWindows(String url){
        String cmd = "rundll32 url.dll,FileProtocolHandler " + url;
        if (System.getProperty("os.name").startsWith("Windows 2000")){
            cmd = "rundll32 shell32.dll,ShellExec_RunDLL " + url;
        }
        try{
            Process process = Runtime.getRuntime().exec(cmd);
            // This avoids a memory leak on some versions of Java on Windows.
            // That's hinted at in <http://developer.java.sun.com/developer/qow/archive/68/>.
            try {
                process.waitFor();
                process.exitValue();
            } catch (InterruptedException ie) {
                throw new IOException("InterruptedException while launching browser: " + ie.getMessage());
            }
        }catch(IOException e){
            System.out.println("Exception while launching browser " + e.getMessage());
        }
    }
    private static void openUrlForOthers(String url){
        // Assume Linux or Unix
        // Based on BareBonesBrowserLaunch (http://www.centerkey.com/java/browser/)
        String[] browsers = {"netscape", "firefox", "konqueror", "mozilla", "opera", "epiphany", "lynx" };
        String browserName = null;
        try{
            for(int count=0; count<browsers.length && browserName==null; count++) {
                String[] c = new String[] {"which", browsers[count]};
                if (Runtime.getRuntime().exec(c).waitFor()==0){
                     browserName = browsers[count];
                }
            }
            if (browserName==null){
                System.out.println("coudn't find a browser");
            }
            else{
                Runtime.getRuntime().exec(new String[] {browserName, url});
            }
        }catch (Exception e) {
            System.out.println("Exception while launching browser: " + e.getMessage());
        }
    }
}