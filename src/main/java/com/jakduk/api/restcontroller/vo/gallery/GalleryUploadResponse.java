package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.GalleryStatus;

/**
 * @author pyohwan
 *         16. 7. 18 오후 9:31
 */

public class GalleryUploadResponse {

    private String id; // 사진 ID
    private String name; // 사진 이름"
    private String fileName; // 사진 파일 이름
    private CommonWriter writer; // 올린이
    private long size; // 사진 크기
    private long fileSize; // 사진 파일 크기
    private String contentType; // 사진 ContentType
    private GalleryStatus status; // 사진 상태
    private String imageUrl; // 사진 URL
    private String thumbnailUrl; // 썸네일 URL

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CommonWriter getWriter() {
        return writer;
    }

    public void setWriter(CommonWriter writer) {
        this.writer = writer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public GalleryStatus getStatus() {
        return status;
    }

    public void setStatus(GalleryStatus status) {
        this.status = status;
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
