package com.jakduk.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 2.
* @desc     :
*/

@Configuration
public class JestConfiguration {

	@Value("${elasticsearch.host.name}")
	private String elasticsearchHostName;
	
	@Bean
	public JestClient jestClient() {
		String[] result = elasticsearchHostName.split(",");
		
    	JestClientFactory factory = new JestClientFactory();
    	factory.setHttpClientConfig(new HttpClientConfig
    			.Builder(Arrays.asList(result))
    			.multiThreaded(true)
    			.build());
    	
    	//System.out.println("phjang=" + factory.getObject());
    	
    	return factory.getObject();
	}
}
