import gov.nih.nci.cagrid.ncia.client.NCIACoreServiceClient;

import org.cagrid.transfer.context.client.TransferServiceContextClient;
import org.cagrid.transfer.context.client.helper.TransferClientHelper;
import org.cagrid.transfer.context.stubs.types.TransferServiceContextReference;
import org.cagrid.transfer.descriptor.DataTransferDescriptor;
import gov.nih.nci.ivi.utils.ZipEntryInputStream;
import java.util.zip.ZipInputStream

gridServiceUrl = "http://imaging-dev.nci.nih.gov/wsrf/services/cagrid/NCIACoreService"

seriesInstanceUID = "1.3.6.1.4.1.9328.50.1.3"
downloadLocation = "c:/download"

//////////////////////////////////////////
	
try {	
    def nciaCoreServiceClient = new NCIACoreServiceClient(gridServiceUrl)

    def transferServiceContextReference = nciaCoreServiceClient.retrieveDicomDataBySeriesUID(seriesInstanceUID);

    def transferServiceContextClient = new TransferServiceContextClient(transferServiceContextReference.getEndpointReference());

    def inputStream = null
    try {
	     inputStream = TransferClientHelper.getData(transferServiceContextClient.getDataTransferDescriptor());

       downloadZip2(inputStream)
    }
    finally {
       inputStream?.close()
       transferServiceContextClient?.destroy();
    }
       
}
catch(e) {
    e.printStackTrace();
}

def downloadZip2(inputStream) {
		if(inputStream == null){
			  println("istream is null");
		}
		else {
     

        def outputFile = new File(downloadLocation, "foo.zip");
            
        def fileOutputStream = new FileOutputStream(outputFile)
        try {
            fileOutputStream << inputStream
         }
         finally {
            fileOutputStream?.close();
         }

       
    }
}

def downloadZip(inputStream) {
		if(inputStream == null){
			  println("istream is null");
		}
		else {
        def zipInputStream = new ZipInputStream(inputStream);
        def zipEntryInputStream = null;

        while(true) {
            try {
                zipEntryInputStream = new ZipEntryInputStream(zipInputStream);
            }
            catch (EOFException e) {
        		    break;
        	  }

            def outputFile = new File(downloadLocation, zipEntryInputStream.getName());
            outputFile.getParentFile().mkdirs();
            
            def fileOutputStream = new FileOutputStream(outputFile)
            try {
                fileOutputStream << zipEntryInputStream
            }
            finally {
                fileOutputStream?.close();
                zipEntryInputStream?.close();
            }
            
            println "unzipped to:"+outputFile.getAbsolutePath();
        }
    }
}
