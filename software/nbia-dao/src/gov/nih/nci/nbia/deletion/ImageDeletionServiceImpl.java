/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.deletion;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.nbia.deletion.dao.AnnotationDAO;
import gov.nih.nci.nbia.deletion.dao.DeletionAuditTrailDAO;
import gov.nih.nci.nbia.deletion.dao.ImageDAO;
import gov.nih.nci.nbia.deletion.dao.PatientDAO;
import gov.nih.nci.nbia.deletion.dao.SeriesDAO;
import gov.nih.nci.nbia.deletion.dao.StudyDAO;
import gov.nih.nci.nbia.exception.DataAccessException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
/**
 * This class provides all necessary services for series deletion.
 * @author zhoujim
 *
 */
@Service
public class ImageDeletionServiceImpl implements ImageDeletionService {
	private static Logger logger = Logger.getLogger(ImageDeletionServiceImpl.class);

	@Autowired
	private SeriesDAO seriesDao;
	@Autowired
	private AnnotationDAO annotationDao;
	@Autowired
	private ImageDAO imageDAO;
	@Autowired
	private StudyDAO studyDao;
	@Autowired
	private PatientDAO patientDao;
	@Autowired
	private DeletionAuditTrailDAO deletionAuditTrailDao;

	private List<String> dicomFileNames;
	//JPG file end with xxx.dicom[512;512;-1].jpeg
	private List<String> annotationFile;

	@Transactional(propagation=Propagation.REQUIRED)
	public List<Integer> getAllDeletedSeries(){
		return seriesDao.listAllDeletedSeries();
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String, List<String>> removeSeries(String userName) throws DataAccessException{
		List<Integer> seriesList = seriesDao.listAllDeletedSeries();
		if (seriesList == null || seriesList.size() == 0)
		{
			//no need to do deletion
			logger.info("There is no series need to be deleted!!!");

			return null;
		}
		annotationFile = annotationDao.deleteAnnotation(seriesList);
		dicomFileNames = imageDAO.removeImages(seriesList); //including CT Images
		//seriesDao.setSeriesObject(seriesList);
		Map<Integer, Integer> studyIds = seriesDao.getStudyMap(seriesList);
		Map<Integer, Integer> patientIds = seriesDao.getPatientMap(seriesList);

		List<DeletionAuditSeriesInfo> seriesDeletionAuditInfoList = seriesDao.getDeletionAuditSeriesInfo(seriesList);
		deletionAuditTrailDao.recordSeries(seriesDeletionAuditInfoList, userName);
		seriesDao.removeSeries(seriesList);

		Map<Integer, Boolean>  patientsOfDeletedStudiesSet = new HashMap<Integer, Boolean>();
		//check study to see if they need to be removed or not
		Iterator<Integer> iterator = studyIds.keySet().iterator();
		while( iterator.hasNext() ){
			Integer studyId =  iterator.next();
			if (studyDao.checkStudyNeedToBeRemoved(studyId, studyIds.get(studyId)))
			{
				DeletionAuditStudyInfo dasi = studyDao.getDeletionAuditStudyInfo(studyId);
				deletionAuditTrailDao.recordStudy(dasi, userName);

				Integer patientId = studyDao.getPatientId(studyId);
				patientsOfDeletedStudiesSet.put(patientId, true);
				studyDao.removeStudy(studyId);
			}
		}

		//if study has not been removed, patient should not be removed.
			//check Patient to see if they need to be removed or not
		iterator = patientIds.keySet().iterator();
		while( iterator. hasNext() )
		{
			Integer patientId =  iterator.next();

			if (patientsOfDeletedStudiesSet.get(patientId) != null
					&& patientsOfDeletedStudiesSet.get(patientId) == true
					&& patientDao.checkPatientNeedToBeRemoved(patientId, patientIds.get(patientId)))
			{
					DeletionAuditPatientInfo dapi = patientDao.getDeletionAuditPatientInfo(patientId);
					deletionAuditTrailDao.recordPatient(dapi, userName);
					patientDao.removePatient(patientId);
			}

		}

		Map<String, List<String>> fileMap = new HashMap<String, List<String>>();
		fileMap.put("dicom", dicomFileNames);
		fileMap.put("annotation", annotationFile);

		return fileMap;


	}

	@Transactional(propagation=Propagation.REQUIRED)
	public List<DeletionDisplayObject> getDeletionDisplayObject()
	{
		List<DeletionDisplayObject> object = new ArrayList<DeletionDisplayObject>();

		List<Integer> seriesList = seriesDao.listAllDeletedSeries();

		object = seriesDao.getDeletionDisplayObjectDTO(seriesList);

		return object;
	}
}
