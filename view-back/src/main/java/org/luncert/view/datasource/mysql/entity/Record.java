package org.luncert.view.datasource.mysql.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Record {

    int id;

    Long pigId;

    String description;

    Date timestamp;

    String picName;

}