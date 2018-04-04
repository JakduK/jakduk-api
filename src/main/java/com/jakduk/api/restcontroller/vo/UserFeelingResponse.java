package com.jakduk.api.restcontroller.vo;

import com.jakduk.api.common.Constants;
import lombok.*;

/**
 * 감정표현
 *
 * @author pyohwan
 * 16. 3. 26 오후 11:57
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserFeelingResponse {

    @Setter
    private Constants.FEELING_TYPE myFeeling; // 나의 감정 표현 종류

    private Integer numberOfLike; // 좋아요 수
    private Integer numberOfDislike; // 싫어요 수
}
