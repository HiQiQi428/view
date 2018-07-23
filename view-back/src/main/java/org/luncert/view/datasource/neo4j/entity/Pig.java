package org.luncert.view.datasource.neo4j.entity;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import lombok.Data;
import net.sf.json.JSONArray;

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

    @Property(name = "picName")
    String picName;

    @Relationship(type="beChildOfFather", direction = Relationship.OUTGOING)
    Pig father;

    @Relationship(type="beChildOfMother", direction = Relationship.OUTGOING)
    Pig mother;

    @Relationship(type="beParentOf", direction = Relationship.OUTGOING)
    transient List<Pig> children;

    public Pig() {
        children = new ArrayList<>();
    }

    public void addChild(Pig child) {
        children.add(child);
    }

    public static class Builder {

        private Pig pig;
        
        public Builder() {
            pig = new Pig();
        }

        public Builder name(String name) {
            pig.name = name;
            return this;
        }

        public Builder userId(String userId) {
            pig.userId = userId;
            return this;
        }

        public Builder beMale(boolean beMale) {
            pig.beMale = beMale;
            return this;
        }

        public Builder birthdate(String birthdate) {
            pig.birthdate = birthdate;
            return this;
        }

        public Builder strain(int strain) {
            pig.strain = strain;
            return this;
        }

        public Builder health(String health) {
            pig.health = health;
            return this;
        }

        public Builder eatingHabits(String eatingHabits) {
            pig.eatingHabits = eatingHabits;
            return this;
        }

        public Builder appetite(String appetite) {
            pig.appetite = appetite;
            return this;
        }

        public Builder picName(String picName) {
            pig.picName = picName;
            return this;
        }

        public Builder father(Pig father) {
            pig.father = father;
            return this;
        }

        public Builder mother(Pig mother) {
            pig.mother = mother;
            return this;
        }

        public Pig build() {
            return pig;
        }
    }

    /**
     * to JSON string
     * 使用JSONObject的fromObject方法会造成无穷递归，必须自己实现序列化以控制深度
     */
    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean all) {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append("\"id\":").append(id)
            .append(",\"name\":").append('"').append(name).append('"')
            .append(",\"userId\":").append('"').append(userId).append('"')
            .append(",\"beMale\":").append(beMale)
            .append(",\"birthdate\":").append('"').append(birthdate).append('"')
            .append(",\"strain\":").append(strain)
            .append(",\"health\":").append('"').append(health).append('"')
            .append(",\"eatingHabits\":").append('"').append(eatingHabits).append('"')
            .append(",\"appetite\":").append('"').append(appetite).append('"')
            .append(",\"picName\":").append('"').append(picName).append('"');
        if (all) {
            JSONArray jsonArray = new JSONArray();
            for (Pig child : children) {
                jsonArray.add(child.toString());
            }
            if (father != null) builder.append(",\"father\":").append(father.toString(false));
            if (mother != null) builder.append(",\"mother\":").append(mother.toString(false));
            builder.append(",\"children\":").append(jsonArray.toString());
        }
        return builder.append("}").toString();
    }

}