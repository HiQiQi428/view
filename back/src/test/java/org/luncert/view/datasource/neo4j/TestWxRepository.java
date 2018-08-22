package org.luncert.view.datasource.neo4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWxRepository {

    @Autowired
    private WxUserRepository wxUserRepos;

    @Autowired
    private PigRepository pigRepos;

    @Test
    public void testFindByUserId() {
        WxUser user = wxUserRepos.findByUserId("lun");
        System.out.println(wxUserRepos.findPigs(user));
    }

    @Test
    public void testAddPig() {
        WxUser user = wxUserRepos.findByUserId("lun");
        Pig pig = pigRepos.findById(138L).get();
        wxUserRepos.addPig(user, pig);
    }

}