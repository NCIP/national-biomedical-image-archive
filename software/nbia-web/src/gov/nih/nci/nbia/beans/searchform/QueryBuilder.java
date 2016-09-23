/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.ncia.criteria.AnatomicalSiteCriteria;
import gov.nih.nci.ncia.criteria.AnnotationOptionCriteria;
import gov.nih.nci.ncia.criteria.CollectionCriteria;
import gov.nih.nci.ncia.criteria.ColorModeOptionCriteria;
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
import gov.nih.nci.ncia.criteria.ConvolutionKernelCriteria;
import gov.nih.nci.ncia.criteria.DateRangeCriteria;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.criteria.ImageSliceThickness;
import gov.nih.nci.ncia.criteria.KilovoltagePeakDistribution;
import gov.nih.nci.ncia.criteria.ManufacturerCriteria;
import gov.nih.nci.ncia.criteria.MinNumberOfStudiesCriteria;
import gov.nih.nci.ncia.criteria.ModalityAndedSearchCriteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.NumFrameOptionCriteria;
import gov.nih.nci.ncia.criteria.NumOfMonthsCriteria;
import gov.nih.nci.ncia.criteria.PatientCriteria;
import gov.nih.nci.ncia.criteria.SeriesDescriptionCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.ncia.criteria.UsMultiModalityCriteria;
import gov.nih.nci.nbia.query.DICOMQuery;
import gov.nih.nci.nbia.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * This used to be inside the SearchWorkflowBean.  This obejct is solely responsible
 * for the act of mapping the UI input to criteria/query.  It relies on the SearchWorkflowBean
 * to get the UI input...and then it spits out a DICOMQuery object.
 */
class QueryBuilder {

	public static DICOMQuery buildQuery(SearchWorkflowBean searchBean, boolean advanced) {

        DICOMQuery query = new DICOMQuery();


        addSimpleCriteria(searchBean, query);

        if (advanced) {
        	addAdvancedCriteria(searchBean, query);
        }

        return query;
	}

	////////////////////////////////////PRIVATE////////////////////////////////////////////////
    private static void addSimpleCriteria(SearchWorkflowBean searchBean,DICOMQuery query) {        
    	// Setup number of previous studies criteria here
        if (!StringUtil.isEmpty(searchBean.getNumberStudies())) {
            MinNumberOfStudiesCriteria nsc = new MinNumberOfStudiesCriteria();
            nsc.setMinNumberOfStudiesValue(Integer.parseInt(searchBean.getNumberStudies()));
            query.setCriteria(nsc);
        }

        DateRangeCriteria drc = buildDateCrit(searchBean);
        if (drc != null&&searchBean.isDateCriteria()) {
        	System.out.println("Date Criteria "+drc);
        	query.setCriteria(drc);
        }

        PatientCriteria prc = buildPatientCrit(searchBean.getPatientInput());
        if (prc != null&&searchBean.isPatientCriteria()) {
        	System.out.println("Patient Criteria "+prc);
        	query.setCriteria(prc);
        }

        // Setup image modality criteria here
        List<String> selectedModalities = searchBean.getSelectedModalityNames();
        if (selectedModalities.size()>0) {
            ImageModalityCriteria imc = new ImageModalityCriteria();
            imc.setImageModalityObjects(selectedModalities);
            query.setCriteria(imc);
        }
        if (selectedModalities.size()>0) {
        	ModalityAndedSearchCriteria masc = new ModalityAndedSearchCriteria();
        	masc.setModalityAndedSearchValue(searchBean.getModalityAndedSearch());
        	query.setCriteria(masc);
        }
        // Setup US Multi modality criteria here
        List<String> selectedUsMultiModalities = searchBean.getSelectedUsMultiModalityNames();
        if (selectedUsMultiModalities.size()>0) {
            UsMultiModalityCriteria ummc = new UsMultiModalityCriteria();
            ummc.setUsMultiModalityObjects(selectedUsMultiModalities);
            query.setCriteria(ummc);
        }
 //       ModalityAndedSearchCriteria umasc = new ModalityAndedSearchCriteria();
//        umasc.setModalityAndedSearchValue(searchBean.getModalityAndedSearch());
 //       query.setCriteria(umasc);

        List<String> selectedAnatomical = searchBean.getSelectedAnatomicalSiteNames();

        // Setup anatomical site criteria here
        if (selectedAnatomical.size()>0) {
            query.setCriteria(new AnatomicalSiteCriteria(selectedAnatomical));
        }

        // Setup collections here
        List<String> selectedCollectionNames = searchBean.getSelectedCollectionNames();
        if (selectedCollectionNames.size()>0) {
            CollectionCriteria cc = new CollectionCriteria();
            cc.setCollectionObjects(selectedCollectionNames);
            query.setCriteria(cc);
        }

        // Setup contrast agent criteria here
        if(searchBean.getContrastAgents().length==1) {
            String theOneContrastAgent = searchBean.getContrastAgents()[0];
            query.setCriteria(new ContrastAgentCriteria(theOneContrastAgent));
        }
        //else it is 0 or 2 in which case there is no filter to be had

        // Setup frame number criteria here
        if(searchBean.getNumFrameOptions().length==1) {
            String frameOption = searchBean.getNumFrameOptions()[0];
           	query.setCriteria(new NumFrameOptionCriteria(frameOption));
        }
        // Setup ultra sound color criteria here
        if(searchBean.getColorModeOptions().length==1) {
            String colorOption = searchBean.getColorModeOptions()[0];
           	query.setCriteria(new ColorModeOptionCriteria(colorOption));
        }



        // Setup AnnotationOption criteria here
        if(searchBean.getAnnotationOptions().length==1) {
            String theOneAnnotationOption = searchBean.getAnnotationOptions()[0];
            query.setCriteria(new AnnotationOptionCriteria(theOneAnnotationOption));
        }
        //else it is 0 or 2 in which case there is no filter to be had


        // Setup image slice thickness criteria here
        if (!StringUtil.isEmpty(searchBean.getThicknessLeftCompare()) && searchBean.getShowThickness()) {
            ImageSliceThickness ist = new ImageSliceThickness(searchBean.getThicknessLeftCompare(),
            		                                          searchBean.getImageThicknessLeft(),
            		                                          searchBean.getThicknessRightCompare(),
            		                                          searchBean.getImageThicknessRight());
            query.setCriteria(ist);
        }
    }

    private static void addAdvancedCriteria(SearchWorkflowBean searchBean,DICOMQuery query) {

        // Setup number of previous studies criteria here
        if (!StringUtil.isEmpty(searchBean.getNumberStudies())) {
            MinNumberOfStudiesCriteria nsc = new MinNumberOfStudiesCriteria();
            nsc.setMinNumberOfStudiesValue(Integer.parseInt(searchBean.getNumberStudies()));
            query.setCriteria(nsc);
        }

        // Setup number of months criteria here
        if (!StringUtil.isEmpty(searchBean.getMonthCompare()) && !StringUtil.isEmpty(searchBean.getNumberMonths())) {
            NumOfMonthsCriteria nm = new NumOfMonthsCriteria(searchBean.getMonthCompare(),
            		                                         searchBean.getNumberMonths());
            query.setCriteria(nm);
        }

        // Setup series description criteria here
        if (!StringUtil.isEmpty(searchBean.getSeriesDescription())) {
            SeriesDescriptionCriteria sdc = new SeriesDescriptionCriteria();
            sdc.setSeriesDescriptionValue(searchBean.getSeriesDescription());
            query.setCriteria(sdc);
        }

        // Setup convolution kernel criteria here
        List<String> selectedKernels = searchBean.getSelectedKernelNames();
        if (selectedKernels.size()>0) {
            ConvolutionKernelCriteria ckt = new ConvolutionKernelCriteria(selectedKernels);
            query.setCriteria(ckt);
        }

        // Setup kv peak criteria here
        if (!StringUtil.isEmpty(searchBean.getKvLeftCompare()) && !searchBean.getKvPeakLeft().equals("")) {
            KilovoltagePeakDistribution kvp = new KilovoltagePeakDistribution(searchBean.getKvLeftCompare(),
            		                                                          searchBean.getKvPeakLeft(),
            		                                                          searchBean.getKvRightCompare(),
            		                                                          searchBean.getKvPeakRight());
            query.setCriteria(kvp);
        }

        // build range criteria dicom query
        searchBean.getRcdSearchBean().buildQuery(query);
        searchBean.getDcdSearchBean().buildQuery(query);
        searchBean.getDxDcdSearchBean().buildQuery(query);
        searchBean.getAmSearchBean().buildQuery(query);

        addManufacturerTreeCriteria(searchBean, query);
    }

    private static void addManufacturerTreeCriteria(SearchWorkflowBean searchBean,
    		                                        DICOMQuery query) {

        // Setup the criteria represented in the tree (Manufacturer, Model,
        // Software version)
        // Setup manufacterer criteria here
        List<String> selectedManufacturers = searchBean.getSelectedManufacturers();
        if ((selectedManufacturers != null) && (selectedManufacturers.size() > 0)) {
            ManufacturerCriteria mc = new ManufacturerCriteria();
            mc.setCollectionObjects(selectedManufacturers);
            query.setCriteria(mc);
        }

        // Setup model criteria here
        List<String> selectedModels = searchBean.getSelectedModels();
        if ((selectedModels != null) && (selectedModels.size() > 0)) {
            ModelCriteria modC = new ModelCriteria();
            modC.setCollectionObjects(selectedModels);
            query.setCriteria(modC);
        }

        // Setup software version criteria here
        List<String> selectedSoftwareVersions = searchBean.getSelectedSoftwareVersions();
        if ((selectedSoftwareVersions != null) && (selectedSoftwareVersions.size() > 0)) {
            SoftwareVersionCriteria svc = new SoftwareVersionCriteria();
            svc.setSoftwareVersionObjects(selectedSoftwareVersions);
            query.setCriteria(svc);
        }
    }


	private static DateRangeCriteria buildDateCrit(SearchWorkflowBean searchBean) {
    	DateRangeCriteria drc = new DateRangeCriteria();
		if ((searchBean.getDateFrom() != null) && (searchBean.getDateTo() != null)) {

	        drc.setFromDate(searchBean.getDateFrom());
	        drc.setToDate(searchBean.getDateTo());
	        return drc;
		}
		else
		if ((searchBean.getDateFrom() != null) && (searchBean.getDateTo() == null)) {
			drc.setFromDate(searchBean.getDateFrom());
			drc.setToDate(new Date());
			return drc;
		}
		else {
			return null;
		}
    }

	private static PatientCriteria buildPatientCrit(String patientInput) {
		PatientCriteria prc = new PatientCriteria();

		if ((patientInput != null) &&  (patientInput.length() > 0)) {
			String [] pList=patientInput.split(",");

			for (int i=0; i < pList.length; ++i) {
				prc.setCollectionValue(pList[i].trim());
			}
			return prc;
		}
		else {
			return null;
		}
    }
}
