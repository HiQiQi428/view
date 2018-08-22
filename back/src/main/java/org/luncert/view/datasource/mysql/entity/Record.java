package org.luncert.view.datasource.mysql.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Record {

    int id;

    Long pigId;

    float weight;

    String description;

    String timestamp;

    String picName;

}