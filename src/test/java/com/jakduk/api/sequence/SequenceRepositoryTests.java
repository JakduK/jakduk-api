package com.jakduk.api.sequence;


import com.jakduk.api.common.Constants;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.MongodbConfig;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.Sequence;
import com.jakduk.api.repository.SequenceRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by pyohwanjang on 2017. 3. 27..
 */
@RunWith(SpringRunner.class)
@DataMongoTest
@Import({JakdukProperties.class, MongodbConfig.class})
public class SequenceRepositoryTests {

    @Autowired
    private SequenceRepository repository;

    @Test
    public void findCommentsCountByIds() {

        Sequence sequence = repository.findOneByName(Constants.SEQ_BOARD)
                .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));

        Assert.assertTrue(sequence.getSeq() > 0);
    }

}
