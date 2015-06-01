/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.ncia.criteria.AcquisitionMatrixCriteria;
import gov.nih.nci.ncia.criteria.AnatomicalSiteCriteria;
import gov.nih.nci.ncia.criteria.AnnotationOptionCriteria;
import gov.nih.nci.ncia.criteria.CollectionCriteria;
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
import gov.nih.nci.ncia.criteria.ConvolutionKernelCriteria;
import gov.nih.nci.ncia.criteria.DataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.DateRangeCriteria;
import gov.nih.nci.ncia.criteria.DxDataCollectionDiameterCriteria;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.criteria.ImageSliceThickness;
import gov.nih.nci.ncia.criteria.KilovoltagePeakDistribution;
import gov.nih.nci.ncia.criteria.ManufacturerCriteria;
import gov.nih.nci.ncia.criteria.MinNumberOfStudiesCriteria;
import gov.nih.nci.ncia.criteria.ModalityAndedSearchCriteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.NodeCriteria;
import gov.nih.nci.ncia.criteria.NumOfMonthsCriteria;
import gov.nih.nci.ncia.criteria.PatientCriteria;
import gov.nih.nci.ncia.criteria.PersistentCriteria;
import gov.nih.nci.ncia.criteria.RangeData;
import gov.nih.nci.ncia.criteria.ReconstructionDiameterCriteria;
import gov.nih.nci.ncia.criteria.SeriesDescriptionCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.ncia.criteria.NumFrameOptionCriteria;
import gov.nih.nci.ncia.criteria.ColorModeOptionCriteria;
import gov.nih.nci.ncia.criteria.UsMultiModalityCriteria;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicCodeMeaningCriteria;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicCodeValuePairCriteria;
import gov.nih.nci.ncia.criteria.ImagingObservationCharacteristicQuantificationCriteria;
import gov.nih.nci.nbia.beans.searchform.SearchWorkflowBean;
import gov.nih.nci.nbia.query.DICOMQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Refactoring class put in place to remove the responsibility of repopulating
 * criteria into a query away from the criteria themselves. This class takes the
 * repopulation logic that is currently maintained in each of the
 * PersistentCriteria and moves it here. This is not an elegant solution, but is pretty straight forward.
 *
 * TODO When we get more time we can go back and revisit the entire UI saved query mechanism.
 *
 * @author BauerD
 */
public class SavedQueryReconstructor {
    private static Logger LOGGER = Logger.getLogger(SavedQueryReconstructor.class);

    public static void repopulatePersistantCriteria(NumFrameOptionCriteria krit,
            SearchWorkflowBean swb, 
            DICOMQuery query) {
    	repopulateNumFrameOptionCriteria( krit, swb, query);

    }
    
    public static void repopulatePersistantCriteria(ColorModeOptionCriteria krit,
            SearchWorkflowBean swb, 
            DICOMQuery query) {
    	repopulateColorModeOptionCriteria( krit, swb, query);

    }
    
    public static void repopulatePersistantCriteria(UsMultiModalityCriteria krit,
            SearchWorkflowBean swb, 
            DICOMQuery query) {
    	repopulateUsMultiModalityCriteria( krit, swb, query);

    }
    
    public static void repopulatePersistantCriteria(PersistentCriteria krit,
                                                    SearchWorkflowBean swb, 
                                                    DICOMQuery query) {

        LOGGER.debug("Received Criteria of type");
      
        /**
         * Massive "Instance of" statements to cover the different persistent criteria repopulation
         */
        if (krit != null) {
            if (krit instanceof AcquisitionMatrixCriteria) {
                repopulateAcquisitionMatrixCriteria(
                        (AcquisitionMatrixCriteria) krit, swb, query);
            } else if (krit instanceof AnatomicalSiteCriteria) {
                repopulateAnatomicalSiteCriteria((AnatomicalSiteCriteria) krit,
                        swb, query);
            } else if (krit instanceof AnnotationOptionCriteria) {
                repopulateAnnotationOptionCriteria(
                        (AnnotationOptionCriteria) krit, swb, query);
            } else if (krit instanceof CollectionCriteria) {
                repopulateCollectionCriteria((CollectionCriteria) krit, swb,
                        query);
            } else if (krit instanceof PatientCriteria) {
                repopulatePatientCriteria((PatientCriteria) krit, swb,
                        query);
            } else if (krit instanceof ContrastAgentCriteria) {
                repopulateContrastAgentCriteria((ContrastAgentCriteria) krit,
                        swb, query);
            } else if (krit instanceof NumFrameOptionCriteria) {
                repopulateNumFrameOptionCriteria((NumFrameOptionCriteria) krit,
                        swb, query);
            } else if (krit instanceof ColorModeOptionCriteria) {
                repopulateColorModeOptionCriteria((ColorModeOptionCriteria) krit,
                        swb, query);
            }else if (krit instanceof UsMultiModalityCriteria) {
            	repopulateUsMultiModalityCriteria((UsMultiModalityCriteria) krit,
                        swb, query);
            }              
            
            else if (krit instanceof ConvolutionKernelCriteria) {
                repopulateConvolutionKernelCriteria(
                        (ConvolutionKernelCriteria) krit, swb, query);
            }else if (krit instanceof DataCollectionDiameterCriteria) {
                repopulateDataCollectionDiameterCriteria(
                        (DataCollectionDiameterCriteria) krit, swb, query);
            }else if (krit instanceof DxDataCollectionDiameterCriteria) {
                repopulateDxDataCollectionDiameterCriteria(
                        (DxDataCollectionDiameterCriteria) krit, swb, query);
            }else if (krit instanceof ImageModalityCriteria) {
                repopulateImageModalityCriteria(
                        (ImageModalityCriteria) krit, swb, query);
            }else if (krit instanceof ModalityAndedSearchCriteria) {
                repopulateImageModalityCriteriaAndSearch(
                        (ModalityAndedSearchCriteria) krit, swb, query);

            }else if (krit instanceof ImageSliceThickness) {
                repopulateImageSliceThicknessCriteria(
                        (ImageSliceThickness) krit, swb, query);
            }else if (krit instanceof KilovoltagePeakDistribution) {
                repopulateKiloVoltagePeakDistributionCriteria(
                        (KilovoltagePeakDistribution) krit, swb, query);
            } else if (krit instanceof ManufacturerCriteria) {
                repopulateManufacturerCriteria(
                        (ManufacturerCriteria) krit, swb, query);
            }else if (krit instanceof MinNumberOfStudiesCriteria) {
                repopulateMinNumberOfStudiesCriteria(
                        (MinNumberOfStudiesCriteria) krit, swb, query);
            }else if (krit instanceof ModelCriteria) {
                repopulateModelCriteria(
                        (ModelCriteria) krit, swb, query);
            }else if (krit instanceof NumOfMonthsCriteria) {
                repopulateNumOfMonthsCriteria(
                        (NumOfMonthsCriteria) krit, swb, query);
            }else if (krit instanceof ReconstructionDiameterCriteria) {
                repopulateReconstructionDiameterCriteria(
                        (ReconstructionDiameterCriteria) krit, swb, query);
            }else if (krit instanceof NodeCriteria) {
                repopulateNodeCriteria(
                        (NodeCriteria) krit,  swb, query);
            }else if (krit instanceof SeriesDescriptionCriteria) {
                repopulateSeriesDescriptionCriteria((SeriesDescriptionCriteria) krit, swb, query);
            }else if (krit instanceof SoftwareVersionCriteria) {
                repopulateSoftwareVersionCriteria((SoftwareVersionCriteria) krit, swb, query);
            } else if (krit instanceof DateRangeCriteria) {
                repopulateDateRangeCriteria((DateRangeCriteria) krit, swb, query);
            } 
            else 
            if (krit instanceof ImagingObservationCharacteristicCodeMeaningCriteria) {
                repopulateImagingObservationCharacteristicCodeMeaningCriteria((ImagingObservationCharacteristicCodeMeaningCriteria) krit, swb, query);
            }
            else 
            if (krit instanceof ImagingObservationCharacteristicCodeValuePairCriteria) {
               repopulateImagingObservationCharacteristicCodeValuePairCriteria((ImagingObservationCharacteristicCodeValuePairCriteria) krit, swb, query);
            }            
            else 
            if (krit instanceof ImagingObservationCharacteristicQuantificationCriteria) {
               repopulateImagingObservationCharacteristicQuantificationCriteria((ImagingObservationCharacteristicQuantificationCriteria) krit, swb, query);
            }             
        }
    }


    private static void repopulateAcquisitionMatrixCriteria(AcquisitionMatrixCriteria amc, 
                                                            SearchWorkflowBean swb,
                                                            DICOMQuery query) {
        if (swb != null) {
            swb.setAdvanced(true);
            RangeData rangeData = amc.getRangeData();
            if (rangeData != null) {
                swb.getAmSearchBean().setCriteriaLeftCompare(
                        rangeData.getFromOperator());
                swb.getAmSearchBean().setCriteriaLeft(
                        rangeData.getFromValue().toString());
                if ((rangeData.getToOperator() != null)
                        && !rangeData.getToOperator().equals("")) {
                    swb.getAmSearchBean().setCriteriaRightCompare(
                            rangeData.getToOperator());
                    swb.getAmSearchBean().setCriteriaRight(
                            rangeData.getToValue().toString());
                }
            }
        }
        query.setCriteria(amc);
    }
    
    
    
    private static void repopulateImagingObservationCharacteristicQuantificationCriteria(ImagingObservationCharacteristicQuantificationCriteria asc,
                                                                                         SearchWorkflowBean swb, 
                                                                                         DICOMQuery query) {
    	
    	Collection<String> quantifications = asc.getImagingObservationCharacteristicQuantifications();
    	if(swb!=null) {
    		swb.getAimSearchWorkflowBean().selectQuantifications(quantifications);
    	}
    	query.setCriteria(asc);
    }     
    
    
    private static void repopulateImagingObservationCharacteristicCodeValuePairCriteria(ImagingObservationCharacteristicCodeValuePairCriteria asc,
                                                                                      SearchWorkflowBean swb, 
                                                                                      DICOMQuery query) {
    	Collection<String> codeValuePairs = asc.getImagingObservationCharacteristicCodeValuePairs();
    	if(swb!=null) {
    		swb.getAimSearchWorkflowBean().selectCodeValuePairs(codeValuePairs);
    	}
    	query.setCriteria(asc);
    }    

    private static void repopulateImagingObservationCharacteristicCodeMeaningCriteria(ImagingObservationCharacteristicCodeMeaningCriteria asc,
                                                                                      SearchWorkflowBean swb, 
                                                                                      DICOMQuery query) {
    	Collection<String> codeMeaningNames = asc.getImagingObservationCharacteristicCodeMeaningNames();
    	System.out.println("codeMeaningNames:"+codeMeaningNames);
    	if(swb!=null) {
    		swb.getAimSearchWorkflowBean().selectCodeMeaningNames(codeMeaningNames);
    	}
    	query.setCriteria(asc);
    }
    
    
    private static void repopulateAnatomicalSiteCriteria(AnatomicalSiteCriteria asc,
                                                          SearchWorkflowBean swb, 
                                                          DICOMQuery query) {
        List<String> asvo = asc.getAnatomicalSiteValueObjects();
        if (swb != null) {
            swb.selectAnatomicalSiteNames(asvo);
        }
        query.setCriteria(asc);
    }

    private static void repopulateNumFrameOptionCriteria(NumFrameOptionCriteria noc, 
                                                           SearchWorkflowBean swb,
                                                           DICOMQuery query) {
    	if (noc != null) {
            if (swb != null) {
                String numFrameOptionValue = noc.getNumFrameOptionValue();
                String[] numFrameOptions = null;
                if(numFrameOptionValue.equals(NumFrameOptionCriteria.NoCondition)) {
                	numFrameOptions = new String[2];

                	numFrameOptions[0] = NumFrameOptionCriteria.SingleFrameOnly;
                	numFrameOptions[1] = NumFrameOptionCriteria.MultiFrame;
                }
                else {
                	numFrameOptions = new String[1];
                	numFrameOptions[0] = numFrameOptionValue;
                }
                
                swb.setNumFrameOptions(numFrameOptions);
            }
            query.setCriteria(noc);
        }
    }
    
    private static void repopulateColorModeOptionCriteria(
    		ColorModeOptionCriteria noc, SearchWorkflowBean swb, DICOMQuery query) {
		if (noc != null) {
			if (swb != null) {
				String colorModeOptionValue = noc.getColorModeOptionValue();
				String[] colorModeOptions = null;
				if (colorModeOptionValue
						.equals(ColorModeOptionCriteria.NoCondition)) {
					colorModeOptions = new String[2];

					colorModeOptions[0] = ColorModeOptionCriteria.BMode;
					colorModeOptions[1] = ColorModeOptionCriteria.ColorMode;
				} else {
					colorModeOptions = new String[1];
					colorModeOptions[0] = colorModeOptionValue;
				}
				swb.setColorModeOptions(colorModeOptions);
			}
			query.setCriteria(noc);
		}
	}
    
    private static void repopulateUsMultiModalityCriteria(
    		UsMultiModalityCriteria noc, SearchWorkflowBean swb, DICOMQuery query) {
		if (noc != null) {
			if (swb != null) {
				swb.selectUsMultiModalityNames(noc.getUsMultiModalityObjects());
			}
			query.setCriteria(noc);
		}
	}
    
    private static void repopulateAnnotationOptionCriteria(AnnotationOptionCriteria aoc, 
            SearchWorkflowBean swb,
            DICOMQuery query) {
if (aoc != null) {
if (swb != null) {
String annotationOptionValue = aoc.getAnnotationOptionValue();
String[] annotationOptions = null;
if(annotationOptionValue.equals(AnnotationOptionCriteria.NoCondition)) {
annotationOptions = new String[2];

annotationOptions[0] = AnnotationOptionCriteria.NoAnnotation;
annotationOptions[1] = AnnotationOptionCriteria.AnnotationOnly;
}
else {
annotationOptions = new String[1];
annotationOptions[0] = annotationOptionValue;
}
swb.setAnnotationOptions(annotationOptions);
}
query.setCriteria(aoc);
}
}

    private static void repopulateCollectionCriteria(CollectionCriteria cc,
                                                     SearchWorkflowBean swb, 
                                                     DICOMQuery query) {
        if (cc != null) {
            if (swb != null) {
                swb.selectCollectionNames(cc.getCollectionObjects());
            }
            query.setCriteria(cc);
        }
    }

    private static void repopulatePatientCriteria(PatientCriteria cc,
                                                  SearchWorkflowBean swb, 
                                                  DICOMQuery query) {
        if (cc != null) {
            if (swb != null) {
                swb.addPatientItems(cc.getPatientIdObjects());
            }
            query.setCriteria(cc);
        }
    }

    private static  void repopulateContrastAgentCriteria(ContrastAgentCriteria cac,
                                                         SearchWorkflowBean swb, 
                                                         DICOMQuery query) {
        if (cac != null  && swb != null) {
            String contrastAgentValue = cac.getContrastAgentValue();
            String[] contrastAgents = null;
            if(contrastAgentValue.equals(ContrastAgentCriteria.EITHER_EN_OR_UN)) {
                contrastAgents = new String[2];
                contrastAgents[0] = ContrastAgentCriteria.ENHANCED;
                contrastAgents[1] = ContrastAgentCriteria.UNENHANCED;
            }
            else {
                contrastAgents = new String[1];
                contrastAgents[0] = contrastAgentValue;
            }
            swb.setContrastAgents(contrastAgents);
        }
        query.setCriteria(cac);
    }

    private static void repopulateConvolutionKernelCriteria(ConvolutionKernelCriteria ckc, 
                                                             SearchWorkflowBean swb,
                                                             DICOMQuery query) {
        if (ckc != null && swb != null) {
            swb.setAdvanced(true);
            swb.selectKernelNames(ckc.getConvolutionKernelValues());
        }

        query.setCriteria(ckc);

    }

    private static void repopulateDataCollectionDiameterCriteria(DataCollectionDiameterCriteria dcdc,
                                                                 SearchWorkflowBean swb,
                                                                 DICOMQuery query) {
        if(dcdc!=null){
            RangeData rd = dcdc.getRangeData();
            if (swb != null && rd != null) {
                swb.setAdvanced(true);
                swb.getDcdSearchBean().setCriteriaLeftCompare(rd.getFromOperator());
                swb.getDcdSearchBean().setCriteriaLeft(rd.getFromValue().toString());

                if ((rd.getToOperator() != null) &&
                        !rd.getToOperator().equals("")) {
                    swb.getDcdSearchBean().setCriteriaRightCompare(rd.getToOperator());
                    swb.getDcdSearchBean().setCriteriaRight(rd.getToValue().toString());
                }
            }

            query.setCriteria(dcdc);
        }
    }

    private static void repopulateDxDataCollectionDiameterCriteria(DxDataCollectionDiameterCriteria ddcdc,
                                                                   SearchWorkflowBean swb,
                                                                   DICOMQuery query) {
         if(ddcdc != null){
             RangeData rd = ddcdc.getRangeData();
             if (swb != null && rd != null) {
                swb.setAdvanced(true);
                swb.getDxDcdSearchBean().setCriteriaLeftCompare(rd.getFromOperator());
                swb.getDxDcdSearchBean().setCriteriaLeft(rd.getFromValue().toString());

                if ((rd.getToOperator() != null) &&
                        !rd.getToOperator().equals("")) {
                    swb.getDxDcdSearchBean().setCriteriaRightCompare(rd.getToOperator());
                    swb.getDxDcdSearchBean().setCriteriaRight(rd.getToValue().toString());
                }
            }

            query.setCriteria(ddcdc);
         }
    }

    private static void repopulateImageModalityCriteria(ImageModalityCriteria imc,
                                                        SearchWorkflowBean swb,
                                                        DICOMQuery query) {
        if(imc!= null && swb != null) {
            swb.selectModalityNames(imc.getImageModalityObjects());
        }

        query.setCriteria(imc);
    }

    private static void repopulateDateRangeCriteria(DateRangeCriteria imc,
                                                    SearchWorkflowBean swb,
                                                    DICOMQuery query) {
        if(imc != null  && swb != null) {
            swb.setDateFrom(imc.getFromDate());
            swb.setDateTo(imc.getToDate());
        }

        query.setCriteria(imc);
    }

    private static  void repopulateImageModalityCriteriaAndSearch(ModalityAndedSearchCriteria imc,
                                                                  SearchWorkflowBean swb,
                                                                  DICOMQuery query) {
        if(imc!= null && swb != null) {
            swb.addModalityAndSearchItem(imc.getModalityAndedSearchValue());
        }
        query.setCriteria(imc);
    }

    private static void repopulateImageSliceThicknessCriteria(ImageSliceThickness  istc,
                                                              SearchWorkflowBean swb,
                                                              DICOMQuery query) {
        if(istc!=null){
            RangeData rd = istc.getRangeData();
            if (swb != null && rd != null) {
                swb.setShowThickness(true);
                swb.setThicknessLeftCompare(rd.getFromOperator());
                swb.setImageThicknessLeft(rd.getFromValue().intValue() + " mm");

                if ((rd.getToOperator() != null) &&
                    !rd.getToOperator().equals("")) {
                    swb.setThicknessRightCompare(rd.getToOperator());
                    swb.setImageThicknessRight(rd.getToValue().intValue() +  " mm");
                } 
                else {
                    swb.setThicknessRightCompare("");
                    swb.setImageThicknessRight("");
                }
            }
        }
       query.setCriteria(istc);
    }

    private static void repopulateKiloVoltagePeakDistributionCriteria(KilovoltagePeakDistribution kvpd, 
                                                                      SearchWorkflowBean swb,
                                                                      DICOMQuery query) {
        if(kvpd != null){
            RangeData rd = kvpd.getRangeData();
            if (swb != null) {
                swb.setAdvanced(true);
                swb.setKvLeftCompare(rd.getFromOperator());
                swb.setKvPeakLeft(rd.getFromValue().toString());

                if ((rd.getToOperator() != null) &&
                    !rd.getToOperator().equals("")) {
                    swb.setKvRightCompare(rd.getToOperator());
                    swb.setKvPeakRight(rd.getToValue().toString());
                }
            }
        }
        query.setCriteria(kvpd);
    }

    private static void repopulateManufacturerCriteria(ManufacturerCriteria mc,
                                                        SearchWorkflowBean swb,
                                                        DICOMQuery query) {
        if(mc != null  && swb != null) {
            swb.setAdvanced(true);
            swb.addManufacturerItems(mc.getManufacturerObjects());
          }

        query.setCriteria(mc);

    }

    private static void repopulateMinNumberOfStudiesCriteria(MinNumberOfStudiesCriteria mnosc,
                                                             SearchWorkflowBean swb,
                                                             DICOMQuery query) {
        if(mnosc != null && swb != null) {
            swb.setAdvanced(true);
            Integer mn = mnosc.getMinNumberOfStudiesValue();
            if(mn!=null){
                swb.setNumberStudies(mn.toString());
              }
        }
        query.setCriteria(mnosc);
    }

    private static void repopulateModelCriteria(ModelCriteria mc,
                                                SearchWorkflowBean swb,
                                                DICOMQuery query) {
        if(mc!=null  && swb != null) {
            swb.setAdvanced(true);
            swb.addModelItems(mc.getModelObjects());
          }
        query.setCriteria(mc);
    }

    private static void repopulateNumOfMonthsCriteria(NumOfMonthsCriteria nomc,
                                                      SearchWorkflowBean swb,
                                                      DICOMQuery query) {
        if(nomc != null && swb != null) {
            RangeData rd = nomc.getRangeData();

            swb.setAdvanced(true);
            swb.setMonthCompare(rd.getFromOperator());
            if(rd.getFromValue()!=null){
                  swb.setNumberMonths(rd.getFromValue().toString());
            }
        }
        query.setCriteria(nomc);
    }

    private static void repopulateReconstructionDiameterCriteria(ReconstructionDiameterCriteria rdc,
                                                                 SearchWorkflowBean swb,
                                                                 DICOMQuery query) {
        if(rdc != null && swb != null) {
            swb.setAdvanced(true);
            RangeData rd = rdc.getRangeData();
            if(rd!=null){
                swb.getRcdSearchBean().setCriteriaLeftCompare(rd.getFromOperator());
                swb.getDcdSearchBean().setCriteriaLeft(rd.getFromValue().toString());
                if ((rd.getToOperator() != null) &&
                       !rd.getToOperator().equals("")) {
                    swb.getDcdSearchBean().setCriteriaRightCompare(rd.getToOperator());
                    swb.getDcdSearchBean().setCriteriaRight(rd.getToValue().toString());
                   }
            }
        }
        query.setCriteria(rdc);
    }

    private static void repopulateSeriesDescriptionCriteria(SeriesDescriptionCriteria sdc,
                                                            SearchWorkflowBean swb,
                                                            DICOMQuery query) {
        if(sdc != null  && swb != null) {
            swb.setAdvanced(true);
            swb.setSeriesDescription(sdc.getSeriesDescriptionValue());
        }
        query.setCriteria(sdc);
    }

    private static  void repopulateSoftwareVersionCriteria(SoftwareVersionCriteria svct,
                                                           SearchWorkflowBean swb,
                                                           DICOMQuery query) {
        if(svct!= null){
            if (swb != null) {
                swb.setAdvanced(true);
                swb.setSelectedSoftwareVersions(new ArrayList(svct.getSoftwareVersionObjects()));
            }
            query.setCriteria(svct);
        }
    }

    
    private static void repopulateNodeCriteria(NodeCriteria rnc,
                                               SearchWorkflowBean swb,
                                               DICOMQuery query) {         
        if(rnc != null  && swb != null) {
            swb.selectRemoteNodes(rnc.getRemoteNodes());
        }

        query.setCriteria(rnc);         
    }    
}
