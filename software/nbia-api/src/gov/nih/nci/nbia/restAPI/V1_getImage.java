//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&Modality=CT&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Modality=CT&format=csv
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getBodyPartValues?Collection=IDRI&format=csv

package gov.nih.nci.nbia.restAPI;

import gov.nih.nci.nbia.dao.ImageDAO2;
import gov.nih.nci.nbia.util.SpringApplicationContext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.springframework.dao.DataAccessException;

import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/v1/getImage")
public class V1_getImage {
	static final int BUFFER = 2048;
	/**
	 * This method get a set of images in a zip file
	 *
	 * @return InputStream - PipedInputStream
	 */
	@GET
	@Produces("application/x-zip-compressed")
	public InputStream  constructResponse(@QueryParam("SeriesInstanceUID") String seriesInstanceUid) throws IOException {
		final String sid = 	seriesInstanceUid;
		// we write to the PipedOutputStream
		// that data is then available in the PipedInputStream which we return
		final PipedOutputStream sink = new PipedOutputStream();
		PipedInputStream source = new PipedInputStream(sink);

		if (sid == null) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("SeriesInstanceUID is required.").build());
//			Response.status(400)
//			.entity("Error: please specify SeriesInstanceUID for this search")
//			.build().;
			//need to improve to provide meaningful feedback
		}

		// apparently we need to write to the PipedOutputStream in a separate
		// thread
		Runnable runnable = new Runnable() {
			public void run() {
				ZipOutputStream zip = new ZipOutputStream(sink);
				PrintStream writer = new PrintStream(new BufferedOutputStream(zip));
				try {

					// return a error is brand is missing from the url string. Actually it will never reach this when sid is null.
					if (sid == null) {
						System.out.println("Error: please specify seriesInstanceUid for this search");
//						return Response.status(400)
//								.entity("Error: please specify seriesInstanceUid for this search")
//								.build();
					}

					List<String> fileNames = getDataFromDB (sid);

					for (String filename : fileNames) {
						FileInputStream fis = new FileInputStream(new File(filename));

			            zip.putNextEntry(new ZipEntry(filename));
			            int count;
			            byte[] data = new byte[2048];

		                while ((count = fis.read(data, 0, data.length)) > 0) {
		                	writer.write(data, 0, count);
		                }


		                closeInputStream(fis);
		                writer.flush();
						zip.closeEntry();
			        }
				} catch (Exception e) {
					e.printStackTrace();
//					return Response.status(500)
//							.entity("Server was not able to process your request")
//							.build();
				}

				writer.flush();
				writer.close();
			}
		};
		Thread writerThread = new Thread(runnable, "FileGenerator");
		writerThread.start();

		return source;

	}


	private List<String> getDataFromDB (String seriesInstanceUid) {
		List<String> results = null;

		ImageDAO2 tDao = (ImageDAO2)SpringApplicationContext.getBean("imageDAO2");
		try {
			results = tDao.getImage(seriesInstanceUid);
		}
		catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		return (List<String>) results;
	}

	private void closeInputStream(FileInputStream bis) throws IOException {
		if (bis != null) {
			bis.close();
			bis = null;
		}
	}
}
