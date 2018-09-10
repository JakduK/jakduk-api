package com.jakduk.api.model.elasticsearch;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */

public class EsTermsBucket {

	private String key;
	private Long count;

	public EsTermsBucket() {
	}

	public EsTermsBucket(String key, Long count) {
		this.key = key;
		this.count = count;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
