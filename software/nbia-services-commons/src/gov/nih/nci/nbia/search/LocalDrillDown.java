/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.search;

import gov.nih.nci.nbia.dao.GeneralSeriesDAO;
import gov.nih.nci.nbia.dao.ImageDAO;
import gov.nih.nci.nbia.dao.StudyDAO;
import gov.nih.nci.nbia.dto.ImageDTO;
import gov.nih.nci.nbia.dto.SeriesDTO;
import gov.nih.nci.nbia.dto.StudyDTO;
import gov.nih.nci.nbia.security.AuthorizationManager;
import gov.nih.nci.nbia.security.PublicData;
import gov.nih.nci.nbia.util.SeriesDTOConverter;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.util.StudyUtil;
import gov.nih.nci.ncia.search.ImageSearchResult;
import gov.nih.nci.ncia.search.ImageSearchResultEx;
import gov.nih.nci.ncia.search.ImageSearchResultExImpl;
import gov.nih.nci.ncia.search.ImageSearchResultImpl;
import gov.nih.nci.ncia.search.NameValuesPairs;
import gov.nih.nci.ncia.search.PatientSearchResult;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.StudySearchResult;
import gov.nih.nci.ncia.search.StudySearchResultImpl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;

/**
 * <p>This impl is concerned with doing drill down on a local database as
 * opposed to a remote node.
 *
 * <p>This impl is used IN the grid service to do a local drill down "over there".
 */
public class LocalDrillDown implements DrillDown {

	/**
	 * Associate an object that knows how to figure out thumbnail urls for
	 * a given DICOM image.  The implemntation will come from presentation.
	 * This must be set before any image related drill down can happen.
	 */
	public void setThumbnailURLResolver(ThumbnailURLResolver thumbnailURLResolver) {
		this.thumbnailURLResolver = thumbnailURLResolver;
	}


	/**
	 * {@inheritDoc}
	 *
	 * <p>If "patient public" is true and this search result isn't for something
	 * that is public, return a 0 length study result array.
	 */
	public StudySearchResult[] retrieveStudyAndSeriesForPatient(PatientSearchResult patientSearchResult) {
		try {
			if (isPatientPublic) {
				PublicData publicData = new PublicData();
				publicData.setAuthorizationManager(authorizationManager);
				if (!publicData.checkPublicPatient(patientSearchResult.getId())) {
					return new StudySearchResult[0];
				}

			}
			StudyDAO studyDAO = (StudyDAO)SpringApplicationContext.getBean("studyDAO");
			List<StudyDTO> studies = studyDAO.findStudiesBySeriesId(patientSearchResult.computeListOfSeriesIds());

			List<StudySearchResultImpl> results = new ArrayList<StudySearchResultImpl>();
			for(StudyDTO studyDTO : studies) {
				results.add(constructResult(studyDTO));
			}

			results = StudyUtil.calculateOffsetValues(results);

			return results.toArray(new StudySearchResult[]{});
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}


	/**
	 * {@inheritDoc}
	 *
	 * <p>If "patient public" is true and this search result isn't for something
	 * that is public, return a 0 length image result array.
	 */
	public ImageSearchResult[] retrieveImagesForSeries(SeriesSearchResult seriesSearchResult) {
        assert thumbnailURLResolver != null;
        if (isPatientPublic) {
        	PublicData publicData = new PublicData();
        	publicData.setAuthorizationManager(authorizationManager);
        	if (!publicData.checkPublicSeries(seriesSearchResult.getId()))
        	{
        		return new ImageSearchResult[]{};
        	}
        }
		return retrieveImagesForSeries(seriesSearchResult.getId());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>If "patient public" is true and this search result isn't for something
	 * that is public, return a 0 length image result array.
	 */
	public ImageSearchResultEx[] retrieveImagesForSeriesEx(SeriesSearchResult seriesSearchResult) {
        assert thumbnailURLResolver != null;
        if (isPatientPublic) {
        	PublicData publicData = new PublicData();
        	publicData.setAuthorizationManager(authorizationManager);
        	if (!publicData.checkPublicSeries(seriesSearchResult.getId()))
        	{
        		return new ImageSearchResultEx[]{};
        	}
        }
		return retrieveImagesForSeriesEx(seriesSearchResult.getId());
	}

	public ImageSearchResultEx[] retrieveImagesForSeriesForAllVersion(SeriesSearchResult seriesSearchResult) {
		return retrieveImagesForSeriesEx(seriesSearchResult);
	}


	/**
	 * This method is only on the local drill down.  It's used by the
	 * qc tool.
	 */
	public ImageSearchResult[] retrieveImagesForSeries(int seriesPkId) {
		return this.retrieveImagesbySeriesPkID(Collections.singletonList(seriesPkId));
	}
	/**
	 * This method is only on the local drill down.  It's used by the
	 * qc tool.
	 */
	public ImageSearchResultEx[] retrieveImagesForSeriesEx(int seriesPkId) {
		return this.retrieveImagesbySeriesPkIDEx(Collections.singletonList(seriesPkId));
	}

	/**
	 * This method is only on the local drill down.  It's used by the
	 * qc tool.  This method does not care about visibility status.
	 */
	public ImageSearchResult[] retrieveImagesForSeries(String seriesInstanceUid) {
        assert thumbnailURLResolver != null;

		try {
	        ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
	        List<ImageDTO> imageDtoList = imageDAO.findImagesbySeriesInstandUid(Collections.singletonList(seriesInstanceUid));
	        if(imageDtoList.size()>0) {
				List<ImageSearchResult> imageSearchResultList = new ArrayList<ImageSearchResult>();
				for(ImageDTO imageDto : imageDtoList) {
					imageSearchResultList.add(constructResult(imageDto));
				}

	        	return imageSearchResultList.toArray(new ImageSearchResult[]{});
	        }
	        else {
	        	return new ImageSearchResult[]{};
	        }
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * This method is only on the local drill down.  It's used by the
	 * qc tool.  This method does not care about visibility status.
	 */
	public ImageSearchResultEx[] retrieveImagesForSeriesEx(String seriesInstanceUid) {
        assert thumbnailURLResolver != null;

		try {
	        ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
	        List<ImageDTO> imageDtoList = imageDAO.findImagesbySeriesInstandUid(Collections.singletonList(seriesInstanceUid));
	        if(imageDtoList.size()>0) {
				List<ImageSearchResultEx> imageSearchResultList = new ArrayList<ImageSearchResultEx>();
				for(ImageDTO imageDto : imageDtoList) {
					imageSearchResultList.add(constructResultEx(imageDto));
				}

	        	return imageSearchResultList.toArray(new ImageSearchResultEx[]{});
	        }
	        else {
	        	return new ImageSearchResultEx[]{};
	        }
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}


	/**
	 * Only necessary on the local drill-down for now (ISPY uses this)
	 */
	public ImageSearchResult[] retrieveImagesbySeriesPkID(List<Integer> seriesPkIds) {
        assert thumbnailURLResolver != null;

		try {
	        ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
	        List<ImageDTO> imageDtoList = imageDAO.findImagesbySeriesPkID(seriesPkIds);

			List<ImageSearchResult> imageSearchResultList = new ArrayList<ImageSearchResult>();
			for(ImageDTO imageDto : imageDtoList) {
				imageSearchResultList.add(constructResult(imageDto));
			}

			return imageSearchResultList.toArray(new ImageSearchResult[]{});
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	/**
	 * Only necessary on the local drill-down for now (ISPY uses this)
	 */
	public ImageSearchResultEx[] retrieveImagesbySeriesPkIDEx(List<Integer> seriesPkIds) {
        assert thumbnailURLResolver != null;

		try {
	        ImageDAO imageDAO = (ImageDAO)SpringApplicationContext.getBean("imageDAO");
	        List<ImageDTO> imageDtoList = imageDAO.findImagesbySeriesPkID(seriesPkIds);

			List<ImageSearchResultEx> imageSearchResultList = new ArrayList<ImageSearchResultEx>();
			for(ImageDTO imageDto : imageDtoList) {
				imageSearchResultList.add(constructResultEx(imageDto));
			}

			return imageSearchResultList.toArray(new ImageSearchResultEx[]{});
		}
		catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	/**
	 * This method is only on the local drill down.  It's used by the
	 * external basket bean.
	 */
	public SeriesSearchResult retrieveSeries(String seriesInstanceUid) {
        assert thumbnailURLResolver != null;

        GeneralSeriesDAO generalSeriesDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");
        List<String> seriesInstanceUids = new ArrayList<String>();
        seriesInstanceUids.add(seriesInstanceUid);
        try {
        	List<SeriesDTO> seriesDtos = generalSeriesDao.findSeriesBySeriesInstanceUID(seriesInstanceUids);

	        if(seriesDtos.size()>0) {
	        	return SeriesDTOConverter.convert(seriesDtos.get(0));
	        }
	        else { //this is a screw up somewhere, throw exception instead?
	        	return null;
	        }
        }
        catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
        }
	}

	/**
	 * This method is only on the local drill down.  It's used by ISPY.
	 */
	public SeriesSearchResult retrieveSeries(int seriesPkId) {
        assert thumbnailURLResolver != null;

        Collection<Integer> ids = new ArrayList<Integer>();
        ids.add(seriesPkId);

        GeneralSeriesDAO generalSeriesDao = (GeneralSeriesDAO)SpringApplicationContext.getBean("generalSeriesDAO");

        try {
        	List<SeriesDTO> seriesDtos = generalSeriesDao.findSeriesBySeriesPkId(ids);

        	return SeriesDTOConverter.convert(seriesDtos.get(0));
        }
        catch(Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
        }
	}

	/**
	 * If the "patient public" bit is set, the authorization manager to use
	 * must be set, otherwise it won't be able to tell what is public.
	 */
	public void setAuthorizationManager(AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	/**
	 * Set this true if drill down should only work for public patients.
	 *
	 * <p>The grid service reuses this code and calls this (locally) and sets to true.  For
	 * a regular drill down this is set false.
	 */
	public void setPatientPublic(boolean isPatientPublic) {
		this.isPatientPublic = isPatientPublic;
	}

	/////////////////////////////////PRIVATE//////////////////////////////////////////////////

	private AuthorizationManager authorizationManager = null;
	private boolean isPatientPublic = false;
	private ThumbnailURLResolver thumbnailURLResolver;

	private ImageSearchResultImpl constructResult(ImageDTO imageDTO) {
		ImageSearchResultImpl result = new ImageSearchResultImpl();
		result.setId(imageDTO.getImagePkId());
		result.setSopInstanceUid(imageDTO.getSopInstanceUid());
		result.setSeriesInstanceUid(imageDTO.getSeriesInstanceUid());
		result.setSeriesId(imageDTO.getSeriesPkId());
		result.setInstanceNumber(imageDTO.getInstanceNumber());
		result.setSize(imageDTO.getSize());
		result.associateLocation(LocalNode.getLocalNode());
		result.setThumbnailURL(thumbnailURLResolver.resolveThumbnailUrl(imageDTO));
		return result;
	}

	private ImageSearchResultExImpl constructResultEx(ImageDTO imageDTO) {
		ImageSearchResultExImpl result = new ImageSearchResultExImpl();
		result.setId(imageDTO.getImagePkId());
		result.setSopInstanceUid(imageDTO.getSopInstanceUid());
		result.setSeriesInstanceUid(imageDTO.getSeriesInstanceUid());
		result.setSeriesId(imageDTO.getSeriesPkId());
		result.setInstanceNumber(imageDTO.getInstanceNumber());
		result.setSize(imageDTO.getSize());
		result.associateLocation(LocalNode.getLocalNode());
		result.setThumbnailURL(thumbnailURLResolver.resolveThumbnailUrl(imageDTO));
		NameValuesPairs nvp = new NameValuesPairs();
		nvp.setName("USFrameNum");
		nvp.setValues(new String []{Integer.toString(imageDTO.getFrameNum())});
		result.setNameValuesPairs(nvp);
		return result;
	}

	private StudySearchResultImpl constructResult(StudyDTO studyDTO) {
		StudySearchResultImpl result = new StudySearchResultImpl();
		result.setId(studyDTO.getId());
		result.setStudyInstanceUid(studyDTO.getStudyId());
		result.setDate(studyDTO.getDate());
		result.setDescription(studyDTO.getDescription());

		result.associateLocation(LocalNode.getLocalNode());
		List<SeriesSearchResult> newSeriesList = new ArrayList<SeriesSearchResult>();
		for(SeriesDTO seriesDto : studyDTO.getSeriesList()) {
			newSeriesList.add(SeriesDTOConverter.convert(seriesDto));
		}
		result.setSeriesList(newSeriesList.toArray(new SeriesSearchResult[]{}));

		return result;
	}
}