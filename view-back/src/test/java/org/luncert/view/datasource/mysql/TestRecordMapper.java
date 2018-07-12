package org.luncert.view.datasource.mysql;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.mullog.Mullog;
import org.luncert.mullog.annotation.BindAppender;
import org.luncert.view.datasource.mysql.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@BindAppender(name = "Test")
public class TestRecordMapper {
    
    Mullog mullog = new Mullog(this);

    @Autowired
    RecordMapper recordMapper;

    // @Test
    public void testAddRecord() {
        recordMapper.addRecord(12L, "good", new Date(), "null");
    }

    @Test
    public void testFetchLast3Week() {
        for (Record r : recordMapper.fetchLast3Week(12L)) {
            mullog.info(r);
        }
    }

}