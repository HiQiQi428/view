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

    // 品系
    @Property(name = "strain")
    int strain;

    @Property(name = "status")
    Status status;

    // 是雄性
    @Property(name = "beMale")
    boolean beMale;

    @Property(name = "enclosure")
    String enclosure;

    @Property(name = "birthdate")
    String birthdate;

    @Property(name = "picName")
    String picName;

    public Pig() {}

    public Pig(Long id, String name, int strain, Status status, boolean beMale, String enclosure, String birthdate, String picName) {
        this.id = id;
        this.name = name;
        this.strain = strain;
        this.status = status;
        this.beMale = beMale;
        this.enclosure = enclosure;
        this.birthdate = birthdate;
        this.picName = picName;
    }

    @Override
    public String toString() {
        JSONBuilder builder = new JSONBuilder()
            .put("id", id)
            .put("name", name)
            .put("strain", strain)
            .put("status", status.toString())
            .put("beMale", beMale)
            .put("enclosure", enclosure)
            .put("birthdate", birthdate)
            .put("picName", picName);
        return builder.toString();
    }

}