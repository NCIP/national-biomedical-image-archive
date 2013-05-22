/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.ncia.criteria;

import gov.nih.nci.nbia.querystorage.QueryAttributeWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NodeCriteria extends MultipleValuePersistentCriteria {

    
    public NodeCriteria() {
        remoteNodes = new ArrayList<String>();
    }
    

    public void setRemoteNodes(List<String> nodes) {
        remoteNodes = nodes;
    }
    
    public List<String> getRemoteNodes() {
        return remoteNodes;
    }
    
    
    public void addValueFromQueryAttribute(QueryAttributeWrapper attribute) {
        addRemoteNode(attribute.getAttributeValue());
        
    }
    
    /////////////////////////////////////////PROTECTED/////////////////////////////////////////
    protected Collection<String> getMultipleValues() {
        return remoteNodes;
    }    
    
    /////////////////////////////////////////PRIVATE///////////////////////////////////////////
    private List<String> remoteNodes;   
    
    private void addRemoteNode(String node) {
        if(remoteNodes == null){
            remoteNodes = new ArrayList<String>();
        }
        if(node != null && !node.equals("")){
            remoteNodes.add(node);
        }
    }
        
}