package com.jakduk.common.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

/**
 * @author pyohwan
 *         16. 7. 31 오후 8:34
 */

@Data
public class DaumProfile {
    private Integer id;
    private String userId;
    private String nickname;
    private String imagePath;
    private String bigImagePath;
    private Boolean openProfile;
}
