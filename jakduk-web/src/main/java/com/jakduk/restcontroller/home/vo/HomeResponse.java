package com.jakduk.restcontroller.home.vo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.model.db.HomeDescription;
import com.jakduk.model.simple.BoardFreeCommentOnHome;
import com.jakduk.model.simple.BoardFreeOnHome;
import com.jakduk.model.simple.GalleryOnList;
import com.jakduk.model.simple.UserOnHome;
import lombok.Data;

import java.util.List;

/**
 * Created by pyohwan on 16. 5. 7.
 */

@Data
@JsonTypeName(value = "response")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
public class HomeResponse {
    private List<BoardFreeOnHome> posts;
    private List<UserOnHome> users;
    private List<GalleryOnList> galleries;
    private List<BoardFreeCommentOnHome> comments;
    private HomeDescription homeDescription;
}
