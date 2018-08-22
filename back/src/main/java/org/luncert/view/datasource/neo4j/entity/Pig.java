package org.luncert.view.datasource.neo4j.entity;

import org.luncert.view.util.JSONBuilder;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NodeEntity
public class Pig {

    public enum Status {
        Healthy,
        Pregnancy,
        Sick,
        Sale,
        Dead
    }

    public static Status statusValueOf(String value) {
        value = value.toLowerCase();
        if (value.equals("healthy")) return Status.Healthy;
        else if (value.equals("pregnancy")) return Status.Pregnancy;
        else if (value.equals("sick")) return Status.Sick;
        else if (value.equals("sale")) return Status.Sale;
        else if (value.equals("dead")) return Status.Dead;
        else return null;
    }

    @Id @GeneratedValue Long id;

    @Property(name = "name")
    String name;

    @Property(name = "userId")
    String userId;

    // 品系
    @Property(name = "strain")
    int strain;

    @Property(name = "status")
    Status status;

    // 是雄性
    @Property(name = "beMale")
    boolean beMale;

    @Property(name = "birthdate")
    String birthdate;

    @Property(name = "picName")
    String picName;

    @Override
    public String toString() {
        JSONBuilder builder = new JSONBuilder()
            .put("id", id)
            .put("name", name)
            .put("userId", userId)
            .put("strain", strain)
            .put("status", status)
            .put("beMale", beMale)
            .put("birthdata", birthdate)
            .put("picName", picName);
        return builder.toString();
    }

}