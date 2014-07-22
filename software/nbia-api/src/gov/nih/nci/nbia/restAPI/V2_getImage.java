//To Test: https://imaging-dev.nci.nih.gov/nbia-api/services/v2/getImage?SeriesInstanceUID=1.3.6.1.4.1.9328.50.1.3

package gov.nih.nci.nbia.restAPI;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.sun.jersey.api.client.ClientResponse.Status;


@Path("/v2/getImage")
public class V2_getImage extends getData {
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
		if (sid == null) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("SeriesInstanceUID is required.").build());
//			Response.status(400)
//			.entity("Error: please specify SeriesInstanceUID for this search")
//			.build().;
			//need to improve to provide meaningful feedback
		}

		Map<String,String> paramMap = new HashMap<String, String>();
		paramMap.put("seriesInstanceUID", sid);

		//SecurityContextHolder will be used to get the user name later.
		if (!isUserHasAccess(null, paramMap))
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Image with given SeriesInstanceUID" +sid + "is not in public domain.").build());


		// we write to the PipedOutputStream
		// that data is then available in the PipedInputStream which we return
		final PipedOutputStream sink = new PipedOutputStream();
		PipedInputStream source = new PipedInputStream(sink);


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

					List<String> fileNames = getImage(sid);

					for (String filename : fileNames) {
						FileInputStream fis = new FileInputStream(new File(filename));
						String fileNameInZip = sid+filename.substring(filename.lastIndexOf(File.separator));
			            zip.putNextEntry(new ZipEntry(fileNameInZip));

			            int count;
			            byte[] data = new byte[2048];

		                while ((count = fis.read(data, 0, data.length)) > 0) {
		                	writer.write(data, 0, count);
		                }


		                closeInputStream(fis);
		                writer.flush();
						zip.closeEntry();
			        }
				}
				catch (FileNotFoundException fnex) {
					fnex.printStackTrace();
					writer.flush();
					writer.close();
					System.out.println("Requested Image file is missing in the file server.");
				}
				catch (Exception e) {
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

	private void closeInputStream(FileInputStream bis) throws IOException {
		if (bis != null) {
			bis.close();
			bis = null;
		}
	}
}
