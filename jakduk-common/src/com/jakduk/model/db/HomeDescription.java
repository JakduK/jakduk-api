package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

`import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 15.
 * @desc     :
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class HomeDescription {
	
	/**
	 * ID
	 */
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String desc;
	
	private Integer priority;
}
