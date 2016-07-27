package com.jakduk.restcontroller.vo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.model.simple.GalleryOnList;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pyohwan on 16. 5. 8.
 */

@Data
@JsonTypeName(value = "response")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
public class GalleriesResponse {
    private List<GalleryOnList> galleries;
    Map<String, Integer> usersLikingCount;
    Map<String, Integer> usersDislikingCount;
}
