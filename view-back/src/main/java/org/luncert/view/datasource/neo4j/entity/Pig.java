package org.luncert.view.datasource.neo4j.entity;

import java.util.Date;
import java.util.List;

import org.luncert.view.util.DateHelper;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Data;

@Data
@NodeEntity
public class Pig {

    @Id @GeneratedValue Long id;

    @Property(name = "name")
    String name;

    @Property(name = "userId")
    String userId;

    // 是雄性
    @Property(name = "beMale")
    boolean beMale;

    @Property(name = "birthdate")
    String birthdate;

    // 品系
    @Property(name = "strain")
    int strain;

    // 健康状况
    @Property(name = "health")
    String health;

    // 饮食习惯
    @Property(name = "eatingHabits")
    String eatingHabits;

    // 食量
    @Property(name = "appetite")
    String appetite;
    
    @Relationship(type="beChildOfFather", direction = Relationship.OUTGOING)
    Pig father;

    @Relationship(type="beChildOfMother", direction = Relationship.OUTGOING)
    Pig mother;

    @Relationship(type="beParentOf", direction = Relationship.OUTGOING)
    transient List<Pig> children;

    public Pig name(String name) {
        this.name = name;
        return this;
    }

    public Pig userId(String userId) {
        this.userId = userId;
        return this;
    }

    public Pig beMale(boolean beMale) {
        this.beMale = beMale;
        return this;
    }

    public Pig birthdate(Date birthdate) {
        this.birthdate = DateHelper.format(birthdate);
        return this;
    }

    public Pig strain(int strain) {
        this.strain = strain;
        return this;
    }

    public Pig health(String health) {
        this.health = health;
        return this;
    }

    public Pig eatingHabits(String eatingHabits) {
        this.eatingHabits = eatingHabits;
        return this;
    }

    public Pig appetite(String appetite) {
        this.appetite = appetite;
        return this;
    }

    public Pig father(Pig father) {
        this.father = father;
        return this;
    }

    public Pig mother(Pig mother) {
        this.mother = mother;
        return this;
    }

}