package org.luncert.view.datasource.mysql;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.view.datasource.mysql.entity.Enclosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEnclosureMapper {
    
    @Autowired
    EnclosureMapper enclosureMapper;

    @Test
    public void testAdd() {
        Enclosure e = new Enclosure();
        e.setUserId("oeZnW5TtLINjtnsMMo_O1EaKKCW4");
        e.setValue("舍1");
        enclosureMapper.add(e);
    }

    @Test
    public void testQuery() {
        List<Enclosure> list = enclosureMapper.query("oeZnW5TtLINjtnsMMo_O1EaKKCW4");
        for (Enclosure e : list)
            System.out.println(e);
    }


}