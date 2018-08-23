package org.luncert.view.datasource.mysql;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.simpleutils.DateHelper;
import org.luncert.view.datasource.mysql.entity.Record;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRecordMapper {
    
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
            System.out.println(r);
        }
    }

    @Test
    public void testDeleteByPigs() {
        List<Pig> pigs = new ArrayList<>();
        pigs.add(Pig.builder().id(140L).build());
        recordMapper.deleteByPigs(pigs);
    }

}