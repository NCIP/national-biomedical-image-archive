/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PatientSearcherServiceFactory {

	public static PatientSearcherService getPatientSearcherService() {
		if(instance==null) {
			instance = createPatientSearcherService();
		}
		return instance;
	}
	
	/////////////////////////////////////PRIVATE////////////////////////////////////
	
	private static PatientSearcherService instance;
	
	private static ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	private static PatientSearcherService createPatientSearcherService() {
		String patientSearcherClassName = System.getProperty("patientSearcherService.className");

		if(patientSearcherClassName==null) {
			throw new RuntimeException("patientSearcherService.className must be defined in system properties");
		}
		else {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = Class.forName(patientSearcherClassName, false, loader);
				PatientSearcherService patientSearcher =  (PatientSearcherService)clazz.newInstance();
				patientSearcher.setExecutorService(executorService);
				return patientSearcher;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}			
	}	
}
