package org.luncert.view.datasource.neo4j;

import java.util.List;

import org.luncert.view.datasource.neo4j.entity.Pig;
import org.luncert.view.datasource.neo4j.entity.WxUser;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface WxUserRepository extends Neo4jRepository<WxUser, Long>{

    WxUser findByUserId(String userId);

    @Query("MATCH (wxUser)-[:beOwnerOf]->(pig:Pig) WHERE id(wxUser) = {0} RETURN pig")
    List<Pig> findPigs(WxUser wxUser);

    /**
     * 与已存在的 pig 建立关系
     */
    @Query("START wxUser=NODE({0}), pig=NODE({1}) CREATE (wxUser)-[:beOwnerOf]->(pig)")
    void addPig(WxUser wxUser, Pig pig);

    /**
     * 删除与 pig 的关系
     */
    @Query("START wxUser=NODE({0}), pig=NODE({1}) MATCH (wxUser)-[r:beOwnerOf]->(pig) DELETE r")
    void removePig(WxUser wxUser, Pig pig);

}