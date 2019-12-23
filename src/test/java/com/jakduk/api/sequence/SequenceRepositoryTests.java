package com.jakduk.api.sequence;


import com.jakduk.api.common.Constants;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Sequence;
import com.jakduk.api.repository.SequenceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
@DataMongoTest
@EnableConfigurationProperties
@Import({JakdukProperties.class, MongodbConfig.class})
public class SequenceRepositoryTests {

    @Autowired
    private SequenceRepository repository;

    @Test
    public void findCommentsCountByIds() {

        Sequence sequence = repository.findOneByName(Constants.SEQ_BOARD)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));

        assertTrue(sequence.getSeq() > 0);
    }

}
