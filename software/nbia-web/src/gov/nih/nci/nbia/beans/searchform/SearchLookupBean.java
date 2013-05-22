/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

/*
 * Created on Jun 13, 2005
 */
package gov.nih.nci.nbia.beans.searchform;

import gov.nih.nci.ncia.criteria.AnnotationOptionCriteria;
import gov.nih.nci.ncia.criteria.ColorModeOptionCriteria;
import gov.nih.nci.ncia.criteria.ContrastAgentCriteria;
import gov.nih.nci.ncia.criteria.NumFrameOptionCriteria;
import gov.nih.nci.ncia.criteria.RangeData;
import gov.nih.nci.nbia.factories.ApplicationFactory;
import gov.nih.nci.nbia.lookup.LookupManager;
import gov.nih.nci.nbia.lookup.LookupManagerFactory;
import gov.nih.nci.nbia.util.NCIAConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.model.SelectItem;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import com.icesoft.faces.component.tree.IceUserObject;

/**
 * This class holds all of the values needed to populate the drop downs on all
 * of the pages. It will call the LookupManager in order to populate these drop
 * downs.
 *
 * <b>DO NOT PUT STATE IN HERE!!!!!</b> The lifecycle of this object is to initialize
 * some collections at session startup and then folks can ask this object for
 * that information.  DON'T SET STUFF IN HERE!!!!!  DON'T "RESET" ANYTHING HERE!!!!!
 *
 * @author shinohaa
 */
public class SearchLookupBean {

    /**
     * In the constructor, all of the drop downs will be created and populated
     * using the LookupManager.
     */
    public SearchLookupBean() {
        lookupManager = LookupManagerFactory.createLookupManager(new ArrayList<String>());
        imageLeftThicknessItems = new ArrayList<SelectItem>();
        imageRightThicknessItems = new ArrayList<SelectItem>();
        minimumStudiesItems = new ArrayList<SelectItem>();
        contrastAgentItems = new ArrayList<SelectItem>();
        annotationOptionItems = new ArrayList<SelectItem>();
        numFrameOptionItems = new ArrayList<SelectItem>();
        colorModeOptionItems = new ArrayList<SelectItem>();

        populateConstrastAgents();
        populateAnnotationOptions();
        populateNumFrameOptions();
        populateColorModeOptions();

        // Populate Image Slice Thicknesses
        imageLeftThicknessItems.add(new SelectItem(""));
        imageLeftThicknessItems.add(new SelectItem("0 mm"));
        imageLeftThicknessItems.add(new SelectItem("1 mm"));
        imageLeftThicknessItems.add(new SelectItem("2 mm"));
        imageLeftThicknessItems.add(new SelectItem("3 mm"));
        imageLeftThicknessItems.add(new SelectItem("4 mm"));
        imageLeftThicknessItems.add(new SelectItem("5 mm"));
        imageLeftThicknessItems.add(new SelectItem("6 mm"));
        imageLeftThicknessItems.add(new SelectItem("7 mm"));
        imageLeftThicknessItems.add(new SelectItem("8 mm"));

        imageRightThicknessItems.add(new SelectItem(""));
        imageRightThicknessItems.add(new SelectItem("0 mm"));
        imageRightThicknessItems.add(new SelectItem("1 mm"));
        imageRightThicknessItems.add(new SelectItem("2 mm"));
        imageRightThicknessItems.add(new SelectItem("3 mm"));
        imageRightThicknessItems.add(new SelectItem("4 mm"));
        imageRightThicknessItems.add(new SelectItem("5 mm"));
        imageRightThicknessItems.add(new SelectItem("6 mm"));
        imageRightThicknessItems.add(new SelectItem("7 mm"));
        imageRightThicknessItems.add(new SelectItem("8 mm"));

        // Populate minimum number of studies
        minimumStudiesItems.add(new SelectItem(""));
        minimumStudiesItems.add(new SelectItem("1"));
        minimumStudiesItems.add(new SelectItem("2"));
        minimumStudiesItems.add(new SelectItem("3"));
        minimumStudiesItems.add(new SelectItem("4"));
        minimumStudiesItems.add(new SelectItem("5"));
        minimumStudiesItems.add(new SelectItem("6"));
        minimumStudiesItems.add(new SelectItem("7"));
        minimumStudiesItems.add(new SelectItem("8"));
        minimumStudiesItems.add(new SelectItem("9"));
        minimumStudiesItems.add(new SelectItem("10"));
        minimumStudiesItems.add(new SelectItem("11"));
        minimumStudiesItems.add(new SelectItem("12"));

        buildTree();
    }

    /**
     * Should the simple search page include the list of
     * collections to search for.  This configurability
     * is a request from the NIAMS people.
     */
    public boolean isShowCollectionSearch() {
    	return NCIAConfig.getShowCollectionSearchCriteria();
    }

    /**
     * Returns the latest curation date present in the database
     *
     * @return the latest curation date
     */
    public Date getNewImagesDate() {

        // Get the latest curation date from the cache
    	return ApplicationFactory.getInstance().getLatestCurationDate();
    }


    public List<SelectItem> getContrastAgentItems() {
        return contrastAgentItems;
    }

    public List<SelectItem> getAnnotationOptionItems() {
        return annotationOptionItems;
    }

    public List<SelectItem> getNumFrameOptionItems() {
        return numFrameOptionItems;
    }

    public List<SelectItem> getColorModeOptionItems() {
        return colorModeOptionItems;
    }

    public List<SelectItem> getImageLeftCompareItems() {
        return RangeData.getLeftOperatorItems();
    }

    public List<SelectItem> getImageLeftThicknessItems() {
        return imageLeftThicknessItems;
    }

    public List<SelectItem> getImageRightCompareItems() {
        return RangeData.getRightOperatorItems();
    }

    public List<SelectItem> getImageRightThicknessItems() {
        return imageRightThicknessItems;
    }

    public List<SelectItem> getMinimumStudiesItems() {
        return minimumStudiesItems;
    }

    public List<SelectItem> getMonthCompareItems() {
        return RangeData.getLeftOperatorItems();
    }

    public DefaultTreeModel getManufacturerTree() {
        return equipmentTreeModel;
    }
    ///////////////////////////////////PRIVATE//////////////////////////////////

    /*
     * These are all the declarations for the drop down lists
     */
    private List<SelectItem> annotationOptionItems;
    private List<SelectItem> numFrameOptionItems;
    private List<SelectItem> colorModeOptionItems;
    private List<SelectItem> contrastAgentItems;
    private List<SelectItem> imageLeftThicknessItems;
    private List<SelectItem> imageRightThicknessItems;
    private List<SelectItem> minimumStudiesItems;

    private DefaultTreeModel equipmentTreeModel;

    private LookupManager lookupManager = null;

    private void populateConstrastAgents() {
        contrastAgentItems.add(new SelectItem(ContrastAgentCriteria.ENHANCED));
        contrastAgentItems.add(new SelectItem(ContrastAgentCriteria.UNENHANCED));
    }

    private void populateAnnotationOptions() {
        //The "value" is usually used as the "label" that shows up in
        //the UI.  the customer wants a different label than the value,
        //but changing the value would affect saved queries, so keep
        //the value but change the label in the ui
        annotationOptionItems.add(new SelectItem(AnnotationOptionCriteria.AnnotationOnly,
                                                 "Annotated"));
        annotationOptionItems.add(new SelectItem(AnnotationOptionCriteria.NoAnnotation,
                                                 "Non-Annotated"));
    }

    private void populateNumFrameOptions() {
        //The "value" is usually used as the "label" that shows up in
        //the UI.  the customer wants a different label than the value,
        //but changing the value would affect saved queries, so keep
        //the value but change the label in the ui
        numFrameOptionItems.add(new SelectItem(NumFrameOptionCriteria.SingleFrameOnly,
                                                 "Single Frame"));
        numFrameOptionItems.add(new SelectItem(NumFrameOptionCriteria.MultiFrame,
                                                 "Multi-frame (Cine Loop)"));
    }

    private void populateColorModeOptions() {
        //The "value" is usually used as the "label" that shows up in
        //the UI.  the customer wants a different label than the value,
        //but changing the value would affect saved queries, so keep
        //the value but change the label in the ui
    	colorModeOptionItems.add(new SelectItem(ColorModeOptionCriteria.ColorMode,
        						"Yes"));
    	colorModeOptionItems.add(new SelectItem(ColorModeOptionCriteria.BMode,
                                                 "No"));

    }

    private void buildTree() {
        // create root node with its children expanded
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        equipmentTreeModel = new DefaultTreeModel(rootTreeNode);

        IceUserObject rootObject = new EquipmentTreeUserObject(rootTreeNode);
        rootObject.setText("All Manufacturers");
        rootObject.setExpanded(false);
        rootTreeNode.setUserObject(rootObject);

        Map<String, Map<String, Set<String>>>
            manufacturerMap = lookupManager.getManufacturerModelSoftwareItems();


        Set<String> manufacturers = manufacturerMap.keySet();

        for (String man : manufacturers) {
        	DefaultMutableTreeNode manufacturerNode = new DefaultMutableTreeNode();
            IceUserObject manufacturerObject = new EquipmentTreeUserObject(manufacturerNode);
            manufacturerObject.setText(man);
            manufacturerNode.setUserObject(manufacturerObject);


            Map<String, Set<String>> modelMap = manufacturerMap.get(man);
            Set<String> models = modelMap.keySet();

            for (String model : models) {
            	DefaultMutableTreeNode modelNode = new DefaultMutableTreeNode();
                IceUserObject modelObject = new EquipmentTreeUserObject(modelNode);
                modelObject.setText(model);
                modelNode.setUserObject(modelObject);

                Set<String> versions = modelMap.get(model);

                for (String ver : versions) {

                	DefaultMutableTreeNode softwareVersionNode = new DefaultMutableTreeNode();
                	softwareVersionNode.setAllowsChildren(false);

                    IceUserObject softwareVersionObject = new EquipmentTreeUserObject(softwareVersionNode);
                    softwareVersionObject.setLeaf(true);
                    softwareVersionObject.setText(ver);
                    softwareVersionNode.setUserObject(softwareVersionObject);

                    modelNode.add(softwareVersionNode);
                }

                manufacturerNode.add(modelNode);
            }

            rootTreeNode.add(manufacturerNode);
        }
    }

}
