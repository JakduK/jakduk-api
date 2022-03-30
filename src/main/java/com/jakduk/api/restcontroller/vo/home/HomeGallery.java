package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.GallerySimple;

/**
 * 최근 사진
 *
 * @author pyohwan
 *         16. 7. 21 오후 9:47
 */

public class HomeGallery {
    private String id; // 사진 ID
    private String name; // 사진 이름
    private CommonWriter writer; // 올린이
    private String imageUrl; // 사진 URL
    private String thumbnailUrl; // 썸네일 URL

    public HomeGallery() {
    }

    public HomeGallery(GallerySimple gallery) {
        this.id = gallery.getId();
        this.name = gallery.getName();
        this.writer = gallery.getWriter();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CommonWriter getWriter() {
        return writer;
    }

    public void setWriter(CommonWriter writer) {
        this.writer = writer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
