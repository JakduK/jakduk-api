package com.jakduk.api.model.embedded;

/**
 * @author pyohwan
 * 15. 12. 26 오후 10:22
 */

public class LocalSimpleName {

	private String language;
	private String name;

	public LocalSimpleName() {
	}

	public LocalSimpleName(String language, String name) {
		this.language = language;
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "LocalSimpleName{" +
			"language='" + language + '\'' +
			", name='" + name + '\'' +
			'}';
	}
}
