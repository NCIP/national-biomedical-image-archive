/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;




public class FileCopier {

	private int numberOfFilesInSrcDirctory=0;

    public FileCopier(String srcDirName, String desDirName, int timeInterval) {
    	System.out.println("Copy file in every "+ timeInterval +" seconds");
    	// read files from src
    	File file = new File(srcDirName);
		
    	if (file.listFiles()==null) {
    		System.out.println("Source directory path "+srcDirName+" is invalid");
    	}
    	
		numberOfFilesInSrcDirctory = file.listFiles().length;

		

		File[] srcFiles = file.listFiles();
		
		for (int i=0;i<numberOfFilesInSrcDirctory;i++){
			System.out.println("Copy file "+
					srcFiles[i].getAbsolutePath()+
					" to "+desDirName+"\\"+srcFiles[i].getName());
			
			copyFile(srcFiles[i],desDirName);
			try {
				Thread.sleep(timeInterval*1000);
			}
			catch (Exception ex){
				System.out.println("interrupted");
				ex.printStackTrace();
			}

		}
    	
    }

private void copyFile(File srcFile, String desDirectory){    
    try {
        // Create channel on the source
        FileChannel srcChannel = new FileInputStream(srcFile.getAbsolutePath()).getChannel();
    
        // Create channel on the destination
        FileChannel dstChannel = new FileOutputStream(desDirectory+"\\"+srcFile.getName()).getChannel();
    
        // Copy file contents from source to destination
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
    
        // Close the channels
        srcChannel.close();
        dstChannel.close();
    } catch (IOException e) {
    	e.printStackTrace();
    }
}

	/** ****************** test program ************************ */
	public static void main(String[] args )throws Exception {
		
		new FileCopier(args[0], args[1], Integer.parseInt( args[2]));

		System.out.println("=== Process is completed ===");
	}		
	
	

}

