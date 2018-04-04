package com.jakduk.api.restcontroller.vo.stats;

import com.jakduk.api.model.aggregate.SupporterCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SupportersDataResponse {
	private List<SupporterCount> supporters; // 클럽별 서포터 수
	private Integer supportersTotal; // 서포터 회원 수
	private Integer usersTotal; // 작두 회원 수
}
