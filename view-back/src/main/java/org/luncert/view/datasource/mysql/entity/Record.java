package org.luncert.view.datasource.mysql.entity;

import lombok.Data;

@Data
public class Record {

    int id;

    Long pigId;

    String description;

    String timestamp;

    String picName;

}