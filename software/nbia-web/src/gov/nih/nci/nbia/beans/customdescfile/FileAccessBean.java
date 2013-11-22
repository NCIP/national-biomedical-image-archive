package gov.nih.nci.nbia.beans.customdescfile;
/*
import gov.nih.nci.nbia.beans.BeanManager;
import gov.nih.nci.nbia.dao.LoginHistoryDAO;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.NCIASecurityManager;
import gov.nih.nci.nbia.security.NCIASecurityManager.RoleType;
import gov.nih.nci.nbia.util.MessageUtil;
import gov.nih.nci.nbia.util.NCIAConfig;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.StringUtil;
*/
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.icesoft.faces.context.FileResource;
import com.icesoft.faces.context.Resource;

import gov.nih.nci.nbia.util.MessageUtil;

import org.apache.log4j.Logger;

/**
 * Session scope bean that allows for access files in host machine.
 *
 * @author V. Nikte and Q. Pan
 *
 */
public class FileAccessBean {
	private static Logger logger = Logger.getLogger(FileAccessBean.class);


	public Resource getReleaseNote() {
				return showPdf( MessageUtil.getString("branding.release.note.pdf"));
			}
			public Resource getMriManual() {
				return showPdf( MessageUtil.getString("branding.manual.pdf"));
			}
			public Resource getXrayManual() {
							return showPdf( MessageUtil.getString("branding.xray.manual.pdf"));
			}
			public Resource getExamLog() {
				return showPdf( MessageUtil.getString("branding.exam.log.pdf"));
			}
			public Resource getInterviewLog() {
				return showPdf( MessageUtil.getString("branding.interview.log.pdf"));
			}

			private Resource showPdf(String fileName) {
				Resource bar = loadResource(fileName);
				return bar;
			}

			private static Resource loadResource(String name) {
				File toLoad = getFile(name);
				logger.info("got the file .."+ name);
				if (toLoad != null) {
					return new FileResource(toLoad);
				}

				return null;
			}

			private static File getFile(String fileName) {
				String location = MessageUtil.getString("gov.nih.nci.ncia.pdf.location");
				logger.info("location:-" + location);
				return new File(location + fileName);
			}

			public static byte[] toByteArray(InputStream input) throws IOException {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buf = new byte[4096];
				int len = 0;
				while ((len = input.read(buf)) > -1)
					output.write(buf, 0, len);
				return output.toByteArray();
			}
}

