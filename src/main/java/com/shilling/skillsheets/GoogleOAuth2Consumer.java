package com.shilling.skillsheets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class GoogleOAuth2Consumer {
	
	private static final String client_id = "407997016708-o3kmbrmnodmqtfmvp2j0hsu9uvh9ittn.apps.googleusercontent.com";
	private static final String redirect_uri = "http://localhost:8080/oauth2callback";
	private static final String response_type = "code";
	private static final String scope = "profile email openid";
	
	private String getUrlString() {
		UriComponents uriComponents;
		try {
			uriComponents = UriComponentsBuilder.newInstance()
					.scheme("https").host("accounts.google.com").path("/o/oauth2/v2/auth")
					.queryParam("client_id", URLEncoder.encode(client_id, "UTF-8"))
					.queryParam("redirect_uri", URLEncoder.encode(redirect_uri, "UTF-8"))
					.queryParam("response_type", URLEncoder.encode(response_type, "UTF-8"))
					.queryParam("scope", URLEncoder.encode(scope, "UTF-8"))
					.build();
			return uriComponents.toUriString();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public RedirectView authenticate() {
		return new RedirectView(this.getUrlString());
	}

}
