package com.nminhthang.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {

	private String clientName;
	private OAuth2User oauAuth2User;
	private String fullName;

	public CustomerOAuth2User(OAuth2User oauAuth2User, String clientName) {
		this.oauAuth2User = oauAuth2User;
		this.clientName = clientName;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauAuth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauAuth2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oauAuth2User.getAttribute("name");
	}
	
	public String getEmail() {
		return oauAuth2User.getAttribute("email");
	}
	
	public String getFullName() {
		return (fullName != null) ? fullName : oauAuth2User.getAttribute("name");
	}

	public String getClientName() {
		return clientName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
