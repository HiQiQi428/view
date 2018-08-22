package org.luncert.view.datasource.neo4j;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPigRepository {
    
    @Autowired
    PigRepository repo;

    // @Test
    public void testSave() {
        Pig pig = Pig.builder().name("pigy").birthdate(new Date().toString()).strain(1).status(Pig.Status.Healthy).build();
        repo.save(pig);
    }

    @Test
    public void testFindByName() {
        List<Pig> pigs = repo.findByName("x9");
        for (Pig pig : pigs) {
            System.out.println(pig.getName());
        }
    }

}