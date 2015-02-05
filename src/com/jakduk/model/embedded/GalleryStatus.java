package com.jakduk.model.embedded;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 3.
 * @desc     :
 */
public class GalleryStatus {
	
	private String use;
	
	private String name;

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "GalleryStatus [use=" + use + ", name=" + name + "]";
	}

}
