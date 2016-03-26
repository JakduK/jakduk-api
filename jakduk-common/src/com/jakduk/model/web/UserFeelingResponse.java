package com.jakduk.model.web;

import lombok.Data;

/**
 * Created by pyohwan on 16. 3. 26.
 */

@Data
public class UserFeelingResponse {
    private String feeling;
    private Integer numberOfLike;
    private Integer numberOfDislike;
}
