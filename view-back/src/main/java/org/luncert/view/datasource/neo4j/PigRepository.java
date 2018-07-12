package org.luncert.view.datasource.neo4j;

import java.util.List;

import org.luncert.view.datasource.neo4j.entity.Pig;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

public interface PigRepository extends Neo4jRepository<Pig, Long> {

    @Query("MATCH (n:Pig) WHERE n.userId = { userId } WITH n RETURN n, ID(n)")
    List<Pig> findByUserId(@Param("userId") String userId);

    @Query("MATCH (n:Pig) WHERE n.userId = { userId } AND ID(n) = { id } WITH n RETURN n, [ [ (n)-[r_b1:beChildOfMother]->(p1:Pig) | [ r_b1, p1 ] ], [ (n)-[r_b1:beParentOf]->(p1:Pig) | [ r_b1, p1 ] ], [ (n)-[r_b1:beChildOfFather]->(p1:Pig) | [ r_b1, p1 ] ] ], ID(n)")
    List<Pig> findById(@Param("userId") String userId, @Param("id") Long id);

    @Query("MATCH (n:Pig) WHERE n.userId = { userId } AND n.name = { name } WITH n RETURN n, ID(n)")
    List<Pig> findByName(@Param("userId") String userId, @Param("name") String name);

    @Query("MATCH (n:Pig) WHERE n.userId = { userId } AND n.strain = { strain } WITH n RETURN n, ID(n)")
    List<Pig> findByStrain(@Param("userId") String userId, @Param("strain") int strain);

}