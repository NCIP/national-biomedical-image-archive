/*L
 *  Copyright SAIC, Ellumen and RSNA (CTP)
 *
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/national-biomedical-image-archive/LICENSE.txt for details.
 */

package gov.nih.nci.cagrid.ncia.util;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import org.cagrid.gaards.authentication.BasicAuthentication;
import org.cagrid.gaards.authentication.client.AuthenticationClient;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.CertificateLifetime;
import org.globus.gsi.GlobusCredential;


public class SecureClientUtil {

	//authUrl not used - ifs or auth should point there?
	public static GlobusCredential generateGlobusCredential(String userId,
			                                                String password,
			                                                String dorianURL,
			                                                String authenticationServiceURL) throws Exception {

		BasicAuthentication auth = new BasicAuthentication();
        auth.setUserId(userId);
        auth.setPassword(password);

        // Authenticate to the IdP (DorianIdP) using credential

        AuthenticationClient authClient = new AuthenticationClient(authenticationServiceURL);
        SAMLAssertion saml = authClient.authenticate(auth);

        // Requested Grid Credential lifetime (12 hours)

        CertificateLifetime lifetime = new CertificateLifetime();
        lifetime.setHours(12);

        // Request PKI/Grid Credential
        GridUserClient dorian = new GridUserClient(dorianURL);
        GlobusCredential credential = dorian.requestUserCertificate(saml, lifetime);
        return credential;

	}


}
