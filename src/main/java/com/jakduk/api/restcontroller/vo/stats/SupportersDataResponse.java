package com.jakduk.api.restcontroller.vo.stats;

import com.jakduk.api.model.aggregate.SupporterCount;

import java.util.List;

public class SupportersDataResponse {
	private List<SupporterCount> supporters; // 클럽별 서포터 수
	private Integer supportersTotal; // 서포터 회원 수
	private Integer usersTotal; // 작두 회원 수

	public List<SupporterCount> getSupporters() {
		return supporters;
	}

	public void setSupporters(List<SupporterCount> supporters) {
		this.supporters = supporters;
	}

	public Integer getSupportersTotal() {
		return supportersTotal;
	}

	public void setSupportersTotal(Integer supportersTotal) {
		this.supportersTotal = supportersTotal;
	}

	public Integer getUsersTotal() {
		return usersTotal;
	}

	public void setUsersTotal(Integer usersTotal) {
		this.usersTotal = usersTotal;
	}
}
