package com.jakduk.authentication.common;

import com.jakduk.common.CommonConst;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

public class JakdukPrincipal implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	//~ Instance fields ================================================================================================
	private String password;
	private String username;							// email
	private CommonConst.ACCOUNT_TYPE providerId;
	private final String nickname;						// 별명
	private final String id;
	private final Set<GrantedAuthority> authorities;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;

	//~ Constructors ===================================================================================================

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
	 *
	 * @param username the username presented to the
	 *        <code>DaoAuthenticationProvider</code>
	 * @param password the password that should be presented to the
	 *        <code>DaoAuthenticationProvider</code>
	 * @param enabled set to <code>true</code> if the user is enabled
	 * @param accountNonExpired set to <code>true</code> if the account has not
	 *        expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials
	 *        have not expired
	 * @param accountNonLocked set to <code>true</code> if the account is not
	 *        locked
	 * @param authorities the authorities that should be granted to the caller
	 *        if they presented the correct username and password and the user
	 *        is enabled. Not null.
	 *
	 * @throws IllegalArgumentException if a <code>null</code> value was passed
	 *         either as a parameter or as an element in the
	 *         <code>GrantedAuthority</code> collection
	 */
	public JakdukPrincipal(String username, String id, String password, String nickname, CommonConst.ACCOUNT_TYPE providerId, boolean enabled, boolean accountNonExpired,
						   boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

		if (Objects.isNull(username) || Objects.isNull(password))
			throw new IllegalArgumentException("Cannot pass null or empty values to constructor");

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

	@Override
	public void eraseCredentials() {
		password = "";
	}

	@Override
	public String getPassword() {
		return password;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getUsername() {
		return username;
	}
	
	public CommonConst.ACCOUNT_TYPE getProviderId() {
		return providerId;
	}

	public String getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities =
				new TreeSet<GrantedAuthority>(new AuthorityComparator());

		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}

		return sortedAuthorities;
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

	/**
	 * Returns {@code true} if the supplied object is a {@code User} instance with the
	 * same {@code username} value.
	 * <p>
	 * In other words, the objects are equal if they have the same username, representing the
	 * same principal.
	 */
	@Override
	public boolean equals(Object rhs) {
		if (rhs instanceof JakdukPrincipal) {
			return username.equals(((JakdukPrincipal) rhs).username);
		}
		return false;
	}

	/**
	 * Returns the hashcode of the {@code username}.
	 */
	@Override
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(": ");
		sb.append("username: ").append(this.username).append("; ");
		sb.append("id: ").append(this.id).append("; ");
		sb.append("password: [PROTECTED]; ");
		sb.append("nickname: ").append(this.nickname).append("; ");
		sb.append("Enabled: ").append(this.enabled).append("; ");
		sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
		sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
		sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

		if (!authorities.isEmpty()) {
			sb.append("Granted Authorities: ");

			boolean first = true;
			for (GrantedAuthority auth : authorities) {
				if (!first) {
					sb.append(",");
				}
				first = false;

				sb.append(auth);
			}
		} else {
			sb.append("Not granted any authorities");
		}

		return sb.toString();
	}
}
