/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.cagrid.ncia.client;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.ivi.utils.ZipEntryInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

import javax.xml.namespace.QName;

import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.xml.sax.InputSource;


public abstract class TransferServiceTestCaseFunctional {




	/////////////////////////////////////////PROTECTED///////////////////////////////////////////////////////



	protected int sendCQLForZipFile(NCIACoreServiceClient client,
			                         File cqlFile,
			                         GlobusCredential globusCred) throws Exception {
		InputSource queryInput = new InputSource(new FileReader(cqlFile));

		CQLQuery cqlQuery = (CQLQuery) ObjectDeserializer.deserialize(queryInput, CQLQuery.class);
		dumpCQL(cqlQuery);

		TransferServiceContextReference tscr = client.retrieveDicomData(cqlQuery);

		return processTransfer(tscr, globusCred);
	}
	
	
	protected int processTransfer(TransferServiceContextReference tscr,
			                      GlobusCredential globusCred) throws Exception {
		TransferServiceContextClient tclient = new TransferServiceContextClient(tscr.getEndpointReference(),
				                                                                globusCred);

		InputStream istream = TransferClientHelper.getData(tclient.getDataTransferDescriptor(),
				                                           globusCred);

		if(istream == null){
			System.out.println("istrea is null");
			return -1;
		}
		int zipEntryCnt = readZipFile(istream);


		tclient.destroy();
		istream.close();
		return zipEntryCnt;
	}
	
	
	protected int readZipFile(InputStream transferClientInputStream) throws Exception {
        ZipInputStream zis = new ZipInputStream(transferClientInputStream);
        ZipEntryInputStream zeis = null;
        BufferedInputStream bis = null;
        int zipEntryCount = 0;
        while(true) {
        	try {
        		zeis = new ZipEntryInputStream(zis);
        	}
        	catch (EOFException e) {
        		break;
            }
        	String unzzipedFile = downloadLocation();
        	System.out.println(zipEntryCount + " filename: " + zeis.getName());

            bis = new BufferedInputStream(zeis);
            byte[] data = new byte[8192];
            int bytesRead = 0;
            
            File tempDir = new File(unzzipedFile);

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(File.createTempFile("pre", ".dcm", tempDir)));

			while ((bytesRead = (bis.read(data, 0, data.length))) > 0)  {
				bos.write(data, 0, bytesRead);
			}
			bos.flush();
            bos.close();

            zipEntryCount+=1;
        }
        return zipEntryCount;
	}


	protected static class CqlFileFilter implements java.io.FileFilter {
		public boolean accept(File file) {
			return file.getAbsolutePath().endsWith(".xml");
		}
	}
	protected static File[] getCQLFiles(String relativePath) {
		File directory = new File(relativePath);
		return directory.listFiles(new CqlFileFilter());
	}

	protected static void dumpCQL(CQLQuery query) throws Exception {
		System.err.println(ObjectSerializer.toString(query,
            				                         new QName("http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery", "CQLQuery")));
	}

	protected String downloadLocation(){
		String localClient= System.getProperty("java.io.tmpdir") + File.separator + "nbia-downloads";
		if(!new File(localClient).exists()){
			new File(localClient).mkdir();
		}
		System.out.println("Local download location: "+localClient);
		return localClient;
	}
}
