package com.jakduk.api.restcontroller.vo.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 18.
 * @desc     :
 */

@NoArgsConstructor
@Setter
@Getter
public class AttendanceClubForm {

	@NotEmpty
	private String origin;
	
	@NotEmpty
	private String league;
	
	@NotNull
	private Integer season;
	
	@NotNull
	private Integer games;
	
	@NotNull
	private Integer total;
	
	@NotNull
	private Integer average;

}
