package com.jakduk.model.simple;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 6.
 * @desc     :
 */
public class UserOnAuthentication {
	
	private String id;
	
	private String email;
	
	private String username;
	
	private String password;
	
	private List<Integer> rules;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Integer> getRules() {
		return rules;
	}

	public void setRules(List<Integer> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "UserOnAuthentication [id=" + id + ", email=" + email
				+ ", username=" + username + ", password=" + password
				+ ", rules=" + rules + "]";
	}

}
