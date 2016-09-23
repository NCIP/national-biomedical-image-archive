/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/**
 * $Id$
 *
 * $Log: not supported by cvs2svn $
 * Revision 1.8  2007/12/06 15:33:38  lethai
 * Tracker id 8478 - Modality AND search
 * Tracker id 7129 - Anatomical Site
 *
 * Revision 1.7  2007/11/16 19:32:56  lethai
 * Rollback changes to Tracker id 10169
 *
 * Revision 1.3  2007/10/01 12:22:10  bauerd
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/24 15:13:33  bauerd
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/07 12:05:16  bauerd
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/05 21:44:39  bauerd
 * Initial Check in of reorganized components
 *
 * Revision 1.52  2007/01/06 06:36:29  zhouro
 * added new search criteria AcquisitionMatrix, ReconstructionDiameter, DataCollectionDiameter(CT) and DataCollectionDiameter(DX)
 *
 * Revision 1.51  2006/12/13 14:04:14  dietrich
 * Grid enhancement
 *
 * Revision 1.50  2006/12/06 22:18:13  zhouro
 * enhancement on annotation option
 *
 * Revision 1.49  2006/10/19 20:32:56  dietrich
 * Changed to OR numeric and string curation data together instead of ANDing them
 * Revision 1.48 2006/10/10 18:48:32 shinohaa
 * 2.1 enhancements
 *
 * Revision 1.47 2006/09/27 20:46:27 panq Reformated with Sun Java Code Style
 * and added a header for holding CVS history.
 *
 */
/**
 * @copyright
 */
package gov.nih.nci.nbia.search;

import gov.nih.nci.ncia.criteria.AcquisitionMatrixCriteria;
import gov.nih.nci.ncia.criteria.AnatomicalSiteCriteria;
import gov.nih.nci.ncia.criteria.AnnotationOptionCriteria;
import gov.nih.nci.ncia.criteria.AuthorizationCriteria;
import gov.nih.nci.ncia.criteria.CollectionCriteria;
import gov.nih.nci.ncia.criteria.ColorModeOptionCriteria;
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
import gov.nih.nci.ncia.criteria.ConvolutionKernelCriteria;
import gov.nih.nci.ncia.criteria.CurationStatusDateCriteria;
import gov.nih.nci.ncia.criteria.DataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.DateRangeCriteria;
import gov.nih.nci.ncia.criteria.DxDataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.criteria.ImageSliceThickness;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicCodeMeaningCriteria;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicCodeValuePairCriteria;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicQuantificationCriteria;
import gov.nih.nci.ncia.criteria.KilovoltagePeakDistribution;
import gov.nih.nci.ncia.criteria.ManufacturerCriteria;
import gov.nih.nci.ncia.criteria.MinNumberOfStudiesCriteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.NumFrameOptionCriteria;
import gov.nih.nci.ncia.criteria.NumOfMonthsCriteria;
import gov.nih.nci.ncia.criteria.PatientCriteria;
import gov.nih.nci.ncia.criteria.ReconstructionDiameterCriteria;
import gov.nih.nci.ncia.criteria.SeriesDescriptionCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.ncia.criteria.UrlParamCriteria;
import gov.nih.nci.ncia.criteria.UsMultiModalityCriteria;
import gov.nih.nci.nbia.criteriahandler.CriteriaHandler;
import gov.nih.nci.nbia.criteriahandler.CriteriaHandlerFactory;
import gov.nih.nci.nbia.dao.AbstractDAO;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.util.CrossDatabaseUtil;
import gov.nih.nci.nbia.util.HqlUtils;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.util.Ultrasound_Util;
import gov.nih.nci.nbia.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Process a provided query in to an HQL statement and pass it to Hibernate
 *
 * @author NCIA Team
 */
public class DICOMQueryHandlerImpl extends AbstractDAO
                                   implements DICOMQueryHandler {
    private static Logger logger = Logger.getLogger(DICOMQueryHandler.class);

    /*
     * Constants for building non-image query Concatenate the IDs so that only
     * one column is returned (improves performance)
     *
     * <p>because n AIM rows will match to 1 series, need distinct.  without AIM, distinct not necessary
     */
    private static final String SQL_QUERY_SELECT = "SELECT distinct p.id || '/' || study.id || '/' || series.id ";

    //switch query to include aim criteria conditionally

    private static final String SQL_QUERY_FROM_NEITHER = "FROM Patient p join p.dataProvenance dp join p.studyCollection study join study.generalSeriesCollection series ";
    private static final String SQL_QUERY_FROM_BOTH = "FROM StudyNumber sn, NumberMonth nm, Patient p join p.dataProvenance dp join p.studyCollection study join study.generalSeriesCollection series ";
    private static final String SQL_QUERY_FROM_STUDY = "FROM StudyNumber sn, Patient p join p.dataProvenance dp join p.studyCollection study join study.generalSeriesCollection series ";
    private static final String SQL_QUERY_FROM_MONTH = "FROM NumberMonth nm, Patient p join p.dataProvenance dp join p.studyCollection study join study.generalSeriesCollection series ";

    private static final String SQL_QUERY_WHERE_NEITHER = " WHERE series.visibility in ('1', '12') ";
    private static final String SQL_QUERY_WHERE_BOTH = " WHERE series.visibility in ('1', '12') and p.id = sn.id and p.id = nm.id ";
    private static final String SQL_QUERY_WHERE_STUDY = " WHERE series.visibility in ('1', '12') and p.id = sn.id ";
    private static final String SQL_QUERY_WHERE_MONTH = " WHERE series.visibility in ('1', '12') and p.id = nm.id ";

    /*
     * Constants for building image criteria query Use exists clause in subquery
     * instead of distinct to significantly improve performance
     */
    private static final String SQL_IMAGE_SELECT = "select gs.id from GeneralSeries gs where exists (select gi.seriesPKId ";
    private static final String SQL_IMAGE_FROM = "FROM GeneralImage gi";
    private static final String SQL_IMAGE_WHERE = " where gi.seriesPKId = gs.id ";

    /* Constants for fields */
    private static final String COLLECTION_FIELD = "dp.project ";
    private static final String SITE_FIELD = "dp.dpSiteName ";
    private static final String IMAGE_MODALITY_FIELD = "series.modality ";
    private static final String SLICE_THICKNESS_FIELD = "gi.sliceThickness ";
    private static final String KVP_FIELD = "ci.KVP ";
    private static final String RCD_FIELD = "ci.reconstructionDiameter ";
    private static final String CTDCD_FIELD = "ci.dataCollectionDiameter ";
    private static final String AM_FIELD = "gi.acquisitionMatrix ";
    private static final String DXDCD_FIELD = "gi.dxDataCollectionDiameter ";
    private static final String NUMBER_MONTHS_FIELD = "nm.numberMonth ";

    private static final String BODY_PART_EXAMINED_NOT_SPECIFIED = "NOT SPECIFIED";
	private static final String PATIENT_ID_IN_MODALITY = " p.id in ( select gsi2.patientPkId from GeneralSeries gsi2 where gsi2.modality = '";

    // Used to obtain criteria handlers
	private CriteriaHandlerFactory handlerFac = CriteriaHandlerFactory.getInstance();
    private DICOMQuery query = null;
    private String selectStmt = null;
    private String fromStmt = null;
    private String whereStmt = null;

    protected DICOMQueryHandlerImpl() {}

    /**
     * This will process a provided query in to an HQL statement and pass it to
     * Hibernate.
     *
     * <p>I think it returns a PatientResultSet per series that mataches
     * the specified criteria (so not necessarily one object returned per patient)
     *
     * <p>Beware that this thing sits on ALL exceptions even though
     * it declares that it throws exceptions.... a little redesign is in
     * order wrt exception handling  A failure will look the same as
     * no results found....
     */
	@Transactional(propagation=Propagation.REQUIRED)
    public List<PatientStudySeriesTriple> findTriples(DICOMQuery theQuery) throws DataAccessException {
		try {
	        query = theQuery;

	        selectStmt = SQL_QUERY_SELECT;
	        fromStmt = SQL_QUERY_FROM_NEITHER;
	        whereStmt = SQL_QUERY_WHERE_NEITHER;

	        /* List to return */
	        List<PatientStudySeriesTriple> patientList = new ArrayList<PatientStudySeriesTriple>();

	        /* Process non-image criteria */
	        this.nonImageProcess();

	        /* Process UrlParamCriteria if the DICOMQuery is URLType */
	        if (query.isQueryFromUrl()) {
	            whereStmt += urlCriteriaProcess();
	        }

	        /* Process Authorization */
	        String rc = authorizationProcess(query);

	        if (rc == null) {
	            return new ArrayList<PatientStudySeriesTriple>();
	        }
	        else {
	        	whereStmt += rc;
	        }
            
	        /* Process image criteria */
	        String imageClause = imageCriteriaProcess(this.query);
	        String hql = selectStmt + fromStmt + whereStmt + imageClause;

	        /* Run the query */
	        logger.info("Search Issuing query : " + hql);
	        long startTime = System.currentTimeMillis();
	        List<String> results = getHibernateTemplate().find(hql);
	        long elapsedTime = System.currentTimeMillis() - startTime;
	        logger.info("Results returned from query in " + elapsedTime + " ms.");

	        /* Convert the results to PatientResultSet objects */
	        if (results != null) {

	            for (String str : results) {

	                //Parse out the primary keys These are concatenated into one
	                //column to improve performance The fewer columns returned, the
	                //better that Hibernate performs
	            	

	                String[] ids = str.split("/");

	                PatientStudySeriesTriple prs = new PatientStudySeriesTriple();
	                prs.setPatientPkId(Integer.valueOf(ids[0]));
	                prs.setStudyPkId(Integer.valueOf(ids[1]));
	                prs.setSeriesPkId(Integer.valueOf(ids[2]));

	                patientList.add(prs);
	            }
	        }


	        return patientList;
		}
		catch(Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }

    private String urlCriteriaProcess() throws Exception {
        CriteriaHandler handler = CriteriaHandlerFactory.getInstance().createUrlParamCriteria();
        String whereStmt = "";

        List<UrlParamCriteria> urlCriteria = query.getUrlParamCriteria();

        if (urlCriteria.size() > 0) {
            whereStmt += (AND + OPEN_PARENTHESIS);

            Iterator<UrlParamCriteria> it = urlCriteria.iterator();
            UrlParamCriteria crit = it.next();
            whereStmt += handler.handle(null, crit);

            while (it.hasNext()) {
                crit = it.next();
                whereStmt += (OR + handler.handle(null, crit));
            }

            whereStmt += CLOSE_PARENTHESIS;
        }

        return whereStmt;
    }

    /**
     * Process non-image criteria
     *
     * @throws Exception
     */
    private void nonImageProcess() throws Exception {
    	initializeFromAndWhereBasedUponNonImageCriteria();

        whereStmt += processCollectionCriteria(query, handlerFac);

        whereStmt += processPatientCriteria(query, handlerFac);

        whereStmt += processMinimumStudiesCriteria(query);

        whereStmt += processNumberOfMonthsCriteria(query, handlerFac);

        whereStmt += processAnnotationCriteria(query);

		whereStmt += processModalityCriteria(query, handlerFac);

        whereStmt += processAnatomicalSiteCriteria(query);

        whereStmt += processSeriesDescriptionCriteria(query);

        generateGeneralEquipmentJoinHql();
    }


    private void initializeFromAndWhereBasedUponNonImageCriteria() {
        MinNumberOfStudiesCriteria msc = query.getMinNumberOfStudiesCriteria();

        if (msc != null) {
            whereStmt = SQL_QUERY_WHERE_STUDY;
            fromStmt = SQL_QUERY_FROM_STUDY;
        }

        NumOfMonthsCriteria nmc = query.getNumOfMonthsCriteria();

        if (nmc != null) {
            if(msc!=null) {
                whereStmt = SQL_QUERY_WHERE_BOTH;
                fromStmt = SQL_QUERY_FROM_BOTH;

            }
            else {
                whereStmt = SQL_QUERY_WHERE_MONTH;
            	fromStmt = SQL_QUERY_FROM_MONTH;
            }
        }
    }

    private static String quoteMe(String s) {
    	return "'"+s+"'";
    }

    private static String processSeriesDescriptionCriteria(DICOMQuery theQuery) {
        String seriesDescriptionWhereStmt = "";
        SeriesDescriptionCriteria sdc = theQuery.getSeriesDescriptionCriteria();
        if (sdc != null) {
        	seriesDescriptionWhereStmt += (SERIES_DESC + sdc.getSeriesDescriptionValue() +
            		      "')||'%' ");
        }
        return seriesDescriptionWhereStmt;
    }

    private static String processNumberOfMonthsCriteria(DICOMQuery theQuery,
    		                                            CriteriaHandlerFactory theHandlerFac) throws Exception {
        NumOfMonthsCriteria nmc = theQuery.getNumOfMonthsCriteria();
        String numMonthWhereStmt = "";
        if (nmc != null) {
        	CriteriaHandler handler = theHandlerFac.createNumOfMonths();
        	numMonthWhereStmt += handler.handle(NUMBER_MONTHS_FIELD, nmc);
        }
        return numMonthWhereStmt;
    }

    private static String processMinimumStudiesCriteria(DICOMQuery theQuery) {
        MinNumberOfStudiesCriteria msc = theQuery.getMinNumberOfStudiesCriteria();
        String minStudiesWhereStmt = "";
        if (msc != null) {
            Integer tempInteger = msc.getMinNumberOfStudiesValue();
            minStudiesWhereStmt += (AND + STUDY_NUMBER + tempInteger + " ");
        }
        return minStudiesWhereStmt;
    }

    private static String processCollectionCriteria(DICOMQuery theQuery,
    		                                        CriteriaHandlerFactory theHandlerFac) throws Exception {
        CollectionCriteria cc = theQuery.getCollectionCriteria();
        CriteriaHandler handler = null;

        String collectionWhereStmt = "";
        if (cc != null) {
            // This assumes the list has already been filtered to include only
            // those
            // collections that the user is allowed to see
            // Need to be careful of rerunning a saved query where the user
            // previously
            // had access but does not now.
            handler = theHandlerFac.createCriteriaCollection();
            collectionWhereStmt += (AND + handler.handle(COLLECTION_FIELD, cc));
        }
        return collectionWhereStmt;
    }

    private static String processPatientCriteria(DICOMQuery theQuery,
            									 CriteriaHandlerFactory theHandlerFac) throws Exception {
		PatientCriteria pc = theQuery.getPatientCriteria();
		CriteriaHandler handler = null;

		String patientWhereStmt = "";
		if (pc != null) {

		handler = theHandlerFac.createCriteriaCollection();
		patientWhereStmt += (AND + handler.handle(PATIENT_ID, pc));
		}
		return patientWhereStmt;
}

    /**
     * Returns the part of the where clause to match a series
     * based upon whether it has annotations.  NOTE: this only
     * matches the series.annotation_flag.  It doesn't care whether
     * there is ACTUALLY a row in the annotation table for a given
     * series.  In other words, there are series in the db that
     * have the annotation flag high, and no rows in annotation table.
     */
    private static String processAnnotationCriteria(DICOMQuery theQuery) {
    	String annotationWhereStmt = "";
        AnnotationOptionCriteria aoc = theQuery.getAnnotationOptionCriteria();
        if (aoc != null) {
        	String annotationOptionValue = aoc.getAnnotationOptionValue();

            if (annotationOptionValue.equalsIgnoreCase(AnnotationOptionCriteria.AnnotationOnly)) {
            	annotationWhereStmt += (AND + SERIES_ANNOTATIONS_FLAG_TRUE);
            }
            else if (annotationOptionValue.equalsIgnoreCase(AnnotationOptionCriteria.NoAnnotation)) {
            	annotationWhereStmt += (AND + SERIES_ANNOTATIONS_FLAG_FALSE);
            }
        }
        return annotationWhereStmt;
    }

    private static String processModalityCriteria(DICOMQuery theQuery,
    		                                      CriteriaHandlerFactory theHandlerFac) throws Exception {
        ImageModalityCriteria imc = theQuery.getImageModalityCriteria();

        String modalityClause = "";
        if (imc != null) {
        	//Modified for boolean search
        	if (theQuery.getModalityAndedSearchCriteria() != null &&
        		theQuery.getModalityAndedSearchCriteria().getModalityAndedSearchValue().equals("all")) {
                for(String modalityCriteria : imc.getImageModalityObjects()){
                	modalityClause += (AND +
                			          PATIENT_ID_IN_MODALITY +
                			          modalityCriteria +
                			          "')");
                }
            } else {
            	CriteriaHandler handler = theHandlerFac.createCriteriaCollection();
            	modalityClause += (AND +
            			handler.handle(IMAGE_MODALITY_FIELD, imc));
            }
        }
        return modalityClause;
    }

    private static String processUsMultiModality(DICOMQuery theQuery){
		UsMultiModalityCriteria ummc = theQuery.getUsMultiModalityCriteria();

		String usMultiModalityClause = " ";
		if (ummc != null) {
        	for(String mmode:ummc.getUsMultiModalityObjects()) {

 			if (usMultiModalityClause.equals(" ")){
 				usMultiModalityClause += "and gi.usMultiModality is not null and";
 			}
 			else {
 				usMultiModalityClause += "or ";
 			}
 			usMultiModalityClause += " gi.usMultiModality like '%"+Ultrasound_Util.getCodeByGivenImageTypeLabel(mmode)+"%' ";
        	}
		}
		return usMultiModalityClause;
	}

    private static String processAnatomicalSiteCriteria(DICOMQuery theQuery) {

        AnatomicalSiteCriteria asc = theQuery.getAnatomicalSiteCriteria();

        String anatomicalSiteClause = "";

        if (asc != null) {
        	List<String> asite = new ArrayList<String>();
        	for(int i=0; i<asc.getAnatomicalSiteValueObjects().size(); i++) {
        		String anatomicalSite = asc.getAnatomicalSiteValueObjects().get(i);
        		asite.add(anatomicalSite);
        	}
        	boolean isNull=false;
        	//When body_part_examined is not specified which is null in the database, UI will display "NOT SPECIFIED" in the
        	// option list, here we need to change it back to search for null.
        	if(asite.contains(BODY_PART_EXAMINED_NOT_SPECIFIED)) {
        		//asite.remove(BODY_PART_EXAMINED_NOT_SPECIFIED);
        		isNull=true;
        	}
        	if(isNull) {
            	if(asite.size() > 0) {
            		anatomicalSiteClause += " and ( series.bodyPartExamined is null " +
            		                        HqlUtils.buildInClause(" or upper(series.bodyPartExamined) in ", asite) + ") ";
            	}
            	else {
            		anatomicalSiteClause += " and series.bodyPartExamined is null  ";
            	}
            }
        	else {
        		anatomicalSiteClause += HqlUtils.buildInClause(" and upper(series.bodyPartExamined) in ", asc.getAnatomicalSiteValueObjects());
            }
        }
        return anatomicalSiteClause;
    }

    // Process the criteria that need to join the general equipment table
    private void generateGeneralEquipmentJoinHql() throws Exception {
        // Process the criteria that need to join the general equipment table
        ManufacturerCriteria mc = query.getManufacturerCriteria();
        ModelCriteria mc1 = query.getModelCriteria();
        SoftwareVersionCriteria svc = query.getSoftwareVersionCriteria();

        if ((mc != null) || (mc1 != null) || (svc != null)) {
            fromStmt += SERIES_GENERAL_EQUIPMENT;
            whereStmt += (AND + "( ");

            if (mc != null) {
            	CriteriaHandler handler = handlerFac.createCriteriaCollection();
                whereStmt += handler.handle(MANUFACTURER_FIELD, mc);

                if ((mc1 != null) || (svc != null)) {
                    whereStmt += OR;
                }
            }

            if (mc1 != null) {
            	CriteriaHandler handler = handlerFac.createModelSoftwareCriteria();
                whereStmt += handler.handle(MODEL_FIELD, mc1);

                if (svc != null) {
                    whereStmt += OR;
                }
            }

            if (svc != null) {
            	CriteriaHandler handler = handlerFac.createModelSoftwareCriteria();
                whereStmt += handler.handle(SOFTWARE_VERSION_FIELD, svc);
            }

            whereStmt += CLOSE_PARENTHESIS;
        }
    }

    private static String processAuthorizationCollections(DICOMQuery theQuery) {
        AuthorizationCriteria authCrit = theQuery.getAuthorizationCriteria();

        if (((authCrit.getCollections() != null) &&
       		 authCrit.getCollections().isEmpty()) ||
       		 ((theQuery.getCollectionCriteria() != null) &&
       		   theQuery.getCollectionCriteria().isEmpty())) {
            // User did not select any collections to view
            // But authorization says they can't view any anyway
            // Since all data belongs to a collection, user is not allowed to
            // see anything
            // Return empty list
            logger.info("No results returned because user does not have access to any collections");

            return null;
        }
        else {
        	String whereStmt = "";
        	/* removed to make the same as dynamic search which does not have this extra restriction but bases the restriction on collection + site
        	 * which is the understood way to do it
            if (authCrit.getCollections() != null) {
                // Collections will only be on auth crit if user did not include
                // collection criteria
                // from the search page
                whereStmt += HqlUtils.buildInClause(AND +
                		                            COLLECTION_FIELD +
                		                            IN,
                		                            authCrit.getCollections());
            }
            */
            return whereStmt;
        }
    }

    private static String processAuthorizationSites(DICOMQuery theQuery)  {
        AuthorizationCriteria authCrit = theQuery.getAuthorizationCriteria();

        if (authCrit.getSites().isEmpty()) {
            // User is not allowed to view any sites.
            // Since all data has a site, user is not allowed to see anything
            // Return empty list
            logger.info("No results returned because user does not have access to any sites");

            return null;
        }
        else {
            // Build HQL for sites
        	String whereStmt = "";
            whereStmt += (AND + OPEN_PARENTHESIS);

            boolean first = true;

            // For each site, need to include both collection and site
            // since site names can be duplicated across collections
            for (SiteData siteData : authCrit.getSites()) {
                if (!first) {
                    whereStmt += OR;
                }

                whereStmt += "(";
                whereStmt += COLLECTION_FIELD;
                whereStmt += " = '";
                whereStmt += siteData.getCollection();
                whereStmt += ("' " + AND);
                whereStmt += SITE_FIELD;
                whereStmt += " = '";
                whereStmt += siteData.getSiteName();
                whereStmt += "') ";
                first = false;
            }

            whereStmt += CLOSE_PARENTHESIS;
            return whereStmt;
        }
    }

    private static String processAuthorizationSecurityGroups(DICOMQuery theQuery) {
        AuthorizationCriteria authCrit = theQuery.getAuthorizationCriteria();

    	String whereStmt = "";
        // Filter by series security groups
        whereStmt += SERIES_SECURITY_GROUP_NULL;

        if ((authCrit.getSeriesSecurityGroups() != null) && !authCrit.getSeriesSecurityGroups().isEmpty()) {
            // Users are always allowed to view null series security groups
            whereStmt += HqlUtils.buildInClause(SERIES_SECURITY_GROUP_IN, authCrit.getSeriesSecurityGroups());
        }

        whereStmt += CLOSE_PARENTHESIS;
        return whereStmt;
    }

    /**
     * Process authorization to ensure the authorization criteria is valid
     *
     * @throws Exception
     */
    private static String authorizationProcess(DICOMQuery theQuery) throws Exception {
    	authorizationCriteriaPreconditions(theQuery);

    	String whereStmt = "";

        String collectionsWhereStmt = processAuthorizationCollections(theQuery);
        if(collectionsWhereStmt==null) {
        	return null;
        }
        else {
        	//whereStmt += collectionsWhereStmt; No collections in dynamic search the current way is site + collection
        }

        String sitesWhereStmt = processAuthorizationSites(theQuery);
        if(sitesWhereStmt==null) {
        	return null;
        }
        else {
        	whereStmt += sitesWhereStmt;
        }

        whereStmt += processAuthorizationSecurityGroups(theQuery);

        return whereStmt;
    }

    /**
     * Make sure authorization criteria is set and collections
     * are specified in the authorization criteria or the collection criteria
     * (when is set in one or the other?)
     */
    private static void authorizationCriteriaPreconditions(DICOMQuery theQuery) {
        AuthorizationCriteria authCrit = theQuery.getAuthorizationCriteria();

        // Check to ensure authorization criteria is valid
        if ((authCrit == null) || authCrit.isEmpty()) {
            throw new RuntimeException("Authorization criteria must be included with the query");
        }

        if ((authCrit.getCollections() == null) && (theQuery.getCollectionCriteria() == null)) {
           // throw new RuntimeException("Collections must either be included in collection criteria or authorization criteria");
        }

        if ((authCrit.getCollections() != null) && (theQuery.getCollectionCriteria() != null) &&
      		!authCrit.isEmpty() && !theQuery.getCollectionCriteria().isEmpty()) {
            throw new RuntimeException("Collections must be in either collection criteria or authorization, but not both");
        }
    }

    private static boolean hasImageCritera(DICOMQuery query) {
        ContrastAgentCriteria cac = query.getContrastAgentCriteria();
        ImageSliceThickness ist = query.getImageSliceThickness();
        CurationStatusDateCriteria csdc = query.getCurationStatusDateCriteria();
        NumFrameOptionCriteria nfoc = query.getNumFrameOptionCriteria();
    	UsMultiModalityCriteria ummc = query.getUsMultiModalityCriteria();
    	ColorModeOptionCriteria cmoc = query.getColorModeOptionCriteria();
        return (cac != null) ||
    	       (ist != null) ||
    	       (csdc != null) ||
    	       (nfoc != null) ||
    	       (ummc != null) ||
    	       (cmoc != null) ||
    	       hasCtImageCritera(query);
    }

    private static boolean hasCtImageCritera(DICOMQuery query) {
        KilovoltagePeakDistribution kvp = query.getKilovoltagePeakDistribution();
        ReconstructionDiameterCriteria rcd = query.getReconstructionDiameterCriteria();
        AcquisitionMatrixCriteria am = query.getAcquisitionMatrixCriteria();
        DataCollectionDiameterCriteria ctDcd = query.getDataCollectionDiameterCriteria();
        DxDataCollectionDiameterCriteria dxDcd = query.getDxDataCollectionDiameterCriteria();
        ConvolutionKernelCriteria ckc = query.getConvolutionKernelCriteria();

        logger.info("am:"+am);
        logger.info("dxDcd:"+dxDcd);

        return (kvp != null) ||
        	   (rcd !=null ) ||
        	   (am !=null )||
        	   (ctDcd !=null )||
               (dxDcd !=null )||
               (ckc != null);
    }
    /**
     * Process image criteria
     *
     * @throws Exception
     */
    private static String imageCriteriaProcess(DICOMQuery theQuery) throws Exception {
        boolean imageCriteriaIncluded = false;
        String imgSelect = SQL_IMAGE_SELECT;
        String imgFrom = SQL_IMAGE_FROM;
        String imgWhere = SQL_IMAGE_WHERE;

        CriteriaHandlerFactory handlerFac = CriteriaHandlerFactory.getInstance();

        // No changes are required if no image criteria is included
        if (hasImageCritera(theQuery)) {
            imageCriteriaIncluded = true;

            imgWhere += processContrastAgent(theQuery);
            imgWhere += processFrameNumOption(theQuery);
            imgWhere += processColorModeOption(theQuery);

            imgWhere += processUsMultiModality(theQuery);

            imgWhere += processCurationStatusDateCriteria(theQuery);

			imgWhere += processImageSliceThickness(theQuery, handlerFac);

            if (hasCtImageCritera(theQuery)) {
                imgFrom += JOIN_CTIMAGE;

                imgWhere += processCtImageCriteria(theQuery);
            }
        }

        String dateRangeClause = processDateRange(theQuery);
        if(!dateRangeClause.equals("")) {
            imageCriteriaIncluded = true;
            imgWhere += dateRangeClause;
        }

        // Build the HQL
        // Always include non-image criteria query
        String hql = "";//selectStmt + fromStmt + whereStmt;

        // Include image criteria query as a subquery
        if (imageCriteriaIncluded) {
            // Add the image criteria as a subselect in the where to ensure that
            // only rows that meet
            // both the non-image criteria and image criteria
            hql += AND;
            hql += SERIES_ID_IN;
            hql += imgSelect;
            hql += imgFrom;
            hql += imgWhere;
            hql += "))";
        }

        return hql;
    }

    private static String processCurationStatusDateCriteria(DICOMQuery theQuery) {
    	String imgWhere = "";
        CurationStatusDateCriteria csdc = theQuery.getCurationStatusDateCriteria();

        if (csdc != null) {
            // Add a filter to only include records with curation timestamp
            // greater than the specified date
            imgWhere += CURATION_TIMESTAMP;
            imgWhere += "'";
            // Format the date according to the configuration
            imgWhere += CrossDatabaseUtil.getDatabaseSpecificDatePattern().format(csdc.getCurationStatusDate());
            imgWhere += "' ";
        }
        return imgWhere;
    }

    private static String processImageSliceThickness(DICOMQuery theQuery,
                                                     CriteriaHandlerFactory theHandlerFac) throws Exception {
        ImageSliceThickness ist = theQuery.getImageSliceThickness();
        CriteriaHandler handler = null;
    	String imgWhere = "";
        if (ist != null) {
            handler = theHandlerFac.createImageSliceThickness();
            imgWhere += handler.handle(SLICE_THICKNESS_FIELD, ist);
        }
        return imgWhere;
    }

    private static String processCtImageCriteria(DICOMQuery theQuery) throws Exception {
        KilovoltagePeakDistribution kvp = theQuery.getKilovoltagePeakDistribution();
        ReconstructionDiameterCriteria rcd = theQuery.getReconstructionDiameterCriteria();
        AcquisitionMatrixCriteria am = theQuery.getAcquisitionMatrixCriteria();
        DataCollectionDiameterCriteria ctDcd = theQuery.getDataCollectionDiameterCriteria();
        DxDataCollectionDiameterCriteria dxDcd = theQuery.getDxDataCollectionDiameterCriteria();
        ConvolutionKernelCriteria ckc = theQuery.getConvolutionKernelCriteria();

        CriteriaHandlerFactory handlerFac = CriteriaHandlerFactory.getInstance();
        CriteriaHandler handler = null;

    	String ctImageWhereClause ="";
        if (kvp != null) {
            handler = handlerFac.createKilovoltagePeakDistribution();
            ctImageWhereClause += handler.handle(KVP_FIELD, kvp);
        }
        if (rcd != null) {
            handler = handlerFac.createReconstructionDiameter();
            ctImageWhereClause += handler.handle(RCD_FIELD, rcd);
        }
        //not ct?
        if (am != null) {
            handler = handlerFac.createAcquisitionMatrix();
            ctImageWhereClause += handler.handle(AM_FIELD, am);
        }
        if (ctDcd != null) {
            handler = handlerFac.createCtDataCollectionDiameter();
            ctImageWhereClause += handler.handle(CTDCD_FIELD, ctDcd);
        }
        //not ct?
        if (dxDcd != null) {
            handler = handlerFac.createDxDataCollectionDiameter();
            ctImageWhereClause += handler.handle(DXDCD_FIELD, dxDcd);
        }
        if (ckc != null) {
            ctImageWhereClause += HqlUtils.buildInClause(CONVOLUTION_KERNEL, ckc.getConvolutionKernelValues());
        }
        return ctImageWhereClause;
    }
    /**
     * Given a query, returns the where clause for the contrast agent
     * portion of the query.  Returns 0 length string if no contrast agent
     */
    private static String processContrastAgent(DICOMQuery theQuery) {
        ContrastAgentCriteria cac = theQuery.getContrastAgentCriteria();

        if (cac != null) {
            String contrastAgentValue = cac.getContrastAgentValue();

            if (contrastAgentValue.equalsIgnoreCase(ContrastAgentCriteria.ENHANCED)) {
                return GI_CONSTRAST_BOLUS_AGENT_NOT_NULL;
            }
            else
            if (contrastAgentValue.equalsIgnoreCase(ContrastAgentCriteria.UNENHANCED)) {
            	return GI_CONSTRAST_BOLUS_AGENT_NULL;
            }
            else {
                throw new RuntimeException("Not supported constrast agent criteria: " + contrastAgentValue);
            }
        }
        return "";
    }

    /**
     * Given a query, returns the where clause for the number of frame
     * portion of the query.  Returns 0 length string if no number of frame
     */
    private static String processFrameNumOption(DICOMQuery theQuery) {
    	NumFrameOptionCriteria nfoc = theQuery.getNumFrameOptionCriteria();

        if (nfoc != null) {
            String numFrameValue = nfoc.getNumFrameOptionValue();

            if (numFrameValue.equalsIgnoreCase(NumFrameOptionCriteria.MultiFrame)) {
                return GI_MULTI_FRAME;
            }
            else
            if (numFrameValue.equalsIgnoreCase(NumFrameOptionCriteria.SingleFrameOnly)) {
            	return GI_SINGLE_FRAME;
            }
            else {
                throw new RuntimeException("Not supported numFrameOptionCriteria: " + numFrameValue);
            }
        }
        return "";
    }

    private static String processColorModeOption(DICOMQuery theQuery) {
    	ColorModeOptionCriteria cmoc = theQuery.getColorModeOptionCriteria();

        if (cmoc != null) {
            String colorModeValue = cmoc.getColorModeOptionValue();

            if (colorModeValue.equalsIgnoreCase(ColorModeOptionCriteria.BMode)) {
                return GI_BMODE;
            }
            else
            if (colorModeValue.equalsIgnoreCase(ColorModeOptionCriteria.ColorMode)) {
            	return GI_COLOR_MODE;
            }
            else {
                throw new RuntimeException("Not supported colorModeOptionCriteria: " + colorModeValue);
            }
        }
        return "";
    }

    /**
     * Given a query, returns the where clause for the date range
     * portion of the query.  Returns 0 length string if no date range
     */
    private static String processDateRange(DICOMQuery theQuery) {
        DateRangeCriteria drc = theQuery.getDateRangeCriteria();
        String fromDateString = null;
        String toDateString = null;

        if ((drc != null) && !(drc.isEmpty())) {
            Date fromDate = drc.getFromDate();
            Date toDate = drc.getToDate();

            // format the date based on the date-format in ncia.properties file
            SimpleDateFormat sdf = CrossDatabaseUtil.getDatabaseSpecificDatePattern();

            if (fromDate != null) {
                fromDateString = sdf.format(fromDate);
            }

            if (toDate != null) {
                toDateString = sdf.format(toDate);
            }

			return CrossDatabaseUtil.curationTimeStampRange(fromDateString, toDateString);
        }

        return "";
    }
}
