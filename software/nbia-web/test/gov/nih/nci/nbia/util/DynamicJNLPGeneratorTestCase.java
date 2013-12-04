/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.util;

import gov.nih.nci.nbia.basket.BasketSeriesItemBean;
import gov.nih.nci.ncia.search.SeriesSearchResult;
import gov.nih.nci.ncia.search.NBIANode;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class DynamicJNLPGeneratorTestCase extends TestCase {

	public void testGenerate() throws Exception {
		System.setProperty("gov.nih.nci.ncia.grid.local.node.name", "local");

		BasketSeriesItemBean item1 = createTestItem(1);
		BasketSeriesItemBean item2 = createTestItem(2);
		BasketSeriesItemBean item3 = createTestItem(3);

		List<BasketSeriesItemBean> seriesItems =
			new ArrayList<BasketSeriesItemBean>();
		seriesItems.add(item1);
		seriesItems.add(item2);
		seriesItems.add(item3);

		DynamicJNLPGenerator dynamicJNLPGenerator = new DynamicJNLPGenerator();

		String jnlp = dynamicJNLPGenerator.generate("fake_userid",
				                                    "fake_password",
				                                    "fake_codebase",
				                                    "fake_server_url",
				                                    true,
				                                    seriesItems,System.currentTimeMillis(),"4");

		//System.out.println("jnlp: " + jnlp);
		SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
        Document jdomDocument = saxBuilder.build(new StringReader(jnlp));

        Element jnlpElement = (Element)XPath.selectSingleNode(jdomDocument, "/jnlp");
        assertEquals(jnlpElement.getAttributeValue("codebase"), "fake_codebase/");

        Element homepageElement = (Element)XPath.selectSingleNode(jdomDocument, "//homepage");
        assertEquals(homepageElement.getAttributeValue("href"), "fake_codebase/welcome.jsp");

        List argumentElementList  = XPath.selectNodes(jdomDocument,
        		                                      "//application-desc/argument");
        assertEquals(argumentElementList.size(), 3);
        Element argumentElement = (Element)argumentElementList.get(0);
        assertEquals(argumentElement.getText(),
        		     "project1|patient1|study1|series1|Yes|1|32|0|http://fakeurl|display|true");

        Object includeAnnoProperty = XPath.selectSingleNode(jdomDocument, "//property[@name='includeAnnotation' and @value='true']");
        assertNotNull(includeAnnoProperty);

        Object userIdProperty = XPath.selectSingleNode(jdomDocument, "//property[@name='userId' and @value='fake_userid']");
        assertNotNull(userIdProperty);
        StringEncrypter encrypter = new StringEncrypter();
        String encryptedPassword = encrypter.encryptString("fake_password");
        //System.out.println("encryptedPassword: " +encryptedPassword);

        Object passwordProperty = XPath.selectSingleNode(jdomDocument, "//property[@name='password' and @value='" + encryptedPassword + "']");
        assertNotNull(passwordProperty);
	}

	protected void setUp() throws Exception {
		super.setUp();

		Util.loadSystemPropertiesFromPropertiesResource("ncia.properties");
	}

	private static BasketSeriesItemBean createTestItem(int index) {
		NBIANode node = new NBIANode(true, "display", "http://fakeurl");
    	SeriesSearchResult seriesSearchResult = new SeriesSearchResult();
    	seriesSearchResult.associateLocation(node);
    	seriesSearchResult.setProject("project"+index);
    	seriesSearchResult.setPatientId("patient"+index);
    	seriesSearchResult.setStudyInstanceUid("study"+index);
    	seriesSearchResult.setSeriesInstanceUid("series"+index);

		BasketSeriesItemBean item = new BasketSeriesItemBean(seriesSearchResult);
		item.setGridLocation(System.getProperty("gov.nih.nci.ncia.grid.local.node.name"));
		item.setProject("project"+index);
		item.setPatientId("patient"+index);
		item.setStudyId("study"+index);
		item.setSeriesId("series"+index);
		item.setAnnotationsFlag(true);
		item.setTotalImagesInSeries(index);
		item.setAnnotationsSize(new Long(0));
		item.setTotalSizeForAllImagesInSeries(new Long(32));

		return item;
	}
}
