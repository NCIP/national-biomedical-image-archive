/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	static final int BUFFER = 2048;

	public static void zip(String filesDirToBeZipped, String destFileName,
			String manifest) {

		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(destFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			byte data[] = new byte[BUFFER];
			// get a list of files from current directory
			File f = new File(filesDirToBeZipped);
			String files[] = f.list();

			Vector v = new Vector();
			for (int i = 0; i < files.length; i++) {
				v.add(filesDirToBeZipped + files[i]);
			}
			if (manifest != null) {
				v.add(manifest);
			}

			for (int i = 0; i < v.size(); i++) {
				String filePath = (String) (v.get(i));
				FileInputStream fi = new FileInputStream(filePath);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(filePath);
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String argv[]) {
		if (argv.length < 2) {
			System.out
					.println("Usage: ZipUtil <DirectoryToBeZipped> <Destination file> <manifest>");
		}

		System.out.println("DirectoryToBeZipped = " + argv[0]);
		System.out.println("<Destination file> : " + argv[1]);
		if (argv.length == 2) {
			ZipUtil.zip(argv[0], argv[1], null);
		}
		else {
			ZipUtil.zip(argv[0], argv[1], argv[2]);
		}
	}

}
