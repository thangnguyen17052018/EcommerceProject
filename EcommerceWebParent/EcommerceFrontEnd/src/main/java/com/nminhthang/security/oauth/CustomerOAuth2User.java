package com.nminhthang.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {

	private OAuth2User oauAuth2User;

	public CustomerOAuth2User(OAuth2User oauAuth2User) {
		this.oauAuth2User = oauAuth2User;
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
		return oauAuth2User.getAttribute("name");
	}
}
