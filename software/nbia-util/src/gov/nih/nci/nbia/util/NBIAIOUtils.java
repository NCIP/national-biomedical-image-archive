/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

//from commons-io with a wrinkle for progress tracking
public class NBIAIOUtils {
	public static interface ProgressInterface {
		public void bytesCopied(int bytes);
	}

	public static int copy(InputStream input,
			               OutputStream output,
			               ProgressInterface runnable) throws IOException {
		long count = copyLarge(input, output, runnable);
		if (count > 2147483647L) {
			return -1;
		}
		else {
			return (int) count;
		}
	}

	public static long copyLarge(InputStream input,
			                     OutputStream output,
			                     ProgressInterface runnable) throws IOException {
		byte buffer[] = new byte[4096];
		long count = 0L;
		for (int n = 0; -1 != (n = input.read(buffer));) {
			output.write(buffer, 0, n);
			count += n;
			if(runnable!=null) {
				runnable.bytesCopied(n);
			}

		}

		return count;
	}

	//move this to a FileUtil or something
	public static File createNewDirectory() throws IOException {
		final int maxAttempts = 100;
		int attemptCount = 0;
		File newResultsDir = null;
		do {
			attemptCount++;
			if(attemptCount > maxAttempts)  {
				throw new IOException("The highly improbable has occurred! Failed to " +
						              "create a unique temporary directory after " +
						              maxAttempts + " attempts.");
			}
			String dirName = UUID.randomUUID().toString();
			newResultsDir = new File(sysTempDir, dirName);

			boolean mkdirResult = newResultsDir.mkdirs();
			if(mkdirResult==false) {
				System.out.println("couldnt create directory - might already exist tho so not failing");
			}
		}
		while(!newResultsDir.exists());
		return newResultsDir;
	}

	private static File sysTempDir = new File(System.getProperty("java.io.tmpdir"));

}
