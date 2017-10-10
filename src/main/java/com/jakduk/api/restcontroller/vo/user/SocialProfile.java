package com.jakduk.api.restcontroller.vo.user;

import lombok.*;

/**
 * access token으로 유저 프로필을 조회하여 담아오는 객체
 *
 * @author pyohwan
 *         16. 7. 31 오후 8:34
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SocialProfile {

    private String id;
    private String nickname;
    @Setter private String email;
    @Setter private String smallPictureUrl;
    @Setter private String largePictureUrl;

}
