package com.jakduk.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static ApiResponse createSuccess() {
        ApiResponse response = new ApiResponse();
        response.code = HttpStatus.OK.value();
        response.message = HttpStatus.OK.name();
        return response;
    }


    public void success(T data) {
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.name();
        this.data = data;
    }

    public static ApiResponse createFail(int code , String message) {
        ApiResponse response = new ApiResponse();
        response.code = code;
        response.message = message;
        return response;
    }


}
