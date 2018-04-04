package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.simple.GallerySimple;
import lombok.*;

/**
 * 최근 사진
 *
 * @author pyohwan
 *         16. 7. 21 오후 9:47
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class HomeGallery {
    private String id; // 사진 ID
    private String name; // 사진 이름
    private CommonWriter writer; // 올린이
    private String imageUrl; // 사진 URL
    private String thumbnailUrl; // 썸네일 URL

    public HomeGallery(GallerySimple gallery) {
        this.id = gallery.getId();
        this.name = gallery.getName();
        this.writer = gallery.getWriter();
    }

}
