package org.luncert.view.datasource.neo4j.entity;

import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Data;

@Data
@NodeEntity
public class WxUser {

    @Id @GeneratedValue Long id;

    @Property(name = "userId")
    String userId;

    @Relationship(type = "beOwnerOf", direction = Relationship.OUTGOING)
    List<Pig> pigs;

}