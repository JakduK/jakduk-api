package com.jakduk.service;

import com.jakduk.model.db.BoardCategory;
import com.jakduk.repository.BoardCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author pyohwan
 *         16. 7. 19 오후 9:35
 */

@Service
public class BoardCategoryService {

    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    public Optional<BoardCategory> findOneByCode(String code) {
        return boardCategoryRepository.findOneByCode(code);
    }
}
