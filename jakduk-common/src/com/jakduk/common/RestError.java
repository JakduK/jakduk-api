package com.jakduk.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Created by pyohwan on 16. 3. 5.
 */

@JsonTypeName(value = "error")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.WRAPPER_OBJECT)
@Data
@AllArgsConstructor
public class RestError {

    private int code;
    private String message;
}
