package com.jakduk.api.configuration.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.UserPictureInfo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

public class UserDetailsImpl implements UserDetails {

	private final String id;
	private final String nickname;                        // 별명
	private final Constants.ACCOUNT_TYPE providerId;
	private final Set<GrantedAuthority> authorities;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;
	private String password;
	private String username;                                    // email
	private UserPictureInfo picture;

	public UserDetailsImpl(String username, String id, String password, String nickname,
		Constants.ACCOUNT_TYPE providerId, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
		boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

		this.username = username;
		this.nickname = nickname;
		this.id = id;
		this.password = password;
		this.providerId = providerId;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities =
			new TreeSet<>(new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public Constants.ACCOUNT_TYPE getProviderId() {
		return providerId;
	}

	public UserPictureInfo getPicture() {
		return picture;
	}

	public void setPicture(UserPictureInfo picture) {
		this.picture = picture;
	}

	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			// Neither should ever be null as each entry is checked before adding it to the set.
			// If the authority is null, it is a custom authority and should precede others.
			if (g2.getAuthority() == null) {
				return -1;
			}

			if (g1.getAuthority() == null) {
				return 1;
			}

			return g1.getAuthority().compareTo(g2.getAuthority());
		}
	}
}
