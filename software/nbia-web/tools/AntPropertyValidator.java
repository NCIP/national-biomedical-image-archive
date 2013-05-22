/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class AntPropertyValidator {

	public static void main(String[] args) throws Exception {
		Set<File> inputSet = new HashSet<File>();
		inputSet.add(new File("c:/workspace/ncia/software/build/install.xml"));
		inputSet.add(new File("c:/workspace/ncia/software/build/jboss.xml"));
		inputSet.add(new File("c:/workspace/ncia/software/build/database.xml"));
		inputSet.add(new File("c:/workspace/ncia/software/build/ncia-core-grid.xml"));
		inputSet.add(new File("c:/workspace/ncia/software/build/ncia-query-grid.xml"));
		inputSet.add(new File("c:/workspace/ncia/software/build/ctp.xml"));
		inputSet.add(new File("c:/workspace/ncia/software/build/macrodefs.xml"));
		inputSet.add(new File("c:/workspace/ncia/software/target/bda-utils/bda-build-utils-1.2.2.xml"));
		new AntPropertyValidator().reportDuplicateProperties(inputSet);
	}
	public void reportDuplicateProperties(Set<File> antFileSet) throws Exception {
		List<String> aggregateList = new ArrayList<String>();
		
		for(File antFile : antFileSet) {
			List<String> propertyNameList = findProperties(antFile);
			aggregateList.addAll(propertyNameList);
		}
		
		reportDuplicates(aggregateList);
	}
	
	private void reportDuplicates(List<String> propertyNameList) {
		if(propertyNameList.size()==0) {
			return;
			
		}
		List<String> copyList = new ArrayList<String>(propertyNameList.subList(1, propertyNameList.size()));
		String propertyName = propertyNameList.get(0);
		
		for(String iterPropertyName : copyList) {
			if(propertyName.equals(iterPropertyName)) {
				System.out.println("Property:"+propertyName+" duplicated");
			}
		}
		
		reportDuplicates(copyList);
	}
	
	private List<String> findProperties(File antFile) throws Exception {
		SAXBuilder saxBuilder=new SAXBuilder("org.apache.xerces.parsers.SAXParser");
        Document  jdomDocument=saxBuilder.build(antFile);

        List<String> propertyNameList = new ArrayList<String>();
        
        List nodeList =XPath.selectNodes(jdomDocument,          
        		                         "//property");

        Iterator iter=nodeList.iterator();
        while(iter.hasNext()) {
            Element element=(Element)iter.next();

            String propertyName = element.getAttributeValue("name");
            if(propertyName!=null) {
            	System.out.println("propertyName:"+propertyName);
            	propertyNameList.add(propertyName);
            }
        }
        return propertyNameList;
	}
}
