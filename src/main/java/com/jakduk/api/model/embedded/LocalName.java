package com.jakduk.api.model.embedded;

/**
 * @author pyohwan
 * 15. 12. 26 오후 8:50
 */

public class LocalName {

    private String language;
    private String fullName;
    private String shortName;

    public LocalName() {
    }

    public LocalName(String language, String fullName, String shortName) {
        this.language = language;
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
