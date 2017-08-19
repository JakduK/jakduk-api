package com.jakduk.api.common.board.category;

import com.jakduk.api.model.embedded.LocalSimpleName;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
public class BoardCategory {

	private String code;

	@Setter
	private List<LocalSimpleName> names;

}
