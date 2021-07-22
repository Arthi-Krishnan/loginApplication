package com.example.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.entity.Role;
import com.example.entity.User;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	Set<GrantedAuthority> authorities=null;
	public CustomUserDetails(User user,Collection<? extends GrantedAuthority> authorities) {
		super(user.getUserName(),user.getPassword(),authorities);
	}

	/*
	 * @Override public Collection<? extends GrantedAuthority> getAuthorities() {
	 * Set<Role> roles = new HashSet<Role>(); roles.add(user.getRole());
	 * List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	 * 
	 * for (Role role : roles) { authorities.add(new
	 * SimpleGrantedAuthority(role.getName())); }
	 * 
	 * return authorities;
	 * }
	 */
	

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	 public void setAuthorities(Set<GrantedAuthority> authorities)
	    {
	        this.authorities=authorities;
	    }
}
