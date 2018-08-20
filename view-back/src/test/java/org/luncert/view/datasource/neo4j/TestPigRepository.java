package org.luncert.view.datasource.neo4j;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.mullog.Mullog;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPigRepository {
    
    Mullog mullog = new Mullog("Test");

    @Autowired
    PigRepository repo;

    // @Test
    public void testSave() {
        Pig pig = Pig.builder().name("pigy").userId("lun").birthdate(new Date().toString()).strain(1).status(Pig.Status.Healthy).build();
        repo.save(pig);
    }

    @Test
    public void testFindByName() {
        List<Pig> pigs = repo.findByName("lun", "pigy");
        for (Pig pig : pigs) {
            mullog.info("findByName", pig.getName());
        }
    }

    @Test
    public void testFindByStrain() {
        List<Pig> pigs = repo.findByStrain("lun", 1);
        for (Pig pig : pigs) {
            mullog.info("findByStrain", pig.getName());
        }
    }

    @Test
    public void testFindByUserId() {
        List<Pig> pigs = repo.findByUserId("lun");
        for (Pig pig : pigs) {
            mullog.info("findByUserId", pig.getName());
        }
    }

}