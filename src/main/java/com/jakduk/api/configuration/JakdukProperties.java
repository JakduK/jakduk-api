package com.jakduk.api.configuration;

import com.jakduk.api.configuration.rabbitmq.RabbitMQ;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 5. 1..
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties("jakduk")
public class JakdukProperties {

	private String rememberMeSeed;
	private Integer rememberMeExpiration;
	private String apiServerUrl;
	private String webServerUrl;

	private ApiUrlPath apiUrlPath = new ApiUrlPath();
	private Rabbitmq rabbitmq = new Rabbitmq();

	@Getter
	@Setter
	@Configuration
	@ConfigurationProperties("jakduk.api-url-path")
	public class ApiUrlPath {
		private String userPictureLarge;
		private String userPictureSmall;
		private String galleryImage;
		private String galleryThumbnail;
	}

	@Getter
	@Setter
	@Configuration
	@ConfigurationProperties("jakduk.rabbitmq")
	public class Rabbitmq {
		private String exchangeName;
		private Map<String, RabbitMQ> queues = new HashMap<>();
		private Map<String, String> routingKeys = new HashMap<>();
	}

	@Getter
	@Setter
	@Configuration
	@ConfigurationProperties("jakduk.elasticsearch")
	@Validated
	public class Elasticsearch {
		private Boolean enable;
		@NotEmpty
		private List<String> hostAndPort;
		private String indexBoard;
		private String indexGallery;
		private String indexSearchWord;
		private Integer bulkActions;
		private Integer bulkConcurrentRequests;
		private Integer bulkFlushIntervalSeconds;
		private Integer bulkSizeMb;
	}

	@Getter
	@Setter
	@Configuration
	@ConfigurationProperties("jakduk.storage")
	public class Storage {
		private String imagePath;
		private String thumbnailPath;
		private String userPictureLargePath;
		private String userPictureSmallPath;
	}

}
