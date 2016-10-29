package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.simple.BoardFreeSimple;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 9.
 */
public interface BoardFreeRepositoryCustom {

    List<BoardFreeSimple> findByUserId(String userId, Integer limit);
}
