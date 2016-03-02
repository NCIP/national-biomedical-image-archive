/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.query;

import gov.nih.nci.ncia.criteria.AnatomicalSiteCriteria;
import gov.nih.nci.ncia.criteria.CollectionCriteria;
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
import gov.nih.nci.ncia.criteria.ConvolutionKernelCriteria;
import gov.nih.nci.ncia.criteria.ImageModalityCriteria;
import gov.nih.nci.ncia.criteria.ImageSliceThickness;
import gov.nih.nci.ncia.criteria.KilovoltagePeakDistribution;
import gov.nih.nci.ncia.criteria.ManufacturerCriteria;
import gov.nih.nci.ncia.criteria.MinNumberOfStudiesCriteria;
import gov.nih.nci.ncia.criteria.ModelCriteria;
import gov.nih.nci.ncia.criteria.NumOfMonthsCriteria;
import gov.nih.nci.ncia.criteria.RangeData;
import gov.nih.nci.ncia.criteria.SeriesDescriptionCriteria;
import gov.nih.nci.ncia.criteria.SoftwareVersionCriteria;
import gov.nih.nci.ncia.criteria.ColorModeOptionCriteria;
import gov.nih.nci.ncia.criteria.NumFrameOptionCriteria;
import gov.nih.nci.ncia.criteria.UsMultiModalityCriteria;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This test tests isEmpty on the various criteria, and then sets
 * the criteria against a query, then asks the query to read the values
 * back.  I rehabilitated this from an older defunct test... it's something
 * at least...
 */
public class DICOMQueryTestCase extends TestCase {

	//this is rehabilitated from an older test
	public void testQuery() throws Exception {
		// Set all Criteria
		// Image modality
		imageModVal.add("CT");
		imageModVal.add("MRI");
		imageModVal.add("PET");

		collectionCritVal.add("RIDER");
		manufacturerVal.add("GE MEDICAL SYSTEMS");
		modelVal.add("LightSpeed16");

		Assert.assertTrue(imageModalityCrit.isEmpty());
		imageModalityCrit.setImageModalityObjects(imageModVal);
		Assert.assertFalse(imageModalityCrit.isEmpty());

		Assert.assertTrue(contrastAgentCriteria.isEmpty());
		contrastAgentCriteria.setContrastAgentValue("Enhanced");
		Assert.assertFalse(contrastAgentCriteria.isEmpty());

		Assert.assertTrue(anatomicalCrit.isEmpty());
		this.anatomicalCrit.setAnatomicalSiteValue("Chest");
		Assert.assertFalse(anatomicalCrit.isEmpty());

		Assert.assertTrue(minStudiesCrit.isEmpty());
		this.minStudiesCrit.setMinNumberOfStudiesValue(new Integer(4));
		Assert.assertFalse(minStudiesCrit.isEmpty());

		Assert.assertTrue(collectionCrit.isEmpty());
		this.collectionCrit.setCollectionObjects(collectionCritVal);
		Assert.assertFalse(collectionCrit.isEmpty());

		Assert.assertTrue(manufactureCrit.isEmpty());
		this.manufactureCrit.setCollectionObjects(manufacturerVal);
		Assert.assertFalse(manufactureCrit.isEmpty());

		Assert.assertTrue(modelCrit.isEmpty());
		this.modelCrit.setCollectionObjects(modelVal);
		Assert.assertFalse(modelCrit.isEmpty());

		Assert.assertTrue(convolutionKernelCrit.isEmpty());
		Collection<String> kernelValue = new ArrayList<String>();
		kernelValue.add("EXPERIMENTAL7");
		this.convolutionKernelCrit.setConvolutionKernelValues(kernelValue);
		Assert.assertFalse(convolutionKernelCrit.isEmpty());

		Assert.assertTrue(softwareVersionCrit.isEmpty());
		this.softwareVersionCrit.setSoftwareVersionValue("3.2 GE");
		Assert.assertFalse(softwareVersionCrit.isEmpty());

		Assert.assertTrue(seriesDescriptionCrit.isEmpty());
		this.seriesDescriptionCrit.setSeriesDescriptionValue("3.2 GE");
		Assert.assertFalse(seriesDescriptionCrit.isEmpty());

		// Build Query
		DICOMQuery dicomQuery = new DICOMQuery();
		dicomQuery.setQueryName("Query 1");
		dicomQuery.setUserID("kascice");

		dicomQuery.setCriteria(this.convolutionKernelCrit);
		dicomQuery.setCriteria(this.softwareVersionCrit);
		dicomQuery.setCriteria(this.seriesDescriptionCrit);
		dicomQuery.setCriteria(this.collectionCrit);
		dicomQuery.setCriteria(this.manufactureCrit);
		dicomQuery.setCriteria(this.modelCrit);
		dicomQuery.setCriteria(this.contrastAgentCriteria);
		dicomQuery.setCriteria(this.imageModalityCrit);
		dicomQuery.setCriteria(this.minStudiesCrit);
		dicomQuery.setCriteria(this.equalsCrit);
		dicomQuery.setCriteria(anatomicalCrit);
		dicomQuery.setCriteria(imageSliceThick);
		dicomQuery.setCriteria(kilovoltagePeakDistribution);
		dicomQuery.setCriteria(colorModeOptionCriteria);
		dicomQuery.setCriteria(numFrameOptionCriteria);
		dicomQuery.setCriteria(usMultiModalityCriteria);
				
		Collection<String> s = dicomQuery.getUsMultiModalityCriteria().getUsMultiModalityObjects();
		Assert.assertTrue(s.size()==2);
		Assert.assertTrue(s.contains("0001"));
		Assert.assertTrue(s.contains("0002"));
		
		Assert.assertSame(dicomQuery.getNumFrameOptionCriteria().getSingleValue(),
                          "65");
		
		Assert.assertSame(dicomQuery.getColorModeOptionCriteria()
				                    .getColorModeOptionValue(),
                          ColorModeOptionCriteria.BMode);
		
		Assert.assertSame(dicomQuery.getQueryName(), "Query 1");
		Assert.assertSame(dicomQuery.getUserID(), "kascice");


		Assert.assertEquals(dicomQuery.getImageModalityCriteria()
				                      .getImageModalityObjects(),
                            imageModVal);
		
		Assert.assertEquals(dicomQuery.getContrastAgentCriteria()
		                              .getContrastAgentValue(), 
                            "Enhanced");

		Assert.assertEquals(dicomQuery.getAnatomicalSiteCriteria()
                                      .getAnatomicalSiteValueObjects()
                                      .iterator()
                                      .next(), 
                            "Chest");
		
		Assert.assertEquals(dicomQuery.getMinNumberOfStudiesCriteria()
		                              .getMinNumberOfStudiesValue(), 
                            new Integer(4));
		
		Assert.assertEquals(dicomQuery.getNumOfMonthsCriteria()
		                              .getRangeData()
		                              .getFromOperator(), 
                            RangeData.EQUAL_TO);
		
		Integer numMonthsFromValue = dicomQuery.getNumOfMonthsCriteria()
		                                       .getRangeData()
		                                       .getFromValue()
		                                       .intValue();
		Assert.assertEquals(numMonthsFromValue, new Integer(10));
		
		Assert.assertEquals(dicomQuery.getCollectionCriteria()
				                      .getCollectionObjects(),
                            collectionCritVal);
		
		Assert.assertEquals(dicomQuery.getManufacturerCriteria()
		                              .getManufacturerObjects(), 
                            manufacturerVal);
		
		Assert.assertEquals(dicomQuery.getModelCriteria()
				                      .getModelObjects(),
				            modelVal);
		
		Assert.assertEquals(dicomQuery.getConvolutionKernelCriteria()
				                      .getConvolutionKernelValues()
				                      .iterator()
				                      .next(), 
                            "EXPERIMENTAL7");
		
		Assert.assertEquals(dicomQuery.getSoftwareVersionCriteria()
				                      .getSoftwareVersionObjects()
				                      .iterator()
				                      .next(), 
                            "3.2 GE");
		
		Assert.assertEquals(dicomQuery.getSeriesDescriptionCriteria()
		                              .getSeriesDescriptionValue(), 
                            "3.2 GE");

		Assert.assertEquals(dicomQuery.getImageSliceThickness()
				                      .getRangeData()
				                      .getFromOperator(), 
                            RangeData.GREATER_THAN);
		
		Assert.assertEquals(dicomQuery.getImageSliceThickness()
		                              .getRangeData()
		                              .getFromValue(), 
                            new Double(2.0));
		
		Assert.assertEquals(dicomQuery.getImageSliceThickness()
		                              .getRangeData()
		                              .getToOperator(), 
                            RangeData.LESS_THAN);
		
		Assert.assertEquals(dicomQuery.getImageSliceThickness()
		                              .getRangeData()
		                              .getToValue(), 
                            new Double(5.0));

		Assert.assertEquals(dicomQuery.getKilovoltagePeakDistribution()
                                      .getRangeData()
                                      .getFromOperator(),
                            RangeData.GREATER_THAN);
		
		Integer kvpFromValue = dicomQuery.getKilovoltagePeakDistribution()
		                                 .getRangeData()
		                                 .getFromValue()
		                                 .intValue();
		Assert.assertEquals(kvpFromValue, new Integer(2));
		
		Assert.assertEquals(dicomQuery.getKilovoltagePeakDistribution()
				                      .getRangeData()
				                      .getToOperator(),
				            RangeData.LESS_THAN);
		
		Integer kvpToValue = dicomQuery.getKilovoltagePeakDistribution()
                                       .getRangeData()
                                       .getToValue()
                                       .intValue();
		Assert.assertEquals(kvpToValue, 
                            new Integer(5));
		
		


	}
	
	/////////////////////////////////PROTECTED///////////////////////////////////////
	
	protected void setUp() throws Exception {
		imageModalityCrit = new ImageModalityCriteria();
		contrastAgentCriteria = new ContrastAgentCriteria();
		anatomicalCrit = new AnatomicalSiteCriteria();
		minStudiesCrit = new MinNumberOfStudiesCriteria();
		convolutionKernelCrit = new ConvolutionKernelCriteria();
		softwareVersionCrit = new SoftwareVersionCriteria();
		seriesDescriptionCrit = new SeriesDescriptionCriteria();
		equalsCrit = new NumOfMonthsCriteria(RangeData.EQUAL_TO, "10");
		colorModeOptionCriteria = new ColorModeOptionCriteria(ColorModeOptionCriteria.BMode);
		numFrameOptionCriteria = new NumFrameOptionCriteria("65");
		usMultiModalityCriteria = new UsMultiModalityCriteria();
		usMultiModalityCriteria.setUsMultiModalityObjects(java.util.Arrays.asList(new String[]{"0001","0002"}));
		
		collectionCrit = new CollectionCriteria();
		manufactureCrit = new ManufacturerCriteria();
		modelCrit = new ModelCriteria();
		imageModVal = new ArrayList<String>();
		collectionCritVal = new ArrayList<String>();
		manufacturerVal = new ArrayList<String>();
		modelVal = new ArrayList<String>();
		imageSliceThick = new ImageSliceThickness(RangeData.GREATER_THAN,
				                                  "2 mm",
				                                  RangeData.LESS_THAN,
				                                  "5 mm");
		kilovoltagePeakDistribution = new KilovoltagePeakDistribution(RangeData.GREATER_THAN,
				                                                      "2",
				                                                      RangeData.LESS_THAN,
				                                                      "5");
	}		
	
	////////////////////////////////////////PRIVATE///////////////////////////////
	

	private ImageModalityCriteria imageModalityCrit;
	private KilovoltagePeakDistribution kilovoltagePeakDistribution;
	private ContrastAgentCriteria contrastAgentCriteria;
	private AnatomicalSiteCriteria anatomicalCrit;
	private MinNumberOfStudiesCriteria minStudiesCrit;
	private NumOfMonthsCriteria equalsCrit;
	
	private CollectionCriteria collectionCrit;
	private ConvolutionKernelCriteria convolutionKernelCrit;
	private SoftwareVersionCriteria softwareVersionCrit;
	private SeriesDescriptionCriteria seriesDescriptionCrit;
	private Collection<String> imageModVal;
	private Collection<String> manufacturerVal;
	private Collection<String> modelVal;
	private Collection<String> collectionCritVal;
	private ImageSliceThickness imageSliceThick;
	private ManufacturerCriteria manufactureCrit;
	private ModelCriteria modelCrit;
	private ColorModeOptionCriteria colorModeOptionCriteria;
	private NumFrameOptionCriteria numFrameOptionCriteria;
	private UsMultiModalityCriteria usMultiModalityCriteria;
}