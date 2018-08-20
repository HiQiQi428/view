package org.luncert.view.datasource.mysql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.mullog.Mullog;
import org.luncert.simpleutils.DateHelper;
import org.luncert.view.datasource.mysql.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRecordMapper {
    
    Mullog mullog = new Mullog("Test");

    @Autowired
    RecordMapper recordMapper;

    // @Test
    public void testAddRecord() {
        recordMapper.addRecord(Record.builder()
            .pigId(12L)
            .weight(12.5f)
            .description("good")
            .timestamp(DateHelper.now())
            .picName(null)
            .build());
    }

    @Test
    public void testFetchLast3Week() {
        for (Record r : recordMapper.fetchLast3Week(12L)) {
            mullog.info(r);
        }
    }

}