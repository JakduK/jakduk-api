package com.jakduk.api.configuration.rabbitmq;

/**
 * Created by Jang,Pyohwan on 2017. 6. 14..
 */

public class RabbitMQ {

	private String bindingQueueName;
	private String bindingRoutingKey;
	private Boolean enabled;

	public String getBindingQueueName() {
		return bindingQueueName;
	}

	public void setBindingQueueName(String bindingQueueName) {
		this.bindingQueueName = bindingQueueName;
	}

	public String getBindingRoutingKey() {
		return bindingRoutingKey;
	}

	public void setBindingRoutingKey(String bindingRoutingKey) {
		this.bindingRoutingKey = bindingRoutingKey;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
