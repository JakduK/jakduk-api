package com.jakduk.core.repository.board.free;

import com.jakduk.core.model.simple.BoardFreeSimple;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 9.
 */
public interface BoardFreeRepositoryCustom {

    List<BoardFreeSimple> findByIdAndUserId(ObjectId id, String userId, Integer limit);
}
