package com.jakduk.core.configuration;

import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.Stream;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 2.
* @desc     :
*/

@Configuration
public class JestConfig {

	@Value("${elasticsearch.cluster.name}")
	private String elasticsearchClusterName;

	@Value("${elasticsearch.index.name}")
	private String elasticsearchIndexName;

	@Value("${elasticsearch.host.name}")
	private String elasticsearchHostName;

	@Bean
	public Client client() {

		Settings settings = Settings.settingsBuilder()
				.put("cluster.name", elasticsearchClusterName)
				.build();

		TransportClient client = TransportClient.builder()
				.settings(settings)
				.build();

		Stream.of(StringUtils.split(elasticsearchHostName, ","))
				.forEach(hostName -> {
					try {
						client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(new URL(hostName).getHost()), 9300));
					} catch (MalformedURLException | UnknownHostException e) {
						e.printStackTrace();
					}
				});

		return client;
	}

	@Bean
	public JestClient jestClient() {
		String[] result = elasticsearchHostName.split(",");
		
    	JestClientFactory factory = new JestClientFactory();
    	factory.setHttpClientConfig(new HttpClientConfig
    			.Builder(Arrays.asList(result))
    			.multiThreaded(true)
    			.build());
    	
    	return factory.getObject();
	}
}
