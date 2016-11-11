package com.jakduk.core.model.web.board;

import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.db.Gallery;
import com.jakduk.core.model.embedded.BoardHistory;
import com.jakduk.core.model.embedded.BoardStatus;
import com.jakduk.core.model.embedded.CommonFeelingUser;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 15 오후 10:24
 */

@Getter
@Setter
public class BoardFreeDetail {

    private String id;

    private int seq;

    private CommonWriter writer;

    private String subject;

    private String content;

    private BoardCategory category;

    private int views;

    private List<CommonFeelingUser> usersLiking;

    private List<CommonFeelingUser> usersDisliking;

    private BoardStatus status;

    private List<BoardHistory> history;

    private List<Gallery> galleries;

}
