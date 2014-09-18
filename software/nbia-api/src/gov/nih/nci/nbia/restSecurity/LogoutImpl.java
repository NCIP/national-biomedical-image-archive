package gov.nih.nci.nbia.restSecurity;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.spi.ServiceRegistry.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.token.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


public class LogoutImpl implements LogoutSuccessHandler  {



	InMemoryTokenStore  tokenstore;


	public InMemoryTokenStore getTokenstore() {
		return tokenstore;
	}


	public void setTokenstore(InMemoryTokenStore tokenstore) {
		this.tokenstore = tokenstore;
	}


	@Override
	public void onLogoutSuccess(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse,
			Authentication paramAuthentication) throws IOException,
			ServletException {
		removeaccess(paramHttpServletRequest);
		paramHttpServletResponse.getOutputStream().write("\n\tYou Have Logged Out successfully.".getBytes());

	}


public void removeaccess(HttpServletRequest req){

		String tokens=req.getHeader("Authorization");
		System.out.println(tokens);
		String value=tokens.substring(tokens.indexOf(" ")).trim();
		DefaultOAuth2AccessToken token= new DefaultOAuth2AccessToken(value);
		System.out.println(token);
		tokenstore.removeAccessToken(value);
		System.out.println("\n\tAccess Token Removed Successfully!!!!!!!!");

	}

}
