package com.jakduk.api.restcontroller.vo.board;

/**
 * Created by pyohwanjang on 2017. 3. 3..
 */

public class BoardGallerySimple {

    private String id; // 사진 ID
    private String thumbnailUrl; // 썸네일 URL

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
