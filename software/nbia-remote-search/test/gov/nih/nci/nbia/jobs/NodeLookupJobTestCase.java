/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.nbia.jobs;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import gov.nih.nci.nbia.remotesearch.RemoteNodes;
import gov.nih.nci.nbia.util.NCIAConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RemoteNodes.class, NCIAConfig.class, NodeLookupJob.class} )
@SuppressStaticInitializationFor("gov.nih.nci.nbia.remotesearch.RemoteNodes")
public class NodeLookupJobTestCase {

	@Test
	public void testDontExecute() throws Exception {
		//create mocks
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

	    expect(NCIAConfig.getDiscoverRemoteNodes()).andReturn("false");
	    expect(RemoteNodes.getInstance()).andThrow(new RuntimeException("shouldnt execute"));

		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);

    	//verify the OUT
		NodeLookupJob job = new NodeLookupJob();
		job.execute(null);

    	//verify the mock
    	verify(NCIAConfig.class);
    	//dont verify RemoteNodes - it will think getInstance should have been invoked
    	//and it shouldnt have been if code is working correctly - we are really
    	//doing a negative asserrtion throw the "andThrow" expectation.
	}

	@Test
	public void testExecute() throws Exception {

		//create mocks
		RemoteNodes remoteNodesMock = createMock(RemoteNodes.class);
		mockStatic(RemoteNodes.class);
		mockStatic(NCIAConfig.class);

	    expect(NCIAConfig.getDiscoverRemoteNodes()).andReturn("true");
	    expect(RemoteNodes.getInstance()).andReturn(remoteNodesMock).times(1);

		//replay the mock
    	replay(NCIAConfig.class);
    	replay(RemoteNodes.class);

    	//verify the OUT
		NodeLookupJob job = new NodeLookupJob();
		job.execute(null);

    	//verify the mock
    	verify(NCIAConfig.class);
    	verify(RemoteNodes.class);
	}
}
