package com.jakduk.api.common.vo;

import lombok.*;

/**
 * @author pyohwan
 *         16. 7. 31 오후 8:34
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialProfile {
    private String id;
    private String nickname;
    private String email;
    @Setter private String smallPictureUrl;
    @Setter private String largePictureUrl;
}
