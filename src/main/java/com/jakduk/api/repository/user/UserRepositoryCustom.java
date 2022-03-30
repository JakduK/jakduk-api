package com.jakduk.api.repository.user;

import com.jakduk.api.model.simple.UserSimple;

import java.util.List;

public interface UserRepositoryCustom {

    List<UserSimple> findSimpleUsers();
}
