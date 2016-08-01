package com.jakduk.core.model.etc;

import lombok.*;

/**
 * @author pyohwan
 *         16. 7. 19 오후 9:30
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GalleryOnBoard {

    private String id;

    private String name;

    private String fileName;

    private long size;

}
